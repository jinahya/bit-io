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


package com.googlecode.jinahya.io;


import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.BitSet;


/**
 * A wrapper class for reading arbitrary length of bits.
 *
 * @author <a href="mailto:jinahya@gmail.com">Jin Kwon</a>
 */
public class BitInput {


    /**
     * An interface for reading bytes.
     */
    public static interface ByteInput {


        /**
         * Reads an unsigned 8-bit integer.
         *
         * @return an unsigned 8-bit integer. -1 for EOF.
         *
         * @throws IOException if an I/O error occurs.
         */
        int readUnsignedByte() throws IOException;


    }


    /**
     * A {@link ByteInput} implementation for {@link InputStream}s.
     */
    public static class StreamInput implements ByteInput {


        /**
         * Creates a new instance.
         *
         * @param stream the stream to wrap.
         */
        public StreamInput(final InputStream stream) {
            super();

            if (stream == null) {
                throw new NullPointerException("stream");
            }

            this.stream = stream;
        }


        @Override
        public int readUnsignedByte() throws IOException {
            return stream.read();
        }


        /**
         * input.
         */
        private final InputStream stream;


    }


    /**
     * A {@link ByteInput} implementation for {@link ReadableByteChannel}s.
     *
     * @deprecated Wrong implementation; Use {@link BufferOutput}.
     */
    @Deprecated
    public static class ChannelInput implements ByteInput {


        /**
         * Creates a new instance.
         *
         * @param channel the channel to wrap
         */
        public ChannelInput(final ReadableByteChannel channel) {
            super();

            if (channel == null) {
                throw new NullPointerException("null channel");
            }

            if (!channel.isOpen()) {
                throw new IllegalArgumentException("closed channel");
            }

            this.channel = channel;
            buffer = ByteBuffer.allocate(1);
        }


        @Override
        public int readUnsignedByte() throws IOException {

            buffer.clear(); // ------------------------------------------- clear

            for (int read = -1;;) {
                read = channel.read(buffer); // --------------------------- read
                if (read == -1) {
                    throw new EOFException("eof");
                }
                if (read == 1) {
                    break;
                }
            }

            buffer.flip(); // --------------------------------------------- flip

            return (buffer.get() & 0xFF); // ------------------------------- get
        }


        /**
         * channel.
         */
        private final ReadableByteChannel channel;


        /**
         * buffer.
         */
        private final ByteBuffer buffer;


    }


    /**
     * A {@link ByteInput} implementation for {@link ByteBuffer}s.
     */
    public static class BufferOutput implements ByteInput {


        /**
         * Creates a new instance.
         *
         * @param buffer the buffer to wrap.
         */
        public BufferOutput(final ByteBuffer buffer) {

            super();

            if (buffer == null) {
                throw new NullPointerException("buffer");
            }

            this.buffer = buffer;
        }


        @Override
        public int readUnsignedByte() throws IOException {

            return (buffer.get() & 0xFF); // BufferUnderflowException
        }


        /**
         * buffer.
         */
        private final ByteBuffer buffer;


    }


    /**
     * Creates a new instance.
     *
     * @param input the byte input
     */
    public BitInput(final ByteInput input) {

        super();

        if (input == null) {
            throw new NullPointerException("input");
        }

        this.input = input;
    }


    /**
     * Reads an {@code length}-bit unsigned byte value.
     *
     * @param length bit length between 0 (exclusive) and 8 (inclusive).
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
            int octet = input.readUnsignedByte();
            if (octet == -1) {
                throw new EOFException("eof");
            }
            count++;
            for (int i = 7; i >= 0; i--) {
                bitset.set(i, (octet & 0x01) == 0x01);
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
            value |= (bitset.get(index++) ? 0x01 : 0x00);
        }

        return value;
    }


    /**
     * Reads a {@code 1}-bit boolean value. {@code 0x00} for {@code false} and
     * {@code 0x01} for {@code true}.
     *
     * @return a boolean value.
     *
     * @throws IOException if an I/O error occurs.
     */
    public boolean readBoolean() throws IOException {

        return readUnsignedByte(1) == 0x01;
    }


