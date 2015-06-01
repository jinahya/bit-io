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


package com.github.jinahya.bio;


//import static com.github.jinahya.bio.BioConstraints.requireValidBytesRange;
//import static com.github.jinahya.bio.BioConstraints.requireValidBytesScale;
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
    //protected abstract void octet(final int value) throws IOException;
    protected void octet(final int value) throws IOException {

        writeUnsignedByte(value);

        ++count;
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

        BioConstraints.requireValidUnsignedByteLength(length);

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
            flags[i] = (value & 0x01) == 0x01 ? true : false;
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
     * Writes a 1-bit boolean value. {@code 0b1} for {@code true} and
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

        BioConstraints.requireValidUnsignedShortLength(length);

        final int quotient = length / 8;
        final int remainder = length % 8;

        if (remainder > 0) {
            writeUnsignedByte(remainder, value >> (quotient * 8));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedByte(8, value >> (8 * i));
        }
    }


    /**
     * Writes an unsigned int value. Only the lower specified number of bits in
     * {@code value} are written.
     *
     * @param length the number of lower bits to write; between {@code 1}
     * inclusive and {@code 32} exclusive.
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #requireValidUnsignedIntLength(int)
     */
    public void writeUnsignedInt(final int length, final int value)
        throws IOException {

        BioConstraints.requireValidUnsignedIntLength(length);

        final int quotient = length / 16;
        final int remainder = length % 16;

        if (remainder > 0) {
            writeUnsignedShort(remainder, value >> (quotient * 16));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedShort(16, value >> (16 * i));
        }
    }


    /**
     * Writes a signed int value. Only the number of specified bits in
     * {@code value} are written including the sign bit at {@code length}.
     *
     * @param length the number of lower bits in {@code value} to write; between
     * {@code 1} exclusive and {@code 32} inclusive.
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #requireValidIntLength(int)
     */
    public void writeInt(final int length, final int value) throws IOException {

        BioConstraints.requireValidIntLength(length);

        if (false) {
            writeUnsignedByte(1, value >> 0x1F); // 31
            writeUnsignedInt((length - 1), value);
            return;
        }

        final int unsigned = length - 1;
        writeUnsignedByte(1, value >> unsigned);
        writeUnsignedInt(unsigned, value);
    }


    /**
     * Writes a float value.
     *
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Float#floatToIntBits(float)
     */
    public void writeFloat32(final float value) throws IOException {

        writeInt(32, Float.floatToIntBits(value));
    }


    /**
     * Writes a float value.
     *
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Float#floatToRawIntBits(float)
     */
    public void writeFloat32Raw(final float value) throws IOException {

        writeInt(32, Float.floatToRawIntBits(value));
    }


    /**
     * Writes an unsigned long value. Only the number of specified bits in
     * {@code value} are writtern.
     *
     * @param length the number of lower valid bits to write; between 1
     * (inclusive) and 64 (exclusive).
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #requireValidUnsignedLongLength(int)
     */
    public void writeUnsignedLong(final int length, final long value)
        throws IOException {

        BioConstraints.requireValidUnsignedLongLength(length);

        final int quotient = length / 31;
        final int remainder = length % 31;

        if (remainder > 0) {
            writeUnsignedInt(remainder, (int) (value >> (quotient * 31)));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedInt(31, (int) (value >> (i * 31)));
        }
    }


    /**
     * Writes a signed long value. Only the number of specified bits in
     * {@code value} are written including the sign bit at {@code length}.
     *
     * @param length the number of lower valid bits to write; between 1
     * (exclusive) and 64 (inclusive).
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void writeLong(final int length, final long value)
        throws IOException {

        BioConstraints.requireValidLongLength(length);

        if (false) {
            writeUnsignedLong(1, value >> 0x3F); // 63
            writeUnsignedLong(length - 1, value);
            return;
        }

        final int unsigned = length - 1;
        writeUnsignedByte(1, (int) (value >> unsigned));
        writeUnsignedLong(unsigned, value);
    }


    /**
     * Writes a double value.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Double#doubleToLongBits(double)
     */
    public void writeDouble64(final double value) throws IOException {

        writeLong(64, Double.doubleToLongBits(value));
    }


    /**
     * Writes a double value.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Double#doubleToRawLongBits(double)
     */
    @Override
    public void writeDouble64Raw(final double value) throws IOException {

        writeLong(64, Double.doubleToRawLongBits(value));
    }


    @Override
    public void writeBytesFully(final int length, final int range,
                                final ByteInput input)
        throws IOException {

        if (length < 0) {
            throw new IllegalArgumentException("length(" + length + ") < 0");
        }

        BioConstraints.requireValidBytesRange(range);

        if (input == null) {
            throw new NullPointerException("null input");
        }

        for (int i = 0; i < length; i++) {
            writeUnsignedByte(range, input.readUnsignedByte());
        }
    }


    protected void writeBytesLength(final int scale, int length)
        throws IOException {

        BioConstraints.requireValidBytesScale(scale);

        writeUnsignedInt(scale, length);
    }


    protected void writeBytes(final int scale, final int length,
                              final int range, final ByteInput input)
        throws IOException {

        writeBytesLength(scale, length);
        writeBytesFully(length, range, input);
    }


    @Override
    public void writeBytes(final int scale, final int range, final byte[] value)
        throws IOException {

        writeBytes(scale, value.length, range,
                   new ArrayInput(value, 0, value.length));
    }


    /**
     * Writes a string value. This method writes the decoded byte array with
     * {@code scale} of {@code 16} and {@code range} of {@code 8}.
     *
     * @param value the string value to write.
     * @param charsetName the character set name to decode the string
     *
     * @throws NullPointerException if {@code value} or {@code charsetName} is
     * {@code null}
     * @throws IOException if an I/O error occurs.
     *
     * @see #writeBytes(int, int, byte[])
     */
    @Override
    public void writeString(final String value, final String charsetName)
        throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        if (charsetName == null) {
            throw new NullPointerException("null charsetName");
        }

        writeBytes(BioConstants.SCALE_MAX, BioConstants.RANGE_MAX,
                   value.getBytes(charsetName));
    }


    /**
     * Writes a {@code US-ASCII} encoded string value. This method writes the
     * decoded byte array with {@code scale} of {@code 16} and {@code range} of
     * {@code 7}.
     *
     * @param value the string value to write.
     *
     * @throws NullPointerException if {@code value} is {@code null}.
     * @throws IOException if an I/O error occurs.
     *
     * @see #writeBytes(int, int, byte[])
     */
    @Override
    public void writeUsAsciiString(final String value) throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        writeBytes(BioConstants.SCALE_MAX, 7, value.getBytes("US-ASCII"));
    }


    /**
     * Aligns to specified number of bytes.
     *
     * @param length the number of bytes to align; between 0 (exclusive) and
     * {@value java.lang.Short#MAX_VALUE} (inclusive).
     *
     * @return the number of bits padded for alignment
     *
     * @throws IllegalArgumentException if {@code length} is not valid.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int align(final int length) throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        if (length > Short.MAX_VALUE) {
            throw new IllegalArgumentException(
                "length(" + length + ") > " + Short.MAX_VALUE);
        }

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


    /**
     * Returns the number of bytes written to the underlying byte output so far.
     *
     * @return the number of bytes written so far.
     */
    public long getCount() {

        return count;
    }


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

