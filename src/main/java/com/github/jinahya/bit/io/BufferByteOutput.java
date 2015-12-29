/*
 * Copyright 2013 Jin Kwon.
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


package com.github.jinahya.bit.io;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;


/**
 * A {@link ByteOutput} implementation for {@link ByteBuffer}s.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteInput
 */
public class BufferByteOutput extends AbstractByteOutput<ByteBuffer> {


    /**
     * Ensures specified byte buffer has remaining for writing. This method, if
     * the buffer has no remaining, writes the buffer to specified channel until
     * at least one byte is written.
     *
     * @param buffer the byte buffer
     * @param channel a channel for draining buffer.
     *
     * @return given buffer.
     *
     * @throws IOException if an I/O error occurs.
     */
    public static ByteBuffer ensureRemaining(final ByteBuffer buffer,
                                             final WritableByteChannel channel)
        throws IOException {

        if (buffer == null) {
            throw new NullPointerException("null buffer");
        }

        if (channel == null) {
            throw new NullPointerException("null channel");
        }

        if (!buffer.hasRemaining()) {
            buffer.flip(); // limit -> position; position -> zero;
            do {
                channel.write(buffer);
            } while (buffer.position() == 0);
            buffer.compact(); // position -> n+1; limit -> capacity
        }

        return buffer;
    }


    public static BufferByteOutput newInstance(final WritableByteChannel channel,
                                               final int capacity,
                                               final boolean direct) {

        if (channel == null) {
            throw new NullPointerException("null channel");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "capacity(" + capacity + ") <= 0");
        }

        return new BufferByteOutput(null) {

            @Override
            public void write(final int value) throws IOException {

                if (target == null) {
                    target = direct
                             ? ByteBuffer.allocateDirect(capacity)
                             : ByteBuffer.allocate(capacity);
                }

                super.write(value);

                ensureRemaining(target, channel);
            }


        };
    }


    /**
     * Creates a new instance with given {@code ByteBufer}.
     *
     * @param buffer the {@code ByteBuffer} to wrap or {@code null} if it's
     * supposed to be lazily initialized and set.
     */
    public BufferByteOutput(final ByteBuffer buffer) {

        super(buffer);
    }


    /**
     * {@inheritDoc} The {@code write(int)} method of {@code BufferByteOutput}
     * class invokes {@link ByteBuffer#put(byte)} on {@link #getTarget()} with
     * given {@code value}. Override this method if {@link #target} is supposed
     * to be lazily initialized or adjusted.
     *
     * @param value {@inheritDoc }
     *
     * @throws IOException {@inheritDoc }
     */
    @Override
    public void write(final int value) throws IOException {

        getTarget().put((byte) value);
    }


}

