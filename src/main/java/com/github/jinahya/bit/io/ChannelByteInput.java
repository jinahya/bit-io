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

@Deprecated
class ChannelByteInput extends BufferByteInput {

    // -----------------------------------------------------------------------------------------------------------------
    public static ChannelByteInput of(final ReadableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new ChannelByteInput(null, channel) {
            @Override
            public int read() throws IOException {
                if (getSource() == null) {
                    setSource((ByteBuffer) allocate(1).position(1));
                }
                return super.read();
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    public ChannelByteInput(final ByteBuffer source, final ReadableByteChannel channel) {
        super(source);
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        final ByteBuffer source = getSource();
        if (source.capacity() == 0) {
            throw new IllegalStateException("source.capacity == 0");
        }
        while (!source.hasRemaining()) {
            source.clear(); // position -> zero, limit -> capacity
            if (getChannel().read(source) == -1) {
                throw new EOFException("reached to an end");
            }
            source.flip(); // limit -> position, position -> zero
        }
        return super.read();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the current value of {@code channel} attribute.
     *
     * @return the current value of {@code channel} attribute.
     */
    public ReadableByteChannel getChannel() {
        return channel;
    }

    /**
     * Replaces the current value of {@code channel} attribute with specified value.
     *
     * @param channel new value for {@code channel} attribute.
     */
    public void setChannel(final ReadableByteChannel channel) {
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ReadableByteChannel channel;
}
