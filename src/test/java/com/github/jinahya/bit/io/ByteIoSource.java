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
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.stream.Stream;

import static java.lang.System.arraycopy;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import static java.util.Arrays.copyOf;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.ThreadLocalRandom.current;

@Slf4j
final class ByteIoSource {

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> sourceByteIoArray() {
        final byte[][] holder = new byte[1][];
        final ByteOutput output = new ArrayByteOutput(holder[0] = new byte[1]) {
            @Override
            public void write(int value) throws IOException {
                final byte[] target = getTarget();
                if (getIndex() == target.length) {
                    setTarget(copyOf(target, target.length << 1));
                    holder[0] = getTarget();
                }
                super.write(value);
            }
        };
        final ByteInput input = new ArrayByteInput(new byte[0]) {
            private boolean sourced;
            @Override
            public int read() throws IOException {
                if (!sourced) {
                    setSource(ofNullable(holder[0]).orElseGet(() -> new byte[0]));
                    setIndex(0);
                    sourced = true;
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoBuffer() {
        final ByteBuffer[] holder = new ByteBuffer[1];
        final ByteOutput output = new BufferByteOutput(holder[0] = allocate(1)) {
            @Override
            public void write(int value) throws IOException {
                final ByteBuffer target = getTarget();
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
                    setTarget(bigger);
                    holder[0] = getTarget();
                }
                super.write(value);
            }
        };
        final ByteInput input = new BufferByteInput(allocate(0)) {
            private boolean sourced;
            @Override
            public int read() throws IOException {
                if (!sourced) {
                    setSource(ofNullable(holder[0]).orElseGet(() -> allocate(0)));
                    ((Buffer) getSource()).flip(); // limit -> position, position -> zero
                    sourced = true;
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoData() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteOutput output = new DataByteOutput(new DataOutputStream(baos));
        final ByteInput input = new DataByteInput(new DataInputStream(new ByteArrayInputStream(new byte[0]))) {
            private boolean sourced;
            @Override
            public int read() throws IOException {
                if (!sourced) {
                    setSource(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
                    sourced = true;
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoStream() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteOutput output = new StreamByteOutput(baos);
        final ByteInput input = new StreamByteInput(new ByteArrayInputStream(new byte[0])) {
            private boolean sourced;
            @Override
            public int read() throws IOException {
                if (!sourced) {
                    setSource(new ByteArrayInputStream(baos.toByteArray()));
                    sourced = true;
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> sourceByteIoChannel() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ChannelByteOutput2 output = new ChannelByteOutput2(null, null) {
            @Override
            public void write(final int value) throws IOException {
                if (getTarget() == null) {
                    setTarget(newChannel(baos));
                }
                if (getBuffer() == null) {
                    setBuffer(allocate(current().nextInt(1, 8)));
                }
                super.write(value);
            }
        };
        final ByteInput input = new ChannelByteInput2(null, null) {
            @Override
            public int read() throws IOException {
                if (getSource() == null) {
                    {
                        final WritableByteChannel target = output.getTarget();
                        final ByteBuffer buffer = output.getBuffer();
                        for (((Buffer) buffer).flip(); buffer.hasRemaining(); ) {
                            target.write(buffer);
                        }
                    }
                    setSource(newChannel(new ByteArrayInputStream(baos.toByteArray())));
                }
                if (getBuffer() == null) {
                    final int capacity = current().nextInt(1, 8);
                    setBuffer((ByteBuffer) allocate(capacity).position(capacity));
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> sourceByteIo() {
        return Stream.of(sourceByteIoArray(), sourceByteIoBuffer(), sourceByteIoData(), sourceByteIoStream(),
                         sourceByteIoChannel())
                .flatMap(s -> s);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteIoSource() {
        super();
    }
}
