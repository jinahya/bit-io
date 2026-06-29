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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

final class RiceGolombTestUtils {

    static byte[] writeRiceFlac(final int parameter, final long value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        RiceFLAC.of(parameter).writeLong(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    static long readRiceFlac(final int parameter, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return RiceFLAC.of(parameter).readLong(input);
    }

    static byte[] writeGolombJpegLs(final int parameter, final long value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        GolombJPEGLS.of(parameter).writeLong(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    static long readGolombJpegLs(final int parameter, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return GolombJPEGLS.of(parameter).readLong(input);
    }

    private RiceGolombTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
