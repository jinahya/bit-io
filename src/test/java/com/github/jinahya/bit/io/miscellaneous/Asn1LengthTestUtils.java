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

final class Asn1LengthTestUtils {

    static byte[] writeBer(final long value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        Asn1BerLength.INSTANCE.write(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    static byte[] writeBerIndefinite() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        Asn1BerLength.writeBerIndefiniteLength(output);
        output.align(1);
        return baos.toByteArray();
    }

    static long readBer(final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return Asn1BerLength.INSTANCE.read(input);
    }

    static void readBerIndefinite(final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        Asn1BerLength.readBerIndefiniteLength(input);
    }

    static byte[] writeDer(final long value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        Asn1DerLength.INSTANCE.write(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    static void writeDerObject(final Long value) throws IOException {
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(new ByteArrayOutputStream()));
        Asn1DerLength.INSTANCE.write(output, value);
    }

    static long readDer(final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return Asn1DerLength.INSTANCE.read(input);
    }

    private Asn1LengthTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
