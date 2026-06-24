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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import static java.nio.ByteBuffer.allocate;

/**
 * A byte output writes bytes to a {@link ByteBuffer}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteInput
 */
public class BufferByteOutput
        extends AbstractByteOutput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A {@link BufferByteOutput} which, when its target buffer has no remaining space, drains the buffered bytes to an
     * underlying {@link WritableByteChannel}.
     */
    private static class ChannelBufferByteOutput
            extends BufferByteOutput {

        /**
         * Creates a new instance with specified target buffer and channel.
         *
         * @param target  the byte buffer to which bytes are written.
         * @param channel the channel to which the {@code target} buffer is drained; must not be {@code null}.
         */
        private ChannelBufferByteOutput(final ByteBuffer target, final WritableByteChannel channel) {
            super(target);
            if (channel == null) {
                throw new NullPointerException("channel is null");
            }
            this.channel = channel;
        }

        @Override
        public void write(final int value) throws IOException {
            super.write(value);
            final ByteBuffer target = getTarget();
            while (!target.hasRemaining()) {
                target.flip();
                channel.write(target);
                target.compact();
            }
        }

        private final WritableByteChannel channel;
    }

    /**
     * Creates a new instance which writes bytes to the specified writable byte channel. The returned instance keeps an
     * internal {@code 1}-byte buffer and drains it to the {@code channel} whenever the buffer becomes full.
     *
     * @param channel the channel to which bytes are written; must not be {@code null}.
     * @return a new instance.
     */
    public static BufferByteOutput from(final WritableByteChannel channel) {
        return new ChannelBufferByteOutput(allocate(1), channel);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance which writes bytes to specified byte buffer.
     *
     * @param target the byte buffer to which bytes are written; must not be {@code null}.
     */
    public BufferByteOutput(final ByteBuffer target) {
        super(target);
        if (target == null) {
            throw new NullPointerException("target is null");
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code BufferByteOutput} class invokes
     * {@link ByteBuffer#put(byte)} method, on what {@link #getTarget()} method returns, with specified value casted as
     * {@code byte}.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #getTarget()
     * @see ByteBuffer#put(byte)
     */
    @Override
    public void write(final int value) throws IOException {
        getTarget().put((byte) value);
    }

    // ---------------------------------------------------------------------------------------------------------- target
    @Override
    protected ByteBuffer getTarget() {
        return super.getTarget();
    }

    @Override
    protected void setTarget(final ByteBuffer target) {
        super.setTarget(target);
    }
}
