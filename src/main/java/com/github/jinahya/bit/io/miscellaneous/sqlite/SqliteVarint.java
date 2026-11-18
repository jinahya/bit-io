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

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.LongBitReader;
import com.github.jinahya.bit.io.LongBitWriter;

import java.io.IOException;
import java.util.Objects;

/**
 * A codec for the variable-length integer ("varint") of the SQLite database file format: a 64-bit two's-complement
 * integer in 1 to 9 bytes, big-endian. Each of the first eight bytes holds 7 bits, with its high-order bit set when
 * another byte follows; a ninth byte, if any, holds all 8 bits.
 *
 * <p>Writing produces the shortest encoding; a value whose high-order 8 bits are not all zero, including every
 * negative value, takes nine bytes. Reading also accepts encodings with redundant leading {@code 0x80} bytes.</p>
 *
 * @see <a href="https://www.sqlite.org/fileformat2.html#varint">Database File Format, 1.6. B-tree Pages</a>
 */
public final class SqliteVarint
        implements LongBitReader, LongBitWriter {

    /**
     * The maximum number of bytes of an encoding. The value is {@value}.
     */
    public static final int MAX_BYTES = 9;

    /**
     * The singleton instance of this codec.
     */
    public static final SqliteVarint INSTANCE = new SqliteVarint();

    private static final int GROUP_SIZE = 7;

    private static final int CONTINUATION = 0x80;

    // -----------------------------------------------------------------------------------------------------------------
    private SqliteVarint() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long readLong(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        long value = 0L;
        for (int i = 0; i < MAX_BYTES - 1; i++) {
            final int b = input.readUnsignedInt(Byte.SIZE);
            value = (value << GROUP_SIZE) | (b & ~CONTINUATION);
            if ((b & CONTINUATION) == 0) {
                return value;
            }
        }
        return (value << Byte.SIZE) | input.readUnsignedInt(Byte.SIZE);
    }

    @Override
    public void writeLong(final BitOutput output, final long value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        if ((value >>> (Long.SIZE - Byte.SIZE)) != 0L) { // nine bytes; 8 x 7 bits, then 8 bits
            for (int shift = Long.SIZE - GROUP_SIZE; shift >= Byte.SIZE; shift -= GROUP_SIZE) {
                output.writeUnsignedInt(Byte.SIZE, CONTINUATION | (int) ((value >>> shift) & 0x7F));
            }
            output.writeUnsignedInt(Byte.SIZE, (int) (value & 0xFF));
            return;
        }
        final int groups = Math.max(1, (Long.SIZE - Long.numberOfLeadingZeros(value) + GROUP_SIZE - 1) / GROUP_SIZE);
        for (int i = groups - 1; i >= 0; i--) {
            final int group = (int) ((value >>> (i * GROUP_SIZE)) & 0x7F);
            output.writeUnsignedInt(Byte.SIZE, i > 0 ? CONTINUATION | group : group);
        }
    }
}
