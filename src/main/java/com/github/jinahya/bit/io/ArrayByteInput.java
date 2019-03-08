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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A byte input reading bytes from an array of bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ArrayByteInput extends AbstractByteInput<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance of {@link ArrayByteInput} which reads bytes from given input stream using an array of
     * bytes whose {@code length} equals to specified.
     *
     * @param length the length of the byte array; must be positive.
     * @param stream the input stream from which bytes are read; must be not {@code null}.
     * @return a new instance of {@link ArrayByteInput}.
     */
    @SuppressWarnings({"Duplicates"})
    public static ArrayByteInput of(final int length, final InputStream stream) {
        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }
        if (stream == null) {
            throw new NullPointerException("stream is null");
        }
        return new ArrayByteInput(null) {

            @Override
            public int read() throws IOException {
                if (source == null) {
                    source = new byte[length];
                    index = source.length;
                }
                if (index == source.length) {
                    final int read = stream.read(source);
                    if (read == -1) {
                        throw new EOFException();
                    }
                    assert read > 0; // source.length > 0
                    index = 0;
                }
                return super.read();
            }

            @Override
            public void setSource(final byte[] source) {
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
        return source[index++] & 0xFF;
    }

    // ---------------------------------------------------------------------------------------------------------- source
    @Override
    public ArrayByteInput source(final byte[] target) {
        return (ArrayByteInput) super.source(target);
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

    /**
     * Replaces the current value of {@code index} with given and returns this instance.
     *
     * @param index new value for {@code index}
     * @return this instance.
     * @see #setIndex(int)
     */
    public ArrayByteInput index(final int index) {
        setIndex(index);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The index in the {@code source} to read.
     */
    int index;
}
