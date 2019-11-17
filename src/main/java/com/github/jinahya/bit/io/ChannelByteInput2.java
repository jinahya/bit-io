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
 * A byte input which reads bytes from a readable byte channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ChannelByteInput
 * @see ChannelByteOutput2
 */
class ChannelByteInput2 extends AbstractByteInput<ReadableByteChannel> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance which writes bytes to specified channel.
     *
     * @param channel the channel to which bytes are written; must be not {@code null}.
     * @return a new instance.
     */
    public static ChannelByteInput2 of(final ReadableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new ChannelByteInput2(channel, null) {
            @Override
            public int read() throws IOException {
                if (getBuffer() == null) {
                    setBuffer((ByteBuffer) allocate(1).position(1));
                }
                return super.read();
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified channel and buffer.
     *
     * @param source the channel from which bytes are read; {@code null} if it's supposed to be lazily initialized and
     *               set.
     * @param buffer a byte buffer for reading bytes from the {@code channel}; {@code null} if it's supposed to be
     *               lazily initialized and set.
     */
    public ChannelByteInput2(final ReadableByteChannel source, final ByteBuffer buffer) {
        super(source);
        this.buffer = buffer;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code ChannelByteInput2} class, if required, charges the {@link
     * #getBuffer() buffer} from the {@link #getSource() channel} and returns the result of {@link ByteBuffer#get()
     * get()} as an unsigned {@code int}.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        final ByteBuffer buffer = getBuffer();
        while (!buffer.hasRemaining()) {
            buffer.clear(); // position -> zero, limit -> capacity
            if (getSource().read(buffer) == -1) {
                throw new EOFException("reached to an end");
            }
            buffer.flip(); // limit -> position, position -> zero
        }
        return buffer.get() & 0xFF;
    }

    // ---------------------------------------------------------------------------------------------------------- source
    @Override
    protected ReadableByteChannel getSource() {
        return super.getSource();
    }

    @Override
    protected void setSource(final ReadableByteChannel source) {
        super.setSource(source);
    }

    // ---------------------------------------------------------------------------------------------------------- buffer

    /**
     * Returns the current value of {@code buffer} attribute.
     *
     * @return the current value of {@code buffer} attribute.
     */
    protected ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * Replaces the current value of {@code buffer} attribute with specified value.
     *
     * @param buffer new value for {@code buffer} attribute.
     */
    protected void setBuffer(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteBuffer buffer;
}
