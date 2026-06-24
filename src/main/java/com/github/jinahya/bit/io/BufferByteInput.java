package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import static java.nio.ByteBuffer.allocate;

/**
 * A byte input reads bytes from a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput
        extends AbstractByteInput<ByteBuffer> {

    /**
     * A {@link BufferByteInput} which, when its source buffer has no remaining bytes, lazily reads bytes from an
     * underlying {@link ReadableByteChannel}.
     */
    private static class ChannelBufferByteInput
            extends BufferByteInput {

        /**
         * Creates a new instance with specified source buffer and channel.
         *
         * @param source  the byte buffer from which bytes are read.
         * @param channel the channel from which the {@code source} buffer is filled; must not be {@code null}.
         */
        private ChannelBufferByteInput(final ByteBuffer source, final ReadableByteChannel channel) {
            super(source);
            if (channel == null) {
                throw new NullPointerException("channel is null");
            }
            this.channel = channel;
        }

        @Override
        public int read() throws IOException {
            final ByteBuffer source = getSource();
            while (!source.hasRemaining()) {
                source.clear(); // position -> zero, limit -> capacity
                if (channel.read(source) == -1) {
                    throw new EOFException("end of channel reached");
                }
                source.flip(); // limit -> position, position -> zero
            }
            return super.read();
        }

        private final ReadableByteChannel channel;
    }

    /**
     * Creates a new instance which reads bytes from the specified readable byte channel. The returned instance keeps an
     * internal {@code 1}-byte buffer and refills it from the {@code channel} whenever the buffer is drained.
     *
     * @param channel the channel from which bytes are read; must not be {@code null}.
     * @return a new instance.
     */
    public static BufferByteInput from(final ReadableByteChannel channel) {
        return new ChannelBufferByteInput((ByteBuffer) allocate(1).position(1), channel);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance which reads bytes from specified byte buffer.
     *
     * @param source the byte buffer from which bytes are read; {@code null} if it's supposed to be lazily initialized
     *               and set.
     */
    public BufferByteInput(final ByteBuffer source) {
        super(source);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code BufferByteInput} class invokes {@link ByteBuffer#get()} method,
     * on what {@link #getSource()} method returns, and returns the result as an unsigned {@code int}.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #getSource()
     * @see ByteBuffer#get()
     */
    @Override
    public int read() throws IOException {
        return getSource().get() & 0xFF;
    }

    // ---------------------------------------------------------------------------------------------------------- source
    @Override
    protected ByteBuffer getSource() {
        return super.getSource();
    }

    @Override
    protected void setSource(final ByteBuffer source) {
        super.setSource(source);
    }
}
