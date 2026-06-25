package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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

import java.io.IOException;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeForUnsignedInt;

/**
 * A {@link BitReader} for reading a length-prefixed array of bytes, with each element read as a signed or unsigned
 * value of a configurable bit width. Obtained via {@link #ofSigned(int, int)} or {@link #ofUnsigned(int, int)}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteArrayWriter
 */
public class ByteArrayReader
        implements BitReader<byte[]> {

    /**
     * Returns a new reader that reads each element as a <em>signed</em> {@code elementSize}-bit value.
     *
     * @param lengthSize  the number of bits for the array length; between {@code 1} and
     *                    ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @param elementSize the number of bits for each (signed) element; between {@code 1} and
     *                    {@value java.lang.Byte#SIZE}, both inclusive.
     * @return a new signed reader.
     * @throws IllegalArgumentException if {@code lengthSize} or {@code elementSize} is not valid.
     */
    public static ByteArrayReader ofSigned(final int lengthSize, final int elementSize) {
        return new ByteArrayReader(lengthSize, false, elementSize);
    }

    /**
     * Returns a new reader that reads each element as an <em>unsigned</em> {@code elementSize}-bit value. Use this to
     * read sub-byte unsigned values losslessly, e.g. nibbles of {@code 0..15} with an {@code elementSize} of
     * {@code 4}.
     *
     * @param lengthSize  the number of bits for the array length; between {@code 1} and
     *                    ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @param elementSize the number of bits for each (unsigned) element; between {@code 1} and
     *                    ({@value java.lang.Byte#SIZE} - {@code 1}), both inclusive.
     * @return a new unsigned reader.
     * @throws IllegalArgumentException if {@code lengthSize} or {@code elementSize} is not valid.
     */
    public static ByteArrayReader ofUnsigned(final int lengthSize, final int elementSize) {
        return new ByteArrayReader(lengthSize, true, elementSize);
    }

    private ByteArrayReader(final int lengthSize, final boolean unsigned, final int elementSize) {
        super();
        this.lengthSize = requireValidSizeForUnsignedInt(lengthSize);
        this.unsigned = unsigned;
        this.elementSize = requireValidSizeByte(unsigned, elementSize);
    }

    /**
     * {@inheritDoc} The {@code read(BitInput)} method of {@code ByteArrayReader} class reads the array length as an
     * unsigned {@code lengthSize}-bit {@code int}, then reads each element as a signed or unsigned
     * {@code elementSize}-bit value, as configured.
     *
     * @param input {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException if {@code input} is {@code null}.
     * @throws IOException          {@inheritDoc}
     */
    @Override
    public byte[] read(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        final byte[] value = new byte[input.readInt(true, lengthSize)];
        for (int i = 0; i < value.length; i++) {
            value[i] = input.readByte(unsigned, elementSize);
        }
        return value;
    }

    private final int lengthSize;

    private final boolean unsigned;

    private final int elementSize;
}
