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

import com.github.jinahya.bit.io.BitOutput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.StreamByteOutput;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous.ExpGolombConstants.MAX_SE;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombConstants.MIN_SE;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.readSe;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.readUe;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.writeSe;
import static com.github.jinahya.bit.io.miscellaneous.ExpGolombTestUtils.writeUe;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class ExpGolombTest {

    @Test
    void ueWritesKnownVectors() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0x80}, writeUe(0L)); // 1
        assertArrayEquals(new byte[]{0x40}, writeUe(1L));        // 010
        assertArrayEquals(new byte[]{0x60}, writeUe(2L));        // 011
        assertArrayEquals(new byte[]{0x20}, writeUe(3L));        // 00100
    }

    @Test
    void ueReadsKnownVectors() throws IOException {
        assertEquals(0L, readUe(new byte[]{(byte) 0x80}));
        assertEquals(1L, readUe(new byte[]{0x40}));
        assertEquals(2L, readUe(new byte[]{0x60}));
        assertEquals(3L, readUe(new byte[]{0x20}));
    }

    @Test
    void ueRoundTripsBoundaryValues() throws IOException {
        final long[] values = {0L, 1L, 2L, 3L, 0xFFL, 0x100L, Integer.MAX_VALUE, Long.MAX_VALUE};
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], readUe(writeUe(values[i])));
        }
    }

    @Test
    void ueRejectsNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> writeUe(-1L));
    }

    @Test
    void seWritesKnownVectors() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0x80}, writeSe(0L)); // codeNum 0 -> 1
        assertArrayEquals(new byte[]{0x40}, writeSe(1L));        // codeNum 1 -> 010
        assertArrayEquals(new byte[]{0x60}, writeSe(-1L));       // codeNum 2 -> 011
        assertArrayEquals(new byte[]{0x20}, writeSe(2L));        // codeNum 3 -> 00100
        assertArrayEquals(new byte[]{0x28}, writeSe(-2L));       // codeNum 4 -> 00101
    }

    @Test
    void seReadsKnownVectors() throws IOException {
        assertEquals(0L, readSe(new byte[]{(byte) 0x80}));
        assertEquals(1L, readSe(new byte[]{0x40}));
        assertEquals(-1L, readSe(new byte[]{0x60}));
        assertEquals(2L, readSe(new byte[]{0x20}));
        assertEquals(-2L, readSe(new byte[]{0x28}));
    }

    @Test
    void seRoundTripsBoundaryValues() throws IOException {
        final long[] values = {MIN_SE, -2L, -1L, 0L, 1L, 2L, MAX_SE};
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], readSe(writeSe(values[i])));
        }
    }

    @Test
    void seRejectsOutOfRangeValues() {
        assertThrows(IllegalArgumentException.class, () -> writeSe(MIN_SE - 1L));
        assertThrows(IllegalArgumentException.class, () -> writeSe(MAX_SE + 1L));
    }

    @Test
    void rejectsNullArguments() {
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(new ByteArrayOutputStream()));
        assertThrows(NullPointerException.class, () -> ExpGolombUE.INSTANCE.read(null));
        assertThrows(NullPointerException.class, () -> ExpGolombSE.INSTANCE.read(null));
        assertThrows(NullPointerException.class, () -> ExpGolombUE.INSTANCE.write(null, 0L));
        assertThrows(NullPointerException.class, () -> ExpGolombSE.INSTANCE.write(null, 0L));
        assertThrows(NullPointerException.class, () -> ExpGolombUE.INSTANCE.write(output, null));
        assertThrows(NullPointerException.class, () -> ExpGolombSE.INSTANCE.write(output, null));
    }

    @Test
    void rejectsCodeNumBeyondLongMaxValue() {
        assertThrows(IOException.class, () -> readUe(new byte[]{
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x80
        }));
    }
}
