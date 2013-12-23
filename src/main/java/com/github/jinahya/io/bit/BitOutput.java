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


import java.io.Closeable;
import java.io.IOException;


/**
 * A wrapper class for writing arbitrary length of bits.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 * @param <T> underlying byte target type parameter
 */
public class BitOutput<T> implements Closeable {


    /**
     * Creates a new instance on top of specified byte output.
     *
     * @param output the byte output on which this bit input is built or
     * {@code null} if {@link #output} may be lazily initialized and set.
     */
    public BitOutput(final ByteOutput<T> output) {

        super();

        this.output = output;
    }


    /**
     * Writes given unsigned byte to underlying byte output and increments
     * count. Override this method if {@link #output} must be lazily initialized
     * and set.
     *
     * @param value the value to write
     *
     * @throws IllegalStateException if {@link #output} is currently
     * {@code null}.
     * @throws IOException if an I/O error occurs.
     */
    protected void writeUnsignedByte(final int value) throws IOException {

        if (output == null) {
            throw new IllegalStateException("the output is currently null");
        }

        output.writeUnsignedByte(value);

        count++;
    }


    /**
     * Writes an unsigned byte. Only the lower {@code length} bits in given
     * {@code value} are written.
     *
     * @param length bit length between 0 (exclusive) and 8 (inclusive).
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
            writeUnsignedByte(value);
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
            writeUnsignedByte(octet);
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
     * Writes a boolean flag whether specified object, which is the subsequent
     * object, is {@code null} or not.
     *
     * @param value the object to check
     *
     * @return {@code true} if {@code value} is {@code} or {@code false} if not.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected boolean isNull(final Object value) throws IOException {

        final boolean flag = value == null;

        writeUnsignedByte(1, flag ? 0x00 : 0x01);

        return flag;
    }


    protected boolean isNotNull(final Object value) throws IOException {

        return !isNull(value);
    }


    /**
     * Writes an unsigned short value. Only the lower specified number of bits
     * in given {@code value} are written.
     *
     * @param length the number of bits between {@code 0} exclusive and
     * {@code 16} inclusive.
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
     * Writes an unsigned int value. The value must be valid in bit range.
     *
     * @param length bit length between {@code 1} inclusive and {@code 32}
     * exclusive.
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

        if ((value >> length) != 0x00) {
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
     * Writes a signed int value. The {@code value} must be valid in bit range.
     *
     * @param length bit length between {@code 1} exclusive and {@code 32}
     * inclusive.
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

        if (length != 32) {
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
     * Writes an unsigned long value. The {@code value} must be valid in bit
     * range.
     *
     * @param length bit length between 1 (inclusive) and 64 (exclusive).
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

        if ((value >> length) != 0L) {
            throw new IllegalArgumentException(
                "(value(" + value + ") >> length(" + length + ")) != 0L");
        }

        final int quotient = length / 16;
        final int remainder = length % 16;

        if (remainder > 0) {
            writeUnsignedShort(remainder, (int) (value >> (quotient * 16)));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedShort(16, (int) (value >> (i * 16)));
        }
    }


    /**
     * Writes a signed long value. The {@code value} must be valid in bit range.
     *
     * @param length bit length between 1 (exclusive) and 64 (inclusive).
     * @param value the value whose lower {@code length}-bits are written.
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

        if (length < 64) {
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

        final int quotient = length / 16;
        final int remainder = length % 16;

        if (remainder > 0) {
            writeUnsignedShort(remainder, (int) (value >> (quotient * 16)));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedShort(16, (int) (value >> (i * 16)));
        }
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
     * is not valid, or {@code value} is too long.
     * @throws IOException if an I/O error occurs.
     */
    public void writeBytes(final int scale, final int range, final byte[] value,
                           int offset, final int length)
        throws IOException {

        if (scale <= 0) {
            throw new IllegalArgumentException("scale(" + scale + ") <= 0");
        }

        if (scale > 16) {
            throw new IllegalArgumentException("scale(" + scale + ") > 16");
        }

        if (range <= 0) {
            throw new IllegalArgumentException("range(" + range + ") <= 0");
        }

        if (range > 8) {
            throw new IllegalArgumentException("range(" + range + ") > 8");
        }

        if (value == null) {
            throw new NullPointerException("value == null");
        }

        if (offset < 0) {
            throw new IllegalArgumentException("offset(" + offset + ") < 0");
        }

        if (length < 0) {
            throw new IllegalArgumentException("length(" + length + ") < 0");
        }

        if (offset + length > value.length) {
            throw new IllegalArgumentException(
                "offset(" + offset + ") + length(" + length + ") = "
                + (offset + length) + ") > value.length(" + value.length + ")");
        }

        if ((length >> scale) > 0) {
            throw new IllegalArgumentException(
                "length(" + length + ") >> scale(" + scale + ") = "
                + (length >> scale) + " > 0");
        }

        writeUnsignedShort(scale, length);

        for (int i = 0; i < length; i++) {
            writeUnsignedByte(range, value[offset++]);
        }
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


    public void writeBytes(final byte[] bytes) throws IOException {

        writeBytes(16, 8, bytes);
    }


    /**
     * Writes a string with {@code scale} of 16 and {@code range} of 8.
     *
     * @param value the string to write.
     * @param charsetName the charset name to decode the string
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #writeBytes(int, int, byte[])
     */
    public void writeString(final String value, final String charsetName)
        throws IOException {

        writeBytes(16, 8, value.getBytes(charsetName));
    }


    /**
     * Writes a {@code US-ASCII} encoded string with {@code scale} of 16 and
     * {@code range} of 7.
     *
     * @param value the string to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #writeBytes(int, int, byte[])
     */
    public void writeUsAsciiString(final String value) throws IOException {

        writeBytes(16, 7, value.getBytes("US-ASCII"));
    }


    /**
     * Aligns to specified number of bytes.
     *
     * @param length the number of bytes to align. must be positive.
     *
     * @return the number of bits padded for alignment
     *
     * @throws IOException if an I/O error occurs.
     */
    public int align(final short length) throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        int bits = 0;

        // writing(padding) remained bits into current byte
        if (index > 0) {
            bits = (8 - index);
            writeUnsignedByte(bits, 0x00); // count incremented
        }

        int bytes = count % length;

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
     * Closes this instance. This method aligns to a single byte and closes
     * {@code output}.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #align(short)
     * @see ByteOutput#close()
     */
    @Override
    public void close() throws IOException {

        align((short) 1);

        output.close();
    }


    public ByteOutput<T> getOutput() {

        return output;
    }


    public void setOutput(final ByteOutput<T> output) {

        this.output = output;
    }


    /**
     * Returns the number of bytes written to the underlying byte output so far.
     *
     * @return the number of bytes written to the underlying byte output so far.
     */
    public int getCount() {

        return count;
    }


    /**
     * target byte output.
     */
    protected ByteOutput<T> output;


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
    private int count = 0;


}

