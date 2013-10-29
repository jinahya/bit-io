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
 * @author <a href="mailto:jinahya@gmail.com">Jin Kwon</a>
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
         * Creates a new instance with given output stream.
         *
         * @param stream the output stream. {@code null} for lazy
         * initialization.
         */
        public StreamOutput(final OutputStream stream) {

            super();

            if (stream == null) {
                //throw new NullPointerException("stream");
            }

            this.stream = stream;
        }


        /**
         * {@inheritDoc} The {@code stream} must be initialized and set if
         * {@code null} passed when this instance was created.
         *
         * @param value {@inheritDoc }
         *
         * @throws IllegalStateException if {@code stream} is currently
         * {@code null}.
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void writeUnsignedByte(final int value) throws IOException {

            if (stream == null) {
                throw new IllegalStateException("the stream is currently null");
            }

            stream.write(value);
        }


        /**
         * {@inheritDoc} This method, if {@code stream} is not {@code null},
         * flushes and closes the {@code stream}.
         *
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void close() throws IOException {

            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }


        /**
         * the output stream.
         */
        protected OutputStream stream;


    }


    /**
     * A {@link ByteOutput} implementation for {@link ByteBuffer}s.
     */
    public static class BufferOutput implements ByteOutput {


        /**
         * Creates a new instance.
         *
         * @param buffer the buffer to wrap. {@code null} for lazy
         * initialization.
         */
        public BufferOutput(final ByteBuffer buffer) {

            super();

            if (buffer == null) {
                //throw new NullPointerException("buffer");
            }

            this.buffer = buffer;
        }


        /**
         * {@inheritDoc} The {@code buffer} must be initialized and set if
         * {@code null} passed when this instance was created.
         *
         * @param value {@inheritDoc }
         *
         * @throws IllegalStateException if {@code buffer} is currently
         * {@code null}.
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void writeUnsignedByte(final int value) throws IOException {

            if (buffer == null) {
                throw new IllegalStateException("buffer is currently null");
            }

            buffer.put(((byte) value)); // BufferOverflowException
        }


        /**
         * Closes this byte output. This method does nothing.
         *
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void close() throws IOException {
        }


        /**
         * the output buffer.
         */
        protected ByteBuffer buffer;


    }


    /**
     * A {@link ByteOutput} implementation for {@link WritableByteChannel}s.
     *
     * @deprecated Wrong implementation; Use {@link BufferOutput}.
     */
    @Deprecated
    public static class ChannelOutput extends BufferOutput {


        /**
         * Creates a new instance.
         *
         * @param buffer the buffer. {@code null} for lazy initialization.
         * @param channel the output channel. {@code null} for lazy
         * initialization.
         */
        public ChannelOutput(final ByteBuffer buffer,
                             final WritableByteChannel channel) {

            super(buffer);

            if (channel == null) {
                //throw new NullPointerException("channel");
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
         * {@inheritDoc} Both {@code buffer} and {@code channel} must be
         * initialized and set if either of them passed as {@code null} when
         * this instance was created.
         *
         * @param value {@inheritDoc }
         *
         * @throws IllegalStateException if either {@code buffer} or
         * {@code channel} is currently {@code null}.
         * @throws IOException {@inheritDoc }
         */
        @Override
        public void writeUnsignedByte(final int value) throws IOException {

            if (buffer == null) {
                throw new IllegalStateException("the buffer is currently null");
            }

            if (channel == null) {
                throw new IllegalStateException(
                    "the channel is currently null");
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
         * {@inheritDoc} This method, if both {@code buffer} and {@code channel}
         * is not {@code null}, writes all remaining bytes in {@code buffer} to
         * {@code channel} and closes the {@code channel}.
         *
         * @throws IOException if an I/O error occurs.
         */
        @Override
        public void close() throws IOException {

            if (channel != null) {
                if (buffer != null) {
                    buffer.flip(); // limit -> position, position -> zero
                    while (buffer.hasRemaining()) {
                        channel.write(buffer);
                    }
                    buffer.clear(); // this may be required for further access
                }
                channel.close();
            }
        }


        /**
         * the output channel.
         */
        protected WritableByteChannel channel;


    }


    /**
     * Creates a new instance.
     *
     * @param output target byte output
     *
     * @throws NullPointerException if {@code output} is null.
     */
    public BitOutput(final ByteOutput output) {

        super();

        if (output == null) {
            throw new NullPointerException("output");
        }

        this.output = output;
    }


    /**
     * Writes given {@code value} to {@code output} and increments
     * {@code count}.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    private void octet(final int value) throws IOException {

        if (output == null) {
            //throw new IllegalStateException("the output is currently null");
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
     * @param length the number of bits between 0 exclusive and 16 inclusive.
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
     * @param length bit length between 1 inclusive and 32 exclusive.
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
     * @param length bit length between 1 (exclusive) and 32 (inclusive).
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
            if (value < 0x00) { // negative
                if (value >> (length - 1) != ~0) {
                    throw new IllegalArgumentException(
                        "value(" + value + ") >> (length(" + length
                        + ") - 1) != ~0");
                }
            } else { // positive
                if (value >> (length - 1) != 0) {
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
            if (value < 0L) {
                if ((value >> (length - 1)) != ~0L) {
                    throw new IllegalArgumentException(
                        "(value(" + value + ") >> (length(" + length
                        + ") - 1)) != ~0L");
                }
            } else {
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
     */
    public void close() throws IOException {

        align((short) 1);

        if (output != null) {
            output.close();
        }
    }


    /**
     * Returns the number of bytes written so far excluding current byte.
     *
     * @return the number of bytes written so far.
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

