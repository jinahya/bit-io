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
 */
public class ArrayByteOutput extends AbstractByteOutput<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given parameters.
     *
     * @param target a byte array
     */
    public ArrayByteOutput(final byte[] target) {
        super(target);
        this.index = target == null || target.length == 0 ? -1 : 0;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code ArrayByteOutput} class sets {@code target[index++]} with
     * given value.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void write(final int value) throws IOException {
        target[index++] = (byte) value;
    }

    // ---------------------------------------------------------------------------------------------------------- target

    /**
     * Replaces the {@code target} with given and returns self.
     *
     * @param target new value for {@code target}
     * @return this instance.
     */
    @Override
    public ArrayByteOutput target(final byte[] target) {
        return (ArrayByteOutput) super.target(target);
    }

    // ----------------------------------------------------------------------------------------------------------- index

    /**
     * Returns the current value of {@code index}.
     *
     * @return the current value of {@code index}.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Replaces the {@code index} with given.
     *
     * @param index new value for {@code index}
     */
    public void setIndex(final int index) {
        this.index = index;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The index in the {@code target} to write.
     */
    int index;
}
