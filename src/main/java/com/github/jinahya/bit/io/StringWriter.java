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
 * A {@link BitWriter} for writing {@code String} values encoded as a length-prefixed array of full bytes in a named
 * charset. It encodes the string with the charset and writes the bytes through a {@link ByteArrayWriter} delegate.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StringReader
 */
public class StringWriter
        implements BitWriter<String> {

    /**
     * Returns a new writer that writes a compressed ({@value java.lang.Byte#SIZE}{@code  - 1}-bit element)
     * {@code US-ASCII} string.
     *
     * <p>Values must consist of {@code US-ASCII} characters ({@code U+0000} through {@code U+007F}); making sure of it
     * is the caller's responsibility. The returned writer encodes values with
     * {@link String#getBytes(java.nio.charset.Charset)}, which <em>silently replaces</em> each other character
     * (including an unpaired surrogate) with {@code '?'}; e.g., {@code "é"} is read back as {@code "?"},
     * indistinguishable from a literal question mark. For text which may contain other characters, use
     * {@link #ofCompressedUnicode(int)}, or an instance for the {@code UTF-8} charset, instead.</p>
     *
     * @param lengthSize the number of bits for the (byte) length; between {@code 1} and
     *                   ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @return a new ASCII writer.
     * @see StringReader#ofAscii(int)
     */
    public static BitWriter<String> ofAscii(final int lengthSize) {
        return new AsciiWriter(lengthSize);
    }


    /**
     * Returns a new writer that writes a compressed Unicode string, readable with
     * {@link StringReader#ofCompressedUnicode(int)}.
     *
     * <p>Each code point is stored as a 2-bit block type followed by its payload bits; {@code 9} bits for
     * {@code U+0000} through {@code U+007F}, {@code 13} bits through {@code U+07FF}, {@code 18} bits through
     * {@code U+FFFF}, and {@code 23} bits through {@code U+10FFFF}; the payload bits of UTF-8, with a block type
     * in place of its marker bits. The format is specific to this library; it is neither UTF-8 nor compatible
     * with {@link #ofAscii(int)}. For text known to be {@code US-ASCII},
     * {@link #ofAscii(int)} is smaller ({@code 7} bits per character).</p>
     *
     * <p>The {@link BitWriter#write(BitOutput, Object) write} method of the returned writer throws an
     * {@link IllegalArgumentException} for a value with an unpaired surrogate, or whose number of code points requires
     * more than {@code lengthSize} bits.</p>
     *
     * @param lengthSize the number of bits for the length, in code points; between {@code 1} and
     *                   ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @return a new compressed Unicode writer.
     * @see StringReader#ofCompressedUnicode(int)
     */
    public static BitWriter<String> ofCompressedUnicode(final int lengthSize) {
        return new CompressedUnicodeWriter(lengthSize);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance writing a length-prefixed array of full bytes encoded in specified charset.
     *
     * @param lengthSize  the number of bits for the (byte) length; between {@code 1} and
     *                    ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @param charsetName the name of the charset for encoding; must not be {@code null}.
     * @throws NullPointerException     if {@code charsetName} is {@code null}.
     * @throws IllegalArgumentException if {@code lengthSize} is not valid.
     */
    public StringWriter(final int lengthSize, final String charsetName) {
        super();
        if (charsetName == null) {
            throw new NullPointerException("charsetName is null");
        }
        this.delegate = new ByteArrayWriter.Signed8(lengthSize);
        this.charsetName = charsetName;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(BitOutput, String)} method of {@code StringWriter} class encodes {@code value}
     * with this writer's {@code charsetName} and writes the bytes through a {@link ByteArrayWriter} delegate (with an
     * {@code elementSize} of {@value java.lang.Byte#SIZE}).
     *
     * @param output {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws NullPointerException     if {@code output} or {@code value} is {@code null}.
     * @throws IllegalArgumentException if the {@code charsetName}-encoded byte length does not fit in
     *                                  {@code lengthSize} bits.
     * @throws IOException              {@inheritDoc} (including an unsupported {@code charsetName}).
     */
    @Override
    public void write(final BitOutput output, final String value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        delegate.write(output, value.getBytes(charsetName));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final ByteArrayWriter delegate;

    private final String charsetName;
}
