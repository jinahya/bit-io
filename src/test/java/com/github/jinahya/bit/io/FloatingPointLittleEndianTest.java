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

class FloatingPointLittleEndianTest {

    @Test
    void half16LeIsBigEndianReversed() throws IOException {
        for (final float value : new float[]{0.0f, 1.0f, -2.0f, 65504.0f, 0x1p-24f}) {
            assertArrayEquals(reversed(write(new BeHalf(value))), write(new LeHalf(value)), "half16 " + value);
        }
    }

    @Test
    void float32LeIsBigEndianReversed() throws IOException {
        for (final float value : new float[]{0.0f, 1.0f, -2.5f, Float.MIN_VALUE, Float.MAX_VALUE}) {
            assertArrayEquals(reversed(write(new BeFloat(value))), write(new LeFloat(value)), "float32 " + value);
        }
    }

    @Test
    void double64LeIsBigEndianReversed() throws IOException {
        for (final double value : new double[]{0.0d, 1.0d, -2.5d, Double.MIN_VALUE, Double.MAX_VALUE}) {
            assertArrayEquals(reversed(write(new BeDouble(value))), write(new LeDouble(value)), "double64 " + value);
        }
    }

    @Test
    void half16LeRoundTrips() throws IOException {
        for (final float value : new float[]{0.0f, -0.0f, 1.0f, -1.0f, 65504.0f, 0x1p-24f,
                Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY}) {
            final byte[] bytes = write(new LeHalf(value));
            final float read = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes))).readHalf16Le();
            assertEquals(Float.floatToRawIntBits(value), Float.floatToRawIntBits(read), "half16 " + value);
        }
    }

    @Test
    void float32LeRoundTrips() throws IOException {
        for (final float value : new float[]{0.0f, -0.0f, 1.0f, -2.5f, Float.MIN_VALUE, Float.MAX_VALUE,
                Float.POSITIVE_INFINITY, Float.NaN}) {
            final byte[] bytes = write(new LeFloat(value));
            final float read =
                    new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes))).readFloat32Le();
            assertEquals(Float.floatToRawIntBits(value), Float.floatToRawIntBits(read), "float32 " + value);
        }
    }

    @Test
    void double64LeRoundTrips() throws IOException {
        for (final double value : new double[]{0.0d, -0.0d, 1.0d, -2.5d, Double.MIN_VALUE, Double.MAX_VALUE,
                Double.POSITIVE_INFINITY, Double.NaN}) {
            final byte[] bytes = write(new LeDouble(value));
            final double read =
                    new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes))).readDouble64Le();
            assertEquals(Double.doubleToRawLongBits(value), Double.doubleToRawLongBits(read), "double64 " + value);
        }
    }

    // -----------------------------------------------------------------------------------------------------------
    // helpers

    private interface Op {

        void write(BitOutput output) throws IOException;
    }

    private static final class BeHalf
            implements Op {

        private BeHalf(final float v) {
            this.v = v;
        }

        public void write(final BitOutput o) throws IOException {
            o.writeHalf16(v);
        }

        private final float v;
    }

    private static final class LeHalf
            implements Op {

        private LeHalf(final float v) {
            this.v = v;
        }

        public void write(final BitOutput o) throws IOException {
            o.writeHalf16Le(v);
        }

        private final float v;
    }

    private static final class BeFloat
            implements Op {

        private BeFloat(final float v) {
            this.v = v;
        }

        public void write(final BitOutput o) throws IOException {
            o.writeFloat32(v);
        }

        private final float v;
    }

    private static final class LeFloat
            implements Op {

        private LeFloat(final float v) {
            this.v = v;
        }

        public void write(final BitOutput o) throws IOException {
            o.writeFloat32Le(v);
        }

        private final float v;
    }

    private static final class BeDouble
            implements Op {

        private BeDouble(final double v) {
            this.v = v;
        }

        public void write(final BitOutput o) throws IOException {
            o.writeDouble64(v);
        }

        private final double v;
    }

    private static final class LeDouble
            implements Op {

        private LeDouble(final double v) {
            this.v = v;
        }

        public void write(final BitOutput o) throws IOException {
            o.writeDouble64Le(v);
        }

        private final double v;
    }

    private static byte[] write(final Op op) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        op.write(output);
        output.align(1);
        return baos.toByteArray();
    }

    private static byte[] reversed(final byte[] a) {
        final byte[] r = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            r[i] = a[a.length - 1 - i];
        }
        return r;
    }
}
