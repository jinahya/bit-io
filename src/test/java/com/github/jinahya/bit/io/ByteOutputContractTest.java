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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteOutputContractTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("byteOutputs")
    void writeAcceptsUnsignedByteValues(final String name, final ByteOutputFactory factory) throws IOException {
        final byte[] expected = {
                (byte) 0x00,
                (byte) 0x01,
                (byte) 0x7F,
                (byte) 0x80,
                (byte) 0xFF
        };
        final ByteOutputTarget target = factory.create(expected.length);
        for (final byte b : expected) {
            target.output.write(b & 0xFF);
        }
        assertArrayEquals(expected, target.toByteArray());
    }

    @ParameterizedTest(name = "{0}: {1}")
    @MethodSource("outOfRangeValues")
    void writeRejectsOutOfRangeValues(final String name, final int value) {
        final Stream<Arguments> factories = byteOutputs();
        factories.forEach(arguments -> {
            final ByteOutputFactory factory = (ByteOutputFactory) arguments.get()[1];
            try {
                final ByteOutputTarget target = factory.create(1);
                assertThrows(IllegalArgumentException.class, () -> target.output.write(value));
            } catch (final IOException ioe) {
                throw new AssertionError(ioe);
            }
        });
    }

    private static Stream<Arguments> byteOutputs() {
        return Stream.of(
                Arguments.of("ArrayByteOutput", new ByteOutputFactory() {
                    @Override
                    public ByteOutputTarget create(final int capacity) {
                        final byte[] array = new byte[capacity];
                        return new ByteOutputTarget(new ArrayByteOutput(array)) {
                            @Override
                            byte[] toByteArray() {
                                return array;
                            }
                        };
                    }
                }),
                Arguments.of("BufferByteOutput", new ByteOutputFactory() {
                    @Override
                    public ByteOutputTarget create(final int capacity) {
                        final ByteBuffer buffer = ByteBuffer.allocate(capacity);
                        return new ByteOutputTarget(new BufferByteOutput(buffer)) {
                            @Override
                            byte[] toByteArray() {
                                final ByteBuffer duplicate = buffer.duplicate();
                                duplicate.flip();
                                final byte[] bytes = new byte[duplicate.remaining()];
                                duplicate.get(bytes);
                                return bytes;
                            }
                        };
                    }
                }),
                Arguments.of("DataByteOutput", new ByteOutputFactory() {
                    @Override
                    public ByteOutputTarget create(final int capacity) {
                        final ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
                        return new ByteOutputTarget(new DataByteOutput(new DataOutputStream(baos))) {
                            @Override
                            byte[] toByteArray() {
                                return baos.toByteArray();
                            }
                        };
                    }
                }),
                Arguments.of("StreamByteOutput", new ByteOutputFactory() {
                    @Override
                    public ByteOutputTarget create(final int capacity) {
                        final ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
                        return new ByteOutputTarget(new StreamByteOutput(baos)) {
                            @Override
                            byte[] toByteArray() {
                                return baos.toByteArray();
                            }
                        };
                    }
                }),
                Arguments.of("BufferByteOutput.from(WritableByteChannel)", new ByteOutputFactory() {
                    @Override
                    public ByteOutputTarget create(final int capacity) {
                        final ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
                        return new ByteOutputTarget(BufferByteOutput.from(Channels.newChannel(baos))) {
                            @Override
                            byte[] toByteArray() {
                                return baos.toByteArray();
                            }
                        };
                    }
                })
        );
    }

    private static Stream<Arguments> outOfRangeValues() {
        return Stream.of(
                Arguments.of("negative one", -1),
                Arguments.of("greater than unsigned byte max", 256),
                Arguments.of("integer minimum", Integer.MIN_VALUE),
                Arguments.of("integer maximum", Integer.MAX_VALUE)
        );
    }

    private interface ByteOutputFactory {

        ByteOutputTarget create(int capacity) throws IOException;
    }

    private abstract static class ByteOutputTarget {

        ByteOutputTarget(final ByteOutput output) {
            this.output = output;
        }

        abstract byte[] toByteArray();

        final ByteOutput output;
    }
}
