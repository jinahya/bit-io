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
import java.io.EOFException;
import java.io.IOException;


/**
 * A wrapper class for reading arbitrary length of bits.
 *
 * @author <a href="mailto:jinahya@gmail.com">Jin Kwon</a>
 * @param <T> underlying byte source type parameter
 */
public class BitInput<T> implements Closeable {


    /**
     * Creates a new instance on top of specified byte reader.
     *
     * @param reader the byte reader on which this bit input is built or
     * {@code null} for lazy initialization.
     */
    public BitInput(final ByteReader<T> reader) {

        super();

        this.reader = reader;
    }


    /**
     * Reads an unsigned byte from the {@link #reader} and increments the
     * {@code count}. Override this method if the {@link #reader} is intended to
     * be lazily initialized and set.
     *
     * @return an unsigned byte value read
     *
     * @throws IllegalStateException if {@link #reader} is currently
     * {@code null}.
     * @throws IOException if an I/O error occurs.
     */
    protected int octet() throws IOException {

        if (reader == null) {
            throw new IllegalStateException("#reader is currently null");
        }

        final int value = reader.readUnsignedByte();
        if (value == -1) {
            throw new EOFException("eof");
        }

        count++;

        return value;
    }


    /**
     * Reads an unsigned byte value.
     *
     * @param length the number of valid bits in value; between {@code 0}
     * (exclusive) and {@code 8} (inclusive).
     *
     * @return an unsigned byte value.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected int readUnsignedByte(final int length) throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        if (length > 8) {
            throw new IllegalArgumentException("length(" + length + ") > 8");
        }

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
    public boolean readBoolean() throws IOException {

        return readUnsignedByte(1) == 0x01;
    }


    /**
     * Reads a boolean flag for nullability of the subsequent object.
     *
     * @return {@code true} if the subsequent object is {@code null} or
     * {@code false} if the subsequent object is not {@code null}.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #isNotNull()
     */
    protected boolean isNull() throws IOException {

        return readBoolean();
    }


    /**
     * Reads a boolean flag for nullability of the subsequent object.
     *
     * @return {@code true} if the subsequent object is not {@code null} or
     * {@code false} if the subsequent object is {@code null}.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #isNull()
     */
    protected boolean isNotNull() throws IOException {

        return !isNull();
    }


    /**
     * Reads an unsigned short value.
     *
     * @param length the number of bits between 0 (exclusive) and 16
     * (inclusive).
     *
     * @return the unsigned short value.
     *
     * @throws IllegalArgumentException if {@code length} is not valid.
     * @throws IOException if an I/O error occurs.
     */
    protected int readUnsignedShort(final int length) throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        if (length > 16) {
            throw new IllegalArgumentException("length(" + length + ") > 16");
        }

        final int quotient = length / 8;
        final int remainder = length % 8;

        int value = 0x00;

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


    /**
     * Reads an unsigned int value.
     *
     * @param length the number of bits between 1 (inclusive) and 32
     * (exclusive).
     *
     * @return the unsigned int value
     *
     * @throws IllegalArgumentException if {@code length} is not valid.
     * @throws IOException if an I/O error occurs
     */
    public int readUnsignedInt(final int length) throws IOException {

        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length >= 32) {
            throw new IllegalArgumentException("length(" + length + ") >= 32");
        }

        final int quotient = length / 16;
        final int remainder = length % 16;

        int value = 0x00;

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


    /**
     * Reads a signed int value.
     *
     * @param length the number of bits between 1 (exclusive) and 32
     * (inclusive).
     *
     * @return a signed int value.
     *
     * @throws IOException if an I/O error occurs.
     */
    public int readInt(final int length) throws IOException {

        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        }

        if (length > 32) {
            throw new IllegalArgumentException("length(" + length + ") > 32");
        }

