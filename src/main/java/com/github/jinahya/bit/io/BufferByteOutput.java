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
 * A byte output writes bytes to a {@link ByteBuffer}.
 *
 * <p>This class writes directly to the {@link #target target} buffer; it does not drain on its own. The buffer is
 * meant to be a <em>caller-managed window</em>: keep a reference to it and, before a run of writes,
 * {@linkplain ByteBuffer#hasRemaining() pre-check} it and purge (drain) it to your ultimate target when it fills.
 * Writing past the end (i.e. without pre-checking) throws an unchecked {@link java.nio.BufferOverflowException} — it is
 * a signal of a missing pre-check, not a recoverable full-target condition.</p>
 *
 * <p>The {@link #from(WritableByteChannel)} factory method, however, returns a channel-backed, write-through variant
 * that drains every byte to a channel as it is written.</p>
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteInput
 */
public class BufferByteOutput
        extends AbstractByteOutput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a new byte output that writes bytes, one at a time, directly to the specified writable byte channel.
     *
     * <p>The returned output is <em>write-through</em>: it is backed by a single-byte {@link ByteBuffer} and drains it
     * to the channel on every {@link ByteOutput#write(int) write}, so no byte is ever left buffered and there is
     * nothing to flush or close. The trade-off is throughput: it issues one
     * {@link WritableByteChannel#write(ByteBuffer) channel write} per byte and is <strong>not</strong> efficient for
     * high-throughput writing; when throughput matters, wrap the target in a buffering layer (for example, a
     * {@link java.io.BufferedOutputStream} bridged to a channel with {@link java.nio.channels.Channels}) instead.</p>
     *
     * <p>A <em>blocking</em> channel is recommended. If the channel reports no progress by returning {@code 0}, the
     * returned output throws an {@link IOException} instead of busy-waiting.</p>
     *
     * @param channel the writable byte channel to which bytes are written; must not be {@code null}.
     * @return a new byte output writing to {@code channel}.
     * @throws NullPointerException if {@code channel} is {@code null}.
     * @see BufferByteInput#from(java.nio.channels.ReadableByteChannel)
     */
    public static ByteOutput from(final WritableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new BufferByteOutput(ByteBuffer.allocate(1)) {
            @Override
            public void write(final int value) throws IOException {
                super.write(value);
                for (target.flip(); target.hasRemaining(); ) {
                    if (channel.write(target) == 0) {
                        throw new IOException("channel write made no progress");
                    }
                }
                target.clear();
            }
        };
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
