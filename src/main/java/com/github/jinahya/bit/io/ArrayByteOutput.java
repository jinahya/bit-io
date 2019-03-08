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
import java.io.OutputStream;

/**
 * A byte output writes byte to an array of bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ArrayByteOutput extends AbstractByteOutput<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance of byte output which writes bytes to given output stream using an array of bytes whose
     * {@code length} equals to specified.
     *
     * @param length the length of byte array; must be positive.
     * @param stream the output stream to which bytes are written; must be not {@code null}.
     * @return an instance byte output.
     */
    @SuppressWarnings({"Duplicates"})
    public static ArrayByteOutput of(final int length, final OutputStream stream) {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }
        if (stream == null) {
            throw new NullPointerException("stream is null");
        }
        return new ArrayByteOutput(null) {

            @Override
            public void write(final int value) throws IOException {
                if (target == null) {
                    target = new byte[length];
                    index = 0;
                }
                super.write(value);
                if (index == target.length) {
                    stream.write(target);
                    index = 0;
                }
            }

            @Override
            public void setTarget(final byte[] target) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setIndex(final int index) {
                throw new UnsupportedOperationException();
            }
        };
    }

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

    /**
     * Replaces the {@code index} with given and returns self.
     *
     * @param index new value for {@code index}
     * @return this instance.
     */
    public ArrayByteOutput index(final int index) {
        setIndex(index);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The index in the {@code target} to write.
     */
    int index;
}
