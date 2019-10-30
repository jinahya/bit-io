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

class ChannelByteInput2 extends AbstractByteInput<ReadableByteChannel> {

    // -----------------------------------------------------------------------------------------------------------------
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
    public ChannelByteInput2(final ReadableByteChannel source, final ByteBuffer buffer) {
        super(source);
        this.buffer = buffer;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        final ReadableByteChannel source = getSource();
        final ByteBuffer buffer = getBuffer();
        while (!buffer.hasRemaining()) {
            buffer.clear(); // position -> zero, limit -> capacity
            if (source.read(buffer) == -1) {
                throw new EOFException("reached to an end");
            }
            buffer.flip(); // limit -> position, position -> zero
        }
        return buffer.get() & 0xFF;
    }

    // ---------------------------------------------------------------------------------------------------------- buffer

    /**
     * Returns the current value of {@code buffer} attribute.
     *
     * @returnp the current value of {@code buffer} attribute.
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * Replaces the current value of {@code buffer} attribute with specified value.
     *
     * @param buffer new value for {@code buffer} attribute.
     */
    public void setBuffer(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteBuffer buffer;
}
