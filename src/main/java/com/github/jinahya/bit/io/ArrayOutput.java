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
import java.io.OutputStream;
import java.io.RandomAccessFile;


/**
 * A {@code ByteOutput} implementation uses a byte array.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ArrayOutput extends AbstractByteOutput<byte[]> {


    public static ArrayOutput newInstance(final OutputStream stream,
                                          final int length) {

        if (stream == null) {
            throw new NullPointerException("null stream");
        }

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        return new ArrayOutput(null, -1, -1) {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                if (target == null) {
                    target = new byte[length];
                    index = 0;
                    limit = target.length;
                }

                super.writeUnsignedByte(value);

                if (index == limit) {
                    stream.write(target);
                    index = 0;
                }
            }

        };
    }


    public static ArrayOutput newInstance(final RandomAccessFile file,
                                          final int length) {

        if (file == null) {
            throw new NullPointerException("null file");
        }

        if (length <= 0) {
            throw new IllegalArgumentException("length(" + length + ") <= 0");
        }

        return new ArrayOutput(null, -1, -1) {

            @Override
            public void writeUnsignedByte(final int value) throws IOException {

                if (target == null) {
                    target = new byte[length];
                    index = 0;
                    limit = target.length;
                }

                super.writeUnsignedByte(value);

                if (index == limit) {
                    file.write(target);
                    index = 0;
                }
            }

        };
    }


    /**
     * Creates a new instance with given parameters.
     *
     * @param target a byte array
     * @param limit the array index that {@code index} can't exceed
     * @param index array index to write
     */
    public ArrayOutput(final byte[] target, final int limit, final int index) {

        super(target);

        this.limit = limit;
        this.index = index;
    }


    /**
     * {@inheritDoc} The {@code writeUnsignedByte(int)} method of
     * {@code ArrayOutput} class executes following statement.
     * <blockquote><pre>target[index++] = (byte) value</pre></blockquote>
     * Override this method if either {@link #target}, {@link #index}, or
     * {@link #limit} needs to be initialized or adjusted.
     *
     * @param value {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     * @throws IndexOutOfBoundsException if {@link #index} is greater than or
     * equals to {@link #limit}.
     */
    @Override
    public void writeUnsignedByte(final int value) throws IOException {

        if (index >= limit) {
            throw new IndexOutOfBoundsException(
                "index(" + index + ") >= limit(" + limit + ")");
        }

        target[index++] = (byte) value;
    }


    public ArrayOutput target(final byte[] target) {

        setTarget(target);

        return this;
    }


    public int getLimit() {

        return limit;
    }


    public void setLimit(final int limit) {

        this.limit = limit;
    }


    public ArrayOutput limit(final int limit) {

        setLimit(limit);

        return this;
    }


    public int getIndex() {

        return index;
    }


    public void setIndex(final int index) {

        this.index = index;
    }


    public ArrayOutput index(final int index) {

        setIndex(index);

        return this;
    }


    /**
     * The index of the {@link #target} which {@link #index} can't exceed.
     */
    protected int limit;


    /**
     * The index in the {@link #target} to write.
     */
    protected int index;

}

