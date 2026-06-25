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
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteAdapterBoundaryTest {

    @Test
    void byteInputConstructorsRejectNullSource() {
        assertThrows(NullPointerException.class, () -> new ArrayByteInput(null));
        assertThrows(NullPointerException.class, () -> new BufferByteInput(null));
        assertThrows(NullPointerException.class, () -> new DataByteInput(null));
        assertThrows(NullPointerException.class, () -> new StreamByteInput(null));
        assertThrows(NullPointerException.class, () -> BufferByteInput.from(null));
    }

    @Test
    void byteOutputConstructorsRejectNullTarget() {
        assertThrows(NullPointerException.class, () -> new ArrayByteOutput(null));
        assertThrows(NullPointerException.class, () -> new BufferByteOutput(null));
        assertThrows(NullPointerException.class, () -> new DataByteOutput(null));
        assertThrows(NullPointerException.class, () -> new StreamByteOutput(null));
        assertThrows(NullPointerException.class, () -> BufferByteOutput.from(null));
    }

    @Test
    void bufferByteInputReadsFromCurrentPosition() throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[]{0x11, 0x22, 0x33});
        buffer.position(1);
        final ByteInput input = new BufferByteInput(buffer);
        assertEquals(0x22, input.read());
        assertEquals(2, buffer.position());
    }

    @Test
    void bufferByteOutputWritesAtCurrentPosition() throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.position(1);
        final ByteOutput output = new BufferByteOutput(buffer);
        output.write(0x7E);
        assertEquals(2, buffer.position());
        assertEquals((byte) 0x7E, buffer.get(1));
    }

    @Test
    void arrayByteInputThrowsEOFExceptionWhenEmpty() {
        final ByteInput input = new ArrayByteInput(new byte[0]);
        assertThrows(EOFException.class, input::read);
    }

    @Test
    void arrayByteOutputThrowsIOExceptionWhenFull() {
        final ByteOutput output = new ArrayByteOutput(new byte[0]);
        assertThrows(IOException.class, () -> output.write(0x00));
    }

    @Test
    void bufferByteOutputThrowsBufferOverflowExceptionWhenFull() {
        final ByteOutput output = new BufferByteOutput(ByteBuffer.allocate(0));
        assertThrows(BufferOverflowException.class, () -> output.write(0x00));
    }

    @Test
    void streamAndDataAdaptersDelegateBytes() throws IOException {
        final ByteArrayOutputStream streamBytes = new ByteArrayOutputStream();
        new StreamByteOutput(streamBytes).write(0xFF);
        assertEquals(0xFF, new StreamByteInput(new ByteArrayInputStream(streamBytes.toByteArray())).read());

        final ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
        new DataByteOutput(new DataOutputStream(dataBytes)).write(0x80);
        assertEquals(0x80, new DataByteInput(new DataInputStream(new ByteArrayInputStream(dataBytes.toByteArray())))
                .read());
    }

    @Test
    void channelFactoriesReadAndWriteBytes() throws IOException {
        final byte[] bytes = {(byte) 0x00, (byte) 0x7F, (byte) 0x80, (byte) 0xFF};
        final ByteInput input = BufferByteInput.from(Channels.newChannel(new ByteArrayInputStream(bytes)));
        assertEquals(0x00, input.read());
        assertEquals(0x7F, input.read());
        assertEquals(0x80, input.read());
        assertEquals(0xFF, input.read());

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteOutput output = BufferByteOutput.from(Channels.newChannel(baos));
        output.write(0x00);
        output.write(0x7F);
        output.write(0x80);
        output.write(0xFF);
        assertEquals(4, baos.toByteArray().length);
    }
}
