/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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


import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;


/**
 * A {@link ByteInput} implementation uses a {@link ByteBuffer} as
 * {@link #source}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput extends AbstractByteInput<ByteBuffer> {


    /**
     * Ensures specified byte buffer has remaining for reading. This method, if
     * the buffer has no remaining, reads the buffer from specified channel
     * until at least one byte is read.
     *
     * @param buffer the byte buffer
     * @param channel a channel for filling buffer.
     *
     * @return given buffer.
     *
     * @throws IOException if an I/O error occurs.
     */
    public static ByteBuffer ensureRemaining(final ByteBuffer buffer,
                                             final ReadableByteChannel channel)
        throws IOException {

        if (buffer == null) {
            throw new NullPointerException("null buffer");
        }

        if (channel == null) {
            throw new NullPointerException("null channel");
        }

        if (!buffer.hasRemaining()) {
            buffer.clear(); // position -> zero; limit -> capacity;
            do {
                final int read = channel.read(buffer);
                if (read == -1) {
                    throw new EOFException();
                }
            } while (buffer.position() == 0);
            buffer.flip(); // limit -> position; position -> zero;
        }

        return buffer;
    }


    public static BufferByteInput newInstance(final ReadableByteChannel channel,
                                              final int capacity,
                                              final boolean direct) {

        if (channel == null) {
            throw new NullPointerException("null channel");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "capacity(" + capacity + ") <= 0");
        }

        return new BufferByteInput(null) {

            @Override
            public int read() throws IOException {

                if (source == null) {
                    source = direct
                             ? ByteBuffer.allocateDirect(capacity)
                             : ByteBuffer.allocate(capacity);
                    source.position(source.limit());
                }

                ensureRemaining(source, channel);

                return super.read();
            }

        };
    }


    /**
     * Creates a new instance built on top of the specified byte buffer.
     *
     * @param source the byte buffer or {@code null} if it's supposed to be
     * lazily initialized and set.
     */
    public BufferByteInput(final ByteBuffer source) {

        super(source);
    }


    /**
     * {@inheritDoc} The {@code read()} method of {@code BufferByteInput} invokes
     * {@link ByteBuffer#get()} on {@link #getSource()} and return the result as
     * an unsigned int. Override this method if {@link #source} is supposed to
     * be lazily initialized or adjusted.
     *
     * @return {@inheritDoc }
     *
     * @throws IOException {@inheritDoc }
     */
    @Override
    public int read() throws IOException {

        return getSource().get() & 0xFF;
    }

}