        return (((readBoolean() ? ~0 : 0) << (length - 1))
                | readUnsignedInt(length - 1));
    }


    /**
     * Reads a float value.
     *
     * @return a float value.
     *
     * @throws IOException if an I/O error occurs
     *
     * @see Float#intBitsToFloat(int)
     */
    public float readFloat() throws IOException {

        return Float.intBitsToFloat(readInt(32));
    }


    /**
     * Reads an unsigned long value.
     *
     * @param length the number of bits between 1 (inclusive) and 64
     * (exclusive).
     *
     * @return an unsigned long value.
     *
     * @throws IllegalArgumentException if {@code length} is not valid
     * @throws IOException if an I/O error occurs
     */
    public long readUnsignedLong(final int length) throws IOException {

        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length >= 64) {
            throw new IllegalArgumentException("length(" + length + ") >= 64");
        }

        final int quotient = length / 31;
        final int remainder = length % 31;

        long value = 0x00L;

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


    /**
     * Reads a signed long value.
     *
     * @param length the number of bits between 1 (exclusive) and 64
     * (inclusive).
     *
     * @return a signed long value
     *
     * @throws IllegalArgumentException if {@code length} is not valid
     * @throws IOException if an I/O error occurs.
     */
    public long readLong(final int length) throws IOException {

        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        }

        if (length > 64) {
            throw new IllegalArgumentException("length(" + length + ") > 64");
        }

        return (((readBoolean() ? -1L : 0L) << (length - 1))
                | readUnsignedLong(length - 1));

    }


    /**
     * Reads a double value.
     *
     * @return a double value
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Double#longBitsToDouble(long)
     */
    public final double readDouble() throws IOException {

        return Double.longBitsToDouble(readLong(0x40));
    }


    /**
     * Reads a series of bytes.
     *
     * @param range the number of valid bits in each byte; between 0 (exclusive)
     * and 8 (inclusive).
     * @param value the array to which each byte are stored.
     * @param offset starting offset in the array.
     * @param length the number of bytes to read
     *
     * @throws IOException if an I/O error occurs.
     */
    protected void readBytes(final int range, final byte[] value, int offset,
                             final int length)
        throws IOException {

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

        if (offset > value.length) {
            throw new IllegalArgumentException(
                "offset(" + offset + ") >= value.length(" + value.length + ")");
        }

        if (length < 0) {
            throw new IllegalArgumentException("length(" + length + ") < 0");
        }

        if (offset + length > value.length) {
            throw new IllegalArgumentException(
                "offset(" + offset + ") + length(" + length + ") = "
                + (offset + length) + " > value.length(" + value.length + ")");
        }

        for (int i = 0; i < length; i++) {
            value[offset++] = (byte) readUnsignedByte(range);
        }
    }


    /**
     * Reads a series of bytes.
     *
     * @param scale the number of bits required for calculating the number of
     * bytes to read; between 0 (exclusive) and 16 (inclusive).
     * @param range the number of valid bits in each byte; between 0 (exclusive)
     * and 8 (inclusive).
     * @param value the array to which each byte are stored
     * @param offset starting offset in the array
     *
     * @return the number of bytes read which is
     * {@code readUnsignedShort(scale)}
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #readBytes(int, byte[], int, int)
     */
    public int readBytes(final int scale, final int range, final byte[] value,
                         final int offset)
        throws IOException {

        if (scale <= 0) {
            throw new IllegalArgumentException("scale(" + scale + ") <= 0");
        }

        if (scale > 16) {
            throw new IllegalArgumentException("scale(" + scale + ") > 16");
        }

        final int length = readUnsignedShort(scale);

        readBytes(range, value, offset, length);

        return length;
    }


    /**
     * Reads an array of bytes.
     *
     * @param scale the number of bits for calculating the number of bytes to
     * read; between 0 (exclusive) and 16 (inclusive).
     * @param range the number of valid bits in each byte; between 0 (exclusive)
     * and 8 (inclusive).
     *
     * @return an array of bytes.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #readBytes(int, byte[], int, int)
     */
    public byte[] readBytes(final int scale, final int range)
        throws IOException {

        if (scale <= 0) {
            throw new IllegalArgumentException("scale(" + scale + ") <= 0");
        }

        if (scale > 16) {
            throw new IllegalArgumentException("scale(" + scale + ") > 16");
        }

        final byte[] value = new byte[readUnsignedShort(scale)];

        readBytes(range, value, 0, value.length);

        return value;
    }


    /**
     * Reads a string. This method reads a byte array via
     * {@link #readBytes(int, int)} with {@code scale} of {@code 16} and
     * {@code range} of {@code 8} and returns the output string created by
     * {@link String#String(byte[], java.lang.String)} with the byte array and
     * given {@code charsetName}.
     *
     * @param charsetName the character set name to encode output string.
     *
     * @return a {@code charsetName} encoded string.
     *
     * @throws NullPointerException if {@code charsetName} is {@code null}.
     * @throws IOException if an I/O error occurs.
     */
    public String readString(final String charsetName) throws IOException {

        if (charsetName == null) {
            throw new NullPointerException("null charsetName");
        }

        return new String(readBytes(16, 8), charsetName);
    }


    /**
     * Reads a US-ASCII string. This method reads a byte array via
     * {@link #readBytes(int, int)} with {@code scale} of {@code 16} and
     * {@code range} of {@code 7} and returns the output string created by
     * {@link String#String(byte[], java.lang.String)} with the byte array and
     * {@code US-ASCII}.
     *
     * @return a {@code US-ASCII} encoded string.
     *
     * @throws IOException if an I/O error occurs.
     */
    public String readUsAsciiString() throws IOException {

        return new String(readBytes(16, 7), "US-ASCII");
    }


    /**
     * Aligns to given number of bytes.
     *
     * @param length the number of bytes to align; between 0 (exclusive) and
     * {@value Short#MAX_VALUE} (inclusive).
     *
     * @return the number of bits discarded for alignment
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

        // discard remained bits in current byte.
        if (index < 8) {
            bits = 8 - index;
            readUnsignedByte(bits); // count increments
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

        for (; bytes > 0L; bytes--) {
            readUnsignedByte(8);
            bits += 8;
        }

        return bits;
    }


    /**
     * Returns the current value of {@link #reader}.
     *
     * @return the current value of {@link #reader}.
     */
    public ByteReader<T> getInput() {

        return reader;
    }


    /**
     * Replaces the value of {@link #reader} with given.
     *
     * @param input a new value for {@link #reader}.
     */
    public void setInput(final ByteReader<T> input) {

        this.reader = input;
    }


    /**
     * Closes this bit input. This method, if {@link #reader} is not
     * {@code null}, aligns to a single byte and closes {@link #reader}.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #align(short)
     * @see ByteInput#close()
     */
    @Override
    public void close() throws IOException {

        if (reader != null) {
            align((short) 1);
            reader.close();
        }
    }


    /**
     * Returns the number of bytes read from the underlying byte input so far.
     *
     * @return the number of bytes read from the underlying byte input so far.
     */
    public long getCount() {

        return count;
    }


    /**
     * The underlying byte input.
     */
    protected ByteReader<T> reader;


    /**
     * bit flags.
     */
    private final boolean[] flags = new boolean[8];


    /**
     * next bit index to read.
     */
    private int index = 8;


    /**
     * number of bytes read so far.
     */
    private long count = 0;


}

