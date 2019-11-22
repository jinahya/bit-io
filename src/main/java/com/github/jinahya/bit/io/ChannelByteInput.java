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
 * A byte input reads bytes from a channel.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Deprecated
class ChannelByteInput extends BufferByteInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance reads bytes from specified channel.
     *
     * @param channel the channel from which bytes are read; must be not {@code null}.
     * @return a new instance.
     */
    public static ChannelByteInput of(final ReadableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new ChannelByteInput(null, channel) {
            @Override
            public int read() throws IOException {
                if (getSource() == null) {
                    setSource((ByteBuffer) allocate(1).position(1)); // already drained
                }
                return super.read();
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified buffer and channel.
     *
     * @param source  the byte buffer for reading bytes from the channel; {@code null} if it's supposed to be lazily
     *                initialized and set.
     * @param channel the channel from which bytes are read; {@code null} if it's supposed to be lazily initialized and
     *                set.
     * @see #getSource()
     * @see #setSource(ByteBuffer)
     * @see #getChannel()
     * @see #setChannel(ReadableByteChannel)
     */
    public ChannelByteInput(final ByteBuffer source, final ReadableByteChannel channel) {
        super(source);
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "channel=" + channel
               + "}";
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        final ByteBuffer source = getSource();
        while (!source.hasRemaining()) {
            source.clear(); // position -> zero, limit -> capacity
            if (getChannel().read(source) == -1) {
                throw new EOFException("reached to an end");
            }
            source.flip(); // limit -> position, position -> zero
        }
        return super.read();
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

    // --------------------------------------------------------------------------------------------------------- channel

    /**
     * Returns the current value of {@code channel} attribute.
     *
     * @return the current value of {@code channel} attribute.
     */
    protected ReadableByteChannel getChannel() {
        return channel;
    }

    /**
     * Replaces the current value of {@code channel} attribute with specified value.
     *
     * @param channel new value for {@code channel} attribute.
     */
    protected void setChannel(final ReadableByteChannel channel) {
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The channel from which bytes are read.
     */
    private ReadableByteChannel channel;
}
