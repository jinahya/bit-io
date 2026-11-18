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
import java.util.Objects;

/**
 * A byte output that writes bytes to a {@link ByteBuffer}.
 *
 * <p>Bytes are written directly to the {@link #target target} buffer; this class never drains it. The buffer is a
 * <em>caller-managed window</em>: keep a reference to it and drain it to your ultimate target before it fills. Writing
 * past its end throws an unchecked {@link java.nio.BufferOverflowException}, which signals a missing drain rather than
 * a recoverable full-target condition.</p>
 *
 * <p>For a channel-backed, write-through output that drains every byte to a channel as it is written, use the
 * {@link #from(WritableByteChannel)} factory method.</p>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BufferByteInput
 */
public class BufferByteOutput
        extends AbstractByteOutput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a new byte output that writes bytes to the specified writable byte channel, draining each byte to the
     * channel as it is written so nothing is left buffered. It issues one
     * {@link WritableByteChannel#write(ByteBuffer) channel write} per byte; this is simple but <strong>not</strong>
     * efficient for high-throughput writing; when throughput matters, wrap the target in a buffering layer instead.
     *
     * <p>The {@code channel} is expected to be in <em>blocking</em> mode; it is the caller's responsibility. The
     * returned output retries a {@link WritableByteChannel#write(ByteBuffer) channel write} which writes no bytes,
     * without any pause or limit, until the byte is written. Hence, with a non-blocking channel (or any channel which
     * keeps writing no bytes), {@link ByteOutput#write(int) write(int)} busy-waits, occupying the calling thread, until
     * the channel accepts the byte.</p>
     *
     * @param channel the writable byte channel to which bytes are written; must not be {@code null}.
     * @return a new byte output writing to {@code channel}.
     * @throws NullPointerException if {@code channel} is {@code null}.
     * @see BufferByteInput#from(java.nio.channels.ReadableByteChannel)
     */
    public static ByteOutput from(final WritableByteChannel channel) {
        Objects.requireNonNull(channel, "channel is null");
        return new ChannelByteOutput(channel, ByteBuffer.allocate(1));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance which writes bytes to specified byte buffer.
     *
     * @param target the byte buffer to which bytes are written; must not be {@code null}.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public BufferByteOutput(final ByteBuffer target) {
        super(target);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code BufferByteOutput} class invokes
     * {@link ByteBuffer#put(byte)} method, on the {@link #target target} buffer, with specified value casted as
     * {@code byte}.
     *
     * @param value {@inheritDoc}
     * @throws java.nio.BufferOverflowException if the {@link #target target} buffer has no remaining space.
     * @throws IOException                      {@inheritDoc}
     * @see ByteBuffer#put(byte)
     * @see BufferByteInput#read()
     */
    @Override
    public void write(final int value) throws IOException {
        target.put((byte) value);
    }
}
