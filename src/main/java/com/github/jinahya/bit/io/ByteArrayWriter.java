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

import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForUnsignedInt;

/**
 * A {@link BitWriter} for writing a length-prefixed array of bytes, with each element written as a signed or unsigned
 * value of a configurable bit width.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteArrayReader
 */
public class ByteArrayWriter
        implements BitWriter<byte[]> {

    /**
     * A {@link ByteArrayWriter} that writes each element as a signed value.
     */
    public static class Signed
            extends ByteArrayWriter {

        /**
         * Creates a new instance.
         *
         * @param lengthSize  the number of bits for the array length.
         * @param elementSize the number of bits for each signed element.
         */
        public Signed(final int lengthSize, final int elementSize) {
            super(lengthSize, false, elementSize);
        }
    }

    /**
     * A {@link ByteArrayWriter} that writes each element as a signed 8-bit value.
     */
    public static class Signed8
            extends Signed {

        /**
         * Creates a new instance.
         *
         * @param lengthSize the number of bits for the array length.
         */
        public Signed8(final int lengthSize) {
            super(lengthSize, Byte.SIZE);
        }
    }

    /**
     * A {@link ByteArrayWriter} that writes each element as an unsigned value.
     */
    public static class Unsigned
            extends ByteArrayWriter {

        /**
         * Creates a new instance.
         *
         * @param lengthSize  the number of bits for the array length.
         * @param elementSize the number of bits for each unsigned element.
         */
        public Unsigned(final int lengthSize, final int elementSize) {
            super(lengthSize, true, elementSize);
        }
    }

    /**
     * A {@link ByteArrayWriter} that writes each element as an unsigned 7-bit value.
     */
    public static class Unsigned7
            extends Unsigned {

        /**
         * Creates a new instance.
         *
         * @param lengthSize the number of bits for the array length.
         */
        public Unsigned7(final int lengthSize) {
            super(lengthSize, Byte.SIZE - 1);
        }
    }

    public ByteArrayWriter(final int lengthSize, final boolean elementUnsigned, final int elementSize) {
        super();
        this.lengthSize = requireValidSizeForUnsignedInt(lengthSize);
        this.elementUnsigned = elementUnsigned;
        this.elementSize = requireValidSizeByte(elementUnsigned, elementSize);
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
            if (elementUnsigned) {
                output.writeUnsignedByte(elementSize, element);
            } else {
                output.writeByte(elementSize, element);
            }
        }
    }

    private final int lengthSize;

    private final boolean elementUnsigned;

    private final int elementSize;
}
