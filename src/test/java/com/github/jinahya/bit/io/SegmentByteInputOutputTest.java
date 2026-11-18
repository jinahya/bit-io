package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
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

import java.io.EOFException;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link SegmentByteInput} and {@link SegmentByteOutput}.
 */
class SegmentByteInputOutputTest {

    @Test
    void writesAndReadsBytesAtIncreasingOffsets() throws IOException {
        try (Arena arena = Arena.ofConfined()) {
            final MemorySegment segment = arena.allocate(3L);
            final SegmentByteOutput output = new SegmentByteOutput(segment);
            output.write(0x00);
            output.write(0x7F);
            output.write(0x1FF); // only the low 8 bits
            assertEquals(3L, output.getOffset());
            assertArrayEquals(new byte[]{0x00, 0x7F, (byte) 0xFF}, segment.toArray(ValueLayout.JAVA_BYTE));
            final SegmentByteInput input = new SegmentByteInput(segment);
            assertEquals(0x00, input.read());
            assertEquals(0x7F, input.read());
            assertEquals(0xFF, input.read());
            assertEquals(3L, input.getOffset());
        }
    }

    @Test
    void readThrowsEofAtTheEndAndKeepsDoingSo() throws IOException {
        final SegmentByteInput input = new SegmentByteInput(MemorySegment.ofArray(new byte[]{0x5A}));
        assertEquals(0x5A, input.read());
        assertThrows(EOFException.class, input::read);
        assertThrows(EOFException.class, input::read);
        assertThrows(EOFException.class, () -> new SegmentByteInput(MemorySegment.ofArray(new byte[0])).read());
    }

    @Test
    void writeThrowsWhenTheSegmentIsFull() throws IOException {
        final SegmentByteOutput output = new SegmentByteOutput(MemorySegment.ofArray(new byte[1]));
        output.write(1);
        final IOException thrown = assertThrows(IOException.class, () -> output.write(2));
        assertTrue(thrown.getMessage().contains("full"), thrown.getMessage());
        assertEquals(1L, output.getOffset());
    }

    @Test
    void segmentErrorsPropagateUnchanged() {
        final MemorySegment readOnly = MemorySegment.ofArray(new byte[1]).asReadOnly();
        assertThrows(IllegalArgumentException.class, () -> new SegmentByteOutput(readOnly).write(1));
        final MemorySegment closed;
        try (Arena arena = Arena.ofConfined()) {
            closed = arena.allocate(1L);
        }
        assertThrows(IllegalStateException.class, () -> new SegmentByteInput(closed).read());
        assertThrows(IllegalStateException.class, () -> new SegmentByteOutput(closed).write(1));
    }

    @Test
    void failedAccessesLeaveTheOffsetUnchanged() throws Exception {
        final SegmentByteOutput readOnlyOutput = new SegmentByteOutput(MemorySegment.ofArray(new byte[1]).asReadOnly());
        assertThrows(IllegalArgumentException.class, () -> readOnlyOutput.write(1));
        assertEquals(0L, readOnlyOutput.getOffset());
        final MemorySegment closed;
        try (Arena arena = Arena.ofConfined()) {
            closed = arena.allocate(1L);
        }
        final SegmentByteInput closedInput = new SegmentByteInput(closed);
        assertThrows(IllegalStateException.class, closedInput::read);
        assertEquals(0L, closedInput.getOffset());
        final SegmentByteOutput closedOutput = new SegmentByteOutput(closed);
        assertThrows(IllegalStateException.class, () -> closedOutput.write(1));
        assertEquals(0L, closedOutput.getOffset());
    }

    @Test
    void wrongThreadAccessesLeaveTheOffsetUnchangedForARetryOnTheOwner() throws Exception {
        try (Arena arena = Arena.ofConfined()) {
            final MemorySegment segment = arena.allocate(2L);
            segment.set(ValueLayout.JAVA_BYTE, 0L, (byte) 0x5A);
            final SegmentByteInput input = new SegmentByteInput(segment);
            final SegmentByteOutput output = new SegmentByteOutput(segment);
            final Throwable[] thrown = new Throwable[2];
            final Thread other = new Thread(() -> {
                try {
                    input.read();
                } catch (final Throwable t) {
                    thrown[0] = t;
                }
                try {
                    output.write(0x33);
                } catch (final Throwable t) {
                    thrown[1] = t;
                }
            });
            other.start();
            other.join();
            assertInstanceOf(WrongThreadException.class, thrown[0]);
            assertInstanceOf(WrongThreadException.class, thrown[1]);
            assertEquals(0L, input.getOffset());
            assertEquals(0L, output.getOffset());
            assertEquals(0x5A, input.read()); // the same byte, on the owner thread
            output.write(0x33);
            assertEquals(1L, input.getOffset());
            assertEquals(1L, output.getOffset());
            assertEquals((byte) 0x33, segment.get(ValueLayout.JAVA_BYTE, 0L));
        }
    }

    @Test
    void bitIoOverSegmentsMatchesBitIoOverArrays() throws IOException {
        final byte[] expected = new byte[32];
        final BitOutput arrayOutput = new DefaultBitOutput(new ArrayByteOutput(expected));
        final MemorySegment segment = MemorySegment.ofArray(new byte[32]);
        final BitOutput segmentOutput = new DefaultBitOutput(new SegmentByteOutput(segment));
        for (final BitOutput output : new BitOutput[]{arrayOutput, segmentOutput}) {
            output.writeBoolean(true);
            output.writeInt(17, -12345);
            output.writeLong64Le(0x0123456789ABCDEFL);
            output.writeDouble64(Math.PI);
            output.align(1);
        }
        assertArrayEquals(expected, segment.toArray(ValueLayout.JAVA_BYTE));
        final BitInput input = new DefaultBitInput(new SegmentByteInput(segment));
        assertTrue(input.readBoolean());
        assertEquals(-12345, input.readInt(17));
        assertEquals(0x0123456789ABCDEFL, input.readLong64Le());
        assertEquals(Math.PI, input.readDouble64());
    }

    @Test
    void rejectsNullSegments() {
        assertThrows(NullPointerException.class, () -> new SegmentByteInput(null));
        assertThrows(NullPointerException.class, () -> new SegmentByteOutput(null));
    }
}
