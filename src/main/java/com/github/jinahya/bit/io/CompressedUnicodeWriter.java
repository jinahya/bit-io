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
import static com.github.jinahya.bit.io.CompressedUnicodeUtils.TYPE_4;
import static com.github.jinahya.bit.io.CompressedUnicodeUtils.TYPE_SIZE;
import static com.github.jinahya.bit.io.CompressedUnicodeUtils.isSurrogate;

/**
 * A {@link BitWriter} for writing compressed Unicode strings: a length in code points, followed by each code point as
 * a block type and the payload bits of the corresponding UTF-8 sequence. Obtained via
 * {@link StringWriter#ofCompressedUnicode(int)}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see CompressedUnicodeReader
 * @see CompressedUnicodeUtils
 */
class CompressedUnicodeWriter
        implements BitWriter<String> {

    CompressedUnicodeWriter(final int lengthSize) {
        super();
        this.lengthSize = requireValidSizeForUnsignedInt(lengthSize);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final BitOutput output, final String value) throws IOException {
        Objects.requireNonNull(output, "output is null");
        Objects.requireNonNull(value, "value is null");
        final int length = codePointCount(value);
        if ((length >>> lengthSize) != 0) {
            throw new IllegalArgumentException(
                    "code point length(" + length + ") requires more than lengthSize(" + lengthSize + ") bits");
        }
        output.writeUnsignedInt(lengthSize, length);
        for (int offset = 0; offset < value.length(); ) {
            final int codePoint = value.codePointAt(offset);
            writeCodePoint(output, codePoint);
            offset += Character.charCount(codePoint);
        }
    }

    /**
     * Counts the code points of specified value, rejecting any unpaired surrogate before anything is written.
     *
     * @param value the value.
     * @return the number of code points of {@code value}.
     * @throws IllegalArgumentException if {@code value} contains an unpaired surrogate.
     */
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

    private static void writeCodePoint(final BitOutput output, final int codePoint) throws IOException {
        if (codePoint <= 0x7F) {
            output.writeUnsignedInt(TYPE_SIZE, TYPE_1);
            output.writeUnsignedInt(7, codePoint);
        } else if (codePoint <= 0x7FF) {
            output.writeUnsignedInt(TYPE_SIZE, TYPE_2);
            output.writeUnsignedInt(5, codePoint >>> 6);
            output.writeUnsignedInt(6, codePoint & 0x3F);
        } else if (codePoint <= 0xFFFF) {
            output.writeUnsignedInt(TYPE_SIZE, TYPE_3);
            output.writeUnsignedInt(4, codePoint >>> 12);
            output.writeUnsignedInt(6, (codePoint >>> 6) & 0x3F);
            output.writeUnsignedInt(6, codePoint & 0x3F);
        } else {
            output.writeUnsignedInt(TYPE_SIZE, TYPE_4);
            output.writeUnsignedInt(3, codePoint >>> 18);
            output.writeUnsignedInt(6, (codePoint >>> 12) & 0x3F);
            output.writeUnsignedInt(6, (codePoint >>> 6) & 0x3F);
            output.writeUnsignedInt(6, codePoint & 0x3F);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final int lengthSize;
}
