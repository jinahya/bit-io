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


import com.github.jinahya.bit.io.octet.ByteInput;
import java.io.IOException;


/**
 * An abstract class partially implementing {@link BitInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class AbstractBitInput implements BitInput, ByteInput {


    /**
     * Returns the value of {@link #read()} while incrementing the {@code count}
     * by one.
     *
     * @return an unsigned byte value.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected int octet() throws IOException {

        final int octet = read() & 0xFF;
        ++count;

        return octet;
    }


    /**
     * Reads an unsigned int value whose size is equals to or less than
     * {@value BitIoConstants#U8_SIZE_MAX}.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#U8_SIZE_MIN} (inclusive)
     * and {@value com.github.jinahya.bit.io.BitIoConstants#U8_SIZE_MAX}
     * (inclusive).
     *
     * @return an unsigned byte value.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected int read8(final int size) throws IOException {

        BitIoConstraints.requireValid8Size(size);

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
            return (read8(available) << required)
                   | read8(required);
        }

        int value = 0x00;

        for (int i = 0; i < size; i++) {
            value <<= 1;
            value |= (flags[index++] ? 0x01 : 0x00);
        }

        return value;
    }


    /**
     * Reads an unsigned int value whose size is equals to or less than
     * {@value BitIoConstants#U16_SIZE_MAX}.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#U16_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#U16_SIZE_MAX}
     * (inclusive).
     *
     * @return an unsigned short value.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected int read16(final int size) throws IOException {

        BitIoConstraints.requireValid16Size(size);

        int value = 0x00;

        final int quotient = size / 8;
        final int remainder = size % 8;

        for (int i = 0; i < quotient; i++) {
            value <<= 8;
            value |= read8(8);
        }

        if (remainder > 0) {
            value <<= remainder;
            value |= read8(remainder);
        }

        return value;
    }


    // ----------------------------------------------------------------- boolean
    @Override
    public boolean readBoolean() throws IOException {

        return readInt(true, 1) == 1;
    }


    @Override
    public byte readByte(final boolean unsigned, final int size)
        throws IOException {

        BitIoConstraints.requireValidSize(unsigned, 3, size);

        return (byte) readInt(unsigned, size);
    }


    @Override
    public short readShort(final boolean unsigned, final int size)
        throws IOException {

        BitIoConstraints.requireValidSize(unsigned, 4, size);

        return (short) readInt(unsigned, size);
    }


    @Override
    public int readInt(final boolean unsigned, final int size)
        throws IOException {

        BitIoConstraints.requireValidSize(unsigned, 5, size);

        if (!unsigned) {
            final int usize = size - 1;
            return ((0 - readInt(true, 1)) << usize) | readInt(true, usize);
        }

        int value = 0x00;

        final int quotient = size / 16;
        final int remainder = size % 16;
        for (int i = 0; i < quotient; i++) {
            value <<= 16;
            value |= read16(16);
        }
        if (remainder > 0) {
            value <<= remainder;
            value |= read16(remainder);
        }

        return value;
    }


    @Override
    public long readLong(final boolean unsigned, final int size)
        throws IOException {

        BitIoConstraints.requireValidSize(unsigned, 6, size);

        if (!unsigned) {
            final int usize = size - 1;
            return ((0L - readLong(true, 1)) << usize) | readLong(true, usize);
        }

        long value = 0x00L;

        final int quotient = size / 31;
        final int remainder = size % 31;
        for (int i = 0; i < quotient; i++) {
            value <<= 31;
            value |= readInt(true, 31);
        }
        if (remainder > 0) {
            value <<= remainder;
            value |= readInt(true, remainder);
        }

        return value;
    }


    // -------------------------------------------------------------------- char
    @Override
    public char readChar(int size) throws IOException {

        BitIoConstraints.requireValidSize(true, 4, size);

        return (char) readInt(true, size);
    }


//    @Deprecated
//    @Override
//    public char readChar() throws IOException {
//
//        return readChar(BitIoConstants.CHAR_SIZE_MAX);
//    }
    // ------------------------------------------------------------------- float
    @Override
    public float readFloat() throws IOException {

        return Float.intBitsToFloat(readInt(false, 32));
    }


    // ------------------------------------------------------------------ double
    @Override
    public double readDouble() throws IOException {

        return Double.longBitsToDouble(readLong(false, 64));
    }


    @Override
    public <T extends BitReadable> T readObject(final Class<T> type)
        throws IOException {

        if (type == null) {
            throw new NullPointerException("null type");
        }

        final T value;
        try {
            value = type.newInstance();
        } catch (final InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }

        value.read(this);

        return value;
    }


    @Override
    public <T extends BitReadable> T readNullable(final Class<T> type)
        throws IOException {

        if (type == null) {
            throw new NullPointerException("null type");
        }

        if (!readBoolean()) {
            return null;
        }

        return readObject(type);
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
            read8((int) bits); // count increments
        }

        final long remainder = count % bytes;
        long octets = (remainder > 0 ? bytes : 0) - remainder;
        for (; octets > 0; octets--) {
            read8(8);
            bits += 8;
        }

        return bits;
    }


    @Override
    public long getCount() {

        return count;
    }


    @Override
    public int getIndex() {

        return index;
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
    private long count = 0L;

}

