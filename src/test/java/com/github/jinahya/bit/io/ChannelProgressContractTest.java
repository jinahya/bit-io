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
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChannelProgressContractTest {

    @Test
    void readableChannelFactoryRejectsZeroProgress() {
        final ByteInput input = BufferByteInput.from(new ZeroProgressReadableByteChannel());
        assertThrows(IOException.class, input::read);
    }

    @Test
    void writableChannelFactoryRejectsZeroProgress() {
        final ByteOutput output = BufferByteOutput.from(new ZeroProgressWritableByteChannel());
        assertThrows(IOException.class, () -> output.write(0x00));
    }

    @Test
    void channelByteInputRejectsZeroProgress() {
        final ByteInput input = new ChannelByteInput(new ZeroProgressReadableByteChannel(), ByteBuffer.allocate(1));
        assertThrows(IOException.class, input::read);
    }

    @Test
    void channelByteOutputRejectsZeroProgress() throws IOException {
        final ByteOutput output = new ChannelByteOutput(new ZeroProgressWritableByteChannel(), ByteBuffer.allocate(1));
        output.write(0x00);
        assertThrows(IOException.class, () -> output.write(0x01));
    }

    @Test
    void channelByteInputHandlesPartialReadsAndThenEof() throws IOException {
        final ByteInput input =
                new ChannelByteInput(new PartialReadableByteChannel(new byte[]{0x11, 0x22, 0x33}, 2),
                                     ByteBuffer.allocate(4));

        assertEquals(0x11, input.read());
        assertEquals(0x22, input.read());
        assertEquals(0x33, input.read());
        assertThrows(java.io.EOFException.class, input::read);
    }

    @Test
    void channelByteOutputHandlesPartialWritesWhenDraining() throws IOException {
        final PartialWritableByteChannel channel = new PartialWritableByteChannel(1);
        final ByteOutput output = new ChannelByteOutput(channel, ByteBuffer.allocate(2));

        output.write(0x11);
        output.write(0x22);
        output.write(0x33); // drains at least part of the full buffer
        output.write(0x44); // drains the remaining part of the previous buffer

        assertArrayEquals(new byte[]{0x11, 0x22}, channel.bytes.toByteArray());
    }

    @Test
    void readableChannelFactoryRejectsZeroProgressBeforeLaterData() {
        final ByteInput input = BufferByteInput.from(new ZeroThenDataReadableByteChannel());

        assertThrows(IOException.class, input::read);
    }

    @Test
    void channelAdaptersToStringIncludeBufferedAdapter() {
        assertTrue(new ChannelByteInput(new ZeroProgressReadableByteChannel(), ByteBuffer.allocate(1))
                           .toString()
                           .contains("buffered="));
        assertTrue(new ChannelByteOutput(new ZeroProgressWritableByteChannel(), ByteBuffer.allocate(1))
                           .toString()
                           .contains("buffered="));
    }

    private static final class ZeroProgressReadableByteChannel
            implements ReadableByteChannel {

        @Override
        public int read(final ByteBuffer dst) {
            return 0;
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {
            // no-op
        }
    }

    private static final class ZeroProgressWritableByteChannel
            implements WritableByteChannel {

        @Override
        public int write(final ByteBuffer src) {
            return 0;
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {
            // no-op
        }
    }

    private static final class ZeroThenDataReadableByteChannel
            implements ReadableByteChannel {

        @Override
        public int read(final ByteBuffer dst) {
            if (reads++ == 0) {
                return 0;
            }
            dst.put((byte) 0x7F);
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

        private int reads;
    }

    private static final class PartialReadableByteChannel
            implements ReadableByteChannel {

        private PartialReadableByteChannel(final byte[] bytes, final int maxBytesPerRead) {
            this.bytes = bytes;
            this.maxBytesPerRead = maxBytesPerRead;
        }

        @Override
        public int read(final ByteBuffer dst) {
            if (index == bytes.length) {
                return -1;
            }
            int read = 0;
            for (final int limit = Math.min(maxBytesPerRead, dst.remaining());
                 read < limit && index < bytes.length; read++) {
                dst.put(bytes[index++]);
            }
            return read;
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

        private final int maxBytesPerRead;

        private int index;
    }

    private static final class PartialWritableByteChannel
            implements WritableByteChannel {

        private PartialWritableByteChannel(final int maxBytesPerWrite) {
            this.maxBytesPerWrite = maxBytesPerWrite;
        }

        @Override
        public int write(final ByteBuffer src) {
            int written = 0;
            for (final int limit = Math.min(maxBytesPerWrite, src.remaining()); written < limit; written++) {
                bytes.write(src.get() & 0xFF);
            }
            return written;
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

        private final int maxBytesPerWrite;
    }
}
