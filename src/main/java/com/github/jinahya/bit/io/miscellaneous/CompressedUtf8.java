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

import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForUnsignedInt;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullInput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullOutput;
import static com.github.jinahya.bit.io.miscellaneous._Utils.requireNonNullValue;

/**
 * A length-prefixed string codec that stores each code point as a two-bit block type followed by {@code 7},
 * {@code 5 + 6}, {@code 4 + 6 + 6}, or {@code 3 + 6 + 6 + 6} payload bits.
 */
final class CompressedUtf8
        implements BitReader<String>, BitWriter<String> {

    private static final int TYPE_SIZE = 2;

    private static final int TYPE_1 = 0;

    private static final int TYPE_2 = 1;

    private static final int TYPE_3 = 2;

    private static final int TYPE_4 = 3;

    private static boolean isSurrogate(final int codePoint) {
        return codePoint >= Character.MIN_SURROGATE && codePoint <= Character.MAX_SURROGATE;
    }

    private static int codePointCount(final String value) {
        int count = 0;
        for (int offset = 0; offset < value.length(); count++) {
            final int codePoint = value.codePointAt(offset);
            if (isSurrogate(codePoint)) {
                throw new IllegalArgumentException("unpaired surrogate code point: " + codePoint);
            }
            offset += Character.charCount(codePoint);
        }
        return count;
    }

    private static int readCodePoint(final BitInput input) throws IOException {
        final int type = input.readUnsignedInt(TYPE_SIZE);
        final int codePoint;
        if (type == TYPE_1) {
            codePoint = input.readUnsignedInt(7);
        } else if (type == TYPE_2) {
            codePoint = input.readUnsignedInt(5) << 6
                        | input.readUnsignedInt(6);
            requireDecodedCodePoint(codePoint, 0x80);
        } else if (type == TYPE_3) {
            codePoint = input.readUnsignedInt(4) << 12
                        | input.readUnsignedInt(6) << 6
                        | input.readUnsignedInt(6);
            requireDecodedCodePoint(codePoint, 0x800);
        } else {
            codePoint = input.readUnsignedInt(3) << 18
                        | input.readUnsignedInt(6) << 12
                        | input.readUnsignedInt(6) << 6
                        | input.readUnsignedInt(6);
            requireDecodedCodePoint(codePoint, 0x10000);
        }
        if (!Character.isValidCodePoint(codePoint) || isSurrogate(codePoint)) {
            throw new IOException("invalid code point: " + codePoint);
        }
        return codePoint;
    }

    private static void requireDecodedCodePoint(final int codePoint, final int minimum) throws IOException {
        if (codePoint < minimum) {
            throw new IOException("non-minimal code point block: " + codePoint);
        }
    }

    private static void writeCodePoint(final BitOutput output, final int codePoint) throws IOException {
        if (!Character.isValidCodePoint(codePoint) || isSurrogate(codePoint)) {
            throw new IllegalArgumentException("invalid code point: " + codePoint);
        }
        if (codePoint <= 0x7F) {
            output.writeUnsignedInt(TYPE_SIZE, TYPE_1);
            output.writeUnsignedInt(7, codePoint);
        } else if (codePoint <= 0x7FF) {
            output.writeUnsignedInt(TYPE_SIZE, TYPE_2);
            output.writeUnsignedInt(5, codePoint >>> 6);
            output.writeUnsignedInt(6, codePoint);
        } else if (codePoint <= 0xFFFF) {
            output.writeUnsignedInt(TYPE_SIZE, TYPE_3);
            output.writeUnsignedInt(4, codePoint >>> 12);
            output.writeUnsignedInt(6, codePoint >>> 6);
            output.writeUnsignedInt(6, codePoint);
        } else {
            output.writeUnsignedInt(TYPE_SIZE, TYPE_4);
            output.writeUnsignedInt(3, codePoint >>> 18);
            output.writeUnsignedInt(6, codePoint >>> 12);
            output.writeUnsignedInt(6, codePoint >>> 6);
            output.writeUnsignedInt(6, codePoint);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    public CompressedUtf8(final int lengthSize) {
        super();
        this.lengthSize = requireValidSizeForUnsignedInt(lengthSize);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String read(final BitInput input) throws IOException {
        requireNonNullInput(input);
        final int length = input.readUnsignedInt(lengthSize);
        final StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.appendCodePoint(readCodePoint(input));
        }
        return builder.toString();
    }

    @Override
    public void write(final BitOutput output, final String value) throws IOException {
        requireNonNullOutput(output);
        final String v = requireNonNullValue(value);
        final int length = codePointCount(v);
        if ((length >>> lengthSize) != 0) {
            throw new IllegalArgumentException(
                    "code point length(" + length + ") requires more than lengthSize(" + lengthSize + ") bits");
        }
        output.writeUnsignedInt(lengthSize, length);
        for (int offset = 0; offset < v.length(); ) {
            final int codePoint = v.codePointAt(offset);
            writeCodePoint(output, codePoint);
            offset += Character.charCount(codePoint);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final int lengthSize;
}
