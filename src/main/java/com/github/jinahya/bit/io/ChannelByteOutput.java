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

class ChannelByteOutput extends BufferByteOutput {

    // -----------------------------------------------------------------------------------------------------------------
    public ChannelByteOutput(final ByteBuffer target, final WritableByteChannel channel) {
        super(target);
        this.channel = channel;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final int value) throws IOException {
        super.write(value);
        if (!getTarget().hasRemaining()) { // no more space to write
            getTarget().flip(); // limit -> position, position -> zero
            while (getChannel().write(getTarget()) == 0) {
                // empty
            }
            getTarget().compact();
            assert getTarget().hasRemaining();
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
