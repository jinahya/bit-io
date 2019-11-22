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
import java.nio.channels.WritableByteChannel;

/**
 * A writable byte channel whose {@link WritableByteChannel#write(ByteBuffer)} method just discharges the buffer.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see WhiteByteChannel
 * @see BlackOutputStream
 */
@Slf4j
final class BlackByteChannel implements WritableByteChannel {

    // -----------------------------------------------------------------------------------------------------------------
    static final WritableByteChannel INSTANCE = new BlackByteChannel();

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a sequence of bytes to this channel from the buffer. The {@code write(ByteBuffer)} method of {@code
     * BlackByteChannel} class simply makes the buffer as <i>fully-drained</i> by setting the {@code position} property
     * with the current value of the {@code limit} property and return the previous value of {@code remaining()}.
     *
     * @param src the buffer whose remaining bytes are drained.
     * @return the number of bytes drained.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int write(final ByteBuffer src) throws IOException {
        final int written = src.remaining();
        src.position(src.limit());
        return written;
    }

    /**
     * Tells whether or not this channel is open. The {@code isOpen()} method of {@code BlackByteChannel} class always
     * returns {@code true}.
     *
     * @return {@code true}
     */
    @Override
    public boolean isOpen() {
        return true;
    }

    /**
     * Closes this channel. The {@code close()} method of {@code BlackByteChannel} class does nothing.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        // does nothing
    }
}
