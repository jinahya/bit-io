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

import java.io.EOFException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BitValidationContractTest {

    @Test
    @SuppressWarnings("deprecation")
    void bitInputRejectsInvalidSizes() {
        final BitInput input = input();
        assertThrows(IllegalArgumentException.class, () -> input.readByte(false, 0));
        assertThrows(IllegalArgumentException.class, () -> input.readByte(true, Byte.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readByte(0));
        assertThrows(IllegalArgumentException.class, () -> input.readByte(Byte.SIZE + 1));
        assertThrows(IllegalArgumentException.class, () -> input.readUnsignedByte(0));
        assertThrows(IllegalArgumentException.class, () -> input.readUnsignedByte(Byte.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readShort(false, 0));
        assertThrows(IllegalArgumentException.class, () -> input.readShort(true, Short.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readShort(0));
        assertThrows(IllegalArgumentException.class, () -> input.readShort(Short.SIZE + 1));
        assertThrows(IllegalArgumentException.class, () -> input.readUnsignedShort(0));
        assertThrows(IllegalArgumentException.class, () -> input.readUnsignedShort(Short.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readInt(false, 0));
        assertThrows(IllegalArgumentException.class, () -> input.readInt(true, Integer.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readInt(0));
        assertThrows(IllegalArgumentException.class, () -> input.readInt(Integer.SIZE + 1));
        assertThrows(IllegalArgumentException.class, () -> input.readUnsignedInt(0));
        assertThrows(IllegalArgumentException.class, () -> input.readUnsignedInt(Integer.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readLong(false, 0));
        assertThrows(IllegalArgumentException.class, () -> input.readLong(true, Long.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readLong(0));
        assertThrows(IllegalArgumentException.class, () -> input.readLong(Long.SIZE + 1));
        assertThrows(IllegalArgumentException.class, () -> input.readUnsignedLong(0));
        assertThrows(IllegalArgumentException.class, () -> input.readUnsignedLong(Long.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readChar(0));
        assertThrows(IllegalArgumentException.class, () -> input.readChar(Character.SIZE + 1));
    }

    @Test
    @SuppressWarnings("deprecation")
    void bitOutputRejectsInvalidSizes() {
        final DefaultBitOutput output = output();
        assertThrows(IllegalArgumentException.class, () -> output.writeByte(false, 0, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeByte(true, Byte.SIZE, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeByte(0, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeByte(Byte.SIZE + 1, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeUnsignedByte(0, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeUnsignedByte(Byte.SIZE, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeShort(false, 0, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeShort(true, Short.SIZE, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeShort(0, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeShort(Short.SIZE + 1, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeUnsignedShort(0, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeUnsignedShort(Short.SIZE, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeInt(false, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeInt(true, Integer.SIZE, 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeInt(0, 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeInt(Integer.SIZE + 1, 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeUnsignedInt(0, 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeUnsignedInt(Integer.SIZE, 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeLong(false, 0, 0L));
        assertThrows(IllegalArgumentException.class, () -> output.writeLong(true, Long.SIZE, 0L));
        assertThrows(IllegalArgumentException.class, () -> output.writeLong(0, 0L));
        assertThrows(IllegalArgumentException.class, () -> output.writeLong(Long.SIZE + 1, 0L));
        assertThrows(IllegalArgumentException.class, () -> output.writeUnsignedLong(0, 0L));
        assertThrows(IllegalArgumentException.class, () -> output.writeUnsignedLong(Long.SIZE, 0L));
        assertThrows(IllegalArgumentException.class, () -> output.writeChar(0, (char) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeChar(Character.SIZE + 1, (char) 0));
    }

    @Test
    void bitInputRejectsInvalidSkipAndAlignArguments() {
        final BitInput input = input();
        assertThrows(IllegalArgumentException.class, () -> input.skip(0));
        assertThrows(IllegalArgumentException.class, () -> input.skip(-1));
        assertThrows(IllegalArgumentException.class, () -> input.align(0));
        assertThrows(IllegalArgumentException.class, () -> input.align(-1));
    }

    @Test
    void bitOutputRejectsInvalidSkipAndAlignArguments() {
        final DefaultBitOutput output = output();
        assertThrows(IllegalArgumentException.class, () -> output.skip(0));
        assertThrows(IllegalArgumentException.class, () -> output.skip(-1));
        assertThrows(IllegalArgumentException.class, () -> output.align(0));
        assertThrows(IllegalArgumentException.class, () -> output.align(-1));
    }

    @Test
    void bitOutputCountStartsAtZeroAndTracksFlushedOctets() throws IOException {
        final DefaultBitOutput output = output();

        assertEquals(0L, output.getCount());
        output.skip(3);
        assertEquals(0L, output.getCount());
        output.align(1);
        assertEquals(1L, output.getCount());
    }

    @Test
    void bitInputCountStartsAtZeroAndTracksReadOctets() throws IOException {
        final DefaultBitInput input = new DefaultBitInput(new ByteInput() {
            @Override
            public int read() {
                return 0x00;
            }
        });

        assertEquals(0L, input.getCount());
        input.readBoolean();
        assertEquals(1L, input.getCount());
        input.align(1);
        assertEquals(1L, input.getCount());
    }

    @Test
    void bitInputRejectsOutOfRangeDelegateValues() {
        assertThrows(IOException.class, () -> new DefaultBitInput(new ConstantByteInput(-2)).readBoolean());
        assertThrows(IOException.class, () -> new DefaultBitInput(new ConstantByteInput(256)).readBoolean());
    }

    @Test
    void skipCrossesIntegerSizedChunksForInputAndOutput() throws IOException {
        final DefaultBitInput input = new DefaultBitInput(new ByteInput() {
            @Override
            public int read() {
                return 0x00;
            }
        });
        final RecordingByteOutput target = new RecordingByteOutput();
        final DefaultBitOutput output = new DefaultBitOutput(target);

        input.skip(40);
        output.skip(40);

        assertEquals(5L, input.getCount());
        assertEquals(5L, output.getCount());
        assertArrayEquals(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00}, target.bytes());
    }

    private static BitInput input() {
        return new DefaultBitInput(new ByteInput() {
            @Override
            public int read() throws IOException {
                throw new EOFException("script exhausted");
            }
        });
    }

    private static DefaultBitOutput output() {
        return new DefaultBitOutput(new ByteOutput() {
            @Override
            public void write(final int value) {
                // discard
            }
        });
    }

    private static final class ConstantByteInput
            implements ByteInput {

        private ConstantByteInput(final int value) {
            this.value = value;
        }

        @Override
        public int read() {
            return value;
        }

        private final int value;
    }

    private static final class RecordingByteOutput
            implements ByteOutput {

        @Override
        public void write(final int value) {
            bytes[count++] = (byte) value;
        }

        private byte[] bytes() {
            final byte[] copy = new byte[count];
            System.arraycopy(bytes, 0, copy, 0, count);
            return copy;
        }

        private final byte[] bytes = new byte[8];

        private int count;
    }
}
