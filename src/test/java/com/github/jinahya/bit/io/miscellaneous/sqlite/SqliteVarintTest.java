package com.github.jinahya.bit.io.miscellaneous.sqlite;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.jinahya.bit.io.ArrayByteInput;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests {@link SqliteVarint}.
 *
 * <p>The expected bytes are those SQLite 3.54.1 wrote as the rowid varints of table b-tree leaf cells.</p>
 *
 * @see <a href="https://www.sqlite.org/fileformat2.html#varint">Database File Format, 1.6. B-tree Pages</a>
 */
class SqliteVarintTest {

    private static final Object[][] VECTORS = {
            {0L, "00"}, {1L, "01"}, {127L, "7f"}, {128L, "8100"}, {240L, "8170"}, {2287L, "916f"},
            {16383L, "ff7f"}, {16384L, "818000"}, {2097151L, "ffff7f"}, {2097152L, "81808000"},
            {268435455L, "ffffff7f"}, {268435456L, "8180808000"},
            {(1L << 35) - 1L, "ffffffff7f"}, {1L << 35, "818080808000"},
            {(1L << 42) - 1L, "ffffffffff7f"}, {1L << 42, "81808080808000"},
            {(1L << 49) - 1L, "ffffffffffff7f"}, {1L << 49, "8180808080808000"},
            {(1L << 56) - 1L, "ffffffffffffff7f"}, {1L << 56, "80c080808080808000"},
            {Long.MAX_VALUE, "bfffffffffffffffff"}, {-1L, "ffffffffffffffffff"}, {-2L, "fffffffffffffffffe"},
            {-128L, "ffffffffffffffff80"}, {-(1L << 56), "ffc080808080808000"},
            {Long.MIN_VALUE, "c08080808080808000"},
            {0x0102030405060708L, "80c0c0b0a0948c8708"}, {0x7F00000000000001L, "bfc080808080808001"}
    };

    @Test
    void vectorsAsSqliteWritesThem() throws IOException {
        for (final Object[] vector : VECTORS) {
            final long value = (Long) vector[0];
            final String hex = (String) vector[1];
            assertEquals(hex, write(value), Long.toString(value));
            assertEquals(value, read(hex), hex);
        }
    }

    @Test
    void readAcceptsRedundantLeadingBytes() throws IOException {
        assertEquals(1L, read("8001"));
        assertEquals(1L, read("80808001"));
        assertEquals(127L, read("807f"));
    }

    @Test
    void readFailsOnTruncatedInput() {
        assertThrows(EOFException.class, () -> SqliteVarint.INSTANCE.readLong(
                new DefaultBitInput(new com.github.jinahya.bit.io.StreamByteInput(
                        new java.io.ByteArrayInputStream(new byte[]{(byte) 0x81})))));
    }

    @Test
    void nullArguments() {
        assertThrows(NullPointerException.class, () -> SqliteVarint.INSTANCE.readLong(null));
        assertThrows(NullPointerException.class, () -> SqliteVarint.INSTANCE.writeLong(null, 0L));
        assertEquals(9, SqliteVarint.MAX_BYTES);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static long read(final String hex) throws IOException {
        final byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        final ArrayByteInput input = new ArrayByteInput(bytes);
        final long value = SqliteVarint.INSTANCE.readLong(new DefaultBitInput(input));
        assertEquals(bytes.length, input.getIndex(), "consumed bytes of " + hex);
        return value;
    }

    private static String write(final long value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(bytes));
        SqliteVarint.INSTANCE.writeLong(output, value);
        output.align(1);
        final StringBuilder builder = new StringBuilder();
        for (final byte b : bytes.toByteArray()) {
            builder.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1));
        }
        return builder.toString();
    }
}
