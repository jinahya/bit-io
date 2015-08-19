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
 * A class for reading arbitrary length of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class AbstractBitInput implements BitInput, ByteInput {


    /**
     * Reads an unsigned byte from {@link #readUnsignedByte()} and increments
     * {@code count}.
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
     * @param length the number of bits for the value; between {@code 0}
     * (exclusive) and {@code 8} (inclusive).
     *
     * @return an unsigned byte value.
     *
     * @throws IllegalArgumentException if {@code length} is not valid.
     * @throws IOException if an I/O error occurs.
     *
     * @see #requireValidUnsignedByteLength(int)
     */
    protected int readUnsignedByte(final int length) throws IOException {

        BitIoConstraints.requireValidUnsignedByteLength(length);

        if (index == 8) {
            int octet = octet();
            if (length == 8) {
                return octet;
            }
            for (int i = 7; i >= 0; i--) {
                flags[i] = (octet & 0x01) == 0x01;
                octet >>= 1;
            }
            index = 0;
        }

        final int available = 8 - index;
        final int required = length - available;

        if (required > 0) {
            return (readUnsignedByte(available) << required)
                   | readUnsignedByte(required);
        }

        int value = 0x00;

        for (int i = 0; i < length; i++) {
            value <<= 1;
            value |= (flags[index++] ? 0x01 : 0x00);
        }

        return value;
    }


    /**
     * Reads a 1-bit boolean value. {@code true} for {@code 0b1} and
     * {@code false} for {@code 0b0}.
     *
     * @return a boolean value.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public boolean readBoolean() throws IOException {

        return readUnsignedByte(1) == 0x01;
    }


    /**
     * Reads an unsigned short value.
     *
     * @param length the number of bits for the value; between 0 (exclusive) and
     * 16 (inclusive).
     *
     * @return an unsigned short value.
     *
     * @throws IllegalArgumentException if {@code length} is not valid.
     * @throws IOException if an I/O error occurs.
     *
     * @see #requireValidUnsignedShortLength(int)
     */
    protected int readUnsignedShort(final int length) throws IOException {

        BitIoConstraints.requireValidUnsignedShortLength(length);

        int value = 0x00;

        final int quotient = length / 8;
        final int remainder = length % 8;

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
    public int readUnsignedInt(final int length) throws IOException {

        BitIoConstraints.requireValidUnsignedIntLength(length);

        int value = 0x00;

        final int quotient = length / 16;
        final int remainder = length % 16;

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
    public int readInt(final int length) throws IOException {

        BitIoConstraints.requireValidIntLength(length);

        final int unsigned = length - 1;

        return (readUnsignedByte(1) == 1 ? -1 << unsigned : 0)
               | readUnsignedInt(unsigned);
    }


//    /**
//     * Reads a float value.
//     *
//     * @return a float value.
//     *
//     * @throws IOException if an I/O error occurs
//     *
//     * @see Float#intBitsToFloat(int)
//     */
//    @Override
//    public float readFloat32() throws IOException {
//
//        return Float.intBitsToFloat(readInt(0x20));
//    }
    @Override
    public long readUnsignedLong(final int length) throws IOException {

        BitIoConstraints.requireValidUnsignedLongLength(length);

        long value = 0x00L;

        final int quotient = length / 31;
        final int remainder = length % 31;

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
    public long readLong(final int length) throws IOException {

        BitIoConstraints.requireValidLongLength(length);

        final int unsigned = length - 1;

        return (readBoolean() ? -1L << unsigned : 0L)
               | readUnsignedLong(unsigned);
    }


//    public long readLong32() throws IOException {
//
//        return readLong(64);
//    }
//    /**
//     * Reads a double value.
//     *
//     * @return a double value
//     *
//     * @throws IOException if an I/O error occurs.
//     *
//     * @see Double#longBitsToDouble(long)
//     */
//    @Override
//    public final double readDouble64() throws IOException {
//
//        return Double.longBitsToDouble(readLong(0x40));
//    }
//    protected void readBytesFully(final int length, final int range,
//                                  final ByteOutput output)
//        throws IOException {
//
//        if (length < 0) {
//            throw new IllegalArgumentException("length(" + length + " < 0");
//        }
//
//        BitIoConstraints.requireValidBytesRange(range);
//
//        if (output == null) {
//            throw new NullPointerException("null output");
//        }
//
//        for (int i = 0; i < length; i++) {
//            output.writeUnsignedByte(readUnsignedByte(range));
//        }
//    }
//    protected int readBytesLength(final int scale) throws IOException {
//
//        return readUnsignedInt(BitIoConstraints.requireValidBytesScale(scale));
//    }
//    /**
//     * Reads a sequence of bytes and writes to specified byte output.
//     *
//     * @param scale the number of bits required for calculating the number of
//     * bytes to read; between 0 (exclusive) and 16 (inclusive).
//     * @param range the number of valid bits in each byte; between 0 (exclusive)
//     * and 8 (inclusive).
//     * @param output the bytes output.
//     *
//     * @return number of bytes written to {@code output}.
//     *
//     * @throws IOException if an I/O error occurs.
//     */
//    protected int readBytes(final int scale, final int range,
//                            final ByteOutput output)
//        throws IOException {
//
//        BitIoConstraints.requireValidBytesScale(scale);
//
//        BitIoConstraints.requireValidBytesRange(range);
//
//        if (output == null) {
//            throw new NullPointerException("null output");
//        }
//
//        final int length = readBytesLength(scale);
//        readBytesFully(length, range, output);
//
//        return length;
//    }
    @Override
    public byte[] readBytes(final int scale, final int range)
        throws IOException {

        BitIoConstraints.requireValidBytesScale(scale);
        BitIoConstraints.requireValidBytesRange(range);

        final byte[] value = new byte[readUnsignedInt(scale)];

        for (int i = 0; i < value.length; i++) {
            value[i] = (byte) readUnsignedByte(range);
        }

        return value;
    }


    @Override
    public String readString(final String charsetName) throws IOException {

        if (charsetName == null) {
            throw new NullPointerException("null charsetName");
        }

        return new String(readBytes(BitIoConstants.BYTES_SCALE_MAX,
                                    BitIoConstants.BYTES_RANGE_MAX),
                          charsetName);
    }


    @Override
    public String readAscii() throws IOException {

        return new String(readBytes(BitIoConstants.BYTES_SCALE_MAX, 7),
                          "US-ASCII");
    }


    @Override
    public int align(final int length) throws IOException {

        BitIoConstraints.requireValidAlighLength(length);

        int bits = 0; // number of bits to be discarded

        // discard remained bits in current byte.
        if (index < 8) {
            bits += (8 - index);
            readUnsignedByte(bits); // count increments
        }

        int bytes = count % length;

        if (bytes == 0) {
            return bits;
        }

        if (bytes > 0) {
            bytes += (length - bytes);
        } else {
            bytes += (0 - bytes);
        }

        for (; bytes > 0; bytes--) {
            readUnsignedByte(8);
            bits += 8;
        }

        return bits;
    }


//    /**
//     * Returns the number of bytes read from the underlying byte input so far.
//     *
//     * @return the number of bytes read so far.
//     */
//    @Override
//    public long getCount() {
//
//        return count;
//    }
    /**
     * The array of bit flags.
     */
    private final boolean[] flags = new boolean[8];


    /**
     * The next bit index to read.
     */
    private int index = 8;


    /**
     * The number of bytes read so far.
     */
    private int count = 0;


}

