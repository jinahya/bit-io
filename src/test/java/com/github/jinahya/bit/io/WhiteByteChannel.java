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

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/**
 * A readable byte channel whose {@link ReadableByteChannel#read(ByteBuffer)} method charges the buffer.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BlackByteChannel
 * @see WhiteInputStream
 */
@Slf4j
final class WhiteByteChannel implements ReadableByteChannel {

    // -----------------------------------------------------------------------------------------------------------------
    static final ReadableByteChannel INSTANCE = new WhiteByteChannel();

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a sequence of bytes from this channel into the given buffer. The {@code read(ByteBuffer)} method of {@code
     * WhiteByteChannel} class simply makes the buffer as <i>fully-charged</i> by setting the {@code position} property
     * with the current value of the {@code limit} property and returns the previous value of {@code remaining()}.
     *
     * @param dst the buffer to be charged.
     * @return the number of bytes charged.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read(final ByteBuffer dst) throws IOException {
        int read = dst.remaining();
        ((ByteBuffer) dst).position(dst.limit());
        return read;
    }

    /**
     * Tells whether or not this channel is open. The {@code isOpen()} method of {@code WhiteByteChannel} class always
     * returns {@code true}.
     *
     * @return {@code true}.
     */
    @Override
    public boolean isOpen() {
        return true;
    }

    /**
     * Closes this channel. The {@code close()} method of {@code WhiteByteChannel} class does nothing.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        // empty
    }
}
