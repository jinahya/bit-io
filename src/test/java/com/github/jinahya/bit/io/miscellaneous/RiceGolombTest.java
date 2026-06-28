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

import static com.github.jinahya.bit.io.miscellaneous.RiceGolombCodes.MAX_SIGNED_VALUE;
import static com.github.jinahya.bit.io.miscellaneous.RiceGolombCodes.MIN_SIGNED_VALUE;
import static com.github.jinahya.bit.io.miscellaneous.RiceGolombTestUtils.readGolombJpegLs;
import static com.github.jinahya.bit.io.miscellaneous.RiceGolombTestUtils.readRiceFlac;
import static com.github.jinahya.bit.io.miscellaneous.RiceGolombTestUtils.writeGolombJpegLs;
import static com.github.jinahya.bit.io.miscellaneous.RiceGolombTestUtils.writeRiceFlac;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class RiceGolombTest {

    @Test
    void riceFlacWritesKnownVectors() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0x80}, writeRiceFlac(2, 0L));  // 100
        assertArrayEquals(new byte[]{(byte) 0xA0}, writeRiceFlac(2, -1L)); // 101
        assertArrayEquals(new byte[]{(byte) 0xC0}, writeRiceFlac(2, 1L));  // 110
        assertArrayEquals(new byte[]{(byte) 0xE0}, writeRiceFlac(2, -2L)); // 111
        assertArrayEquals(new byte[]{0x40}, writeRiceFlac(2, 2L));         // 0100
    }

    @Test
    void riceFlacReadsKnownVectors() throws IOException {
        assertEquals(0L, readRiceFlac(2, new byte[]{(byte) 0x80}));
        assertEquals(-1L, readRiceFlac(2, new byte[]{(byte) 0xA0}));
        assertEquals(1L, readRiceFlac(2, new byte[]{(byte) 0xC0}));
        assertEquals(-2L, readRiceFlac(2, new byte[]{(byte) 0xE0}));
        assertEquals(2L, readRiceFlac(2, new byte[]{0x40}));
    }

    @Test
    void golombJpegLsWritesKnownVectors() throws IOException {
        assertArrayEquals(new byte[]{0x00}, writeGolombJpegLs(2, 0L));         // 000
        assertArrayEquals(new byte[]{0x20}, writeGolombJpegLs(2, -1L));        // 001
        assertArrayEquals(new byte[]{0x40}, writeGolombJpegLs(2, 1L));         // 010
        assertArrayEquals(new byte[]{0x60}, writeGolombJpegLs(2, -2L));        // 011
        assertArrayEquals(new byte[]{(byte) 0x80}, writeGolombJpegLs(2, 2L));  // 1000
    }

    @Test
    void golombJpegLsReadsKnownVectors() throws IOException {
        assertEquals(0L, readGolombJpegLs(2, new byte[]{0x00}));
        assertEquals(-1L, readGolombJpegLs(2, new byte[]{0x20}));
        assertEquals(1L, readGolombJpegLs(2, new byte[]{0x40}));
        assertEquals(-2L, readGolombJpegLs(2, new byte[]{0x60}));
        assertEquals(2L, readGolombJpegLs(2, new byte[]{(byte) 0x80}));
    }

    @Test
    void roundTripsRepresentativeValues() throws IOException {
        final int[] parameters = {0, 1, 2, 10, 62};
        final long[] values = {-33L, -2L, -1L, 0L, 1L, 2L, 33L};
        for (int i = 0; i < parameters.length; i++) {
            for (int j = 0; j < values.length; j++) {
                assertEquals(values[j], readRiceFlac(parameters[i], writeRiceFlac(parameters[i], values[j])));
                assertEquals(values[j], readGolombJpegLs(parameters[i], writeGolombJpegLs(parameters[i], values[j])));
            }
        }
    }

    @Test
    void exposesParameter() {
        assertEquals(7, RiceFLAC.of(7).parameter());
        assertEquals(7, GolombJPEGLS.of(7).parameter());
    }

    @Test
    void rejectsInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> RiceFLAC.of(-1));
        assertThrows(IllegalArgumentException.class, () -> RiceFLAC.of(63));
        assertThrows(IllegalArgumentException.class, () -> GolombJPEGLS.of(-1));
        assertThrows(IllegalArgumentException.class, () -> GolombJPEGLS.of(63));
    }

    @Test
    void rejectsOutOfRangeValues() {
        assertThrows(IllegalArgumentException.class, () -> writeRiceFlac(0, MIN_SIGNED_VALUE - 1L));
        assertThrows(IllegalArgumentException.class, () -> writeRiceFlac(0, MAX_SIGNED_VALUE + 1L));
        assertThrows(IllegalArgumentException.class, () -> writeGolombJpegLs(0, MIN_SIGNED_VALUE - 1L));
        assertThrows(IllegalArgumentException.class, () -> writeGolombJpegLs(0, MAX_SIGNED_VALUE + 1L));
    }

    @Test
    void rejectsNullArguments() {
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(new ByteArrayOutputStream()));
        assertThrows(NullPointerException.class, () -> RiceFLAC.of(0).read(null));
        assertThrows(NullPointerException.class, () -> GolombJPEGLS.of(0).read(null));
        assertThrows(NullPointerException.class, () -> RiceFLAC.of(0).write(null, 0L));
        assertThrows(NullPointerException.class, () -> GolombJPEGLS.of(0).write(null, 0L));
        assertThrows(NullPointerException.class, () -> RiceFLAC.of(0).write(output, null));
        assertThrows(NullPointerException.class, () -> GolombJPEGLS.of(0).write(output, null));
    }
}
