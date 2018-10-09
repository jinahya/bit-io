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
 * A implementation of {@link ByteOutput} uses an instance of {@link WritableByteChannel} as its final target.
 *
 * @param <T> channel type parameter.
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class ChannelByteOutput<T extends WritableByteChannel> extends BufferByteOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance built on top of specified byte buffer and channel.
     *
     * @param buffer  the byte buffer to which bytes are written or {@code null} if it's supposed to be lazily
     *                initialized and set.
     * @param channel the channel to which buffered bytes are written or {@code null} if it's supposed to be lazily
     *                initialized and set.
     */
    public ChannelByteOutput(final ByteBuffer buffer, final T channel) {
        super(buffer);
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes an unsigned 8 bit value to the desired target. The {@code write(int)} method of {@code ChannelByteOutput}
     * class loops for writing already buffered bytes to {@link #channel} while {@link #target} has no remaining and
     * calls {@link BufferByteOutput#write(int)} method with given. Override this method if the {@link #target} or
     * {@link #channel} is supposed to be lazily initialized or adjusted.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #target
     * @see ByteBuffer#put(byte)
     */
    @Override
    public void write(final int value) throws IOException {
        while (!target.hasRemaining()) {
            target.flip(); // limit->position, position->zero
            channel.write(target);
            target.compact(); // position->n+1, limit->capacity
        }
        super.write(value);
    }

    // ---------------------------------------------------------------------------------------------------------- target
    @Override
    @SuppressWarnings("unchecked")
    public ChannelByteOutput<T> target(final ByteBuffer target) {
        return (ChannelByteOutput<T>) super.target(target);
    }

    // --------------------------------------------------------------------------------------------------------- channel

    /**
     * Returns the current value of {@link #channel}.
     *
     * @return the current value of {@link #channel}.
     */
    public T getChannel() {
        return channel;
    }

    /**
     * Replaces the current value of {@link #channel} with given.
     *
     * @param channel new value for {@link #channel}
     */
    public void setChannel(final T channel) {
        this.channel = channel;
    }

    /**
     * Replaces the current value of {@link #channel} with given and returns this instance.
     *
     * @param channel new value for {@link #channel}
     * @return this instance
     */
    public ChannelByteOutput<T> channel(final T channel) {
        setChannel(channel);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The target to which buffered bytes are written from {@link #target}.
     */
    protected T channel;
}
