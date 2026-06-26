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
 * A byte input reads bytes from an array of bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayByteOutput
 * @deprecated Prefer {@link BufferByteInput} over a {@link java.nio.ByteBuffer#wrap(byte[]) wrapped} array, which also
 *         lets the caller {@linkplain java.nio.Buffer#hasRemaining() pre-check} and refill.
 */
@Deprecated
public class ArrayByteInput
        extends AbstractByteInput<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given arguments. The {@code index} attribute will be set as {@code 0}, or {@code -1}
     * when the {@code source}'s {@code length} is {@code 0}.
     *
     * @param source a byte array from which bytes are read; must not be {@code null}.
     * @throws NullPointerException if {@code source} is {@code null}.
     */
    public ArrayByteInput(final byte[] source) {
        super(source);
        this.index = source.length == 0 ? -1 : 0;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return super.toString() + "{"
               + "index=" + index
               + "}";
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code ArrayByteInput} class returns
     * {@link #source source}[{@code index}] as an unsigned 8-bit value and increases the {@code index} by {@code 1}.
     *
     * @return {@inheritDoc}
     * @throws IOException                    {@inheritDoc}
     * @throws ArrayIndexOutOfBoundsException if the {@code index} is out of the {@code source}'s bounds; that is, when
     *                                        the {@code source} has been exhausted (or was empty).
     * @see ArrayByteOutput#write(int)
     */
    @Override
    public int read() throws IOException {
        return source[index++] & 0xFF;
    }

    // ----------------------------------------------------------------------------------------------------------- index

    /**
     * Returns the current value of the {@code index} attribute; the position in the {@link #source source} from which
     * the next byte is read.
     *
     * @return the current value of the {@code index} attribute.
     */
    public int getIndex() {
        return index;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The index in the {@code source} to read.
     */
    int index;
}
