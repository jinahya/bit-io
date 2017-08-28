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
 * A {@code ByteOutput} implementation uses a byte array.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ArrayByteOutput1 extends AbstractByteOutput<byte[]> {

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance with given parameters.
     *
     * @param target a byte array
     * @param index array index to write
     */
    public ArrayByteOutput1(final byte[] target, final int index) {
        super(target);
        this.index = index;
    }

    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc} The {@code write(int)} method of {@code ArrayByteOutput}
     * sets {@code getTarget()[getIndex()]} with given value and increments the
     * {@link #index} using {@link #setIndex(int)}. Override this method if
     * either {@link #target} or {@link #index} needs to be lazily initialized
     * or adjusted.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @throws IllegalStateException if {@link #index} is equal to or greater
     * than {@link #limit}
     */
    @Override
    public void write(final int value) throws IOException {
        getTarget()[getIndex()] = (byte) value;
        setIndex(getIndex() + 1);
    }

    // ------------------------------------------------------------------ target
    /**
     * Replaces the {@link #target} with given and returns self.
     *
     * @param target new value for {@link #target}
     * @return this instance.
     */
    @Override
    public ArrayByteOutput1 target(final byte[] target) {
        return (ArrayByteOutput1) super.target(target);
    }

    // ------------------------------------------------------------------- index
    /**
     * Returns the current value of {@link #index}
     *
     * @return the current value of {@link #index}.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Replaces the {@link #index} with given.
     *
     * @param index new value for {@link #index}
     */
    public void setIndex(final int index) {
        this.index = index;
    }

    /**
     * Replaces the {@link #index} with given and returns self.
     *
     * @param index new value for {@link #index}
     * @return this instance.
     */
    public ArrayByteOutput1 index(final int index) {
        setIndex(index);
        return this;
    }

    // -------------------------------------------------------------------------
    /**
     * The index in the {@link #target} to write.
     */
    protected int index;
}
