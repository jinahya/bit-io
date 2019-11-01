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
 * <p>
 * Note that this implementation only tracks a single {@link #getIndex() index} as a next position to read in the
 * backing array which means there is no way to limit the maximum value of the {@code index} in the backing array. Use
 * {@link BufferByteInput} or {@link StreamByteInput} for a way of continuously supplying bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ArrayByteOutput
 */
public class ArrayByteInput extends AbstractByteInput<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given arguments. The {@link #getIndex() index} attribute will be set as {@code 0}, or
     * {@code -1} when {@code source} is {@code null} or its {@code length} is {@code 0}. It's crucial to set the {@link
     * #setIndex(int) index} attribute when the {@link #setSource(Object) source} attribute is lazily initialized.
     *
     * <blockquote><pre>{@code
     * final ByteInput byteInput = new ArrayByteInput(null) { // index = -1
     *     //_at_Override
     *     public int read() throws IOException {
     *         if (getSource() == null) {
     *             setSource(new byte[16]);
     *             setIndex(getSource().length); // set as if it's already drained
     *         }
     *         if (getIndex() == getSource().length) { // no more space to read; charge it.
     *             readFully(getSource());
     *             setIndex(0);
     *         }
     *         return super.read();
     *     }
     * }
     * }</pre></blockquote>
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
     * {@inheritDoc} The {@code read()} method of {@code ArrayByteInput} class returns the value at {@link #getIndex()
     * index} in {@link #getSource() source} as an unsigned 8-bit value. The {@link #setIndex(int) index} attribute,
     * when successfully returns, is increased by {@code 1}.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        final int index = getIndex();
        final int result = getSource()[index] & 0xFF;
        setIndex(index + 1);
        return result;
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
