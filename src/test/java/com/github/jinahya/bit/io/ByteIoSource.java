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

    /**
     * The output and the input are wired to the two ends of an NIO {@link Pipe}. The output is <em>write-through</em>
     * ({@link BufferByteOutput#from(java.nio.channels.WritableByteChannel)}), so every byte is drained to the sink as
     * it is written and nothing is ever stranded in an intermediate buffer — which keeps the single-threaded
     * write-then- read pattern (including multi-cycle tests) from blocking.
     */
    static Stream<Arguments> sourceByteIoChannel() throws IOException {
        final Pipe pipe = Pipe.open();
        return Stream.of(Arguments.of(BufferByteOutput.from(pipe.sink()), BufferByteInput.from(pipe.source())));
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
