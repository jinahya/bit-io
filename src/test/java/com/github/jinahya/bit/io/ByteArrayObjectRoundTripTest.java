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
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ByteArrayObjectRoundTripTest {

    @Test
    void signedArraysRoundTripThroughReadObjectAndWriteObject() throws IOException {
        roundTripViaObject(ByteArrayWriter.ofSigned(3, 1), ByteArrayReader.ofSigned(3, 1),
                           new byte[]{0, -1, 0});
        roundTripViaObject(ByteArrayWriter.ofSigned(3, 4), ByteArrayReader.ofSigned(3, 4),
                           new byte[]{0, 7, -8});
        roundTripViaObject(ByteArrayWriter.ofSigned(4, 8), ByteArrayReader.ofSigned(4, 8),
                           new byte[]{Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE});
    }

    @Test
    void unsignedArraysRoundTripThroughDirectReaderAndWriter() throws IOException {
        roundTripDirect(ByteArrayWriter.ofUnsigned(3, 1), ByteArrayReader.ofUnsigned(3, 1),
                        new byte[]{0, 1, 0, 1});
        roundTripDirect(ByteArrayWriter.ofUnsigned(3, 4), ByteArrayReader.ofUnsigned(3, 4),
                        new byte[]{0x0, 0x7, 0xF});
        roundTripDirect(ByteArrayWriter.ofUnsigned(3, 7), ByteArrayReader.ofUnsigned(3, 7),
                        new byte[]{0, 0x40, 0x7F});
    }

    @Test
    void emptySingleAndMaxLengthArraysRoundTrip() throws IOException {
        roundTripViaObject(ByteArrayWriter.ofSigned(3, 8), ByteArrayReader.ofSigned(3, 8), new byte[0]);
        roundTripViaObject(ByteArrayWriter.ofUnsigned(3, 4), ByteArrayReader.ofUnsigned(3, 4), new byte[]{0x0F});
        roundTripViaObject(ByteArrayWriter.ofUnsigned(3, 1), ByteArrayReader.ofUnsigned(3, 1),
                           new byte[]{1, 0, 1, 0, 1, 0, 1});
    }

    @Test
    void writerRejectsLengthOverflow() {
        final ByteArrayWriter writer = ByteArrayWriter.ofSigned(3, 8);
        final BitOutput output = output(new ByteArrayOutputStream());

        assertThrows(IllegalArgumentException.class, () -> writer.write(output, new byte[8]));
    }

    @Test
    void factoriesRejectInvalidSizes() {
        assertThrows(IllegalArgumentException.class, () -> ByteArrayWriter.ofSigned(0, 8));
        assertThrows(IllegalArgumentException.class, () -> ByteArrayReader.ofSigned(0, 8));
        assertThrows(IllegalArgumentException.class, () -> ByteArrayWriter.ofSigned(Integer.SIZE, 8));
        assertThrows(IllegalArgumentException.class, () -> ByteArrayReader.ofSigned(Integer.SIZE, 8));
        assertThrows(IllegalArgumentException.class, () -> ByteArrayWriter.ofSigned(3, Byte.SIZE + 1));
        assertThrows(IllegalArgumentException.class, () -> ByteArrayReader.ofSigned(3, Byte.SIZE + 1));
        assertThrows(IllegalArgumentException.class, () -> ByteArrayWriter.ofUnsigned(3, Byte.SIZE));
        assertThrows(IllegalArgumentException.class, () -> ByteArrayReader.ofUnsigned(3, Byte.SIZE));
    }

    @Test
    void unsignedNibblesHaveExpectedPackedLayout() throws IOException {
        final byte[] bytes = writeDirect(ByteArrayWriter.ofUnsigned(3, 4), new byte[]{0x0, 0x7, 0xF});

        assertArrayEquals(new byte[]{0x60, (byte) 0xFE}, bytes);
        assertArrayEquals(new byte[]{0x0, 0x7, 0xF},
                          readDirect(ByteArrayReader.ofUnsigned(3, 4), bytes));
    }

    @Test
    void readerAndWriterRejectNullArguments() {
        final ByteArrayReader reader = ByteArrayReader.ofSigned(3, 8);
        final ByteArrayWriter writer = ByteArrayWriter.ofSigned(3, 8);

        assertThrows(NullPointerException.class, () -> reader.read(null));
        assertThrows(NullPointerException.class, () -> writer.write(null, new byte[0]));
        assertThrows(NullPointerException.class, () -> writer.write(output(new ByteArrayOutputStream()), null));
    }

    private static void roundTripViaObject(final ByteArrayWriter writer, final ByteArrayReader reader,
                                           final byte[] value)
            throws IOException {
        final byte[] bytes = writeViaObject(writer, value);
        final byte[] actual = input(bytes).readObject(reader);

        assertArrayEquals(value, actual);
    }

    private static void roundTripDirect(final ByteArrayWriter writer, final ByteArrayReader reader, final byte[] value)
            throws IOException {
        assertArrayEquals(value, readDirect(reader, writeDirect(writer, value)));
    }

    private static byte[] writeViaObject(final ByteArrayWriter writer, final byte[] value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = output(bytes);
        output.writeObject(writer, value);
        output.align(1);
        return bytes.toByteArray();
    }

    private static byte[] writeDirect(final ByteArrayWriter writer, final byte[] value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = output(bytes);
        writer.write(output, value);
        output.align(1);
        return bytes.toByteArray();
    }

    private static byte[] readDirect(final ByteArrayReader reader, final byte[] bytes) throws IOException {
        return reader.read(input(bytes));
    }

    private static BitInput input(final byte[] bytes) {
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
    }

    private static BitOutput output(final ByteArrayOutputStream bytes) {
        return new DefaultBitOutput(new StreamByteOutput(bytes));
    }
}
