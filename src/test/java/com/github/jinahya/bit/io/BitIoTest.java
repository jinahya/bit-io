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
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.function.Consumer;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class BitIoTest {


    private static final Logger logger = getLogger(BitIoTest.class);


    public static void array(final Consumer<BitOutput> writer,
                             final Consumer<BitInput> reader)
        throws IOException {

        if (writer == null) {
            throw new NullPointerException("null writer");
        }

        if (reader == null) {
            throw new NullPointerException("null reader");
        }

        final byte[] array = new byte[1048576];

        final DefaultBitOutput<ArrayOutput> output
            = new DefaultBitOutput<>(new ArrayOutput(array, array.length, 0));
        writer.accept(output);
        final long padded = output.align(1);

        final BitInput input = new DefaultBitInput<>(
            new ArrayInput(array, output.getDelegate().getIndex(), 0));
        reader.accept(input);
        final long discarded = input.align(1);

        assertEquals(discarded, padded, "discarded != padded");
    }


    public static void buffer(final Consumer<BitOutput> writer,
                              final Consumer<BitInput> reader)
        throws IOException {

        if (writer == null) {
            throw new NullPointerException("null writer");
        }

        if (reader == null) {
            throw new NullPointerException("null reader");
        }

        final ByteBuffer buffer = ByteBuffer.allocate(1048576);

        final BitOutput output
            = new DefaultBitOutput<>(new BufferOutput(buffer));
        writer.accept(output);
        final long padded = output.align(1);

        buffer.flip();

        final BitInput input = new DefaultBitInput<>(new BufferInput(buffer));
        reader.accept(input);
        final long discarded = input.align(1);

        assertEquals(discarded, padded);
    }


    public static void file(final Consumer<BitOutput> writer,
                            final Consumer<BitInput> reader)
        throws IOException {

        if (writer == null) {
            throw new NullPointerException("null writer");
        }

        if (reader == null) {
            throw new NullPointerException("null reader");
        }

        final File file = Files.createTempFile(null, null).toFile();
        file.deleteOnExit();

        final RandomAccessFile target = new RandomAccessFile(file, "rwd");
        final BitOutput output = new DefaultBitOutput<>(new FileOutput(target));
        writer.accept(output);
        final long padded = output.align(1);
        target.close();

        final RandomAccessFile source = new RandomAccessFile(file, "r");
        final BitInput input = new DefaultBitInput<>(new FileInput(source));
        reader.accept(input);
        final long discarded = input.align(1);
        source.close();

        assertEquals(discarded, padded);
    }


    public static void stream(final Consumer<BitOutput> writer,
                              final Consumer<BitInput> reader)
        throws IOException {

        if (writer == null) {
            throw new NullPointerException("null writer");
        }

        if (reader == null) {
            throw new NullPointerException("null reader");
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream(1048576);
        final BitOutput output
            = new DefaultBitOutput<>(new StreamOutput(target));
        writer.accept(output);
        final long padded = output.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final BitInput input = new DefaultBitInput<>(new StreamInput(source));
        reader.accept(input);
        final long discarded = input.align(1);

        assertEquals(discarded, padded);
    }


    public static void all(final Consumer<BitOutput> writer,
                           final Consumer<BitInput> reader)
        throws IOException {

        if (writer == null) {
            throw new NullPointerException("null writer");
        }

        if (reader == null) {
            throw new NullPointerException("null reader");
        }

        array(writer, reader);
        buffer(writer, reader);
        file(writer, reader);
        stream(writer, reader);
    }


    private BitIoTest() {

        super();
    }

}

