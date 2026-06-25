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

/**
 * A byte output which writes bytes to a writable byte channel.
 * <p>
 * Note that a flushing might be required when the {@code buffer}'s capacity is greater than {@code 1}.
 * <blockquote><pre>{@code
 * for (buffer.flip(); buffer.hasRemaining(); ) {
 *     target.write(buffer);
 * }
 * }</pre></blockquote>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteInput
 */
class ChannelByteOutput
        extends AbstractByteOutput<WritableByteChannel> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified channel and buffer. The {@code buffer}'s position and limit are
     * {@link ByteBuffer#clear() reset} so that it starts with full remaining space; only its
     * {@link ByteBuffer#capacity() capacity} matters.
     *
     * @param target the channel to which bytes are written; must not be {@code null}.
     * @param buffer the buffer in which bytes are stored before written to the channel; must not be {@code null} and
     *               must have a non-zero {@link ByteBuffer#capacity() capacity}.
     * @throws NullPointerException     if {@code target} or {@code buffer} is {@code null}.
     * @throws IllegalArgumentException if {@code buffer}'s {@link ByteBuffer#capacity() capacity} is zero.
     */
    ChannelByteOutput(final WritableByteChannel target, final ByteBuffer buffer) {
        super(target);
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        if (buffer.capacity() == 0) {
            throw new IllegalArgumentException("buffer.capacity is zero");
        }
        buffer.clear(); // start with full remaining space
        this.buffered = new BufferByteOutput(buffer);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "buffered=" + buffered
               + "}";
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code ChannelByteOutput} class, if required, drains the
     * {@link #buffered}'s buffer to the {@link #target channel} and writes the value through its
     * {@link BufferByteOutput#write(int)}.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see ChannelByteInput#read()
     */
    @Override
    public void write(final int value) throws IOException {
        if (!buffered.target.hasRemaining()) {
            buffered.target.flip();
            do {
                if (target.write(buffered.target) == 0) {
                    throw new IOException("channel write made no progress");
                }
            } while (buffered.target.position() == 0);
            buffered.target.compact();
        }
        buffered.write(value);
    }

    // -------------------------------------------------------------------------------------------------------- buffered

    /**
     * The buffer-backed byte output written to before its buffer is drained to the {@link #target channel}.
     */
    private final BufferByteOutput buffered;
}
