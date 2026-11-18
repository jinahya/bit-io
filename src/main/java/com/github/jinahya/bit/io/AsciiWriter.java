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

import java.io.IOException;

/**
 * A {@link BitWriter} for writing compressed ({@value java.lang.Byte#SIZE}{@code  - 1}-bit element) {@code US-ASCII}
 * strings. It encodes the string in {@code US-ASCII} and writes the bytes through an unsigned {@link ByteArrayWriter}
 * delegate (each element a {@value java.lang.Byte#SIZE}{@code  - 1}-bit unsigned value). Obtained via
 * {@link StringWriter#ofAscii(int)}, which replaces each unmappable character with {@code '?'}, or via
 * {@link StringWriter#ofAsciiStrict(int)}, which rejects a value containing any unmappable character.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see AsciiReader
 */
class AsciiWriter
        implements BitWriter<String> {

    AsciiWriter(final int lengthSize, final boolean strict) {
        super();
        this.delegate = new ByteArrayWriter.Unsigned7(lengthSize); // 7-bit unsigned elements
        this.strict = strict;
    }

    @Override
    public void write(final BitOutput output, final String value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (strict) {
            for (int i = 0; i < value.length(); i++) {
                final char c = value.charAt(i);
                if (c > 0x7F) {
                    throw new IllegalArgumentException(
                            "value contains a non-ASCII character(\\u" + Integer.toHexString(c | 0x10000).substring(1)
                            + ") at index " + i);
                }
            }
        }
        delegate.write(output, value.getBytes(BitIoConstants.US_ASCII));
    }

    private final ByteArrayWriter delegate;

    /**
     * Indicates whether a value containing any non-ASCII character is rejected, rather than replaced with {@code '?'}.
     */
    private final boolean strict;
}
