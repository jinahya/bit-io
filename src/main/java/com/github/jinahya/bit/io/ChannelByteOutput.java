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

import static java.nio.ByteBuffer.allocate;

@Deprecated
class ChannelByteOutput extends BufferByteOutput {

    // -----------------------------------------------------------------------------------------------------------------
    public static ChannelByteOutput of(final WritableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new ChannelByteOutput(null, channel) {
            @Override
            public void write(final int value) throws IOException {
                final ByteBuffer target = getTarget();
                if (target == null) {
                    setTarget(allocate(1));
                }
                super.write(value);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    public ChannelByteOutput(final ByteBuffer target, final WritableByteChannel channel) {
        super(target);
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final int value) throws IOException {
        final ByteBuffer target = getTarget();
        if (target.capacity() == 0) {
            throw new IllegalStateException("target.capacity == 0");
        }
        while (!target.hasRemaining()) {
            target.flip(); // limit -> position, position -> zero
            final int written = getChannel().write(target);
            target.compact();
        }
        super.write(value);
    }

    // --------------------------------------------------------------------------------------------------------- channel

    /**
     * Returns the current value of {@code channel} attribute.
     *
     * @return the current value of {@code channel} attribute.
     */
    public WritableByteChannel getChannel() {
        return channel;
    }

    /**
     * Replaces the current value of {@code channel} attribute with specified value.
     *
     * @param channel new value for {@code channel} attribute.
     */
    public void setChannel(final WritableByteChannel channel) {
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The channel to which bytes are written.
     */
    private WritableByteChannel channel;
}
