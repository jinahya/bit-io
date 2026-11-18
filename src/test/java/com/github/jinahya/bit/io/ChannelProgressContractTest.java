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
        output.write(0x22); // fills the buffer, then drains one byte (partial write)
        output.write(0x33); // fills again, drains one more byte
        output.write(0x44); // fills again, drains one more byte

        assertArrayEquals(new byte[]{0x11, 0x22, 0x33}, channel.bytes.toByteArray());
    }

    /**
     * Tests that reads which read no bytes are retried until progress, neither dropping nor duplicating bytes.
     *
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://github.com/jinahya/bit-io/issues/20">#20</a>
     */
    @Test
    void channelByteInputRetriesZeroProgressReads() throws IOException {
        final StutteringReadableByteChannel channel = new StutteringReadableByteChannel(
                new PartialReadableByteChannel(new byte[]{0x11, 0x22, 0x33, 0x44, 0x55}, 2), 3);
        final ByteInput input = new ChannelByteInput(channel, ByteBuffer.allocate(4));

        assertEquals(0x11, input.read());
        assertEquals(0x22, input.read());
        assertEquals(0x33, input.read());
        assertEquals(0x44, input.read());
        assertEquals(0x55, input.read());
        assertThrows(java.io.EOFException.class, input::read);
        assertTrue(channel.zeros > 0, "zero-progress reads occurred");
    }

    /**
     * Tests that zero-progress reads through the public factory are retried until progress.
     *
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://github.com/jinahya/bit-io/issues/20">#20</a>
     */
    @Test
    void channelFactoryInputRetriesZeroProgressReads() throws IOException {
        final ByteInput input = BufferByteInput.from(new StutteringReadableByteChannel(
                new PartialReadableByteChannel(new byte[]{0x11, 0x22}, 1), 2));

        assertEquals(0x11, input.read());
        assertEquals(0x22, input.read());
        assertThrows(java.io.EOFException.class, input::read);
    }

    /**
     * Tests that writes which write no bytes are retried until progress, neither dropping nor duplicating bytes.
     *
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://github.com/jinahya/bit-io/issues/20">#20</a>
     */
    @Test
    void channelByteOutputRetriesZeroProgressWrites() throws IOException {
        final PartialWritableByteChannel partial = new PartialWritableByteChannel(1);
        final StutteringWritableByteChannel channel = new StutteringWritableByteChannel(partial, 3);
        final ByteOutput output = new ChannelByteOutput(channel, ByteBuffer.allocate(2));

        output.write(0x11);
        output.write(0x22);
        output.write(0x33);
        output.write(0x44);

        assertArrayEquals(new byte[]{0x11, 0x22, 0x33}, partial.bytes.toByteArray());
        assertTrue(channel.zeros > 0, "zero-progress writes occurred");
    }

    /**
     * Tests that zero-progress writes through the public factory are retried until progress.
     *
     * @throws IOException if an I/O error occurs.
     * @see <a href="https://github.com/jinahya/bit-io/issues/20">#20</a>
     */
    @Test
    void channelFactoryOutputRetriesZeroProgressWrites() throws IOException {
        final PartialWritableByteChannel partial = new PartialWritableByteChannel(1);
        final ByteOutput output = BufferByteOutput.from(new StutteringWritableByteChannel(partial, 2));

        output.write(0x11);
        output.write(0x22);

        assertArrayEquals(new byte[]{0x11, 0x22}, partial.bytes.toByteArray());
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

    /**
     * A readable byte channel which reads no bytes for a number of calls before each delegated read.
     */
    private static final class StutteringReadableByteChannel
            implements ReadableByteChannel {

        private StutteringReadableByteChannel(final ReadableByteChannel delegate, final int zerosBeforeEach) {
            this.delegate = delegate;
            this.zerosBeforeEach = zerosBeforeEach;
        }

        @Override
        public int read(final ByteBuffer dst) throws IOException {
            if (pending++ < zerosBeforeEach) {
                zeros++;
                return 0;
            }
            pending = 0;
            return delegate.read(dst);
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {
            // no-op
        }

        private final ReadableByteChannel delegate;

        private final int zerosBeforeEach;

        private int pending;

        private int zeros;
    }

    /**
     * A writable byte channel which writes no bytes for a number of calls before each delegated write.
     */
    private static final class StutteringWritableByteChannel
            implements WritableByteChannel {

        private StutteringWritableByteChannel(final WritableByteChannel delegate, final int zerosBeforeEach) {
            this.delegate = delegate;
            this.zerosBeforeEach = zerosBeforeEach;
        }

        @Override
        public int write(final ByteBuffer src) throws IOException {
            if (pending++ < zerosBeforeEach) {
                zeros++;
                return 0;
            }
            pending = 0;
            return delegate.write(src);
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void close() {
            // no-op
        }

        private final WritableByteChannel delegate;

        private final int zerosBeforeEach;

        private int pending;

        private int zeros;
    }
}
