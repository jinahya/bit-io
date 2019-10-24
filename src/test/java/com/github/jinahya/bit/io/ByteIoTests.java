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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.provider.Arguments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static java.lang.System.arraycopy;
import static java.nio.ByteBuffer.allocate;
import static java.util.Arrays.copyOf;

@Slf4j
final class ByteIoTests {

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> sourceByteIoArray() {
        final byte[][] holder = new byte[][] {
                new byte[1]
        };
        final ArrayByteOutput output = new ArrayByteOutput(holder[0]) {
            @Override
            public void write(int value) throws IOException {
                if (index == target.length) {
                    target = copyOf(target, target.length << 1);
                    holder[0] = target;
                }
                super.write(value);
            }
        };
        final ArrayByteInput input = new ArrayByteInput(null) {
            @Override
            public int read() throws IOException {
                if (source == null) {
                    source = holder[0];
                    index = 0;
                    output.target = null;
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoBuffer() {
        final ByteBuffer[] holder = new ByteBuffer[] {allocate(1)};
        final BufferByteOutput output = new BufferByteOutput(null) {
            @Override
            public void write(int value) throws IOException {
                if (target == null) {
                    target = holder[0];
                }
                if (!target.hasRemaining()) {
                    final ByteBuffer bigger = allocate(target.capacity() << 1);
                    if (target.hasArray() && bigger.hasArray()) {
                        arraycopy(target.array(), target.arrayOffset(), bigger.array(), target.arrayOffset(),
                                  target.position());
                        bigger.position(target.position());
                    } else {
                        for (target.flip(); target.hasRemaining(); ) {
                            bigger.put(target.get());
                        }
                    }
                    target = bigger;
                    holder[0] = bigger;
                }
                super.write(value);
            }
        };
        final BufferByteInput input = new BufferByteInput(null) {
            @Override
            public int read() throws IOException {
                if (source == null) {
                    source = holder[0];
                    source.flip();
                    output.target = null;
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoData() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataByteOutput output = new DataByteOutput(null) {
            @Override
            public void write(final int value) throws IOException {
                if (target == null) {
                    target = new DataOutputStream(baos);
                }
                super.write(value);
            }
        };
        final DataByteInput input = new DataByteInput(null) {
            @Override
            public int read() throws IOException {
                if (source == null) {
                    source = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
                    output.target = null;
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoStream() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final StreamByteOutput output = new StreamByteOutput(null) {
            @Override
            public void write(final int value) throws IOException {
                if (target == null) {
                    target = baos;
                }
                super.write(value);
            }
        };
        final StreamByteInput input = new StreamByteInput(null) {
            @Override
            public int read() throws IOException {
                if (source == null) {
                    source = new ByteArrayInputStream(baos.toByteArray());
                    output.target = null;
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIo() {
        return Stream.of(sourceByteIoArray(), sourceByteIoBuffer(), sourceByteIoData(), sourceByteIoStream())
                .flatMap(s -> s);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteIoTests() {
        super();
    }
}
