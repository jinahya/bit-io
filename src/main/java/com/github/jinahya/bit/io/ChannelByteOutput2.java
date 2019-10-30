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

class ChannelByteOutput2 extends AbstractByteOutput<WritableByteChannel> {

    // -----------------------------------------------------------------------------------------------------------------
    public static ChannelByteOutput2 of(final WritableByteChannel channel) {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return new ChannelByteOutput2(channel, null) {
            @Override
            public void write(final int value) throws IOException {
                final ByteBuffer buffer = getBuffer();
                if (buffer == null) {
                    setBuffer(allocate(1));
                }
                super.write(value);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    public ChannelByteOutput2(final WritableByteChannel target, final ByteBuffer buffer) {
        super(target);
        this.buffer = buffer;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final int value) throws IOException {
        final WritableByteChannel target = getTarget();
        final ByteBuffer buffer = getBuffer();
        while (!buffer.hasRemaining()) {
            buffer.flip(); // limit -> position, position -> zero
            final int written = target.write(buffer);
            buffer.compact();
        }
        buffer.put((byte) value);
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

    /**
     * The buffer to which bytes are written.
     */
    private ByteBuffer buffer;
}
