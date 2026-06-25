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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing of {@link BufferByteOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BufferByteInputTest
 */
class BufferByteOutputTest {

    @Test
    void writesBytesToBuffer() throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(3);
        final BufferByteOutput output = new BufferByteOutput(buffer);

        output.write(0x00);
        output.write(0x7F);
        output.write(0xFF);

        assertArrayEquals(new byte[]{0x00, 0x7F, (byte) 0xFF}, buffer.array());
    }

    @Test
    void rejectsNullBuffer() {
        assertThrows(NullPointerException.class, () -> new BufferByteOutput(null));
    }

    @Test
    void throwsBufferOverflowWhenBufferFull() throws IOException {
        final BufferByteOutput output = new BufferByteOutput(ByteBuffer.allocate(0));

        assertThrows(BufferOverflowException.class, () -> output.write(0x00));
    }

    @Test
    void writesAdvancePositionAndRespectLimit() throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.position(1);
        buffer.limit(2);
        final BufferByteOutput output = new BufferByteOutput(buffer);

        output.write(0x7E);
        assertArrayEquals(new byte[]{0x00, 0x7E, 0x00}, buffer.array());
        assertThrows(BufferOverflowException.class, () -> output.write(0x7F));
    }

    // ------------------------------------------------------------------------------------------------------------ from

    @Test
    void from_NullPointerException_NullChannel() {
        assertThrows(NullPointerException.class, () -> BufferByteOutput.from(null));
    }

    /**
     * Verifies that {@link BufferByteOutput#from(WritableByteChannel)} is write-through: every written byte reaches the
     * channel immediately, with no flushing required.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void from_WritesThrough_ToChannel() throws IOException {
        final byte[] bytes = new byte[ThreadLocalRandom.current().nextInt(1, 1024)];
        ThreadLocalRandom.current().nextBytes(bytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final WritableByteChannel channel = java.nio.channels.Channels.newChannel(baos);
        final ByteOutput output = BufferByteOutput.from(channel);
        for (final byte b : bytes) {
            output.write(b & 0xFF);
        }
        // write-through: all bytes are already in the stream, without any flush/close
        assertArrayEquals(bytes, baos.toByteArray());
    }

    @Test
    void from_UsesSingleByteWrites_ToChannel() throws IOException {
        final RecordingWritableByteChannel channel = new RecordingWritableByteChannel();
        final ByteOutput output = BufferByteOutput.from(channel);

        output.write(0x11);
        assertArrayEquals(new byte[]{0x11}, channel.bytes.toByteArray());
        assertEquals(1, channel.writes);
        output.write(0x22);
        assertArrayEquals(new byte[]{0x11, 0x22}, channel.bytes.toByteArray());
        assertEquals(2, channel.writes);
    }

    private static final class RecordingWritableByteChannel
            implements WritableByteChannel {

        @Override
        public int write(final ByteBuffer src) {
            assertEquals(1, src.remaining());
            writes++;
            bytes.write(src.get() & 0xFF);
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

        private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        private int writes;
    }
}
