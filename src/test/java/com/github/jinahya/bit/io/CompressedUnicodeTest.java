package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
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
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompressedUnicodeTest {

    @Test
    void roundTripsStrings() throws IOException {
        roundTrip("");
        roundTrip("ABC");
        roundTrip("\u00a2");
        roundTrip("\u20ac");
        roundTrip(new String(Character.toChars(0x1F600)));
        roundTrip("A\u00a2\u20ac" + new String(Character.toChars(0x1F600)));
    }

    @Test
    void writesExpectedBlockLayouts() throws IOException {
        final CompressedUnicodeWriter codec = new CompressedUnicodeWriter(3);

        assertArrayEquals(fromHex("2410"), write(codec, "A"));
        assertArrayEquals(fromHex("28a2"), write(codec, "\u00a2"));
        assertArrayEquals(fromHex("310560"), write(codec, "\u20ac"));
        assertArrayEquals(fromHex("387d8000"), write(codec, new String(Character.toChars(0x1F600))));
    }

    @Test
    void rejectsNullArguments() {
        final CompressedUnicodeReader reader = new CompressedUnicodeReader(3);
        final CompressedUnicodeWriter writer = new CompressedUnicodeWriter(3);

        assertThrows(NullPointerException.class, () -> reader.read(null));
        assertThrows(NullPointerException.class, () -> writer.write(null, ""));
        assertThrows(NullPointerException.class, () -> writer.write(output(new ByteArrayOutputStream()), null));
    }

    @Test
    void rejectsInvalidSizesAndValues() {
        assertThrows(IllegalArgumentException.class, () -> new CompressedUnicodeReader(0));
        assertThrows(IllegalArgumentException.class, () -> new CompressedUnicodeWriter(Integer.SIZE));

        final CompressedUnicodeWriter writer = new CompressedUnicodeWriter(1);
        assertThrows(IllegalArgumentException.class, () -> write(writer, "ab"));
        assertThrows(IllegalArgumentException.class, () -> write(writer, "\uD800"));
    }

    @Test
    void rejectsNonMinimalAndInvalidDecodedBlocks() {
        final CompressedUnicodeReader reader = new CompressedUnicodeReader(3);

        assertThrows(IOException.class, () -> reader.read(input(fromHex("2820"))));   // type 1 for 'A'
        assertThrows(IOException.class, () -> reader.read(input(fromHex("300a20")))); // type 2 for U+00A2
        assertThrows(IOException.class, () -> reader.read(input(fromHex("36c000")))); // surrogate
    }

    @Test
    void factoriesRoundTripEveryBlockSize() throws IOException {
        final String value = "Aé€😀"; // 1, 2, 3, and 4 UTF-8 bytes
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = output(bytes);
        output.writeObject(StringWriter.ofCompressedUnicode(3), value);
        // 3 (length) + 9 + 13 + 18 + 23 = 66 bits; padded with 6 bits to 9 bytes
        assertEquals(6L, output.align(1));
        assertEquals(9, bytes.size());
        assertEquals(value, input(bytes.toByteArray()).readObject(StringReader.ofCompressedUnicode(3)));
    }

    @Test
    void factoriesWriteTaggedBlocksUnlikeAscii() throws IOException {
        // "AB" with an 8-bit length: 00000010 | 00 1000001 | 00 1000010 (26 bits)
        assertArrayEquals(new byte[]{0x02, 0x20, (byte) 0x90, (byte) 0x80},
                          write(StringWriter.ofCompressedUnicode(8), "AB"));
        // ofAscii: 00000010 | 1000001 | 1000010 (22 bits)
        assertArrayEquals(new byte[]{0x02, (byte) 0x83, 0x08}, write(StringWriter.ofAscii(8), "AB"));
    }

    private static byte[] write(final BitWriter<String> writer, final String value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = output(bytes);
        writer.write(output, value);
        output.align(1);
        return bytes.toByteArray();
    }

    private static byte[] fromHex(final String hex) {
        return HexFormat.of().parseHex(hex);
    }

    private static void roundTrip(final String value) throws IOException {
        assertEquals(value, new CompressedUnicodeReader(8).read(input(write(new CompressedUnicodeWriter(8), value))));
    }


    private static BitInput input(final byte[] bytes) {
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
    }

    private static BitOutput output(final ByteArrayOutputStream bytes) {
        return new DefaultBitOutput(new StreamByteOutput(bytes));
    }
}
