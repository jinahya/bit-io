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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance reading a length-prefixed array of full bytes decoded in specified charset.
     *
     * @param lengthSize  the number of bits for the (byte) length; between {@code 1} and
     *                    ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @param charsetName the name of the charset for decoding; must not be {@code null}.
     * @throws NullPointerException     if {@code charsetName} is {@code null}.
     * @throws IllegalArgumentException if {@code lengthSize} is not valid.
     */
    public StringReader(final int lengthSize, final String charsetName) {
        super();
        if (charsetName == null) {
            throw new NullPointerException("charsetName is null");
        }
        this.delegate = ByteArrayReader.ofSigned(lengthSize, Byte.SIZE);
        this.charsetName = charsetName;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read(BitInput)} method of {@code StringReader} class reads the bytes through a
     * {@link ByteArrayReader} delegate (with an {@code elementSize} of {@value java.lang.Byte#SIZE}) and decodes them
     * with this reader's {@code charsetName}.
     *
     * @param input {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException if {@code input} is {@code null}.
     * @throws IOException          {@inheritDoc} (including an unsupported {@code charsetName}).
     */
    @Override
    public String read(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        return new String(delegate.read(input), charsetName);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final ByteArrayReader delegate;

    private final String charsetName;
}
