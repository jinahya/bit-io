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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HalfRoundTripTest {

    // ------------------------------------------------------------------------------------- half16 (conformant
    // binary16)

    @Test
    void half16WritesExactlyTwoBytes() throws IOException {
        assertEquals(2, write16(1.0f).length);
    }

    @Test
    void half16RepresentableValuesRoundTripExactly() throws IOException {
        final float[] values = {
                0.0f, -0.0f,
                1.0f, -1.0f, 2.0f, 0.5f,
                65504.0f, -65504.0f,          // max finite normal
                0x1p-14f, -0x1p-14f,          // min normal
                0x1p-15f,                     // a subnormal
                1.0f + 0x1p-10f,              // 1 + 1 ulp (= 0x3C01), exactly representable
                0x1p-24f, -0x1p-24f,          // min positive/negative subnormal
                Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY
        };
        for (final float value : values) {
            assertEquals(Float.floatToRawIntBits(value), Float.floatToRawIntBits(roundTrip16(value)),
                         "round-trip changed " + value);
        }
    }

    @Test
    void half16RoundsToNearestEvenOnOverflowBoundary() throws IOException {
        assertEquals(65504.0f, roundTrip16(65519.0f));                   // below midpoint -> max normal
        assertEquals(Float.POSITIVE_INFINITY, roundTrip16(65520.0f));    // midpoint, ties to even (Inf)
        assertEquals(Float.POSITIVE_INFINITY, roundTrip16(65536.0f));    // above -> Infinity
    }

    @Test
    void half16RoundsTiesToEven() throws IOException {
        // 1 + 2^-11 is the midpoint between 1.0 (0x3C00, even) and 1+2^-10 (0x3C01, odd) -> ties to 1.0
        assertEquals(1.0f, roundTrip16(1.0f + 0x1p-11f));
        // 1 + 3*2^-11 is the midpoint between 0x3C01 (odd) and 0x3C02 (even) -> ties to 0x3C02 = 1 + 2^-9
        assertEquals(1.0f + 0x1p-9f, roundTrip16(1.0f + 3 * 0x1p-11f));
    }

    @Test
    void half16UnderflowRoundsToSignedZero() throws IOException {
        assertEquals(0x00000000, Float.floatToRawIntBits(roundTrip16(0x1p-25f)));  // tie -> even (zero)
        assertEquals(0x80000000, Float.floatToRawIntBits(roundTrip16(-0x1p-25f)));
        assertEquals(0x00000000, Float.floatToRawIntBits(roundTrip16(0x1p-26f)));  // far below -> zero
        assertEquals(0x00000000, Float.floatToRawIntBits(roundTrip16(Float.MIN_VALUE))); // float subnormal
    }

    @Test
    void half16RoundsUpJustAboveSubnormalMidpoint() throws IOException {
        // slightly more than 2^-25 -> nearest is the least subnormal 2^-24
        assertEquals(0x1p-24f, roundTrip16(0x1p-25f + 0x1p-40f));
    }

    @Test
    void half16NanRoundTripsAsNanPreservingQuietBit() throws IOException {
        final float qNaN = roundTrip16(Float.intBitsToFloat(0x7FC00001));
        assertTrue(Float.isNaN(qNaN));
        assertTrue((Float.floatToRawIntBits(qNaN) & 0x00400000) != 0); // native quiet bit (bit 22) stays set
    }

    // ----------------------------------------------------------------------------------------- half (reduced, 2.
    // .5/2..10)

    @Test
    void reducedHalfRejectsOutOfRangeSizes() {
        assertThrows(IllegalArgumentException.class, () -> writeReducedHalf(1, 10, 1.0f));   // exponentSize < 2
        assertThrows(IllegalArgumentException.class, () -> writeReducedHalf(6, 10, 1.0f));   // exponentSize > 5
        assertThrows(IllegalArgumentException.class, () -> writeReducedHalf(5, 1, 1.0f));    // fractionSize < 2
        assertThrows(IllegalArgumentException.class, () -> writeReducedHalf(5, 11, 1.0f));   // fractionSize > 10
    }

    @Test
    void reducedHalfMatchesReducedFloatAtSameWidths() throws IOException {
        // writeHalf(e,f) is the reduced float codec bounded to binary16 widths -> same bytes as writeFloat(e,f)
        for (final float value : new float[]{0.0f, 1.0f, -2.0f, 0.5f, 0x1p-14f}) {
            assertArrayEquals(writeReducedFloat(5, 10, value), writeReducedHalf(5, 10, value),
                              "byte mismatch for " + value);
        }
    }

    @Test
    void reducedHalfRoundTripsRepresentableNormals() throws IOException {
        for (final int[] ef : new int[][]{{2, 2}, {3, 5}, {5, 10}}) {
            for (final float value : new float[]{0.0f, 1.0f, -1.0f}) {
                assertEquals(Float.floatToRawIntBits(value),
                             Float.floatToRawIntBits(roundTripReducedHalf(ef[0], ef[1], value)));
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------
    // helpers

    private static byte[] write16(final float value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeHalf16(value);
        output.align(1);
        return baos.toByteArray();
    }

    private static float roundTrip16(final float value) throws IOException {
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(write16(value)))).readHalf16();
    }

    private static byte[] writeReducedHalf(final int e, final int f, final float value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeHalf(e, f, value);
        output.align(1);
        return baos.toByteArray();
    }

    private static byte[] writeReducedFloat(final int e, final int f, final float value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeFloat(e, f, value);
        output.align(1);
        return baos.toByteArray();
    }

    private static float roundTripReducedHalf(final int e, final int f, final float value) throws IOException {
        final byte[] bytes = writeReducedHalf(e, f, value);
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes))).readHalf(e, f);
    }
}
