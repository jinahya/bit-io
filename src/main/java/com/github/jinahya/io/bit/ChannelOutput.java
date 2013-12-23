/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.github.jinahya.io.bit;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;


/**
 * A {@link ByteOutput} implementation for {@link WritableByteChannel}s.
 */
public class ChannelOutput extends ByteOutput<WritableByteChannel> {


    /**
     * Creates a new instance on top of specified byte channel.
     *
     * @param target the target channel to which bytes are written.
     * @param buffer the buffer to buffering the output
     */
    public ChannelOutput(final WritableByteChannel target,
                         final ByteBuffer buffer) {

        super(target);

        this.buffer = buffer;
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The {@code writeUnsignedByte(int)} method of {@code ChannelOutput} first
     * tries to drain {@link #buffer}, if it is fully replenished, to
     * {@link #target} and calls {@link ByteBuffer#put(byte)} with
     * {@code value}.
     *
     * @param value {@inheritDoc }
     *
     * @throws IllegalStateException if either {@link #target} or
     * {@link #buffer} is {@code null}.
     * @throws IOException {@inheritDoc }
     */
    @Override
    public void writeUnsignedByte(final int value) throws IOException {

        if (buffer == null) {
            throw new IllegalStateException("#buffer is currently null");
        }

        if (buffer.capacity() == 0) {
            throw new IllegalStateException("#buffer.capacity == 0");
        }

        if (!buffer.hasRemaining()) {
            buffer.flip(); // limit -> position, position -> zero
            while (buffer.position() == 0) {
                target.write(buffer);
            }
            buffer.compact(); // position -> n + 1, limit -> capacity
        }
        assert buffer.hasRemaining();

        buffer.put((byte) value); // BufferOverflowException, ReadOnlyBufferException
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The {@code close()} method of {@code ChannelOutput} class first writes,
     * if both {@link #buffer} and {@link #target} are not {@code null}, all
     * remaining bytes in {@link #buffer} to {@link #target} and closes, if
     * {@link #target} is not {@code null}, the {@link #target}.
     *
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void close() throws IOException {

        if (target != null) {

            if (buffer != null) {
                buffer.flip(); // limit -> position, position -> zero
                while (buffer.hasRemaining()) {
                    target.write(buffer);
                }
            }

            target.close();
        }
    }


    /**
     * Returns the underlying byte channel on which this output built.
     *
     * @return the underlying byte channel.
     */
    public ByteBuffer getBuffer() {

        return buffer;
    }


    /**
     * Replaces the value of {@link #buffer} with given.
     *
     * @param buffer the new value for {@link #buffer}.
     */
    public void setBuffer(final ByteBuffer buffer) {

        this.buffer = buffer;
    }


    /**
     * The byte buffer for buffering the channel.
     */
    protected ByteBuffer buffer;


}

