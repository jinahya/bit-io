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
 * A byte output writes byte to an array of bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ArrayByteInput
 */
public class ArrayByteOutput extends AbstractByteOutput<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given parameters. The {@code index} attribute will be set as {@code 0}, or {@code -1}
     * when {@code target} argument is {@code null} or its {@code length} is {@code 0}.
     *
     * @param target a byte array on which bytes are set; {@code null} if it's supposed to be lazily initialized an
     *               set.
     */
    public ArrayByteOutput(final byte[] target) {
        super(target);
        this.index = target == null || target.length == 0 ? -1 : 0;
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
     * {@inheritDoc} The {@code write(int)} method of {@code ArrayByteOutput} class sets {@link #getTarget()
     * target}[{@link #getIndex() index}] with specified {@code value}. The {@link #setIndex(int) index} attribute, when
     * successfully returns, is increased by {@code 1}.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void write(final int value) throws IOException {
        getTarget()[getIndexAndIncrement()] = (byte) value;
    }

    // ---------------------------------------------------------------------------------------------------------- target
    @Override
    protected byte[] getTarget() {
        return super.getTarget();
    }

    @Override
    protected void setTarget(final byte[] target) {
        super.setTarget(target);
    }

    // ----------------------------------------------------------------------------------------------------------- index

    /**
     * Returns the current value of {@code index} attribute.
     *
     * @return the current value of {@code index} attribute.
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
     * Replaces the {@code index} attribute with given value.
     *
     * @param index new value for {@code index} attribute.
     */
    protected void setIndex(final int index) {
        this.index = index;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The index in the {@code target} to write.
     */
    private int index;
}
