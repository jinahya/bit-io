/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */
package com.github.jinahya.bit.io;

import java.io.IOException;

/**
 * A {@code ByteInput} implementation uses a byte array and an index.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ArrayByteInput extends AbstractByteInput<byte[]> {

    /**
     * Creates a new instance with given parameters.
     *
     * @param source a byte array
     * @param index array index to read
     */
    public ArrayByteInput(final byte[] source, final int index) {
        super(source);
        this.index = index;
    }

    /**
     * {@inheritDoc} The {@code read()} method of {@code ArrayByteInput} class
     * returns {@code source[index]} as an unsigned int and increments
     * {@link #index}. Override this method if either {@link #source}, or
     * {@link #index} needs to be lazily initialized or adjusted.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        return source[index++] & 0xFF;
    }

    /**
     * Replaces the value of {@link #source} with given and returns this.
     *
     * @param target new value of {@link #source}.
     * @return this instance.
     */
    public ArrayByteInput source(final byte[] target) {
        setSource(target);
        return this;
    }

    /**
     * Returns the current value of {@link #index}.
     *
     * @return current value of {@link #index}.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Replaces the current value of {@link #index} with given.
     *
     * @param index new value for {@link #index}.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Replaces the current value of {@link #index} with given and returns this
     * instance.
     *
     * @param index new value for {@link #index}
     *
     * @return this instance.
     */
    public ArrayByteInput index(final int index) {
        setIndex(index);
        return this;
    }

    /**
     * The index in the {@link #source} to read.
     */
    protected int index;
}
