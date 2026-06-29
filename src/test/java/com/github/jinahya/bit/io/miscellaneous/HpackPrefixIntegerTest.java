package com.github.jinahya.bit.io.miscellaneous;

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

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteInput;
import com.github.jinahya.bit.io.StreamByteOutput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class HpackPrefixIntegerTest {

    @Test
    void exposesPrefixSize() {
        assertEquals(5, HpackPrefixInteger.of(5).prefixSize());
        assertEquals(8, HpackPrefixInteger.of(8).prefixSize());
    }

    @Test
    void rejectsInvalidPrefixSizes() {
        assertThrows(IllegalArgumentException.class, () -> HpackPrefixInteger.of(0));
        assertThrows(IllegalArgumentException.class, () -> HpackPrefixInteger.of(9));
    }

    @Test
    void writesRfc7541Examples() throws IOException {
        assertArrayEquals(_Utils.fromHex("0a"), writeStandalone(5, 10L));
        assertArrayEquals(_Utils.fromHex("1f9a0a"), writeStandalone(5, 1337L));
        assertArrayEquals(_Utils.fromHex("2a"), writeStandalone(8, 42L));
    }

    @Test
    void readsRfc7541Examples() throws IOException {
        assertEquals(10L, readStandalone(5, _Utils.fromHex("0a")));
        assertEquals(1337L, readStandalone(5, _Utils.fromHex("1f9a0a")));
        assertEquals(42L, readStandalone(8, _Utils.fromHex("2a")));
    }

    @Test
    void composesAfterHighBitsInFirstOctet() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeUnsignedInt(3, 0b101);
        HpackPrefixInteger.of(5).writeLong(output, 10L);
        output.align(1);
        assertArrayEquals(_Utils.fromHex("aa"), baos.toByteArray());

        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(_Utils.fromHex("aa"))));
        assertEquals(0b101, input.readUnsignedInt(3));
        assertEquals(10L, HpackPrefixInteger.of(5).readLong(input));
    }

    @Test
    void rejectsNullAndNegativeArguments() throws IOException {
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(new ByteArrayOutputStream()));
        assertThrows(NullPointerException.class, () -> HpackPrefixInteger.of(5).readLong(null));
        assertThrows(NullPointerException.class, () -> HpackPrefixInteger.of(5).writeLong(null, 0L));
        assertThrows(IllegalArgumentException.class, () -> HpackPrefixInteger.of(5).writeLong(output, -1L));
    }

    private static long readStandalone(final int prefixSize, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        if (prefixSize < Byte.SIZE) {
            input.skip(Byte.SIZE - prefixSize);
        }
        return HpackPrefixInteger.of(prefixSize).readLong(input);
    }

    private static byte[] writeStandalone(final int prefixSize, final long value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        if (prefixSize < Byte.SIZE) {
            output.writeUnsignedInt(Byte.SIZE - prefixSize, 0);
        }
        HpackPrefixInteger.of(prefixSize).writeLong(output, value);
        output.align(1);
        return baos.toByteArray();
    }
}
