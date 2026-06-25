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

/**
 * A byte input which reads bytes from a readable byte channel. Bytes are read through an internal
 * {@link java.nio.ByteBuffer} which is recharged from the channel whenever it drains.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteOutput
 */
class ChannelByteInput
        extends AbstractByteInput<ReadableByteChannel> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified channel and buffer. The {@code buffer}'s position and limit are reset so
     * that it starts <em>drained</em> (no {@link ByteBuffer#hasRemaining() remaining}); only its
     * {@link ByteBuffer#capacity() capacity} matters.
     *
     * @param source the channel from which bytes are read; must not be {@code null}.
     * @param buffer a byte buffer for reading bytes from the {@code channel}; must not be {@code null} and must have a
     *               non-zero {@link ByteBuffer#capacity() capacity}.
     * @throws NullPointerException     if {@code source} or {@code buffer} is {@code null}.
     * @throws IllegalArgumentException if {@code buffer}'s {@link ByteBuffer#capacity() capacity} is zero.
     */
    ChannelByteInput(final ReadableByteChannel source, final ByteBuffer buffer) {
        super(source);
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        if (buffer.capacity() == 0) {
            throw new IllegalArgumentException("buffer.capacity is zero");
        }
        buffer.position(buffer.limit()); // start drained so the first read() charges from the channel
        this.buffered = new BufferByteInput(buffer);
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
     * {@inheritDoc} The {@code read()} method of {@code ChannelByteInput} class, if required, charges the
     * {@link #buffered}'s buffer from the {@link #source channel} and returns the result of its
     * {@link BufferByteInput#read()}.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        if (!buffered.source.hasRemaining()) {
            for (buffered.source.clear(); buffered.source.position() == 0; ) {
                if (source.read(buffered.source) == -1) {
                    throw new EOFException("end of channel reached");
                }
            }
            buffered.source.flip();
        }
        return buffered.read();
    }

    // -------------------------------------------------------------------------------------------------------- buffered

    /**
     * The buffer-backed byte input read from after its buffer is charged from the {@link #source channel}.
     */
    private final BufferByteInput buffered;
}
