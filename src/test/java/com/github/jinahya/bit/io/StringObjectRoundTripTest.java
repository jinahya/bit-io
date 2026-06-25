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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringObjectRoundTripTest {

    @Test
    void stringsRoundTripAcrossCharsetsThroughReadObjectAndWriteObject() throws IOException {
        roundTripViaObject(8, "US-ASCII", "");
        roundTripViaObject(8, "US-ASCII", "plain ASCII 0123");
        roundTripViaObject(8, "UTF-8", "plain ASCII 0123");
        roundTripViaObject(8, "UTF-8", "cafe \u00e9 \uc138\uacc4");
        roundTripViaObject(8, "UTF-16BE", "");
        roundTripViaObject(8, "UTF-16BE", "\ube44\ud2b8 IO");
    }

    @Test
    void stringsRoundTripThroughDirectReaderAndWriter() throws IOException {
        final String expected = "\u03c0 \ud55c\uae00";
        final byte[] bytes = writeDirect(new StringWriter(8, "UTF-8"), expected);

        assertEquals(expected, new StringReader(8, "UTF-8").read(input(bytes)));
    }

    @Test
    void stringReaderAndWriterRejectNullArguments() {
        assertThrows(NullPointerException.class, () -> new StringReader(8, null));
        assertThrows(NullPointerException.class, () -> new StringWriter(8, null));

        final StringReader reader = new StringReader(8, "UTF-8");
        final StringWriter writer = new StringWriter(8, "UTF-8");

        assertThrows(NullPointerException.class, () -> reader.read(null));
        assertThrows(NullPointerException.class, () -> writer.write(null, "value"));
        assertThrows(NullPointerException.class, () -> writer.write(output(new ByteArrayOutputStream()), null));
    }

    @Test
    void asciiFactoriesRoundTripText() throws IOException {
        final String expected = "ASCII 0123 ~";
        final byte[] bytes = writeViaObject(StringWriter.ofAscii(8), expected);

        assertEquals(expected, input(bytes).readObject(StringReader.ofAscii(8)));
    }

    @Test
    void asciiFactoryUsesSevenBitPackedElements() throws IOException {
        final String value = "ABC";
        final byte[] asciiPacked = writeDirect(StringWriter.ofAscii(5), value);
        final byte[] fullWidth = writeDirect(new StringWriter(5, "US-ASCII"), value);

        assertArrayEquals(new byte[]{0x1C, 0x18, 0x50, (byte) 0xC0}, asciiPacked);
        assertNotEquals(toHex(fullWidth), toHex(asciiPacked));
        assertEquals(value, StringReader.ofAscii(5).read(input(asciiPacked)));
    }

    @Test
    void asciiReadersAndWritersRejectNullArguments() {
        final BitReader<String> reader = StringReader.ofAscii(8);
        final BitWriter<String> writer = StringWriter.ofAscii(8);

        assertThrows(NullPointerException.class, () -> reader.read(null));
        assertThrows(NullPointerException.class, () -> writer.write(null, "value"));
        assertThrows(NullPointerException.class, () -> writer.write(output(new ByteArrayOutputStream()), null));
    }

    private static void roundTripViaObject(final int lengthSize, final String charsetName, final String value)
            throws IOException {
        final byte[] bytes = writeViaObject(new StringWriter(lengthSize, charsetName), value);

        assertEquals(value, input(bytes).readObject(new StringReader(lengthSize, charsetName)));
    }

    private static byte[] writeViaObject(final BitWriter<String> writer, final String value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = output(bytes);
        output.writeObject(writer, value);
        output.align(1);
        return bytes.toByteArray();
    }

    private static byte[] writeDirect(final BitWriter<String> writer, final String value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = output(bytes);
        writer.write(output, value);
        output.align(1);
        return bytes.toByteArray();
    }

    private static BitInput input(final byte[] bytes) {
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
    }

    private static BitOutput output(final ByteArrayOutputStream bytes) {
        return new DefaultBitOutput(new StreamByteOutput(bytes));
    }

    private static String toHex(final byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        for (final byte b : bytes) {
            builder.append(Integer.toHexString((b & 0xF0) >>> 4));
            builder.append(Integer.toHexString(b & 0x0F));
        }
        return builder.toString();
    }
}
