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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.util.stream.Stream;

import static java.nio.ByteBuffer.allocate;

@Slf4j
final class ByteIoSource {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The number of bytes the backing array/buffer must hold; large enough for any single value a test round-trips (the
     * widest is a {@value java.lang.Long#BYTES}-byte {@code long}).
     */
    private static final int CAPACITY = 128;

    /**
     * The output and the input share a single backing array; the input reads back exactly what the output writes.
     */
    static Stream<Arguments> sourceByteIoArray() {
        final byte[] array = new byte[CAPACITY];
        return Stream.of(Arguments.of(new ArrayByteOutput(array), new ArrayByteInput(array)));
    }

    /**
     * The output and the input share a single backing buffer; the input reads through an independent {@code position}
     * via {@link ByteBuffer#duplicate()}.
     */
    static Stream<Arguments> sourceByteIoBuffer() {
        final ByteBuffer buffer = allocate(CAPACITY);
        return Stream.of(Arguments.of(new BufferByteOutput(buffer), new BufferByteInput(buffer.duplicate())));
    }

    /**
     * The output and the input are wired to two ends of a connected pipe.
     */
    static Stream<Arguments> sourceByteIoData() throws IOException {
        final PipedOutputStream pos = new PipedOutputStream();
        final PipedInputStream pis = new PipedInputStream(pos);
        return Stream.of(Arguments.of(new DataByteOutput(new DataOutputStream(pos)),
                                      new DataByteInput(new DataInputStream(pis))));
    }

    /**
     * The output and the input are wired to two ends of a connected pipe.
     */
    static Stream<Arguments> sourceByteIoStream() throws IOException {
        final PipedOutputStream pos = new PipedOutputStream();
        final PipedInputStream pis = new PipedInputStream(pos);
        return Stream.of(Arguments.of(new StreamByteOutput(pos), new StreamByteInput(pis)));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> sourceByteIoChannel() throws IOException {
        final Pipe pipe = Pipe.open();
        final ByteBuffer obuffer = allocate(1);
        final ChannelByteOutput output = new ChannelByteOutput(pipe.sink(), obuffer);
        final ByteInput input = new ChannelByteInput(pipe.source(), allocate(1)) {
            private boolean flushed;

            @Override
            public int read() throws IOException {
                if (!flushed) { // drain the output's 1-byte buffer to the channel before reading it back
                    for (((Buffer) obuffer).flip(); obuffer.hasRemaining(); ) {
                        pipe.sink().write(obuffer);
                    }
                    flushed = true;
                }
                return super.read();
            }
        };
        return Stream.of(Arguments.of(output, input));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> sourceByteIo() throws IOException {
        return Stream.of(sourceByteIoArray(), sourceByteIoBuffer(), sourceByteIoData(), sourceByteIoStream(),
                         sourceByteIoChannel())
                .flatMap(s -> s);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private ByteIoSource() {
        super();
    }
}
