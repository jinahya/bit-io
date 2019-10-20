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
 * A byte input reading bytes from an array of bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ArrayByteOutput
 */
public class ArrayByteInput extends AbstractByteInput<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given arguments.
     *
     * @param source a byte array; {@code null} if it's supposed to be lazily initialized an set.
     */
    public ArrayByteInput(final byte[] source) {
        super(source);
        this.index = source == null || source.length == 0 ? -1 : 0;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code ArrayByteInput} class returns {@code source[index++]} as an
     * unsigned 8-bit value.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        return getSource()[index++] & 0xFF;
    }

    // ----------------------------------------------------------------------------------------------------------- index

    /**
     * Returns the current value of {@code index}.
     *
     * @return current value of {@code index}.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Replaces the current value of {@code index} with given.
     *
     * @param index new value for {@code index}
     */
    public void setIndex(final int index) {
        this.index = index;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The index in the {@code source} to read.
     */
    private int index;
}
