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


import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;


/**
 * A {@code ByteInput} implementation uses a byte array, a limit, and an index.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 *
 * @see #source
 * @see #limit
 * @see #index
 */
public class ArrayInput extends AbstractByteInput<byte[]> {


    /**
     * Creates a new instance which continuously supplies bytes from specified
     * {@code InputStream}.
     *
     * @param stream the {@code InputStream}
     * @param length the length of the byte array; must be positive.
     *
     * @return a new instance.
     */
    public static ArrayInput newInstance(final InputStream stream,
                                         final int length) {

        if (stream == null) {
            throw new NullPointerException("null stream");
        }

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        return new ArrayInput(null, -1, -1) {

            @Override
            public int read() throws IOException {

                if (source == null) {
                    source = new byte[length];
                    limit = source.length;
                    index = limit;
                }

                if (index == limit) {
                    final int read = stream.read(source);
                    if (read == -1) {
                        throw new EOFException();
                    }
                    limit = read;
                    index = 0;
                }

                return super.read();
            }

        };
    }


    /**
     * Creates a new instance which continuously supplies bytes from specified
     * {@code RandomAccessFile}.
     *
     * @param file the {@code RandomAccessFile}
     * @param length the length of the byte array; must be positive.
     *
     * @return a new instance.
     */
    public static ArrayInput newInstance(final RandomAccessFile file,
                                         final int length) {

        if (file == null) {
            throw new NullPointerException("null file");
        }

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        return new ArrayInput(null, -1, -1) {

            @Override
            public int read() throws IOException {

                if (source == null) {
                    source = new byte[length];
                    limit = source.length;
                    index = limit;
                }

                if (index == limit) {
                    final int read = file.read(source);
                    if (read == -1) {
                        throw new EOFException();
                    }
                    limit = read;
                    index = 0;
                }

                return super.read();
            }

        };
    }


    /**
     * Creates a new instance with given parameters.
     *
     * @param source a byte array
     * @param limit the array index that {@code index} can't exceed
     * @param index array index to read
     */
    public ArrayInput(final byte[] source, final int limit, final int index) {

        super(source);

        this.limit = limit;
        this.index = index;
    }


    /**
     * {@inheritDoc} The {@code read()} method of {@code ArrayInput} class
     * returns {@code source[index]} as an unsigned int while incrementing
     * {@link #index}. Override this method if either
     * {@link #source}, {@link #index}, or {@link #limit} needs to be
     * initialized or adjusted.
     *
     * @return {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     * @throws IndexOutOfBoundsException if {@link #index} is greater than or
     * equals to {@link #limit}.
     */
    @Override
    public int read() throws IOException {

        if (index >= limit) {
            throw new IndexOutOfBoundsException(
                "index(" + index + ") >= limit(" + limit + ")");
        }

        return source[index++] & 0xFF;
    }


    /**
     * Replaces the value of {@link #source} with given and returns this.
     *
     * @param target new value of {@link #source}.
     *
     * @return this instance.
     */
    public ArrayInput source(final byte[] target) {

        setSource(target);

        return this;
    }


    /**
     * Returns the current value of {@link #limit}.
     *
     * @return current value of {@link #limit}.
     */
    public int getLimit() {

        return limit;
    }


    /**
     * Replaces the value of {@link #limit} with given.
     *
     * @param limit new value for {@link #limit}.
     */
    public void setLimit(int limit) {

        this.limit = limit;
    }


    /**
     * Replaces the value of {@link #limit} with given and returns this
     * instance.
     *
     * @param limit new value for {@link #limit}
     *
     * @return this instance.
     */
    public ArrayInput limit(final int limit) {

        setLimit(limit);

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
    public ArrayInput index(final int index) {

        setIndex(index);

        return this;
    }


    /**
     * The index of the {@link #source} which {@link #index} can't exceed.
     */
    protected int limit;


    /**
     * The index in the {@link #source} to read.
     */
    protected int index;

}

