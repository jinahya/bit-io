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
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ArrayByteOutput
 */
public class ArrayByteInput extends AbstractByteInput<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given arguments. The {@link #getIndex() index} attribute will be set as {@code 0}, or
     * {@code -1} when {@code source} is {@code null} or its {@code length} is {@code 0}.
     *
     * @param source a byte array from which bytes are read; {@code null} if it's supposed to be lazily initialized an
     *               set.
     */
    public ArrayByteInput(final byte[] source) {
        super(source);
        this.index = source == null || source.length == 0 ? -1 : 0;
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
     * {@inheritDoc} The {@code read()} method of {@code ArrayByteInput} class returns {@link #getSource()
     * source}[{@link #getIndex() index}] as an unsigned 8-bit value. The {@link #setIndex(int) index} attribute, when
     * successfully returns, is increased by {@code 1}.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        return getSource()[getIndexAndIncrement()] & 0xFF;
    }

    // ---------------------------------------------------------------------------------------------------------- source
    @Override
    protected byte[] getSource() {
        return super.getSource();
    }

    @Override
    protected void setSource(final byte[] source) {
        super.setSource(source);
    }

    // ----------------------------------------------------------------------------------------------------------- index

    /**
     * Returns the current value of {@code index} attribute.
     *
     * @return current value of {@code index} attribute.
     */
    protected int getIndex() {
        return index;
    }

    /**
     * Returns the current value of {@code index} attribute and increments it by {@code 1}.
     *
     * @return the current value of {@code index} attribute.
     */
    int getIndexAndIncrement() {
        final int result = getIndex();
        setIndex(result + 1);
        return result;
    }

    /**
     * Replaces the current value of {@code index} attribute with given.
     *
     * @param index new value for {@code index} attribute.
     */
    protected void setIndex(final int index) {
        this.index = index;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The index in the {@code source} to read.
     */
    private int index;
}
