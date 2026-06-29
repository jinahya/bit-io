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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BitFloatingPointRoundTripTest {

    @Test
    void writeFloatAtFullWidthRoundTripsRawBits() throws IOException {
        final float[] values = {
                0.0f,
                -0.0f,
                1.0f,
                -1.0f,
                Float.MIN_NORMAL,
                -Float.MIN_NORMAL,
                Float.MAX_VALUE,
                -Float.MAX_VALUE,
                Float.POSITIVE_INFINITY,
                Float.NEGATIVE_INFINITY
        };
        for (final float value : values) {
            assertEquals(Float.floatToRawIntBits(value),
                         Float.floatToRawIntBits(readFloat(writeFloat(8, 23, value), 8, 23)));
        }
    }

    @Test
    void writeFloat32RoundTripsRawBits() throws IOException {
        final float[] values = {
                0.0f,
                -0.0f,
                1.0f,
                -1.0f,
                Float.MIN_NORMAL,
                Float.MAX_VALUE,
                Float.POSITIVE_INFINITY,
                Float.NEGATIVE_INFINITY
        };
        for (final float value : values) {
            assertEquals(Float.floatToRawIntBits(value), Float.floatToRawIntBits(readFloat32(writeFloat32(value))));
        }
    }

    @Test
    void writeDoubleAtFullWidthRoundTripsRawBits() throws IOException {
        final double[] values = {
                0.0d,
                -0.0d,
                1.0d,
                -1.0d,
                Double.MIN_NORMAL,
                -Double.MIN_NORMAL,
                Double.MAX_VALUE,
                -Double.MAX_VALUE,
                Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY
        };
        for (final double value : values) {
            assertEquals(Double.doubleToRawLongBits(value),
                         Double.doubleToRawLongBits(readDouble(writeDouble(11, 52, value), 11, 52)));
        }
    }

    @Test
    void writeDouble64RoundTripsRawBits() throws IOException {
        final double[] values = {
                0.0d,
                -0.0d,
                1.0d,
                -1.0d,
                Double.MIN_NORMAL,
                Double.MAX_VALUE,
                Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY
        };
        for (final double value : values) {
            assertEquals(Double.doubleToRawLongBits(value),
                         Double.doubleToRawLongBits(readDouble64(writeDouble64(value))));
        }
    }

    @Test
    void reducedFloatPreservesZerosInfinitiesAndNanFormatBits() throws IOException {
        assertEquals(0x00000000, Float.floatToRawIntBits(readFloat(writeFloat(3, 2, 0.0f), 3, 2)));
        assertEquals(0x80000000, Float.floatToRawIntBits(readFloat(writeFloat(3, 2, -0.0f), 3, 2)));
        assertEquals(0x7F800000,
                     Float.floatToRawIntBits(readFloat(writeFloat(3, 2, Float.POSITIVE_INFINITY), 3, 2)));
        assertEquals(0xFF800000,
                     Float.floatToRawIntBits(readFloat(writeFloat(3, 2, Float.NEGATIVE_INFINITY), 3, 2)));
        assertTrue(Float.isNaN(readFloat(writeFloat(3, 2, Float.intBitsToFloat(0x7FC00001)), 3, 2)));

        final FloatFields quiet = readFloatFields(writeFloat(3, 2, Float.intBitsToFloat(0x7FC00001)), 3, 2);
        assertEquals(0, quiet.sign);
        assertEquals(0x07, quiet.exponent);
        assertEquals(0x02, quiet.fraction); // qNaN: 10

        final int signalingBits = 0xFF800001;
        final float signalingValue = Float.intBitsToFloat(signalingBits);
        assumeTrue(isSignalingFloatNaN(Float.floatToRawIntBits(signalingValue)));
        final FloatFields signaling = readFloatFields(writeFloat(3, 2, signalingValue), 3, 2);
        assertEquals(1, signaling.sign);
        assertEquals(0x07, signaling.exponent);
        assertEquals(0x01, signaling.fraction); // sNaN: 01, guarded after payload truncation
    }

    @Test
    void reducedDoublePreservesZerosInfinitiesAndNanFormatBits() throws IOException {
        assertEquals(0x0000000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(4, 2, 0.0d), 4, 2)));
        assertEquals(0x8000000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(4, 2, -0.0d), 4, 2)));
        assertEquals(0x7FF0000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(4, 2, Double.POSITIVE_INFINITY), 4, 2)));
        assertEquals(0xFFF0000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(4, 2, Double.NEGATIVE_INFINITY), 4, 2)));
        assertTrue(Double.isNaN(readDouble(writeDouble(4, 2, Double.longBitsToDouble(0x7FF8000000000001L)), 4, 2)));

        final DoubleFields quiet = readDoubleFields(writeDouble(4, 2, Double.longBitsToDouble(0x7FF8000000000001L)),
                                                    4, 2);
        assertEquals(0, quiet.sign);
        assertEquals(0x0F, quiet.exponent);
        assertEquals(0x02L, quiet.fraction); // qNaN: 10

        final long signalingBits = 0xFFF0000000000001L;
        final double signalingValue = Double.longBitsToDouble(signalingBits);
        assumeTrue(isSignalingDoubleNaN(Double.doubleToRawLongBits(signalingValue)));
        final DoubleFields signaling = readDoubleFields(writeDouble(4, 2, signalingValue), 4, 2);
        assertEquals(1, signaling.sign);
        assertEquals(0x0F, signaling.exponent);
        assertEquals(0x01L, signaling.fraction); // sNaN: 01, guarded after payload truncation
    }

    @Test
    void reducedFloatSaturatesOverflowToInfinityAndUnderflowToZero() throws IOException {
        assertEquals(0x7F800000, Float.floatToRawIntBits(readFloat(writeFloat(3, 4, 16.0f), 3, 4)));
        assertEquals(0xFF800000, Float.floatToRawIntBits(readFloat(writeFloat(3, 4, -16.0f), 3, 4)));
        assertEquals(0x00000000, Float.floatToRawIntBits(readFloat(writeFloat(3, 4, 0.125f), 3, 4)));
        assertEquals(0x80000000, Float.floatToRawIntBits(readFloat(writeFloat(3, 4, -0.125f), 3, 4)));
    }

    @Test
    void reducedDoubleSaturatesOverflowToInfinityAndUnderflowToZero() throws IOException {
        assertEquals(0x7FF0000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(3, 4, 16.0d), 3, 4)));
        assertEquals(0xFFF0000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(3, 4, -16.0d), 3, 4)));
        assertEquals(0x0000000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(3, 4, 0.125d), 3, 4)));
        assertEquals(0x8000000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(3, 4, -0.125d), 3, 4)));
    }

    @Test
    void reducedFloatTruncatesFractionTowardZero() throws IOException {
        final float positive = 1.9999999f;
        final float positiveResult = readFloat(writeFloat(8, 10, positive), 8, 10);
        assertTrue(Math.abs(positiveResult) <= Math.abs(positive));
        assertTrue(positiveResult > 0.0f);

        final float negative = -1.9999999f;
        final float negativeResult = readFloat(writeFloat(8, 10, negative), 8, 10);
        assertTrue(Math.abs(negativeResult) <= Math.abs(negative));
        assertTrue(negativeResult < 0.0f);
    }

    @Test
    void reducedDoubleTruncatesFractionTowardZero() throws IOException {
        final double positive = 1.9999999999999998d;
        final double positiveResult = readDouble(writeDouble(11, 12, positive), 11, 12);
        assertTrue(Math.abs(positiveResult) <= Math.abs(positive));
        assertTrue(positiveResult > 0.0d);

        final double negative = -1.9999999999999998d;
        final double negativeResult = readDouble(writeDouble(11, 12, negative), 11, 12);
        assertTrue(Math.abs(negativeResult) <= Math.abs(negative));
        assertTrue(negativeResult < 0.0d);
    }

    @Test
    void fullWidthRoundTripsNativeSubnormals() throws IOException {
        final float[] floats = {
                Float.MIN_VALUE,                          // smallest positive subnormal
                -Float.MIN_VALUE,
                Float.intBitsToFloat(0x007FFFFF),         // largest positive subnormal
                Float.intBitsToFloat(0x807FFFFF)          // largest negative subnormal
        };
        for (final float value : floats) {
            assertEquals(Float.floatToRawIntBits(value),
                         Float.floatToRawIntBits(readFloat(writeFloat(8, 23, value), 8, 23)));
            assertEquals(Float.floatToRawIntBits(value),
                         Float.floatToRawIntBits(readFloat32(writeFloat32(value))));
        }
        final double[] doubles = {
                Double.MIN_VALUE,                         // smallest positive subnormal
                -Double.MIN_VALUE,
                Double.longBitsToDouble(0x000FFFFFFFFFFFFFL), // largest positive subnormal
                Double.longBitsToDouble(0x800FFFFFFFFFFFFFL)  // largest negative subnormal
        };
        for (final double value : doubles) {
            assertEquals(Double.doubleToRawLongBits(value),
                         Double.doubleToRawLongBits(readDouble(writeDouble(11, 52, value), 11, 52)));
            assertEquals(Double.doubleToRawLongBits(value),
                         Double.doubleToRawLongBits(readDouble64(writeDouble64(value))));
        }
    }

    @Test
    void reducedEncodingUnderflowsTiniestSubnormalToSignedZero() throws IOException {
        assertEquals(0x00000000, Float.floatToRawIntBits(readFloat(writeFloat(3, 2, Float.MIN_VALUE), 3, 2)));
        assertEquals(0x80000000, Float.floatToRawIntBits(readFloat(writeFloat(3, 2, -Float.MIN_VALUE), 3, 2)));
        assertEquals(0x0000000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(4, 2, Double.MIN_VALUE), 4, 2)));
        assertEquals(0x8000000000000000L,
                     Double.doubleToRawLongBits(readDouble(writeDouble(4, 2, -Double.MIN_VALUE), 4, 2)));
    }

    @Test
    void floatingPointSizeConstraintsRejectOutOfRangeValues() {
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidExponentSizeFloat(1));
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidExponentSizeFloat(9));
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidFractionSizeFloat(1));
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidFractionSizeFloat(24));
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidExponentSizeDouble(1));
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidExponentSizeDouble(12));
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidFractionSizeDouble(1));
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidFractionSizeDouble(53));
    }

    private static byte[] writeFloat(final int exponentSize, final int fractionSize, final float value)
            throws IOException {
        return write(new WriteOperation() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeFloat(exponentSize, fractionSize, value);
            }
        });
    }

    private static float readFloat(final byte[] bytes, final int exponentSize, final int fractionSize)
            throws IOException {
        final BitInput input = input(bytes);
        return input.readFloat(exponentSize, fractionSize);
    }

    private static byte[] writeFloat32(final float value) throws IOException {
        return write(new WriteOperation() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeFloat32(value);
            }
        });
    }

    private static float readFloat32(final byte[] bytes) throws IOException {
        return input(bytes).readFloat32();
    }

    private static byte[] writeDouble(final int exponentSize, final int fractionSize, final double value)
            throws IOException {
        return write(new WriteOperation() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeDouble(exponentSize, fractionSize, value);
            }
        });
    }

    private static double readDouble(final byte[] bytes, final int exponentSize, final int fractionSize)
            throws IOException {
        final BitInput input = input(bytes);
        return input.readDouble(exponentSize, fractionSize);
    }

    private static byte[] writeDouble64(final double value) throws IOException {
        return write(new WriteOperation() {
            @Override
            public void write(final BitOutput output) throws IOException {
                output.writeDouble64(value);
            }
        });
    }

    private static double readDouble64(final byte[] bytes) throws IOException {
        return input(bytes).readDouble64();
    }

    private static FloatFields readFloatFields(final byte[] bytes, final int exponentSize, final int fractionSize)
            throws IOException {
        final BitInput input = input(bytes);
        final int sign = input.readUnsignedInt(1);
        final int exponent = input.readUnsignedInt(exponentSize);
        final int fraction = input.readUnsignedInt(fractionSize);
        return new FloatFields(sign, exponent, fraction);
    }

    private static DoubleFields readDoubleFields(final byte[] bytes, final int exponentSize, final int fractionSize)
            throws IOException {
        final BitInput input = input(bytes);
        final int sign = input.readUnsignedInt(1);
        final int exponent = input.readUnsignedInt(exponentSize);
        final long fraction = input.readUnsignedLong(fractionSize);
        return new DoubleFields(sign, exponent, fraction);
    }

    private static byte[] write(final WriteOperation operation) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        operation.write(output);
        output.align(1);
        return baos.toByteArray();
    }

    private static BitInput input(final byte[] bytes) {
        return new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
    }

    private static boolean isSignalingFloatNaN(final int bits) {
        final int rawExp = (bits >>> 23) & 0xFF;
        final int rawFrac = bits & 0x7FFFFF;
        return rawExp == 0xFF && rawFrac != 0 && (rawFrac & 0x400000) == 0;
    }

    private static boolean isSignalingDoubleNaN(final long bits) {
        final long rawExp = (bits >>> 52) & 0x7FFL;
        final long rawFrac = bits & 0x000FFFFFFFFFFFFFL;
        return rawExp == 0x7FFL && rawFrac != 0L && (rawFrac & 0x0008000000000000L) == 0L;
    }

    private interface WriteOperation {

        void write(BitOutput output) throws IOException;
    }

    private static class FloatFields {

        FloatFields(final int sign, final int exponent, final int fraction) {
            this.sign = sign;
            this.exponent = exponent;
            this.fraction = fraction;
        }

        final int sign;

        final int exponent;

        final int fraction;
    }

    private static class DoubleFields {

        DoubleFields(final int sign, final int exponent, final long fraction) {
            this.sign = sign;
            this.exponent = exponent;
            this.fraction = fraction;
        }

        final int sign;

        final int exponent;

        final long fraction;
    }
}
