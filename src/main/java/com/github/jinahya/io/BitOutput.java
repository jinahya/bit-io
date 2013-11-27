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


import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;


/**
 * A wrapper class for writing arbitrary length of bits.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public class BitOutput {


    /**
     * An interface for writing bytes.
     */
    public interface ByteOutput { // status? redundant!


        /**
         * Writes an unsigned 8-bit integer.
         *
         * @param value an unsigned 8-bit integer.
         *
         * @throws IOException if an I/O error occurs.
         */
        void writeUnsignedByte(final int value) throws IOException;


        /**
         * Closes this byte output.
         *
         * @throws IOException if an I/O error occurs.
         */
        void close() throws IOException;


    }


    /**
     * A {@link ByteOutput} implementation for {@link OutputStream}s.
     */
    public static class StreamOutput implements ByteOutput {


        /**
         * Creates a new instance on top of specified output stream.
         *
         * @param stream the output stream to wrap.
         *
         * @throws NullPointerException if {@code stream} is {@code null}.
         */
        public StreamOutput(final OutputStream stream) {

            super();

            if (stream == null) {
                throw new NullPointerException("null stream");
            }

            this.stream = stream;
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code writeUnsginedByte(int)} method of {@code StreamOutput}
         * class calls {@link OutputStream#write(int)} on {@link #stream} with
         * {@code value}.
         *
         * @param value {@inheritDoc }
         *
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void writeUnsignedByte(final int value) throws IOException {

            stream.write(value);
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code close()} method of {@code StreamOutput} class calls
         * {@link OutputStream#flush()} and {@link OutputStream#close()} in
         * series on {@link #stream}.
         *
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void close() throws IOException {

            stream.flush();
            stream.close();
        }


        /**
         * The underlying output stream.
         */
        protected final OutputStream stream;


    }


    /**
     * A {@link ByteOutput} implementation for {@link ByteBuffer}s.
     */
    public static class BufferOutput implements ByteOutput {


        /**
         * Creates a new instance on the of specified byte buffer.
         *
         * @param buffer the byte buffer to wrap.
         *
         * @throws NullPointerException if {@code buffer} is {@code null}.
         */
        public BufferOutput(final ByteBuffer buffer) {

            super();

            if (buffer == null) {
                throw new NullPointerException("null buffer");
            }

            this.buffer = buffer;
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code writeUnsignedByte(int)} method of {@code BufferOutput}
         * calls {@link ByteBuffer#put(byte)} on {@link #buffer} with
         * {@code value}.
         *
         * @param value {@inheritDoc }
         *
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void writeUnsignedByte(final int value) throws IOException {

            buffer.put(((byte) value)); // BufferOverflowException
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code close()} method of {@code BufferOutput} class does
         * nothing.
         *
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void close() throws IOException {

            // do nothing.
        }


        /**
         * Returns the underlying byte buffer on which this output built.
         *
         * @return the underlying byte buffer.
         */
        public ByteBuffer getBuffer() {

            return buffer;
        }


        /**
         * The underlying byte buffer.
         */
        protected final ByteBuffer buffer;


    }


    /**
     * A {@link ByteOutput} implementation for {@link WritableByteChannel}s.
     */
    public static class ChannelOutput extends BufferOutput {


        /**
         * Creates a new instance on top of specified byte channel.
         *
         * @param buffer the buffer to use
         * @param channel the output channel to wrap.
         *
         * @throws NullPointerException if either {@code buffer} or
         * {@code channel} is {@code null}
         */
        public ChannelOutput(final ByteBuffer buffer,
                             final WritableByteChannel channel) {

            super(buffer);

            if (channel == null) {
                throw new NullPointerException("channel");
            }

            this.channel = channel;
        }


        /**
         * Creates a new instance.
         *
         * @param channel the output channel. {@code null} for lazy
         * initialization.
         */
        public ChannelOutput(final WritableByteChannel channel) {

            this(ByteBuffer.allocate(1024), channel);
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code writeUnsignedByte(int)} method of {@code ChannelOutput}
         * first tries to drain the {@link #buffer} to {@link #channel} if it is
         * full and calls {@link BufferOutput#writeUnsignedByte(int)} with
         * {@code value}.
         *
         * @param value {@inheritDoc }
         *
         * @throws RuntimeException if {@link #buffer}'s capacity is zero.
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void writeUnsignedByte(final int value) throws IOException {

            if (buffer.capacity() == 0) {
                throw new RuntimeException("buffer.capacity == 0");
            }

            if (!buffer.hasRemaining()) {
                buffer.flip(); // limit -> position, position -> zero
                while (buffer.position() == 0) {
                    channel.write(buffer);
                }
                buffer.compact(); // position -> n + 1, limit -> capacity
            }

            super.writeUnsignedByte(value);
        }


        /**
         * {@inheritDoc}
         * <p/>
         * The {@code close()} method of {@code ChannelOutput} class first
         * writes all remaining bytes in {@link #buffer} to {@link #channel} and
         * closes the {@link #channel}.
         *
         * @throws IOException {@inheritDoc}
         */
        @Override
        public void close() throws IOException {

            buffer.flip(); // limit -> position, position -> zero
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }

            channel.close();
        }


        /**
         * Returns the underlying byte channel on which this output built.
         *
         * @return the underlying byte channel.
         */
        public WritableByteChannel getChannel() {

            return channel;
        }


        /**
         * The underlying byte channel.
         */
        protected WritableByteChannel channel;


    }


    /**
     * Creates a new instance on top of specified byte output.
     *
     * @param output the byte output to wrap
     *
     * @throws NullPointerException if {@code output} is {@code null}.
     */
    public BitOutput(final ByteOutput output) {

        super();

        if (output == null) {
            throw new NullPointerException("null output");
        }

        this.output = output;
    }


    /**
     * Writes given {@code value} to {@link #output} and increments
     * {@code count}.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    private void octet(final int value) throws IOException {

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
     * @throws IllegalArgumentexception if either {@code scale} or {@code range}
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
     *
     * @deprecated by {@link #align(short) }
     */
    @Deprecated
    public long align(final int length) throws IOException {

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        long bits = 0;

        // writing(padding) remained bits into current byte
        if (index > 0) {
            bits = (8 - index);
            writeUnsignedByte((int) bits, 0x00); // count incremented
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
     */
    public void close() throws IOException {

        align((short) 1);

        if (output != null) {
            output.close();
        }
    }


    /**
     * Returns the number of bytes written to the {@code output} so far.
     *
     * @return the number of bytes written to the {@code output} so far.
     */
    public int getCount() {

        return count;
    }


    /**
     * target byte output.
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
    private int count = 0;


}

