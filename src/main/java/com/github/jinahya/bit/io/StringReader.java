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
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * A {@link BitReader} for reading {@code String} values encoded as a length-prefixed array of full bytes in a named
 * charset. It reads the bytes through a {@link ByteArrayReader} delegate and decodes them with the charset.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see StringWriter
 */
public class StringReader
        implements BitReader<String> {

    /**
     * Returns a new reader that reads a compressed ({@value java.lang.Byte#SIZE}{@code  - 1}-bit element)
     * {@code US-ASCII} string.
     *
     * @param lengthSize the number of bits for the (byte) length; between {@code 1} and
     *                   ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @return a new ASCII reader.
     * @see StringWriter#ofAscii(int)
     */
    public static BitReader<String> ofAscii(final int lengthSize) {
        return new AsciiReader(lengthSize);
    }

    /**
     * Returns a new reader that reads a compressed Unicode string, written by
     * {@link StringWriter#ofCompressedUnicode(int)}.
     *
     * <p>Each code point is stored as a 2-bit block type followed by its payload bits; {@code 9} bits for
     * {@code U+0000} through {@code U+007F}, {@code 13} bits through {@code U+07FF}, {@code 18} bits through
     * {@code U+FFFF}, and {@code 23} bits through {@code U+10FFFF}; the payload bits of UTF-8, with a block type in
     * place of its marker bits. The format is specific to this library; it is neither UTF-8 nor compatible with
     * {@link #ofAscii(int)}. For text known to be {@code US-ASCII}, {@link #ofAscii(int)} is smaller ({@code 7} bits
     * per character).</p>
     *
     * @param lengthSize the number of bits for the length, in code points; between {@code 1} and
     *                   ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @return a new compressed Unicode reader.
     * @see StringWriter#ofCompressedUnicode(int)
     */
    public static BitReader<String> ofCompressedUnicode(final int lengthSize) {
        return new CompressedUnicodeReader(lengthSize);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance reading a length-prefixed array of full bytes decoded in specified charset.
     *
     * @param lengthSize the number of bits for the (byte) length; between {@code 1} and
     *                   ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @param charset    the charset for decoding; must not be {@code null}; e.g.,
     *                   {@link java.nio.charset.StandardCharsets#UTF_8}.
     * @throws NullPointerException     if {@code charset} is {@code null}.
     * @throws IllegalArgumentException if {@code lengthSize} is not valid.
     * @see StringWriter#StringWriter(int, Charset)
     */
    public StringReader(final int lengthSize, final Charset charset) {
        super();
        Objects.requireNonNull(charset, "charset is null");
        this.delegate = new ByteArrayReader.Signed8(lengthSize);
        this.charset = charset;
    }

    /**
     * Creates a new instance reading a length-prefixed array of full bytes decoded in the charset of specified name.
     * The name is resolved immediately, with {@link Charset#forName(String)}.
     *
     * @param lengthSize  the number of bits for the (byte) length; between {@code 1} and
     *                    ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @param charsetName the name of the charset for decoding; must not be {@code null}.
     * @throws NullPointerException                         if {@code charsetName} is {@code null}.
     * @throws java.nio.charset.IllegalCharsetNameException if {@code charsetName} is not a legal charset name.
     * @throws java.nio.charset.UnsupportedCharsetException if the charset of {@code charsetName} is not supported.
     * @throws IllegalArgumentException                     if {@code lengthSize} is not valid.
     * @deprecated Use {@link #StringReader(int, Charset)} instead; this constructor will be removed in the next major
     *         version.
     */
    @Deprecated(forRemoval = true)
    public StringReader(final int lengthSize, final String charsetName) {
        this(lengthSize, charset(charsetName));
    }

    /**
     * Returns the charset of specified name.
     *
     * @param charsetName the name of the charset.
     * @return the charset named {@code charsetName}.
     * @throws NullPointerException if {@code charsetName} is {@code null}.
     */
    static Charset charset(final String charsetName) {
        Objects.requireNonNull(charsetName, "charsetName is null");
        return Charset.forName(charsetName);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read(BitInput)} method of {@code StringReader} class reads the bytes through a
     * {@link ByteArrayReader} delegate (with an {@code elementSize} of {@value java.lang.Byte#SIZE}) and decodes them
     * with this reader's charset; malformed input and unmappable characters are replaced, as
     * {@link String#String(byte[], Charset)} does.
     *
     * @param input {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException if {@code input} is {@code null}.
     * @throws IOException          {@inheritDoc}
     */
    @Override
    public String read(final BitInput input) throws IOException {
        Objects.requireNonNull(input, "input is null");
        return new String(delegate.read(input), charset);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final ByteArrayReader delegate;

    private final Charset charset;
}
