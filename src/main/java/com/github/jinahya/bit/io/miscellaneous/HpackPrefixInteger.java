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
import com.github.jinahya.bit.io.LongBitReader;
import com.github.jinahya.bit.io.LongBitWriter;

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForSignedByte;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;

/**
 * A codec for HPACK/QPACK integer representations with an N-bit prefix.
 *
 * <p>This codec reads and writes only the integer representation: first {@code prefixSize} bits, followed by zero or
 * more 7-bit continuation octets. For prefix sizes smaller than 8, callers are expected to read or write the enclosing
 * representation bits before this codec consumes or emits the low prefix bits.</p>
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc7541.html#section-5.1">RFC 7541, Section 5.1</a>
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9204.html">RFC 9204: QPACK</a>
 */
public final class HpackPrefixInteger
        implements LongBitReader, LongBitWriter {

    /**
     * Returns a codec for the specified prefix size.
     *
     * @param prefixSize the prefix size; between {@code 1} and {@code 8}, both inclusive.
     * @return a codec for {@code prefixSize}.
     */
    public static HpackPrefixInteger of(final int prefixSize) {
        return new HpackPrefixInteger(prefixSize);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private HpackPrefixInteger(final int prefixSize) {
        super();
        this.prefixSize = requireValidSizeForSignedByte(prefixSize);
        prefixLimit = (1 << prefixSize) - 1;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long readLong(final BitInput input) throws IOException {
        requireNonNullInput(input);
        long value = input.readUnsignedInt(prefixSize);
        if (value < prefixLimit) {
            return value;
        }
        for (int shift = 0; ; shift += 7) {
            final int octet = input.readUnsignedInt(Byte.SIZE);
            final int payload = octet & 0x7F;
            if (shift >= Long.SIZE - 1 || payload > (Long.MAX_VALUE - value) >> shift) {
                throw new IOException("HPACK prefix integer exceeds signed long range");
            }
            value += ((long) payload) << shift;
            if ((octet & 0x80) == 0) {
                return value;
            }
        }
    }

    @Override
    public void writeLong(final BitOutput output, final long value) throws IOException {
        requireNonNullOutput(output);
        long v = value;
        if (v < 0L) {
            throw new IllegalArgumentException("negative HPACK prefix integer: " + v);
        }
        if (v < prefixLimit) {
            output.writeUnsignedInt(prefixSize, (int) v);
            return;
        }
        output.writeUnsignedInt(prefixSize, prefixLimit);
        v -= prefixLimit;
        while (v >= 128L) {
            output.writeUnsignedInt(Byte.SIZE, (int) (v & 0x7F) | 0x80);
            v >>>= 7;
        }
        output.writeUnsignedInt(Byte.SIZE, (int) v);
    }

    /**
     * Returns the prefix size.
     *
     * @return the prefix size.
     */
    public int prefixSize() {
        return prefixSize;
    }

    private final int prefixSize;

    private final int prefixLimit;
}
