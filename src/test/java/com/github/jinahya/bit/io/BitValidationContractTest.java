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

import static org.junit.jupiter.api.Assertions.assertThrows;

class BitValidationContractTest {

    @Test
    void bitInputRejectsInvalidSizes() {
        final BitInput input = input();
        assertThrows(IllegalArgumentException.class, () -> input.readByte(false, 0));
        assertThrows(IllegalArgumentException.class, () -> input.readByte(true, Byte.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readShort(false, 0));
        assertThrows(IllegalArgumentException.class, () -> input.readShort(true, Short.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readInt(false, 0));
        assertThrows(IllegalArgumentException.class, () -> input.readInt(true, Integer.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readLong(false, 0));
        assertThrows(IllegalArgumentException.class, () -> input.readLong(true, Long.SIZE));
        assertThrows(IllegalArgumentException.class, () -> input.readChar(0));
        assertThrows(IllegalArgumentException.class, () -> input.readChar(Character.SIZE + 1));
    }

    @Test
    void bitOutputRejectsInvalidSizes() {
        final BitOutput output = output();
        assertThrows(IllegalArgumentException.class, () -> output.writeByte(false, 0, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeByte(true, Byte.SIZE, (byte) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeShort(false, 0, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeShort(true, Short.SIZE, (short) 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeInt(false, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeInt(true, Integer.SIZE, 0));
        assertThrows(IllegalArgumentException.class, () -> output.writeLong(false, 0, 0L));
        assertThrows(IllegalArgumentException.class, () -> output.writeLong(true, Long.SIZE, 0L));
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
        final BitOutput output = output();
        assertThrows(IllegalArgumentException.class, () -> output.skip(0));
        assertThrows(IllegalArgumentException.class, () -> output.skip(-1));
        assertThrows(IllegalArgumentException.class, () -> output.align(0));
        assertThrows(IllegalArgumentException.class, () -> output.align(-1));
    }

    private static BitInput input() {
        return new DefaultBitInput(new ByteInput() {
            @Override
            public int read() throws IOException {
                throw new EOFException("script exhausted");
            }
        });
    }

    private static BitOutput output() {
        return new DefaultBitOutput(new ByteOutput() {
            @Override
            public void write(final int value) {
                // discard
            }
        });
    }
}
