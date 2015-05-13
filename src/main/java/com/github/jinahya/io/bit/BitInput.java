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


import java.io.EOFException;
import java.io.IOException;


/**
 * A class for reading arbitrary length of bits.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public class BitInput extends BitBase {


//    /**
//     * Creates a new instance consuming bytes from given byte source.
//     *
//     * @param source the byte source.
//     *
//     * @return a new instance.
//     *
//     * @throws NullPointerException if {@code source} is {@code null}.
//     */
//    public static BitInput newInstance(final InputStream source) {
//
//        if (source == null) {
//            throw new NullPointerException("null source");
//        }
//
//        // @todo: lambda this
//        return new BitInput(new StreamInput(source));
//    }
//
//
//    /**
//     * Creates a new instance consuming bytes from specified byte source.
//     *
//     * @param source the byte source.
//     *
//     * @return a new instance.
//     */
//    public static BitInput newInstance(final ByteBuffer source) {
//
//        if (source == null) {
//            throw new NullPointerException("null source");
//        }
//
//        return new BitInput(new BufferInput(source));
//    }
    /**
     * Creates a new instance built on top of the specified byte input.
     *
     * @param input the byte input on which this bit input is built.
     *
     * @throws NullPointerException if the specified {@code input} is
     * {@code null}.
     */
    public BitInput(final ByteInput input) {

        super();

        if (input == null) {
            throw new NullPointerException("null input");
        }

        this.input = input;
    }


    /**
     * Reads an unsigned byte from {@code input} and increments the
     * {@code count}.
     *
     * @return an unsigned byte value.
     *
     * @throws IOException if an I/O error occurs.
     */
    private int octet() throws IOException {

        final int value = input.readUnsignedByte();
        if (value == -1) {
            throw new EOFException("eof");
        }

        count++;

        return value;
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
     * @param length the number of bits for the value; between 0 (exclusive) and
     * 16 (inclusive).
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


    /**
     * Reads an unsigned int value.
     *
     * @param length the number of bits for the value; between 1 (inclusive) and
     * 32 (exclusive).
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


    /**
     * Reads a signed int value.
     *
     * @param length the number of bits for the value; between 1 (exclusive) and
     * 32 (inclusive).
     *
     * @return a signed int value.
     *
     * @throws IllegalArgumentException if {@code length} is not valid.
     * @throws IOException if an I/O error occurs.
     */
    public int readInt(final int length) throws IOException {

        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        }

        if (length > 32) {
            throw new IllegalArgumentException("length(" + length + ") > 32");
        }

        return ((readBoolean() ? -1 << (length - 1) : 0)
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
    public float readFloat32() throws IOException {

        return Float.intBitsToFloat(readInt(32));
    }


    /**
     * Reads an unsigned long value.
     *
     * @param length the number of bits for the value; between 1 (inclusive) and
     * 64 (exclusive).
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


    /**
     * Reads a signed long value.
     *
     * @param length the number of bits for the value; between 1 (exclusive) and
     * 64 (inclusive).
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

        return ((readBoolean() ? -1L << (length - 1) : 0L)
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
    public final double readDouble64() throws IOException {

        return Double.longBitsToDouble(readLong(0x40));
    }


//    /**
//     *
//     * @param scale
//     * @param range
//     * @param output
//     *
//     * @throws IOException if an I/O error occurs.
//     *
//     * @see #BYTES_SCALE_MIN
//     * @see #BYTES_SCALE_MAX
//     * @see #BYTES_RANGE_MIN
//     * @see #BYTES_RANGE_MAX
//     * @see #requireValidBytesScale(int)
//     * @see #requireValidBytesRange(int)
//     */
//    public void readBytes(final int scale, final int range,
//                          final Supplier<ByteOutput> output)
//        throws IOException {
//
//        requireValidBytesScale(scale);
//
//        requireValidBytesRange(range);
//
//        if (output == null) {
//            throw new NullPointerException("null output");
//        }
//
//        final int length = readUnsignedShort(scale);
//        for (int i = 0; i < length; i++) {
//            output.get().writeUnsignedByte(readUnsignedByte(range));
//        }
//    }
    protected void readBytesFully(final int length, final int range,
                                  final ByteOutput output)
        throws IOException {

        requireValidBytesRange(range);

        if (output == null) {
            throw new NullPointerException("null output");
        }

        for (int i = 0; i < length; i++) {
            output.writeUnsignedByte(readUnsignedByte(range));
        }
    }


    protected int readBytesLength(final int scale) throws IOException {

        return readUnsignedInt(requireValidBytesScale(scale));
    }


    /**
     * Reads a sequence of bytes.
     *
     * @param scale the number of bits required for calculating the number of
     * bytes to read; between 0 (exclusive) and 16 (inclusive).
     * @param range the number of valid bits in each byte; between 0 (exclusive)
     * and 8 (inclusive).
     * @param output
     *
     * @return number of bytes written to {@code output}.
     *
     * @throws IOException if an I/O error occurs.
     */
    protected int readBytes(final int scale, final int range,
                            final ByteOutput output)
        throws IOException {

        requireValidBytesScale(scale);

        requireValidBytesRange(range);

        if (output == null) {
            throw new NullPointerException("null output");
        }

        final int length = readBytesLength(scale);
        readBytesFully(length, range, output);

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

        requireValidBytesScale(scale);

        final byte[] value = new byte[readBytesLength(scale)];

        readBytesFully(value.length, range, new ArrayOutput(value, 0));

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
     * @return a string value.
     *
     * @throws NullPointerException if {@code charsetName} is {@code null}.
     * @throws IOException if an I/O error occurs.
     *
     * @see #readBytes(int, int)
     * @see String#String(byte[], java.lang.String)
     */
    public String readString(final String charsetName) throws IOException {

        if (charsetName == null) {
            throw new NullPointerException("null charsetName");
        }

        return new String(readBytes(BYTES_SCALE_MAX, BYTES_RANGE_MAX),
                          charsetName);
    }


//    public String readString(final Charset charset) throws IOException {
//
//        if (charset == null) {
//            throw new NullPointerException("null charset");
//        }
//
//        return new String(readBytes(BYTES_SCALE_MAX, BYTES_RANGE_MAX), charset);
//    }
    /**
     * Reads a {@code US-ASCII} encoded string. This method reads a byte array
     * via {@link #readBytes(int, int)} with {@code scale} of {@code 16} and
     * {@code range} of {@code 7} and returns the output string created by
     * {@link String#String(byte[], java.lang.String)} with the byte array and
     * {@code US-ASCII}.
     *
     * @return a {@code US-ASCII} encoded string.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #readBytes(int, int)
     * @see String#String(byte[], java.lang.String)
     */
    public String readUsAsciiString() throws IOException {

        return new String(readBytes(BYTES_SCALE_MAX, 7), "US-ASCII");
    }


    /**
     * Aligns to given number of bytes.
     *
     * @param length the number of bytes to align; between 0 (exclusive) and
     * {@value java.lang.Short#MAX_VALUE} (inclusive).
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
     * Returns the number of bytes read from the underlying byte input so far.
     *
     * @return the number of bytes read so far.
     */
    public long getCount() {

        return count;
    }


    /**
     * The underlying byte input.
     */
    protected final ByteInput input;


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
    private long count = 0;


}

