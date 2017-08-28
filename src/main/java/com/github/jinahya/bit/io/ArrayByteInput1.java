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
 * A {@code ByteInput} implementation uses a byte array.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ArrayByteInput1 extends AbstractByteInput<byte[]> {

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance with given parameters.
     *
     * @param source a byte array; {@code null} if it's supposed to be lazily
     * initialized an set.
     * @param index array index to read
     */
    public ArrayByteInput1(final byte[] source, final int index) {
        super(source);
        this.index = index;
    }

    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc} The {@code read()} method of {@code ArrayByteInput} class
     * returns the value of {@code getSource[getIndex()]} as an unsigned 8-bit
     * value and increments the {@link #index} via {@link #setIndex(int)}.
     * Override this method if either {@link #source} of {@link #index} needs to
     * be lazily initialized or adjusted.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        final int value = getSource()[getIndex()] & 0xFF;
        setIndex(getIndex() + 1);
        return value;
    }

    // ------------------------------------------------------------------ source
    @Override
    public ArrayByteInput1 source(final byte[] target) {
        return (ArrayByteInput1) super.source(target);
    }

    // ------------------------------------------------------------------- index
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
     * @param index new value for {@link #index}
     */
    public void setIndex(final int index) {
        this.index = index;
    }

    /**
     * Replaces the current value of {@link #index} with given and returns this
     * instance.
     *
     * @param index new value for {@link #index}
     * @return this instance.
     * @see #setIndex(int)
     */
    public ArrayByteInput1 index(final int index) {
        setIndex(index);
        return this;
    }

    // -------------------------------------------------------------------------
    /**
     * The index in the {@link #source} to read.
     */
    protected int index;
}