    /**
     * Reads an {@code length}-bit unsigned short value.
     *
     * @param length bit length between 0 (exclusive) and 16 (inclusive).
     *
     * @return the unsigned short value.
     *
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
     * Reads an {@code length}-bit unsigned int value.
     *
     * @param length bit length between 1 (inclusive) and 32 (exclusive).
     *
     * @return the unsigned int value read from the input.
     *
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
     * Reads a {@code length}-bit signed int value.
     *
     * @param length bit length between 1 (exclusive) and 32 (inclusive).
     *
     * @return the value read from the input.
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
     */
    public float readFloat() throws IOException {

        return Float.intBitsToFloat(readInt(32));
    }


    /**
     * Reads a {@code length}-bit unsigned long value.
     *
     * @param length bit length between 1 (inclusive) and 64 (exclusive)
     *
     * @return an unsigned long value read from the input
     *
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

        long result = 0x00L;

        for (int i = 0; i < quotient; i++) {
            result <<= 31;
            result |= readUnsignedInt(31);
        }

        if (remainder > 0) {
            result <<= remainder;
            result |= readUnsignedInt(remainder);
        }

        return result;
    }


    /**
     * Reads a {@code length}-bit signed long value.
     *
     * @param length bit length between 1 (exclusive) and 64 (inclusive).
     *
     * @return a signed long value read from the input.
     *
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
     * @return a double value read from the input
     *
     * @throws IOException if an I/O error occurs.
     */
    public final double readDouble() throws IOException {

        return Double.longBitsToDouble(readLong(0x40));
    }


    /**
     * Reads an array of bytes.
     *
     * @param scale array length scale; between 0 exclusive and 16 inclusive.
     * @param range valid bit range in each bytes; between 0 exclusive and 8
     * inclusive.
     *
     * @return an array of bytes.
     *
     * @throws IOException if an I/O error occurs.
     */
    public byte[] readBytes(final int scale, final int range)
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

        final byte[] bytes = new byte[readUnsignedShort(scale)];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) readUnsignedByte(range);
        }

        return bytes;
    }


    /**
     * Reads a String.
     *
     * @param charsetName the charset name to decode the string.
     *
     * @return a String read.
     *
     * @throws IOException if an I/O error occurs.
     */
    public String readString(final String charsetName) throws IOException {

        if (charsetName == null) {
            throw new NullPointerException("charsetName");
        }

        return new String(readBytes(16, 8), charsetName);
    }


    /**
     * Reads an ASCII string with {@code scale} of 16.
     *
     * @return an US-ASCII String.
     *
     * @throws IOException if an I/O error occurs.
     */
    public String readUsAsciiString() throws IOException {

        return new String(readBytes(16, 7), "US-ASCII");
    }


    /**
     * Align to given {@code length} bytes.
     *
     * @param length number of bytes to align; must be non-zero positive.
     *
     * @return the number of bits discarded for alignment.
     *
     * @throws IOException if an I/O error occurs.
     */
    public int align(final int length) throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        int bits = 0;

        // reading(discarding) remained bits from current byte.
        if (index < 8) {
            bits = 8 - index;
            readUnsignedByte(bits); // count increments
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
            readUnsignedByte(8);
            bits += 8;
        }

        return bits;
    }


    /**
     * Returns the number of bytes read so far including current byte.
     *
     * @return the number of bytes read so far.
     */
    public int getCount() {
        return count;
    }


    /**
     * source byte input.
     */
    private final ByteInput input;


    /**
     * bits in current octet.
     */
    private final BitSet bitset = new BitSet(8);


    /**
     * bit index to read.
     */
    private int index = 8;


    /**
     * number of bytes read so far.
     */
    private int count = 0;


}
