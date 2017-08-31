/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
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
package com.github.jinahya.bit.io.ext;

import com.github.jinahya.bit.io.ArrayByteInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <T> stream type parameter
 */
public class BufferedStreamByteInput<T extends InputStream>
        extends ArrayByteInput {

    public BufferedStreamByteInput(final byte[] source, final int offset,
                                   final int limit, final T stream) {
        super(source, offset, limit);
        this.stream = stream;
    }

    @Override
    public int read() throws IOException {
        if (getIndex() >= getLimit()) {
            final int read = getStream().read(getSource(), 0, getLimit());
            if (read == -1) {
                throw new EOFException("unexpected end-of-streame");
            }
            setLimit(read);
            setIndex(0);
        }
        return super.read();
    }

    // ------------------------------------------------------------------ source
    @Override
    @SuppressWarnings("unchecked")
    public BufferedStreamByteInput<T> source(byte[] target) {
        return (BufferedStreamByteInput<T>) super.source(target);
    }

    // ------------------------------------------------------------------- index
    @Override
    @SuppressWarnings("unchecked")
    public BufferedStreamByteInput<T> index(int index) {
        return (BufferedStreamByteInput<T>) super.index(index);
    }

    // ------------------------------------------------------------------- limit
    @Override
    @SuppressWarnings("unchecked")
    public BufferedStreamByteInput<T> limit(int limit) {
        return (BufferedStreamByteInput<T>) super.limit(limit);
    }

    // ------------------------------------------------------------------ stream
    /**
     * Returns the current value of {@link #stream}.
     *
     * @return the current value of {@link #stream}
     */
    public T getStream() {
        return stream;
    }

    /**
     * Replaces the value of {@link #stream} with given.
     *
     * @param stream new value for {@link #stream}
     */
    public void setStream(final T stream) {
        this.stream = stream;
    }

    /**
     * Replaces the value of {@link #stream} with given and returns this
     * instance.
     *
     * @param stream new value for {@link #stream}
     * @return this instance
     */
    public BufferedStreamByteInput<T> stream(final T stream) {
        setStream(stream);
        return this;
    }

    // -------------------------------------------------------------------------
    /**
     * The input stream from which bytes are written onto {@link #source}.
     */
    protected T stream;
}
