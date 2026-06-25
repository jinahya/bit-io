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
 * A byte input reads bytes from a {@link ByteBuffer}.
 *
 * <p>This class reads directly from the {@link #source source} buffer; it does not refill on its own. The buffer is
 * meant to be a <em>caller-managed window</em>: keep a reference to it and, before a run of reads,
 * {@linkplain ByteBuffer#hasRemaining() pre-check} it and refill from your ultimate source when it runs low. Reading
 * past the end (i.e. without pre-checking) throws an unchecked {@link java.nio.BufferUnderflowException} — it is a
 * signal of a missing pre-check, not a recoverable end-of-input.</p>
 *
 * <p>The {@link #from(ReadableByteChannel)} factory method, however, returns a channel-backed variant that does refill
 * from a channel and signals end-of-input with an {@link EOFException} instead.</p>
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput
        extends AbstractByteInput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a new byte input that reads bytes, one at a time, directly from the specified readable byte channel.
     *
     * <p>The returned input is <em>unbuffered</em>: it is backed by a single-byte {@link ByteBuffer} and issues one
     * {@link ReadableByteChannel#read(ByteBuffer) channel read} per byte. This keeps it simple and free of any buffered
     * state, but it is <strong>not</strong> efficient for high-throughput reading; when throughput matters, wrap the
     * source in a buffering layer (for example, a {@link java.io.BufferedInputStream} bridged to a channel with
     * {@link java.nio.channels.Channels}) instead.</p>
     *
     * <p>A <em>blocking</em> channel is recommended. Each read loops, invoking the channel until at least one byte is
     * read; a blocking channel parks until a byte (or end-of-input) is available, whereas a non-blocking channel that
     * is not ready returns {@code 0} and the loop busy-waits (spins) — potentially forever if the channel never becomes
     * ready. The result is still correct in either case; only a blocking channel avoids the spin.</p>
     *
     * @param channel the readable byte channel from which bytes are read; must not be {@code null}.
     * @return a new byte input reading from {@code channel}; its {@link ByteInput#read() read()} method throws an
     * {@link EOFException} when {@code channel} reaches its end.
     * @throws NullPointerException if {@code channel} is {@code null}.
     * @see BufferByteOutput#from(java.nio.channels.WritableByteChannel)
     */
    public static ByteInput from(final ReadableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new BufferByteInput(ByteBuffer.allocate(1)) {
            @Override
            public int read() throws IOException {
                for (source.clear(); source.hasRemaining(); ) {
                    if (channel.read(source) == -1) {
                        throw new EOFException("end of channel reached");
                    }
                }
                source.flip();
                return super.read();
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance which reads bytes from specified byte buffer.
     *
     * @param source the byte buffer from which bytes are read; must not be {@code null}.
     * @throws NullPointerException if {@code source} is {@code null}.
     */
    public BufferByteInput(final ByteBuffer source) {
        super(source);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code BufferByteInput} class invokes {@link ByteBuffer#get()} method,
     * on the {@link #source source} buffer, and returns the result as an unsigned {@code int}.
     *
     * @return {@inheritDoc}
     * @throws java.nio.BufferUnderflowException if the {@link #source source} buffer has no remaining bytes.
     * @throws IOException                       {@inheritDoc}
     * @see ByteBuffer#get()
     * @see BufferByteOutput#write(int)
     */
    @Override
    public int read() throws IOException {
        return source.get() & 0xFF;
    }
}
