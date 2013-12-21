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


package com.github.jinahya.io;


import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;


/**
 * A wrapper class for reading arbitrary length of bits.
 *
 * @author <a href="mailto:jinahya@gmail.com">Jin Kwon</a>
 */
public class BitInput {


    /**
     * An interface for reading bytes.
     */
    public interface ByteInput { // static? redundant!


        /**
         * Reads the next unsigned 8-bit byte.
         *
         * @return the next unsigned 8-bit byte, or {@code -1} if the end of the
         * stream is reached.
         *
         * @throws IOException if an I/O error occurs.
         */
        int readUnsignedByte() throws IOException;


        /**
         * Closes this byte input and releases any system resources associated
         * with the input.
         *
         * @throws IOException if an I/O error occurs.
         */
        void close() throws IOException;


    }


    /**
     * A {@link ByteInput} implementation for {@link InputStream}s.
     */
    public static class StreamInput implements ByteInput {


        /**
         * Creates a new instance.
         *
         * @param stream the stream to wrap.
         *
         * @throws NullPointerException if {@code stream} is {@code null}.
         */
        public StreamInput(final InputStream stream) {

            super();

            if (stream == null) {
                throw new NullPointerException("null stream");
            }

            this.stream = stream;
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code readUnsignedByte()} method of {@code StreamInput} class
         * calls {@link InputStream#read()} on underlying {@link #stream} and
         * returns the result.
         *
         * @return {@inheritDoc}
         *
         * @throws IOException {@inheritDoc}
         */
        @Override
        public int readUnsignedByte() throws IOException {

            return stream.read();
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code close()} method of {@code StreamInput} class calls
         * {@link InputStream#close()} on underlying {@link #stream}.
         *
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void close() throws IOException {

            stream.close();
        }


        /**
         * Returns the underlying {@link #stream}.
         *
         * @return the underlying {@link #stream}.
         */
        public InputStream getStream() {

            return stream;
        }


        /**
         * The underlying input stream.
         */
        protected final InputStream stream;


    }


    /**
     * A {@link ByteInput} implementation for {@link ByteBuffer}s.
     */
    public static class BufferInput implements ByteInput {


        /**
         * Creates a new instance on top of specified byte buffer.
         *
         * @param buffer the buffer to wrap.
         *
         * @throws NullPointerException if {@code buffer} is {@code null}.
         */
        public BufferInput(final ByteBuffer buffer) {

            super();

            if (buffer == null) {
                throw new NullPointerException("null buffer");
            }

            this.buffer = buffer;
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code readUnsignedByte()} method of {@code ByteBuffer} class
         * calls {@link ByteBuffer#get()} on underlying {@link #buffer} and
         * returns the result.
         *
         * @return {@inheritDoc }
         *
         * @throws IOException {@inheritDoc}
         */
        @Override
        public int readUnsignedByte() throws IOException {

            try {
                return (buffer.get() & 0xFF);
            } catch (final BufferUnderflowException bue) {
                return -1;
            }
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code close()} method of {@code BufferInput} class does nothing.
         *
         * @throws IOException {@inheritDoc}
         */
        @Override
        public void close() throws IOException {

            // do nothing
        }


        /**
         * Returns the underlying {@link #buffer}.
         *
         * @return the underlying {@link #buffer}.
         */
        public ByteBuffer getBuffer() {

            return buffer;
        }


        /**
         * The underlying buffer.
         */
        protected final ByteBuffer buffer;


    }


    /**
     * A {@link ByteInput} implementation for {@link ReadableByteChannel}s.
     */
    public static class ChannelInput extends BufferInput {


        /**
         * Creates a new instance on top of specified channel.
         *
         * @param buffer the buffer to buffering input.
         * @param channel the underlying channel.
         *
         * @throws NullPointerException if either {@code buffer} or
         * {@code channel} is {@code null}.
         *
         */
        public ChannelInput(final ByteBuffer buffer,
                            final ReadableByteChannel channel) {

            super(buffer);

            if (channel == null) {
                throw new NullPointerException("null channel");
            }

            this.channel = channel;
        }


        /**
         * Creates a new instance on top of specified channel.
         *
         * @param channel the channel to wrap.
         *
         * @throws NullPointerException if {@code channel} is {@code null}.
         */
        public ChannelInput(final ReadableByteChannel channel) {

            this(ByteBuffer.allocate(1024), channel);
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code readUnsignedByte()} method of {@code ChannelInput} class
         * first tries to replenish the {@link #buffer} if it is drained and
         * calls {@link BufferInput#readUnsignedByte()} and returns the result.
         *
         * @return {@inheritDoc }
         *
         * @throws IOException {@inheritDoc}
         */
        @Override
        public int readUnsignedByte() throws IOException {

            if (buffer.capacity() == 0) {
                throw new IllegalStateException("buffer.capacity == 0");
            }

            if (!buffer.hasRemaining()) {
                buffer.clear(); // position -> zero, limit -> capacity
                while (buffer.position() == 0) {
                    if (channel.read(buffer) == -1) {
                        return -1;
                    }
                }
                assert buffer.position() > 0;
                buffer.flip(); // limit -> position, position -> zero
            }

            return super.readUnsignedByte();
        }


        /**
         * {@inheritDoc }
         * <p/>
         * The {@code close()} method of {@code ChannelInput} class calls
         * {@link java.nio.channels.Channel#close()} on {@link #channel}.
         *
         * @throws IOException {@inheritDoc}
         */
        @Override
        public void close() throws IOException {

            channel.close();
        }


        /**
         * Returns the underlying channel on which this input built.
         *
         * @return the underlying channel
         */
        public ReadableByteChannel getChannel() {

            return channel;
        }


        /**
         * The underlying channel instance.
         */
        protected final ReadableByteChannel channel;


    }


    /**
     * Creates a new instance the specified byte input.
     *
     * @param input the byte input
     */
    public BitInput(final ByteInput input) {

        super();

        if (input == null) {
            throw new NullPointerException("null input");
        }

        this.input = input;
    }


    /**
     * Reads next unsigned byte from the {@code input} and increments the
     * {@code count}.
     *
     * @return next unsigned byte
     *
     * @throws IOException if an I/O error occurs.
     */
    private int octet() throws IOException {

        final int octet = input.readUnsignedByte();
        if (octet == -1) {
            throw new EOFException("eof");
        }

        count++;

        return octet;
    }


    /**
     * Reads an unsigned byte value.
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

        return readUnsignedByte(1) == 0x00;
    }


    /**
     * Reads a boolean flag for nullability of subsequent object.
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
     * @param length the number of bits between 0 exclusive and 16 inclusive
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
     * @param length the number of bits between 1 inclusive and 32 exclusive
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
     * Reads a signed int.
     *
     * @param length the number of bits between 1 exclusive and 32 inclusive.
     *
     * @return an signed int
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
     * @param length the number of bits between 1 inclusive and 64 exclusive
     *
     * @return an unsigned long value
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
     * @param length the number of bits between 1 exclusive and 64 inclusive
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
     * @param range the number of valid bits in each byte; between 0 exclusive
     * and 8 inclusive
     * @param value the array to which each byte are stored
     * @param offset starting offset in the array
     * @param length the number of bytes to read
     *
     * @throws IOException
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
     * @param scale the number of bits required for {@code length}; between 0
     * exclusive and 16 inclusive.
     * @param range the number of valid bits in each byte; between 0 exclusive
     * and 8 inclusive
     * @param value the array to which each byte are stored
     * @param offset starting offset in the array
     *
     * @return the number of bytes read
     *
     * @throws IOException if an I/O error occurs.
     */
    public int readBytes(final int scale, final int range, final byte[] value,
                         int offset)
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

        final int length = readUnsignedShort(scale);

        for (int i = 0; i < length; i++) {
            value[offset++] = (byte) readUnsignedByte(range);
        }

        return length;
    }


    /**
     * Reads an array of bytes.
     *
     * @param scale the number of bits for array length; between 0 exclusive and
     * 16 inclusive.
     * @param range the number of valid bits in each byte; between 0 exclusive
     * and 8 inclusive.
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

        final byte[] value = new byte[readUnsignedShort(scale)];

        for (int i = 0; i < value.length; i++) {
            value[i] = (byte) readUnsignedByte(range);
        }

        return value;
    }


    /**
     * Reads an array of bytes.
     *
     * @return an array of bytes
     *
     * @throws IOException if an I/O error occurs.
     */
    public byte[] readBytes() throws IOException {

        return readBytes(16, 8);
    }


    /**
     * Reads a string.
     *
     * @param charsetName the character set name to encode output string.
     *
     * @return a string
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
     * Reads a US-ASCII string.
     *
     * @return a US-ASCII encoded string.
     *
     * @throws IOException if an I/O error occurs.
     */
    public String readUsAsciiString() throws IOException {

        return new String(readBytes(16, 7), "US-ASCII");
    }


    /**
     * Aligns to given number of bytes.
     *
     * @param length the number of bytes to align; must be positive.
     *
     * @return the number of bits discarded
     *
     * @throws IllegalArgumentException if {@code length} is less than or equals
     * to zero.
     * @throws IOException if an I/O error occurs.
     */
    public int align(final short length) throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
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
     * Closes this bit input. This method aligns to a single byte and closes the
     * underlying byte input.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #align(short)
     */
    public void close() throws IOException {

        align((short) 1);

        if (input != null) {
            input.close();
        }
    }


    /**
     * Returns the number of bytes read from the {@code input} so far.
     *
     * @return the number of bytes read from the {@code input} so far.
     */
    public long getCount() {

        return count;
    }


    /**
     * source byte input.
     */
    private final ByteInput input;


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
    //private int count = 0;
    private long count = 0;


}

