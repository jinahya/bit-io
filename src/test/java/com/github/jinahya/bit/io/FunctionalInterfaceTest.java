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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that the single-abstract-method interfaces work as Java 8 lambdas and method references.
 */
class FunctionalInterfaceTest {

    @Test
    void bitReaderAndWriterAsLambdas() throws IOException {
        final BitWriter<Integer> writer = (output, value) -> output.writeUnsignedInt(3, value);
        final BitReader<Integer> reader = input -> input.readUnsignedInt(3);
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = BitOutputs.from(bytes);
        output.writeObject(writer, 5);
        output.align(1);
        assertEquals(Integer.valueOf(5), BitInputs.from(bytes.toByteArray()).readObject(reader));
    }

    @Test
    void primitiveReadersAndWritersAsLambdas() throws IOException {
        final BooleanBitWriter booleanWriter = (output, value) -> output.writeBoolean(!value);
        final BooleanBitReader booleanReader = input -> !input.readBoolean();
        final IntBitWriter intWriter = (output, value) -> output.writeInt(5, value);
        final IntBitReader intReader = input -> input.readInt(5);
        final LongBitWriter longWriter = (output, value) -> output.writeLong(40, value);
        final LongBitReader longReader = input -> input.readLong(40);
        final FloatBitWriter floatWriter = BitOutput::writeFloat32;
        final FloatBitReader floatReader = BitInput::readFloat32;
        final DoubleBitWriter doubleWriter = BitOutput::writeDouble64;
        final DoubleBitReader doubleReader = BitInput::readDouble64;
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = BitOutputs.from(bytes);
        output.writeBoolean(booleanWriter, true);
        output.writeInt(intWriter, -7);
        output.writeLong(longWriter, -1234567890L);
        output.writeFloat(floatWriter, 1.5f);
        output.writeDouble(doubleWriter, -2.5d);
        output.align(1);
        final BitInput input = BitInputs.from(bytes.toByteArray());
        assertTrue(input.readBoolean(booleanReader));
        assertEquals(-7, input.readInt(intReader));
        assertEquals(-1234567890L, input.readLong(longReader));
        assertEquals(1.5f, input.readFloat(floatReader));
        assertEquals(-2.5d, input.readDouble(doubleReader));
    }

    @Test
    void byteInputAndOutputAsMethodReferences() throws IOException {
        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(target::write);
        output.writeUnsignedInt(12, 0xABC);
        output.align(1);
        assertArrayEquals(new byte[]{(byte) 0xAB, (byte) 0xC0}, target.toByteArray());
        final ByteInput source = new ByteArrayInputStream(target.toByteArray())::read;
        assertEquals(0xABC, new DefaultBitInput(source).readUnsignedInt(12));
    }
}
