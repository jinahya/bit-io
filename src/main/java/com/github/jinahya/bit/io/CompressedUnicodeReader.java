package com.github.jinahya.bit.io;

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

import java.io.IOException;
import java.util.Objects;

import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForUnsignedInt;
import static com.github.jinahya.bit.io.CompressedUnicodeUtils.TYPE_1;
import static com.github.jinahya.bit.io.CompressedUnicodeUtils.TYPE_2;
import static com.github.jinahya.bit.io.CompressedUnicodeUtils.TYPE_3;
import static com.github.jinahya.bit.io.CompressedUnicodeUtils.TYPE_SIZE;
import static com.github.jinahya.bit.io.CompressedUnicodeUtils.isSurrogate;

/**
 * A {@link BitReader} for reading compressed Unicode strings: a length in code points, followed by each code point as
 * a block type and the payload bits of the corresponding UTF-8 sequence. Obtained via
 * {@link StringReader#ofCompressedUnicode(int)}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see CompressedUnicodeWriter
 * @see CompressedUnicodeUtils
 */
class CompressedUnicodeReader
        implements BitReader<String> {

    CompressedUnicodeReader(final int lengthSize) {
        super();
        this.lengthSize = requireValidSizeForUnsignedInt(lengthSize);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String read(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        final int length = input.readUnsignedInt(lengthSize);
        final StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.appendCodePoint(readCodePoint(input));
        }
        return builder.toString();
    }

    private static int readCodePoint(final BitInput input) throws IOException {
        final int type = input.readUnsignedInt(TYPE_SIZE);
        final int codePoint;
        if (type == TYPE_1) {
            codePoint = input.readUnsignedInt(7);
        } else if (type == TYPE_2) {
            codePoint = input.readUnsignedInt(5) << 6
                        | input.readUnsignedInt(6);
            requireMinimal(codePoint, 0x80);
        } else if (type == TYPE_3) {
            codePoint = input.readUnsignedInt(4) << 12
                        | input.readUnsignedInt(6) << 6
                        | input.readUnsignedInt(6);
            requireMinimal(codePoint, 0x800);
        } else {
            codePoint = input.readUnsignedInt(3) << 18
                        | input.readUnsignedInt(6) << 12
                        | input.readUnsignedInt(6) << 6
                        | input.readUnsignedInt(6);
            requireMinimal(codePoint, 0x10000);
        }
        if (!Character.isValidCodePoint(codePoint) || isSurrogate(codePoint)) {
            throw new IOException("invalid code point: " + codePoint);
        }
        return codePoint;
    }

    private static void requireMinimal(final int codePoint, final int minimum) throws IOException {
        if (codePoint < minimum) {
            throw new IOException("non-minimal code point block: " + codePoint);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final int lengthSize;
}
