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
 * A byte output reads bytes from a channel.
 *
 * @param <T> channel type parameter
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @see ChannelByteOutput
 * @deprecated Use {@link BufferByteInput#of(ReadableByteChannel, int)}
 */
@Deprecated // forRemoval = true
public class ChannelByteInput<T extends ReadableByteChannel> extends BufferByteInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance built on top of the specified byte buffer and the channel.
     *
     * @param buffer  the byte buffer; {@code null} if it's supposed to be lazily initialized and set.
     * @param channel the channel from which bytes are read or {@code null} if it's supposed to be lazily initialized
     *                and set.
     */
    public ChannelByteInput(final ByteBuffer buffer, final T channel) {
        super(buffer);
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code ChannelByteInput} invokes {@link
     * ReadableByteChannel#read(java.nio.ByteBuffer)} with {@link #source} while it has no remaining and invokes {@link
     * BufferByteInput#read() super.read()}. Override this method if either {@link #source} or {@link #channel} is
     * supposed to be lazily initialized and set.
     *
     * @return {@inheritDoc }
     * @throws IOException {@inheritDoc}
     * @see #source
     * @see #channel
     * @see ReadableByteChannel#read(java.nio.ByteBuffer)
     * @see BufferByteInput#read()
     */
    @Override
    public int read() throws IOException {
        while (!source.hasRemaining()) {
            source.clear(); // position->zero, limit->capacity
            while (source.position() == 0) {
                if (channel.read(source) == -1) {
                    throw new EOFException();
                }
            }
            source.flip(); // limit->position, position->zero
        }
        return super.read();
    }

    // ---------------------------------------------------------------------------------------------------------- source
    @Override
    @SuppressWarnings("unchecked")
    public ChannelByteInput<T> source(final ByteBuffer source) {
        return (ChannelByteInput<T>) super.source(source);
    }

    // --------------------------------------------------------------------------------------------------------- channel

    /**
     * Returns the current value of {@link #channel}.
     *
     * @return the current value of {@link #channel}
     */
    public T getChannel() {
        return channel;
    }

    /**
     * Replaces the value of {@link #channel} with given.
     *
     * @param channel new value for {@link #channel}
     */
    public void setChannel(final T channel) {
        this.channel = channel;
    }

    /**
     * Replaces the value of {@link #channel} with given and returns this instance.
     *
     * @param channel new value for {@link #channel}.
     * @return this instance
     */
    public ChannelByteInput<T> channel(final T channel) {
        setChannel(channel);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The channel from which bytes are read into {@link #source}.
     */
    protected T channel;
}
