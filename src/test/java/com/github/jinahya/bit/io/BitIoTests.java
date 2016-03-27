/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */
package com.github.jinahya.bit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class BitIoTests {

    private static final Logger logger = getLogger(BitIoTests.class);

    public static void array(final Consumer<? super BitOutput> writer,
                             final Consumer<? super BitInput> reader)
            throws IOException {
        final byte[] array = new byte[1048576];
        final ArrayByteOutput target
                = new ArrayByteOutput(array, 0, array.length);
        final BitOutput output = new DefaultBitOutput<>(target);
        writer.accept(output);
        final long padded = output.align(1);
        final ByteInput source = new ArrayByteInput(array, 0, array.length);
        final BitInput input = new DefaultBitInput<>(source);
        reader.accept(input);
        final long discarded = input.align(1);
        assertEquals(discarded, padded, "discarded != padded");
    }

    public static void buffer(final Consumer<? super BitOutput> writer,
                              final Consumer<? super BitInput> reader)
            throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(1048576);
        final BitOutput output
                = new DefaultBitOutput<>(new BufferByteOutput(buffer));
        writer.accept(output);
        final long padded = output.align(1);
        buffer.flip();
        final BitInput input
                = new DefaultBitInput<>(new BufferByteInput(buffer));
        reader.accept(input);
        final long discarded = input.align(1);
        assertEquals(discarded, padded);
    }

    public static void data(final Consumer<? super BitOutput> writer,
                            final Consumer<? super BitInput> reader)
            throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(1048576);
        final DataOutputStream target = new DataOutputStream(baos);
        final BitOutput output
                = new DefaultBitOutput<>(new DataByteOutput(target));
        writer.accept(output);
        final long padded = output.align(1);
        target.flush();
        final ByteArrayInputStream bais
                = new ByteArrayInputStream(baos.toByteArray());
        final DataInputStream dis = new DataInputStream(bais);
        final BitInput input = new DefaultBitInput<>(new DataByteInput(dis));
        reader.accept(input);
        final long discarded = input.align(1);
        assertEquals(discarded, padded);
    }

    public static void stream(final Consumer<? super BitOutput> writer,
                              final Consumer<? super BitInput> reader)
            throws IOException {
        final ByteArrayOutputStream target = new ByteArrayOutputStream(1048576);
        final BitOutput output
                = new DefaultBitOutput<>(new StreamByteOutput(target));
        writer.accept(output);
        final long padded = output.align(1);
        target.flush();
        final ByteArrayInputStream source
                = new ByteArrayInputStream(target.toByteArray());
        final BitInput input
                = new DefaultBitInput<>(new StreamByteInput(source));
        reader.accept(input);
        final long discarded = input.align(1);
        assertEquals(discarded, padded);
    }

    public static void all(final Consumer<? super BitOutput> writer,
                           final Consumer<? super BitInput> reader)
            throws IOException {
        array(writer, reader);
        buffer(writer, reader);
        data(writer, reader);
        stream(writer, reader);
    }

//    public static <T extends BitReadable & BitWritable> void all(
//            final boolean nullable, final Class<? extends T> type, final T expected)
//            throws IOException {
//
//        all(
//                o -> {
//                    try {
//                        o.writeObject(nullable, expected);
//                    } catch (final IOException ioe) {
//                        throw new UncheckedIOException(ioe);
//                    }
//                },
//                i -> {
//                    try {
//                        final T actual = i.readObject(nullable, type);
//                        assertEquals(actual, expected);
//                    } catch (final IOException ioe) {
//                        throw new UncheckedIOException(ioe);
//                    }
//                });
//    }
    private BitIoTests() {
        super();
    }
}
