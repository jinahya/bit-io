/*
 *  Copyright 2010 Jin Kwon.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package com.github.jinahya.bit.io;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


/**
 * An abstract class partially implementing {@link BitInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class AbstractBitInput implements BitInput, ByteInput {


    /**
     * Returns the value of {@link #readUnsignedByte()} while incrementing the
     * {@code count} by one.
     *
     * @return an unsigned byte value.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected int octet() throws IOException {

        final int octet = readUnsignedByte();
        ++count;

        return octet;
    }


    /**
     * Reads an unsigned byte value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#UBYTE_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#UBYTE_SIZE_MAX}
     * (inclusive).
     *
     * @return an unsigned byte value.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected int readUnsignedByte(final int size) throws IOException {

        BitIoConstraints.requireValidUnsignedByteSize(size);

        if (index == 8) {
            int octet = octet();
            if (size == 8) {
                return octet;
            }
            for (int i = 7; i >= 0; i--) {
                flags[i] = (octet & 0x01) == 0x01;
                octet >>= 1;
            }
            index = 0;
        }

        final int available = 8 - index;
        final int required = size - available;

        if (required > 0) {
            return (readUnsignedByte(available) << required)
                   | readUnsignedByte(required);
        }

        int value = 0x00;

        for (int i = 0; i < size; i++) {
            value <<= 1;
            value |= (flags[index++] ? 0x01 : 0x00);
        }

        return value;
    }


    @Override
    public boolean readBoolean() throws IOException {

        return readUnsignedByte(1) == 0x01;
    }


    /**
     * Reads an unsigned short value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#USHORT_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#USHORT_SIZE_MAX}
     * (inclusive).
     *
     * @return an unsigned short value.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected int readUnsignedShort(final int size) throws IOException {

        BitIoConstraints.requireValidUnsignedShortSize(size);

        int value = 0x00;

        final int quotient = size / 8;
        final int remainder = size % 8;

        for (int i = 0; i < quotient; i++) {
            value <<= 8;
            value |= readUnsignedByte(8);
        }

        if (remainder > 0) {
            value <<= remainder;
            value |= readUnsignedByte(remainder);
        }

        return value;
    }


    @Override
    public int readUnsignedInt(final int size) throws IOException {

        BitIoConstraints.requireValidUnsignedIntSize(size);

        int value = 0x00;

        final int quotient = size / 16;
        final int remainder = size % 16;

        for (int i = 0; i < quotient; i++) {
            value <<= 16;
            value |= readUnsignedShort(16);
        }

        if (remainder > 0) {
            value <<= remainder;
            value |= readUnsignedShort(remainder);
        }

        return value;
    }


    @Override
    public int readInt(final int size) throws IOException {

        BitIoConstraints.requireValidIntSize(size);

        final int usize = size - 1;

        return (readBoolean() ? (-1 << usize) : 0) | readUnsignedInt(usize);
    }


    @Override
    public long readUnsignedLong(final int size) throws IOException {

        BitIoConstraints.requireValidUnsignedLongSize(size);

        long value = 0x00L;

        final int quotient = size / 31;
        final int remainder = size % 31;

        for (int i = 0; i < quotient; i++) {
            value <<= 31;
            value |= readUnsignedInt(31);
        }

        if (remainder > 0) {
            value <<= remainder;
            value |= readUnsignedInt(remainder);
        }

        return value;
    }


    @Override
    public long readLong(final int size) throws IOException {

        BitIoConstraints.requireValidLongSize(size);

        final int usize = size - 1;

        return (readBoolean() ? (-1L << usize) : 0L) | readUnsignedLong(usize);
    }


    @Override
    public <T> T readObject(final Function<BitInput, T> reader)
        throws IOException {

        if (reader == null) {
            throw new NullPointerException("null reader");
        }

        try {
            return reader.apply(this);
        } catch (final RuntimeException re) {
            final Throwable cause = re.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw re;
        }
    }


    @Override
    public <T> T[] readArray(final int scale,
                             final Function<BitInput, T> reader)
        throws IOException {

        BitIoConstraints.requireValidUnsignedIntSize(scale);

        if (reader == null) {
            throw new NullPointerException("null reader");
        }

        @SuppressWarnings("unchecked")
        final T[] array = (T[]) new Object[readUnsignedInt(scale)];

        for (int i = 0; i < array.length; i++) {
            array[i] = readObject(reader);
        }

        return array;
    }


    @Override
    public <T> List<T> readList(final int scale,
                                final Function<BitInput, T> reader)
        throws IOException {

        BitIoConstraints.requireValidUnsignedIntSize(scale);

        if (reader == null) {
            throw new NullPointerException("null reader");
        }

        final int size = readUnsignedInt(scale);

        final List<T> value = new ArrayList<T>(size);

        for (int i = 0; i < size; i++) {
            value.add(readObject(reader));
        }

        return value;
    }


    @Override
    public void readBytes(final byte[] array, final int offset,
                          final int length, final int range)
        throws IOException {

        BitIoConstraints.requireValidArrayOffsetLength(array, offset, length);
        BitIoConstraints.requireValidUnsignedByteSize(range);

        final int limit = offset + length;
        for (int i = offset; i < limit; i++) {
            array[i] = (byte) readUnsignedByte(range);
        }
    }


    @Override
    public byte[] readBytes(final int scale, final int range)
        throws IOException {

        BitIoConstraints.requireValidUnsignedIntSize(scale);
        BitIoConstraints.requireValidUnsignedByteSize(range);

        final int length = readUnsignedInt(scale);
        final byte[] value = new byte[length];

        for (int i = 0; i < value.length; i++) {
            value[i] = (byte) readUnsignedByte(range);
        }

        return value;
    }


    @Override
    public long align(final int bytes) throws IOException {

        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }

        long bits = 0; // number of bits to be discarded

        // discard remained bits in current octet.
        if (index < 8) {
            bits += (8 - index);
            readUnsignedByte((int) bits); // count increments
        }

//        final long remainder = (count > 0L ? count : ~count + 1L) % bytes;
//        if (remainder == 0) {
//            return bits;
//        }
//
//        long octets = bytes - remainder;
        final long remainder = count % bytes;
        long octets = (remainder > 0 ? bytes : 0) - remainder;
        for (; octets > 0; octets--) {
            readUnsignedByte(8);
            bits += 8;
        }

        return bits;
    }


    /**
     * bit flags.
     */
    private final boolean[] flags = new boolean[8];


    /**
     * The next bit index to read.
     */
    private int index = 8;


    /**
     * number of bytes read so far.
     */
    private long count = 0;

}

