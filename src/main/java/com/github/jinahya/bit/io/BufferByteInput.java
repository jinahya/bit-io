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
 * A {@link ByteInput} uses an instance of {@link ByteBuffer} as its {@link #source}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BufferByteOutput
 */
public class BufferByteInput extends AbstractByteInput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------
    @SuppressWarnings({"Duplicates"})
    public static BufferByteInput of(final ReadableByteChannel channel, final int capacity) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity(" + capacity + ") <= 0");
        }
        return new BufferByteInput(null) {
            @Override
            public int read() throws IOException {
                if (source == null) {
                    source = ByteBuffer.allocate(capacity); // position: zero, limit: capacity
                    source.position(source.limit());
                }
                if (!source.hasRemaining()) { // no bytes to read
                    source.clear(); // position -> zero, limit -> capacity
                    do {
                        if (channel.read(source) == -1) {
                            throw new EOFException("the channel has reached end-of-stream");
                        }
                    } while (source.position() == 0);
                    source.flip(); // limit -> position, position -> zero
                }
                return super.read();
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance built on top of the specified byte buffer.
     *
     * @param source the byte buffer; {@code null} if it's supposed to be lazily initialized and set.
     */
    public BufferByteInput(final ByteBuffer source) {
        super(source);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code BufferByteInput} invokes {@link ByteBuffer#get()}, on what
     * {@link #getSource()} gives, and returns the result as an unsigned 8-bit int. Override this method if {@link
     * #source} is supposed to be lazily initialized or adjusted.
     *
     * @return {@inheritDoc }
     * @throws IOException {@inheritDoc}
     * @see #source
     * @see ByteBuffer#get()
     */
    @Override
    public int read() throws IOException {
        return getSource().get() & 0xFF;
    }

    // ---------------------------------------------------------------------------------------------------------- source

    /**
     * {@inheritDoc}
     *
     * @param source {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BufferByteInput source(final ByteBuffer source) {
        return (BufferByteInput) super.source(source);
    }
}
