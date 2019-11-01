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
 * A byte output which writes bytes to a writable byte channel.
 * <p>
 * Note that a flushing might be required when the {@code buffer}'s capacity is greater than {@code 1}.
 * <blockquote><pre>{@code
 * final WritableByteChannel channel = getTarget();
 * final ByteBuffer buffer = getBuffer();
 * for (buffer.flip(); buffer.hasRemaining(); ) {
 *     channel.write(buffer);
 * }
 * }</pre></blockquote>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteOutput
 * @see ChannelByteInput2
 */
class ChannelByteOutput2 extends AbstractByteOutput<WritableByteChannel> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance which writes bytes to specified channel.
     *
     * @param channel the channel to which bytes are written; must be not {@code null}.
     * @return a new instance.
     */
    public static ChannelByteOutput2 of(final WritableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new ChannelByteOutput2(channel, null) {
            @Override
            public void write(final int value) throws IOException {
                if (getBuffer() == null) {
                    setBuffer(allocate(1));
                }
                super.write(value);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified channel and buffer.
     *
     * @param target the channel to which bytes are written.
     * @param buffer the buffer in which bytes are stored before written to the channel.
     */
    public ChannelByteOutput2(final WritableByteChannel target, final ByteBuffer buffer) {
        super(target);
        this.buffer = buffer;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code ChannelByteOutput2} class, if required, drains the {@link
     * #getBuffer() buffer} to the {@link #getTarget() channel} and {@link ByteBuffer#put(byte) puts} specified value to
     * the {@code buffer}.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void write(final int value) throws IOException {
        final ByteBuffer buffer = getBuffer();
        if (buffer.capacity() == 0) {
            throw new IllegalStateException("buffer.capacity == 0");
        }
        while (!buffer.hasRemaining()) {
            buffer.flip(); // limit -> position, position -> zero
            final int written = getTarget().write(buffer);
            buffer.compact();
        }
        buffer.put((byte) value);
    }

    // ---------------------------------------------------------------------------------------------------------- buffer

    /**
     * Returns the current value of {@code buffer} attribute.
     *
     * @return the current value of {@code buffer} attribute.
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * Replaces the current value of {@code buffer} attribute with specified value.
     *
     * @param buffer new value for {@code buffer} attribute.
     */
    public void setBuffer(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The buffer to which bytes are written.
     */
    private ByteBuffer buffer;
}
