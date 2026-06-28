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
 * A {@link BitWriter} for writing a length-prefixed array of bytes, with each element written as a signed or unsigned
 * value of a configurable bit width. Obtained via {@link #ofSigned(int, int)} or {@link #ofUnsigned(int, int)}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteArrayReader
 */
public class ByteArrayWriter
        implements BitWriter<byte[]> {

    /**
     * Returns a new writer that writes each element as a <em>signed</em> {@code elementSize}-bit value.
     *
     * @param lengthSize  the number of bits for the array length; between {@code 1} and
     *                    ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @param elementSize the number of bits for each (signed) element; between {@code 1} and
     *                    {@value java.lang.Byte#SIZE}, both inclusive.
     * @return a new signed writer.
     * @throws IllegalArgumentException if {@code lengthSize} or {@code elementSize} is not valid.
     */
    public static ByteArrayWriter ofSigned(final int lengthSize, final int elementSize) {
        return new ByteArrayWriter(lengthSize, false, elementSize);
    }

    /**
     * Returns a new writer that writes each element as an <em>unsigned</em> {@code elementSize}-bit value. Use this to
     * write sub-byte unsigned values losslessly, e.g. nibbles of {@code 0..15} with an {@code elementSize} of
     * {@code 4}.
     *
     * @param lengthSize  the number of bits for the array length; between {@code 1} and
     *                    ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @param elementSize the number of bits for each (unsigned) element; between {@code 1} and
     *                    ({@value java.lang.Byte#SIZE} - {@code 1}), both inclusive.
     * @return a new unsigned writer.
     * @throws IllegalArgumentException if {@code lengthSize} or {@code elementSize} is not valid.
     */
    public static ByteArrayWriter ofUnsigned(final int lengthSize, final int elementSize) {
        return new ByteArrayWriter(lengthSize, true, elementSize);
    }

    private ByteArrayWriter(final int lengthSize, final boolean unsigned, final int elementSize) {
        super();
        this.lengthSize = requireValidSizeForUnsignedInt(lengthSize);
        this.unsigned = unsigned;
        this.elementSize = requireValidSizeByte(unsigned, elementSize);
    }

    /**
     * {@inheritDoc} The {@code write(BitOutput, byte[])} method of {@code ByteArrayWriter} class writes the array
     * length as an unsigned {@code lengthSize}-bit {@code int}, then writes each element as a signed or unsigned
     * {@code elementSize}-bit value, as configured.
     *
     * @param output {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws NullPointerException     if {@code output} or {@code value} is {@code null}.
     * @throws IllegalArgumentException if {@code value.length} does not fit in {@code lengthSize} bits.
     * @throws IOException              {@inheritDoc}
     */
    @Override
    public void write(final BitOutput output, final byte[] value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        final int length = value.length;
        if ((length >>> lengthSize) != 0) {
            throw new IllegalArgumentException(
                    "value.length(" + length + ") requires more than lengthSize(" + lengthSize + ") bits");
        }
        output.writeUnsignedInt(lengthSize, length);
        for (final byte element : value) {
            if (unsigned) {
                output.writeUnsignedByte(elementSize, element);
            } else {
                output.writeByte(elementSize, element);
            }
        }
    }

    private final int lengthSize;

    private final boolean unsigned;

    private final int elementSize;
}
