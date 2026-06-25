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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteInputContractTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("byteInputs")
    void readReturnsUnsignedByteValues(final String name, final ByteInputFactory factory) throws IOException {
        final byte[] bytes = {
                (byte) 0x00,
                (byte) 0x01,
                (byte) 0x7F,
                (byte) 0x80,
                (byte) 0xFF
        };
        final ByteInput input = factory.create(bytes);
        for (final byte b : bytes) {
            final int actual = input.read();
            assertEquals(b & 0xFF, actual);
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("eofSignalingByteInputs")
    void readThrowsEOFExceptionWhenEmpty(final String name, final ByteInputFactory factory) throws IOException {
        final ByteInput input = factory.create(new byte[0]);
        assertThrows(EOFException.class, input::read);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("eofSignalingByteInputs")
    void readThrowsEOFExceptionAfterLastByte(final String name, final ByteInputFactory factory) throws IOException {
        final ByteInput input = factory.create(new byte[]{(byte) 0x5A});
        assertEquals(0x5A, input.read());
        assertThrows(EOFException.class, input::read);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("callerManagedWindowByteInputs")
    void readThrowsBufferUnderflowExceptionForCallerManagedWindowWhenEmpty(
            final String name, final ByteInputFactory factory)
            throws IOException {
        final ByteInput input = factory.create(new byte[0]);
        assertThrows(BufferUnderflowException.class, input::read);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("callerManagedWindowByteInputs")
    void readThrowsBufferUnderflowExceptionForCallerManagedWindowAfterLastByte(
            final String name, final ByteInputFactory factory)
            throws IOException {
        final ByteInput input = factory.create(new byte[]{(byte) 0x5A});
        assertEquals(0x5A, input.read());
        assertThrows(BufferUnderflowException.class, input::read);
    }

    private static Stream<Arguments> byteInputs() {
        return Stream.of(
                Arguments.of("ArrayByteInput", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return new ArrayByteInput(bytes);
                    }
                }),
                Arguments.of("BufferByteInput", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return new BufferByteInput(ByteBuffer.wrap(bytes));
                    }
                }),
                Arguments.of("DataByteInput", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return new DataByteInput(new DataInputStream(new ByteArrayInputStream(bytes)));
                    }
                }),
                Arguments.of("StreamByteInput", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return new StreamByteInput(new ByteArrayInputStream(bytes));
                    }
                }),
                Arguments.of("BufferByteInput.from(ReadableByteChannel)", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return BufferByteInput.from(Channels.newChannel(new ByteArrayInputStream(bytes)));
                    }
                })
        );
    }

    private static Stream<Arguments> eofSignalingByteInputs() {
        return Stream.of(
                Arguments.of("ArrayByteInput", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return new ArrayByteInput(bytes);
                    }
                }),
                Arguments.of("DataByteInput", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return new DataByteInput(new DataInputStream(new ByteArrayInputStream(bytes)));
                    }
                }),
                Arguments.of("StreamByteInput", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return new StreamByteInput(new ByteArrayInputStream(bytes));
                    }
                }),
                Arguments.of("BufferByteInput.from(ReadableByteChannel)", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return BufferByteInput.from(Channels.newChannel(new ByteArrayInputStream(bytes)));
                    }
                })
        );
    }

    private static Stream<Arguments> callerManagedWindowByteInputs() {
        return Stream.of(
                Arguments.of("BufferByteInput", new ByteInputFactory() {
                    @Override
                    public ByteInput create(final byte[] bytes) {
                        return new BufferByteInput(ByteBuffer.wrap(bytes));
                    }
                })
        );
    }

    private interface ByteInputFactory {

        ByteInput create(byte[] bytes) throws IOException;
    }
}
