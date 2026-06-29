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
import com.github.jinahya.bit.io.BitReader;
import com.github.jinahya.bit.io.BitWriter;
import com.github.jinahya.bit.io.BooleanBitReader;
import com.github.jinahya.bit.io.BooleanBitWriter;
import com.github.jinahya.bit.io.DefaultBitInput;
import com.github.jinahya.bit.io.DefaultBitOutput;
import com.github.jinahya.bit.io.LongBitReader;
import com.github.jinahya.bit.io.LongBitWriter;
import com.github.jinahya.bit.io.StreamByteInput;
import com.github.jinahya.bit.io.StreamByteOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

final class _TestUtils {

    static <T> T read(final BitReader<T> reader, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return reader.read(input);
    }

    static boolean read(final BooleanBitReader reader, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return input.readBoolean(reader);
    }

    static long read(final LongBitReader reader, final byte[] bytes) throws IOException {
        final BitInput input = new DefaultBitInput(new StreamByteInput(new ByteArrayInputStream(bytes)));
        return input.readLong(reader);
    }

    static <T> byte[] write(final BitWriter<T> writer, final T value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        writer.write(output, value);
        output.align(1);
        return baos.toByteArray();
    }

    static byte[] write(final BooleanBitWriter writer, final boolean value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeBoolean(writer, value);
        output.align(1);
        return baos.toByteArray();
    }

    static byte[] write(final LongBitWriter writer, final long value) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new DefaultBitOutput(new StreamByteOutput(baos));
        output.writeLong(writer, value);
        output.align(1);
        return baos.toByteArray();
    }

    private _TestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
