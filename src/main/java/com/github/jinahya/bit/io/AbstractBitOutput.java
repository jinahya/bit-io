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
 * A abstract class implementing {@link IBitInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class AbstractBitOutput implements BitOutput, ByteOutput {


    /**
     * Consumes given unsigned byte value.
     *
     * @param value the unsigned byte value
     *
     * @throws IOException if an I/O error occurs.
     */
    protected void octet(final int value) throws IOException {

        writeUnsignedByte(value);

        count++;
    }


    /**
     * Writes an unsigned byte value. Only the lower {@code length} bits in
     * given {@code value} are written.
     *
     * @param length the number of lower bits to write; between 0 (exclusive)
     * and 8 (inclusive).
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #requireValidUnsignedByteLength(int)
     */
    protected void writeUnsignedByte(final int length, int value)
        throws IOException {

        BitIoConstraints.requireValidUnsignedByteLength(length);

        if (length == 8 && index == 0) {
            octet(value);
            return;
        }

        final int required = length - (8 - index);
        if (required > 0) {
            writeUnsignedByte(length - required, value >> required);
            writeUnsignedByte(required, value);
            return;
        }

        for (int i = index + length - 1; i >= index; i--) {
            flags[i] = (value & 0x01) == 0x01;
            value >>= 1;
        }
        index += length;

        if (index == 8) {
            int octet = 0x00;
            for (int i = 0; i < 8; i++) {
                octet <<= 1;
                octet |= (flags[i] ? 0x01 : 0x00);
            }
            octet(octet);
            index = 0;
        }
    }


    /**
     * Writes a 1-bit boolean value. Writes {@code 0b1} for {@code true} and
     * {@code 0b0} for {@code false}.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeBoolean(final boolean value) throws IOException {

        writeUnsignedByte(1, value ? 0x01 : 0x00);
    }


//    /**
//     * Writes a boolean flag whether specified (subsequent) object is
//     * {@code null} or not. This method writes either {@code true} if
//     * {@code value} is {@code null} or {@code false} if {@code value} is not
//     * {@code null}.
//     *
//     * @param value the (subsequent) object to check
//     *
//     * @return the written value; either {@code true} if {@code value} is
//     * {@code null} or {@code false} if {@code value} is not {@code null}.
//     *
//     * @throws IOException if an I/O error occurs.
//     *
//     * @see #isNotNull(java.lang.Object)
//     */
//    protected boolean isNull(final Object value) throws IOException {
//
//        final boolean flag = value == null;
//
//        writeBoolean(flag);
//
//        return flag;
//    }
//
//
//    /**
//     * Writes a boolean flag whether specified (subsequent) object is
//     * {@code null} or not. The method writes either {@code true} if
//     * {@code value} is not {@code null} or {@code false} if {@code value} is
//     * {@code null}.
//     *
//     * @param value the (subsequent) object to check
//     *
//     * @return the written value; either {@code true} if {@code value} is not
//     * {@code null} or {@code false} if {@code value} is {@code null}.
//     *
//     * @throws IOException if an I/O error occurs.
//     *
//     * @see #isNull(java.lang.Object)
//     */
//    protected boolean isNotNull(final Object value) throws IOException {
//
//        return !isNull(value);
//    }
    /**
     * Writes an unsigned short value. Only the lower specified number of bits
     * in given {@code value} are written.
     *
     * @param length the number of lower bits to write; between {@code 0}
     * exclusive and {@code 16} inclusive.
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs
     *
     * @see #requireValidUnsignedShortLength(int)
     */
    protected void writeUnsignedShort(final int length, final int value)
        throws IOException {

        BitIoConstraints.requireValidUnsignedShortLength(length);

        final int quotient = length / 8;
        final int remainder = length % 8;

        if (remainder > 0) {
            writeUnsignedByte(remainder, value >> (quotient * 8));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedByte(8, value >> (8 * i));
        }
    }


    @Override
    public void writeUnsignedInt(final int length, final int value)
        throws IOException {

        BitIoConstraints.requireValidUnsignedIntLength(length);

        final int quotient = length / 16;
        final int remainder = length % 16;

        if (remainder > 0) {
            writeUnsignedShort(remainder, value >> (quotient * 16));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedShort(16, value >> (16 * i));
        }
    }


    @Override
    public void writeInt(final int length, final int value) throws IOException {

        BitIoConstraints.requireValidIntLength(length);

        final int unsigned = length - 1;
        writeUnsignedByte(1, value >> unsigned);
        writeUnsignedInt(unsigned, value);
    }


//    /**
//     * Writes a float value.
//     *
//     * @param value the value to write.
//     *
//     * @throws IOException if an I/O error occurs.
//     *
//     * @see Float#floatToIntBits(float)
//     */
//    public void writeFloat32(final float value) throws IOException {
//
//        writeInt(32, Float.floatToIntBits(value));
//    }
//    /**
//     * Writes a float value.
//     *
//     * @param value the value to write.
//     *
//     * @throws IOException if an I/O error occurs.
//     *
//     * @see Float#floatToRawIntBits(float)
//     */
//    public void writeFloat32Raw(final float value) throws IOException {
//
//        writeInt(32, Float.floatToRawIntBits(value));
//    }
    @Override
    public void writeUnsignedLong(final int length, final long value)
        throws IOException {

        BitIoConstraints.requireValidUnsignedLongLength(length);

        final int quotient = length / 31;
        final int remainder = length % 31;

        if (remainder > 0) {
            writeUnsignedInt(remainder, (int) (value >> (quotient * 31)));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedInt(31, (int) (value >> (i * 31)));
        }
    }


    @Override
    public void writeLong(final int length, final long value)
        throws IOException {

        BitIoConstraints.requireValidLongLength(length);

        final int unsigned = length - 1;
        writeUnsignedByte(1, (int) (value >> unsigned));
        writeUnsignedLong(unsigned, value);
    }


//    /**
//     * Writes a double value.
//     *
//     * @param value the value to write
//     *
//     * @throws IOException if an I/O error occurs.
//     *
//     * @see Double#doubleToLongBits(double)
//     */
//    public void writeDouble64(final double value) throws IOException {
//
//        writeLong(64, Double.doubleToLongBits(value));
//    }
//    /**
//     * Writes a double value.
//     *
//     * @param value the value to write
//     *
//     * @throws IOException if an I/O error occurs.
//     *
//     * @see Double#doubleToRawLongBits(double)
//     */
//    @Override
//    public void writeDouble64Raw(final double value) throws IOException {
//
//        writeLong(64, Double.doubleToRawLongBits(value));
//    }
//    /**
//     * Reads specified number of bytes from given input.
//     *
//     * @param length the number of bytes to read.
//     * @param range the valid bit range in each bytes.
//     * @param input the byte input
//     *
//     * @throws IOException if an I/O error occurs.
//     */
//    protected void writeBytesFully(final int length, final int range,
//                                   final ByteInput input)
//        throws IOException {
//
//        if (length < 0) {
//            throw new IllegalArgumentException("length(" + length + ") < 0");
//        }
//
//        BitIoConstraints.requireValidBytesRange(range);
//
//        if (input == null) {
//            throw new NullPointerException("null input");
//        }
//
//        for (int i = 0; i < length; i++) {
//            writeUnsignedByte(range, input.readUnsignedByte());
//        }
//    }
//    protected void writeBytesLength(final int scale, int length)
//        throws IOException {
//
//        BitIoConstraints.requireValidBytesScale(scale);
//
//        writeUnsignedInt(scale, length);
//    }
    @Override
    public void writeBytes(final int scale, final int range, final byte[] value)
        throws IOException {

        BitIoConstraints.requireValidBytesScale(scale);

        BitIoConstraints.requireValidBytesRange(range);

        if (value == null) {
            throw new NullPointerException("null value");
        }
        if (value.length >> scale != 0) {
            throw new IllegalArgumentException(
                "value.length(" + value.length + ") >> scale(" + scale
                + " != 0");
        }

        writeUnsignedInt(scale, value.length);

        for (int i = 0; i < value.length; i++) {
            writeUnsignedByte(range, value[i]);
        }
    }


    @Override
    public void writeString(final String value, final String charsetName)
        throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        if (charsetName == null) {
            throw new NullPointerException("null charsetName");
        }

        writeBytes(BitIoConstants.BYTES_SCALE_MAX,
                   BitIoConstants.BYTES_RANGE_MAX, value.getBytes(charsetName));
    }


    @Override
    public void writeAscii(final String value) throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        writeBytes(BitIoConstants.BYTES_SCALE_MAX, 7,
                   value.getBytes("US-ASCII"));
    }


    @Override
    public int align(final int length) throws IOException {

        BitIoConstraints.requireValidAlighLength(length);

        int bits = 0; // number of bits to be padded

        // pad remained bits into current byte
        if (index > 0) {
            bits += (8 - index);
            writeUnsignedByte(bits, 0x00); // count incremented
        }

        long bytes = count % length;

        if (bytes == 0) {
            return bits;
        }

        if (bytes > 0) {
            bytes = length - bytes;
        } else {
            bytes = 0 - bytes;
        }

        for (; bytes > 0; bytes--) {
            writeUnsignedByte(8, 0x00);
            bits += 8;
        }

        return bits;
    }


//    /**
//     * Returns the number of bytes written to the underlying byte output so far.
//     *
//     * @return the number of bytes written so far.
//     */
//    public long getCount() {
//
//        return count;
//    }
    /**
     * bit flags.
     */
    private final boolean[] flags = new boolean[8];


    /**
     * bit index to write.
     */
    private int index = 0;


    /**
     * number of bytes written so far.
     */
    private long count = 0;


}

