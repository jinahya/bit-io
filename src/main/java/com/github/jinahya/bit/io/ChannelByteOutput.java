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

class ChannelByteOutput extends BufferByteOutput {

    // -----------------------------------------------------------------------------------------------------------------
    public ChannelByteOutput(final ByteBuffer target, final WritableByteChannel channel) {
        super(target);
        this.channel = channel;
    }

    public ChannelByteOutput(final WritableByteChannel channel) {
        this(allocate(1), channel);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final int value) throws IOException {
        super.write(value);
        final ByteBuffer target = getTarget();
        final WritableByteChannel channel = getChannel();
        while (!target.hasRemaining()) {
            target.flip(); // limit -> position, position -> zero
            final int written = channel.write(target);
            target.compact();
        }
    }

    // --------------------------------------------------------------------------------------------------------- channel

    public WritableByteChannel getChannel() {
        return channel;
    }

    public void setChannel(final WritableByteChannel channel) {
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The channel to which bytes are written.
     */
    private WritableByteChannel channel;
}
