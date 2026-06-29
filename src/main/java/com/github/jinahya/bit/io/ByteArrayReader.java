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
 * A {@link BitReader} for reading a length-prefixed array of bytes, with each element read as a signed or unsigned
 * value of a configurable bit width.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteArrayWriter
 */
public class ByteArrayReader
        implements BitReader<byte[]> {

    /**
     * A {@link ByteArrayReader} that reads each element as a signed value.
     */
    public static class Signed
            extends ByteArrayReader {

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
     * A {@link ByteArrayReader} that reads each element as a signed 8-bit value.
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
     * A {@link ByteArrayReader} that reads each element as an unsigned value.
     */
    public static class Unsigned
            extends ByteArrayReader {

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
     * A {@link ByteArrayReader} that reads each element as an unsigned 7-bit value.
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

    public ByteArrayReader(final int lengthSize, final boolean elementUnsigned, final int elementSize) {
        super();
        this.lengthSize = requireValidSizeForUnsignedInt(lengthSize);
        this.elementUnsigned = elementUnsigned;
        this.elementSize = requireValidSizeByte(elementUnsigned, elementSize);
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
        final byte[] value = new byte[input.readUnsignedInt(lengthSize)];
        for (int i = 0; i < value.length; i++) {
            value[i] = elementUnsigned ? input.readUnsignedByte(elementSize) : input.readByte(elementSize);
        }
        return value;
    }

    private final int lengthSize;

    private final boolean elementUnsigned;

    private final int elementSize;
}
