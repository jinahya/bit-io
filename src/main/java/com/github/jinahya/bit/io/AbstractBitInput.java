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


/**
 * A abstract class partially implementing {@link BitInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class AbstractBitInput implements BitInput, ByteInput {


    /**
     * Returns the value returned from {@link #readUnsignedByte()} while
     * incrementing the {@code count} by one.
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

        return (readBoolean() ? -1 << usize : 0) | readUnsignedInt(usize);
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

        return (readBoolean() ? -1L << usize : 0L) | readUnsignedLong(usize);
    }


    @Override
    public byte[] readBytes(final byte[] array, final int offset,
                            final int length, final int range)
        throws IOException {

        BitIoConstraints.requireValidBytesRange(range);

        final int limit = offset + length;
        for (int i = offset; i < limit; i++) {
            array[i] = (byte) readUnsignedByte(range);
        }

        return array;
    }


    @Override
    public byte[] readBytes(final byte[] array, final int offset,
                            final int range)
        throws IOException {

        return readBytes(array, offset, array.length - offset, range);
    }


    @Override
    public byte[] readBytes(final byte[] array, final int range)
        throws IOException {

        return readBytes(array, 0, range);
    }


    int readLenght(final int scale) throws IOException {

        BitIoConstraints.requireValidLengthSize(scale);

        return readUnsignedInt(scale);
    }


    @Override
    public byte[] readBytes(int scale, byte[] array, int offset, int range)
        throws IOException {

        BitIoConstraints.requireValidLengthSize(scale);

        final int length = readUnsignedInt(scale);
        return readBytes(array, offset, length, range);
    }


    @Override
    public byte[] readBytes(final int scale, final byte[] array,
                            final int range)
        throws IOException {

        return readBytes(scale, array, 0, range);
    }


//    @Deprecated
//    @Override
//    public byte[] readBytes(final int scale, final int range)
//        throws IOException {
//
//        BitIoConstraints.requireValidBytesScale(scale);
//        BitIoConstraints.requireValidBytesRange(range);
//
////        final byte[] value = new byte[readUnsignedInt(scale)];
////
////        for (int i = 0; i < value.length; i++) {
////            value[i] = (byte) readUnsignedByte(range);
////        }
////
////        return value;
//        return readBytes(new byte[readUnsignedInt(scale)], range);
//    }
    @Override
    public String readString(final int scale, final String charsetName)
        throws IOException {

        final byte[] bytes = readBytes(
            new byte[readLenght(scale)], BitIoConstants.UBYTE_SIZE_MAX);

        return new String(bytes, charsetName);
    }


    @Override
    public String readString(final String charsetName) throws IOException {

        return readString(BitIoConstants.LENGTH_SIZE_MAX, charsetName);
    }


//    @Override
//    public String readString(final String charsetName) throws IOException {
//
//        return readString(BitIoConstants.LENGTH_SIZE_MAX, charsetName);
//    }
    @Override
    public String readAscii(final int scale) throws IOException {

        final byte[] bytes = readBytes(new byte[readLenght(scale)], 7);

        return new String(bytes, "US-ASCII");
    }


    @Override
    public String readAscii() throws IOException {

        return readAscii(BitIoConstants.LENGTH_SIZE_MAX);
    }

//    @Override
//    public String readAscii() throws IOException {
//
//        return readAscii(BitIoConstants.LENGTH_SIZE_MAX);
//    }

    @Override
    public int align(final int bytes) throws IOException {

        BitIoConstraints.requireValidAlighBytes(bytes);

        int bits = 0; // number of bits to be discarded

        // discard remained bits in current byte.
        if (index < 8) {
            bits += (8 - index);
            readUnsignedByte(bits); // count increments
        }

        int octets = count % bytes;

        if (octets == 0) {
            return bits;
        }

        if (octets > 0) {
            octets += (bytes - octets);
        } else {
            octets += (0 - octets);
        }

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
    private int count = 0;

}

