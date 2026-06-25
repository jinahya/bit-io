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
     * @param lengthSize the number of bits for the (byte) length; between {@code 1} and
     *                   ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @return a new ASCII writer.
     * @see StringReader#ofAscii(int)
     */
    public static BitWriter<String> ofAscii(final int lengthSize) {
        return new AsciiWriter(lengthSize);
    }

    /**
     * Creates a new instance writing a length-prefixed array of full bytes encoded in specified charset.
     *
     * @param lengthSize  the number of bits for the (byte) length; between {@code 1} and
     *                    ({@value java.lang.Integer#SIZE} - {@code 1}), both inclusive.
     * @param charsetName the name of the charset for encoding; must not be {@code null}.
     * @throws NullPointerException if {@code charsetName} is {@code null}.
     */
    public StringWriter(final int lengthSize, final String charsetName) {
        super();
        if (charsetName == null) {
            throw new NullPointerException("charsetName is null");
        }
        this.delegate = ByteArrayWriter.ofSigned(lengthSize, Byte.SIZE);
        this.charsetName = charsetName;
    }

    /**
     * {@inheritDoc} The {@code write(BitOutput, String)} method of {@code StringWriter} class encodes {@code value}
     * with this writer's {@code charsetName} and writes the bytes through a {@link ByteArrayWriter} delegate (with an
     * {@code elementSize} of {@value java.lang.Byte#SIZE}).
     *
     * @param output {@inheritDoc}
     * @param value  {@inheritDoc}
     * @throws NullPointerException     if {@code output} or {@code value} is {@code null}.
     * @throws IllegalArgumentException if this writer's {@code lengthSize} is not valid.
     * @throws IOException              {@inheritDoc} (including an unsupported {@code charsetName}).
     */
    @Override
    public void write(final BitOutput output, final String value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        delegate.write(output, value.getBytes(charsetName));
    }

    private final ByteArrayWriter delegate;

    private final String charsetName;
}
