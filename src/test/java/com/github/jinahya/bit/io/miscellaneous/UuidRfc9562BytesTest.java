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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class UuidRfc9562BytesTest {

    @Test
    void exposesLength() {
        assertEquals(16, UuidRfc9562Bytes.LENGTH);
    }

    @Test
    void readsExactlySixteenOctets() throws IOException {
        final byte[] bytes = sample();
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));

        final byte[] value = UuidRfc9562Bytes.INSTANCE.read(input);

        assertArrayEquals(bytes, value);
        assertNotSame(bytes, value);
    }

    @Test
    void writesExactlySixteenOctets() throws IOException {
        final byte[] value = sample();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));

        UuidRfc9562Bytes.INSTANCE.write(output, value);
        output.align(1);

        assertArrayEquals(value, baos.toByteArray());
    }

    @Test
    void roundTripsKnownRfc9562ExampleBytes() throws IOException {
        final byte[] value = {
                0x01, 0x23, 0x45, 0x67,
                (byte) 0x89, (byte) 0xAB,
                (byte) 0xCD, (byte) 0xEF,
                0x01, 0x23,
                0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF
        };

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        UuidRfc9562Bytes.INSTANCE.write(output, value);
        output.align(1);

        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(baos.toByteArray())));
        assertArrayEquals(value, UuidRfc9562Bytes.INSTANCE.read(input));
    }

    @Test
    void rejectsNullArguments() {
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(new ByteArrayOutputStream()));
        assertThrows(NullPointerException.class, () -> UuidRfc9562Bytes.INSTANCE.read(null));
        assertThrows(NullPointerException.class, () -> UuidRfc9562Bytes.INSTANCE.write(null, sample()));
        assertThrows(NullPointerException.class, () -> UuidRfc9562Bytes.INSTANCE.write(output, null));
    }

    @Test
    void rejectsWrongLengthValues() {
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(new ByteArrayOutputStream()));
        assertThrows(IllegalArgumentException.class, () -> UuidRfc9562Bytes.INSTANCE.write(output, new byte[15]));
        assertThrows(IllegalArgumentException.class, () -> UuidRfc9562Bytes.INSTANCE.write(output, new byte[17]));
    }

    private static byte[] sample() {
        return new byte[]{
                0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0A, 0x0B,
                0x0C, 0x0D, 0x0E, 0x0F
        };
    }
}
