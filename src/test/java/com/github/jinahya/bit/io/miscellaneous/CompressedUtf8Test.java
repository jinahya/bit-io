package com.github.jinahya.bit.io.miscellaneous;

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

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteInput;
import com.github.jinahya.bit.io.StreamByteOutput;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompressedUtf8Test {

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
        final CompressedUtf8 codec = new CompressedUtf8(3);

        assertArrayEquals(_Utils.fromHex("2410"), write(codec, "A"));
        assertArrayEquals(_Utils.fromHex("28a2"), write(codec, "\u00a2"));
        assertArrayEquals(_Utils.fromHex("310560"), write(codec, "\u20ac"));
        assertArrayEquals(_Utils.fromHex("387d8000"), write(codec, new String(Character.toChars(0x1F600))));
    }

    @Test
    void rejectsNullArguments() {
        final CompressedUtf8 codec = new CompressedUtf8(3);

        assertThrows(NullPointerException.class, () -> codec.read(null));
        assertThrows(NullPointerException.class, () -> codec.write(null, ""));
        assertThrows(NullPointerException.class, () -> codec.write(output(new ByteArrayOutputStream()), null));
    }

    @Test
    void rejectsInvalidSizesAndValues() {
        assertThrows(IllegalArgumentException.class, () -> new CompressedUtf8(0));
        assertThrows(IllegalArgumentException.class, () -> new CompressedUtf8(Integer.SIZE));

        final CompressedUtf8 codec = new CompressedUtf8(1);
        assertThrows(IllegalArgumentException.class, () -> write(codec, "ab"));
        assertThrows(IllegalArgumentException.class, () -> write(codec, "\uD800"));
    }

    @Test
    void rejectsNonMinimalAndInvalidDecodedBlocks() {
        final CompressedUtf8 codec = new CompressedUtf8(3);

        assertThrows(IOException.class, () -> codec.read(input(_Utils.fromHex("2820"))));   // type 1 for 'A'
        assertThrows(IOException.class, () -> codec.read(input(_Utils.fromHex("300a20")))); // type 2 for U+00A2
        assertThrows(IOException.class, () -> codec.read(input(_Utils.fromHex("36c000")))); // surrogate
    }

    private static void roundTrip(final String value) throws IOException {
        final CompressedUtf8 codec = new CompressedUtf8(8);

        assertEquals(value, codec.read(input(write(codec, value))));
    }

    private static byte[] write(final CompressedUtf8 codec, final String value) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final BitOutput output = output(bytes);
        codec.write(output, value);
        output.align(1);
        return bytes.toByteArray();
    }

    private static BitInput input(final byte[] bytes) {
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
    }

    private static BitOutput output(final ByteArrayOutputStream bytes) {
        return new DefaultBitOutput(new StreamByteOutput(bytes));
    }
}
