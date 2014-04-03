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


package com.github.jinahya.io.bit;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


/**
 * A class for writing arbitrary length of bits.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public class BitOutput extends BitBase {


    /*
     *
     * @param <T> byte target type parameter. @param target byte target. @param
     * function byte output function.
     *
     * @return a new instance. public static <T> Supplier<BitOutput>
     * newInstance( final Supplier<T> target, final Function<T, ByteOutput>
     * function) {
     *
     * return () -> new BitOutput(function.apply(target.get())); }
     */
    /**
     * Creates a new instance supplying bytes to specified target.
     *
     * @param target the byte target.
     *
     * @return a new instance.
     */
    public static BitOutput newInstance(final OutputStream target) {

        if (target == null) {
            throw new NullPointerException("null target");
        }

        return new BitOutput(new StreamOutput(target));
    }


    public static BitOutput newInstance(final ByteBuffer target) {

        if (target == null) {
            throw new NullPointerException("null target");
        }

        return new BitOutput(new BufferOutput(target));
    }


    /**
     * Creates a new instance built on top of the specified byte input.
     *
     * @param output the byte input on which this bit output is built.
     *
     * @throws NullPointerException if the specified {@code output} is
     * {@code null}.
     */
    public BitOutput(final ByteOutput output) {

        super();

        if (output == null) {
            throw new NullPointerException("null output");
        }

        this.output = output;
    }


    /**
     * Writes given unsigned byte value to {@code output} and increments
     * {@code count}.
     *
     * @param value the unsigned byte value
     *
     * @throws IOException if an I/O error occurs.
     */
    private void octet(final int value) throws IOException {

        output.writeUnsignedByte(value);

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
     */
    protected void writeUnsignedByte(final int length, int value)
            throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        if (length > 8) {
            throw new IllegalArgumentException("length(" + length + ") > 8");
        }

        if (index == 0 && length == 8) {
            // direct write
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
    public void writeBoolean(final boolean value) throws IOException {

        writeUnsignedByte(1, value ? 0x01 : 0x00);
    }


    /**
     * Writes a boolean flag whether specified (subsequent) object is
     * {@code null} or not. This method writes either {@code true} if
     * {@code value} is {@code null} or {@code false} if {@code value} is not
     * {@code null}.
     *
     * @param value the (subsequent) object to check
     *
     * @return the written value; either {@code true} if {@code value} is
     * {@code null} or {@code false} if {@code value} is not {@code null}.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #isNotNull(java.lang.Object)
     */
    protected boolean isNull(final Object value) throws IOException {

        final boolean flag = value == null;

        writeBoolean(flag);

        return flag;
    }


    /**
     * Writes a boolean flag whether specified (subsequent) object is
     * {@code null} or not. The method writes either {@code true} if
     * {@code value} is not {@code null} or {@code false} if {@code value} is
     * {@code null}.
     *
     * @param value the (subsequent) object to check
     *
     * @return the written value; either {@code true} if {@code value} is not
     * {@code null} or {@code false} if {@code value} is {@code null}.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #isNull(java.lang.Object)
     */
    protected boolean isNotNull(final Object value) throws IOException {

        return !isNull(value);
    }


    /**
     * Writes an unsigned short value. Only the lower specified number of bits
     * in given {@code value} are written.
     *
     * @param length the number of lower bits to write; between {@code 0}
     * exclusive and {@code 16} inclusive.
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs
     */
    protected void writeUnsignedShort(final int length, final int value)
            throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        if (length > 16) {
            throw new IllegalArgumentException("length(" + length + ") > 16");
        }

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
     */
    public void writeUnsignedInt(final int length, final int value)
            throws IOException {

        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length >= 32) {
            throw new IllegalArgumentException("length(" + length + ") >= 32");
        }

        if (false && (value >> length) != 0x00) {
            throw new IllegalArgumentException(
                    "value(" + value + ") >> length(" + length + ") != 0x00");
        }

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
     */
    public void writeInt(final int length, final int value) throws IOException {

        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        }

        if (length > 32) {
            throw new IllegalArgumentException("length(" + length + ") > 32");
        }

        if (false && length != 32) {
            if (value < 0) { // negative
                if ((value >> (length - 1)) != -1) {
                    throw new IllegalArgumentException(
                            "value(" + value + ") >> (length(" + length
                            + ") - 1) != -1");
                }
            } else { // positive
                if ((value >> (length - 1)) != 0) {
                    throw new IllegalArgumentException(
                            "value(" + value + ") >> (length(" + length
                            + ") - 1) != 0");
                }
            }
        }

//        final int quotient = length / 16;
//        final int remainder = length % 16;
//
//        if (remainder > 0) {
//            writeUnsignedShort(remainder, value >> (quotient * 16));
//        }
//
//        for (int i = quotient - 1; i >= 0; i--) {
//            writeUnsignedShort(16, value >> (16 * i));
//        }
        writeUnsignedByte(1, value >> (length - 1));
        writeUnsignedInt((length - 1), value);
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
    public void writeFloat(final float value) throws IOException {

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
     */
    public void writeUnsignedLong(final int length, final long value)
            throws IOException {

        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length >= 64) {
            throw new IllegalArgumentException("length(" + length + ") >= 64");
        }

        if (false && (value >> length) != 0L) {
            throw new IllegalArgumentException(
                    "(value(" + value + ") >> length(" + length + ")) != 0L");
        }

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

        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        }

        if (length > 64) {
            throw new IllegalArgumentException("length(" + length + ") > 64");
        }

        if (false && length < 64) {
            if (value < 0L) { // negative
                if ((value >> (length - 1)) != -1L) {
                    throw new IllegalArgumentException(
                            "(value(" + value + ") >> (length(" + length
                            + ") - 1)) != -1L");
                }
            } else { // positive
                if ((value >> (length - 1)) != 0L) {
                    throw new IllegalArgumentException(
                            "(value(" + value + ") >> (length(" + length
                            + ") - 1)) != 0L");
                }
            }
        }

//        final int quotient = length / 16;
//        final int remainder = length % 16;
//
//        if (remainder > 0) {
//            writeUnsignedShort(remainder, (int) (value >> (quotient * 16)));
//        }
//
//        for (int i = quotient - 1; i >= 0; i--) {
//            writeUnsignedShort(16, (int) (value >> (i * 16)));
//        }
        writeUnsignedByte(1, (int) (value >> (length - 1)));
        writeUnsignedLong((length - 1), value);
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
    public void writeDouble(final double value) throws IOException {

        writeLong(64, Double.doubleToRawLongBits(value));
    }


    /**
     * Writes a specified number of bytes in given array starting at given
     * offset.
     *
     * @param range the number of valid bits in each byte; between 0 (exclusive)
     * and 8 (inclusive).
     * @param value the array of bytes
     * @param offset the starting offset in the array.
     * @param length the number of bytes to write
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeBytes(final int range, final byte[] value, int offset,
                    final int length)
            throws IOException {

        requireValidBytesRange(range);

        if (value == null) {
            throw new NullPointerException("null value");
        }

        if (offset < 0) {
            throw new IllegalArgumentException("offset(" + offset + ") < 0");
        }

        if (false && offset >= value.length) {
            throw new IllegalArgumentException(
                    "offset(" + offset + ") >= value.length(" + value.length
                    + ")");
        }

        if (length < 0) {
            throw new IllegalArgumentException("length(" + length + ") < 0");
        }

        if (offset + length > value.length) {
            throw new IllegalArgumentException(
                    "offset(" + offset + ") + length(" + length + ") = "
                    + (offset + length) + " > value.length(" + value.length
                    + ")");
        }

        for (int i = 0; i < length; i++) {
            writeUnsignedByte(range, value[offset++]);
        }
    }


    /**
     * Writes specified number of bytes in {@code value} starting from
     * {@code offset}.
     *
     * @param scale the number of bits required for length of the array; between
     * 0 exclusive and 16 inclusive.
     * @param range the number of lower valid bits in each byte; between 0
     * exclusive and 8 inclusive.
     * @param value the array of bytes to write.
     * @param offset the starting offset in byte array
     * @param length the number of bytes from {@code offset} to write
     *
     * @throws IllegalArgumentException if either {@code scale} or {@code range}
     * is not valid or {@code offset} is not valid, or {@code length} is not
     * valid.
     * @throws IOException if an I/O error occurs.
     */
    public void writeBytes(final int scale, final int range, final byte[] value,
                           int offset, final int length)
            throws IOException {

        requireValidBytesScale(scale);

        if (length < 0) {
            throw new IllegalArgumentException("length(" + length + ") < 0");
        }

        if ((length >> scale) > 0) {
            throw new IllegalArgumentException(
                    "length(" + length + ") >> scale(" + scale + ") = "
                    + (length >> scale) + " > 0");
        }

        writeUnsignedShort(scale, length);

        writeBytes(range, value, offset, length);
    }


    /**
     * Writes an array of bytes.
     *
     * @param scale the number of bits for array length between 0 (exclusive)
     * and 16 (inclusive).
     * @param range the number of lower bits in each byte to write between 0
     * (exclusive) and 8 (inclusive).
     * @param value the array of bytes to write
     *
     * @throws IOException if an I/O error occurs.
     */
    public void writeBytes(final int scale, final int range, final byte[] value)
            throws IOException {

        writeBytes(scale, range, value, 0, value.length);
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
    public void writeString(final String value, final String charsetName)
            throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        if (charsetName == null) {
            throw new NullPointerException("null charsetName");
        }

        writeBytes(BYTES_SCALE_MAX, BYTES_RANGE_MAX,
                   value.getBytes(charsetName));
    }


    public void writeString(final String value, final Charset charset)
            throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        if (charset == null) {
            throw new NullPointerException("null charset");
        }

        writeBytes(BYTES_SCALE_MAX, BYTES_RANGE_MAX, value.getBytes(charset));
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
    public void writeUsAsciiString(final String value) throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        writeBytes(BYTES_SCALE_MAX, 7, value.getBytes("US-ASCII"));
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
    public int align(final int length) throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        if (length > Short.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "length(" + length + ") > " + Short.MAX_VALUE);
        }

        int bits = 0;

        // writing(padding) remained bits into current byte
        if (index > 0) {
            bits = (8 - index);
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
     * The underlying byte input.
     */
    protected final ByteOutput output;


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

