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

/**
 * Constants and utilities shared by {@link CompressedUnicodeReader} and {@link CompressedUnicodeWriter}.
 *
 * <p>Each code point is a {@value #TYPE_SIZE}-bit block type followed by the payload bits of the corresponding UTF-8
 * sequence, without its marker bits:</p>
 * <table>
 * <caption>Block types</caption>
 * <tr><th>Type</th><th>Code points</th><th>Payload bits</th></tr>
 * <tr><td>{@value #TYPE_1}</td><td>{@code U+0000} - {@code U+007F}</td><td>{@code 7}</td></tr>
 * <tr><td>{@value #TYPE_2}</td><td>{@code U+0080} - {@code U+07FF}</td><td>{@code 5 + 6}</td></tr>
 * <tr><td>{@value #TYPE_3}</td><td>{@code U+0800} - {@code U+FFFF}</td><td>{@code 4 + 6 + 6}</td></tr>
 * <tr><td>{@value #TYPE_4}</td><td>{@code U+10000} - {@code U+10FFFF}</td><td>{@code 3 + 6 + 6 + 6}</td></tr>
 * </table>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StringReader#ofCompressedUnicode(int)
 * @see StringWriter#ofCompressedUnicode(int)
 */
final class CompressedUnicodeUtils {

    /**
     * The number of bits of a block type. The value is {@value}.
     */
    static final int TYPE_SIZE = 2;

    /**
     * The block type of a code point of 7 payload bits. The value is {@value}.
     */
    static final int TYPE_1 = 0;

    /**
     * The block type of a code point of 11 payload bits. The value is {@value}.
     */
    static final int TYPE_2 = 1;

    /**
     * The block type of a code point of 16 payload bits. The value is {@value}.
     */
    static final int TYPE_3 = 2;

    /**
     * The block type of a code point of 21 payload bits. The value is {@value}.
     */
    static final int TYPE_4 = 3;

    /**
     * Checks whether specified code point is a surrogate code point.
     *
     * @param codePoint the code point to check.
     * @return {@code true} if {@code codePoint} is a surrogate code point; {@code false} otherwise.
     */
    static boolean isSurrogate(final int codePoint) {
        return codePoint >= Character.MIN_SURROGATE && codePoint <= Character.MAX_SURROGATE;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private CompressedUnicodeUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
