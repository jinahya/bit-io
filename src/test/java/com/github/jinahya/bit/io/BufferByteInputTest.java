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

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing {@link BufferByteInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BufferByteOutputTest
 */
class BufferByteInputTest {

    @Test
    void readsUnsignedBytesFromBuffer() throws IOException {
        final BufferByteInput input = new BufferByteInput(ByteBuffer.wrap(new byte[]{0x00, 0x7F, (byte) 0xFF}));

        assertEquals(0x00, input.read());
        assertEquals(0x7F, input.read());
        assertEquals(0xFF, input.read());
    }

    @Test
    void rejectsNullBuffer() {
        assertThrows(NullPointerException.class, () -> new BufferByteInput(null));
    }

    @Test
    void throwsBufferUnderflowWhenBufferExhausted() throws IOException {
        final BufferByteInput input = new BufferByteInput(ByteBuffer.allocate(0));

        assertThrows(BufferUnderflowException.class, input::read);
    }

    @Test
    void readsAdvancePositionAndRespectLimit() throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[]{0x10, 0x20, 0x30});
        buffer.position(1);
        buffer.limit(2);
        final BufferByteInput input = new BufferByteInput(buffer);

        assertEquals(0x20, input.read());
        assertEquals(2, buffer.position());
        assertEquals(2, buffer.limit());
        assertThrows(BufferUnderflowException.class, input::read);
    }

    // ------------------------------------------------------------------------------------------------------------ from

    @Test
    void from_NullPointerException_NullChannel() {
        assertThrows(NullPointerException.class, () -> BufferByteInput.from(null));
    }

    @Test
    void from_ReadsAllBytes_FromChannel() throws IOException {
        final byte[] bytes = new byte[ThreadLocalRandom.current().nextInt(1, 1024)];
        ThreadLocalRandom.current().nextBytes(bytes);
        final ReadableByteChannel channel = java.nio.channels.Channels.newChannel(new ByteArrayInputStream(bytes));
        final ByteInput input = BufferByteInput.from(channel);
        for (final byte b : bytes) {
            assertEquals(b & 0xFF, input.read());
        }
    }

    @Test
    void from_UsesSingleByteReads_FromChannel() throws IOException {
        final SingleByteReadableByteChannel channel = new SingleByteReadableByteChannel(new byte[]{0x11, 0x22});
        final ByteInput input = BufferByteInput.from(channel);

        assertEquals(0x11, input.read());
        assertEquals(1, channel.reads);
        assertEquals(0x22, input.read());
        assertEquals(2, channel.reads);
        assertThrows(EOFException.class, input::read);
        assertEquals(3, channel.reads);
    }

    @Test
    void from_EOFException_WhenChannelExhausted() throws IOException {
        final ReadableByteChannel channel =
                java.nio.channels.Channels.newChannel(new ByteArrayInputStream(new byte[0]));
        final ByteInput input = BufferByteInput.from(channel);
        assertThrows(EOFException.class, input::read);
    }

    private static final class SingleByteReadableByteChannel
            implements ReadableByteChannel {

        private SingleByteReadableByteChannel(final byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public int read(final ByteBuffer dst) {
            assertEquals(1, dst.remaining());
            reads++;
            if (index == bytes.length) {
                return -1;
            }
            dst.put(bytes[index++]);
            return 1;
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {
            // no-op
        }

        private final byte[] bytes;

        private int index;

        private int reads;
    }
}
