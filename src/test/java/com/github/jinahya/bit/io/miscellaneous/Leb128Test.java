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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous.Leb128TestUtils.readSigned;
import static com.github.jinahya.bit.io.miscellaneous.Leb128TestUtils.readUnsigned;
import static com.github.jinahya.bit.io.miscellaneous.Leb128TestUtils.writeSigned;
import static com.github.jinahya.bit.io.miscellaneous.Leb128TestUtils.writeSignedObject;
import static com.github.jinahya.bit.io.miscellaneous.Leb128TestUtils.writeUnsigned;
import static com.github.jinahya.bit.io.miscellaneous.Leb128TestUtils.writeUnsignedObject;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class Leb128Test {

    @Test
    void unsignedWritesKnownVector() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0xE5, (byte) 0x8E, 0x26}, writeUnsigned(624485L));
    }

    @Test
    void unsignedReadsKnownVector() throws IOException {
        assertEquals(624485L, readUnsigned(new byte[]{(byte) 0xE5, (byte) 0x8E, 0x26}));
    }

    @Test
    void unsignedRoundTripsBoundaryValues() throws IOException {
        final long[] values = {0L, 1L, 0x7FL, 0x80L, 0x3FFFL, 0x4000L, Long.MAX_VALUE};
        for (final long value : values) {
            assertEquals(value, readUnsigned(writeUnsigned(value)));
        }
    }

    @Test
    void unsignedRejectsNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> writeUnsigned(-1L));
    }

    @Test
    void signedWritesKnownNegativeVector() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0xC0, (byte) 0xBB, 0x78}, writeSigned(-123456L));
    }

    @Test
    void signedReadsKnownNegativeVector() throws IOException {
        assertEquals(-123456L, readSigned(new byte[]{(byte) 0xC0, (byte) 0xBB, 0x78}));
    }

    @Test
    void signedRoundTripsBoundaryValues() throws IOException {
        final long[] values = {
                Long.MIN_VALUE, -624485L, -123456L, -65L, -64L, -1L, 0L, 1L, 63L, 64L, 123456L, 624485L,
                Long.MAX_VALUE
        };
        for (final long value : values) {
            assertEquals(value, readSigned(writeSigned(value)));
        }
    }

    @Test
    void rejectsNullArguments() {
        assertThrows(NullPointerException.class, () -> Leb128Reader.UNSIGNED.read(null));
        assertThrows(NullPointerException.class, () -> Leb128Reader.SIGNED.read(null));
        assertThrows(NullPointerException.class, () -> Leb128Writer.UNSIGNED.write(null, 0L));
        assertThrows(NullPointerException.class, () -> Leb128Writer.SIGNED.write(null, 0L));
        assertThrows(NullPointerException.class, () -> writeUnsignedObject(null));
        assertThrows(NullPointerException.class, () -> writeSignedObject(null));
    }

    @Test
    void unsignedRejectsTooLargeEncodedValue() {
        assertThrows(IOException.class, () -> readUnsigned(new byte[]{
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x01
        }));
    }

    @Test
    void signedRejectsUnterminatedEncodedValue() {
        assertThrows(IOException.class, () -> readSigned(new byte[]{
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80
        }));
    }
}
