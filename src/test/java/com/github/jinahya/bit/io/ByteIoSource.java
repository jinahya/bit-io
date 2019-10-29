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
import java.nio.channels.WritableByteChannel;
import java.util.stream.Stream;

import static java.lang.System.arraycopy;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import static java.util.Arrays.copyOf;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
final class ByteIoSource {

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> sourceByteIoArray() {
        final byte[][] holder = new byte[1][];
        final ByteOutput output = new ArrayByteOutput(null) {
            @Override
            public void write(int value) throws IOException {
                if (getTarget() == null) {
                    setTarget(holder[0] = new byte[1]);
                    setIndex(0);
                }
                final byte[] target = getTarget();
                if (getIndex() == target.length) {
                    setTarget(copyOf(target, target.length << 1));
                    holder[0] = getTarget();
                }
                super.write(value);
            }
        };
        final ByteInput input = new ArrayByteInput(null) {
            @Override
            public int read() throws IOException {
                if (getSource() == null) {
                    setSource(ofNullable(holder[0]).orElseGet(() -> new byte[0]));
                    setIndex(0);
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoBuffer() {
        final ByteBuffer[] holder = new ByteBuffer[1];
        final ByteOutput output = new BufferByteOutput(null) {
            @Override
            public void write(int value) throws IOException {
                if (getTarget() == null) {
                    setTarget(holder[0] = allocate(1));
                }
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
        final ByteInput input = new BufferByteInput(null) {
            @Override
            public int read() throws IOException {
                if (getSource() == null) {
                    setSource(ofNullable(holder[0]).orElseGet(() -> allocate(0)));
                    getSource().flip(); // limit -> position, position -> zero
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoData() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteOutput output = new DataByteOutput(null) {
            @Override
            public void write(final int value) throws IOException {
                if (getTarget() == null) {
                    setTarget(new DataOutputStream(baos));
                }
                super.write(value);
            }
        };
        final ByteInput input = new DataByteInput(null) {
            @Override
            public int read() throws IOException {
                if (getSource() == null) {
                    setSource(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoStream() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteOutput output = new StreamByteOutput(null) {
            @Override
            public void write(final int value) throws IOException {
                if (getTarget() == null) {
                    setTarget(baos);
                }
                super.write(value);
            }
        };
        final ByteInput input = new StreamByteInput(null) {
            @Override
            public int read() throws IOException {
                if (getSource() == null) {
                    setSource(new ByteArrayInputStream(baos.toByteArray()));
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    static Stream<Arguments> sourceByteIoChannel() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ChannelByteOutput output = new ChannelByteOutput(null, null) {
            @Override
            public void write(final int value) throws IOException {
                if (getChannel() == null) {
                    setChannel(newChannel(baos));
                }
                if (getTarget() == null) {
                    setTarget(allocate(current().nextInt(1, 8)));
                }
                super.write(value);
            }
        };
        final ByteInput input = new ChannelByteInput(null, null) {
            @Override
            public int read() throws IOException {
                if (getChannel() == null) {
                    {
                        final WritableByteChannel channel = output.getChannel();
                        final ByteBuffer target = output.getTarget();
                        assertNotNull(channel);
                        assertNotNull(target);
                        for (target.flip(); target.hasRemaining(); ) {
                            channel.write(target);
                        }
                    }
                    setChannel(newChannel(new ByteArrayInputStream(baos.toByteArray())));
                }
                if (getSource() == null) {
                    final int capacity = current().nextInt(1, 8);
                    setSource((ByteBuffer) allocate(capacity).position(capacity));
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

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
