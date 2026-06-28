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

import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullValue;

/**
 * A codec for CBOR simple values, excluding floats and the break stop code.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc8949.html">RFC 8949: Concise Binary Object Representation</a>
 */
public final class CborSimpleValue
        implements BitReader<Integer>, BitWriter<Integer> {

    public static final CborSimpleValue INSTANCE = new CborSimpleValue();

    private CborSimpleValue() {
        super();
    }

    @Override
    public Integer read(final BitInput input) throws IOException {
        requireNonNullInput(input);
        final int first = input.readUnsignedInt(Byte.SIZE);
        if ((first >>> 5) != 7) {
            throw new IOException("not a CBOR simple/floating major type");
        }
        final int additional = first & 0x1F;
        if (additional < 24) {
            return Integer.valueOf(additional);
        }
        if (additional == 24) {
            final int value = input.readUnsignedInt(Byte.SIZE);
            if (value < 32) {
                throw new IOException("non-minimal CBOR simple value");
            }
            return Integer.valueOf(value);
        }
        throw new IOException("not a CBOR simple value additional information: " + additional);
    }

    @Override
    public void write(final BitOutput output, final Integer value) throws IOException {
        requireNonNullOutput(output);
        final int v = requireNonNullValue(value).intValue();
        if (v < 0 || v > 255) {
            throw new IllegalArgumentException("CBOR simple value out of range: " + v);
        }
        if (v >= 24 && v < 32) {
            throw new IllegalArgumentException("reserved CBOR simple value: " + v);
        }
        if (v < 24) {
            output.writeUnsignedInt(Byte.SIZE, 0xE0 | v);
        } else {
            output.writeUnsignedInt(Byte.SIZE, 0xF8);
            output.writeUnsignedInt(Byte.SIZE, v);
        }
    }
}
