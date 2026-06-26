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
 * A byte output writes bytes to an array of bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ArrayByteInput
 * @deprecated Prefer {@link BufferByteOutput} over a {@link java.nio.ByteBuffer#wrap(byte[]) wrapped} array, which also
 *         lets the caller {@linkplain java.nio.Buffer#hasRemaining() pre-check} and purge.
 */
@Deprecated
public class ArrayByteOutput
        extends AbstractByteOutput<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with given parameters. The {@code index} attribute will be set as {@code 0}, or {@code -1}
     * when the {@code target}'s {@code length} is {@code 0}.
     *
     * @param target a byte array on which bytes are set; must not be {@code null}.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public ArrayByteOutput(final byte[] target) {
        super(target);
        this.index = target.length == 0 ? -1 : 0;
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
     * {@inheritDoc} The {@code write(int)} method of {@code ArrayByteOutput} class sets
     * {@link #target target}[{@code index}] with specified {@code value} and increases the {@code index} by {@code 1}.
     *
     * @param value {@inheritDoc}
     * @throws IOException                    {@inheritDoc}
     * @throws ArrayIndexOutOfBoundsException if the {@code index} is out of the {@code target}'s bounds; that is, when
     *                                        the {@code target} has been filled (or was empty).
     * @see ArrayByteInput#read()
     */
    @Override
    public void write(final int value) throws IOException {
        target[index++] = (byte) value;
    }

    // ----------------------------------------------------------------------------------------------------------- index

    /**
     * Returns the current value of the {@code index} attribute; the position in the {@link #target target} to which the
     * next byte is written.
     *
     * @return the current value of the {@code index} attribute.
     */
    public int getIndex() {
        return index;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The index in the {@code target} to write.
     */
    int index;
}
