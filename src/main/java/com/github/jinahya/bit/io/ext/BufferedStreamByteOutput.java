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

import com.github.jinahya.bit.io.ArrayByteOutput;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <T> stream type parameter
 */
public class BufferedStreamByteOutput<T extends OutputStream>
        extends ArrayByteOutput {

    public BufferedStreamByteOutput(final byte[] target, final int index,
                                    final int limit, final T stream) {
        super(target, index, limit);
        this.stream = stream;
    }

    // -------------------------------------------------------------------------
    @Override
    public void write(int value) throws IOException {
        if (getIndex() >= getLimit()) {
            getStream().write(getTarget(), 0, getLimit());
            setLimit(getTarget().length);
            setIndex(0);
        }
        super.write(value);
    }

    // ------------------------------------------------------------------ target
    @Override
    @SuppressWarnings(value = "unchecked")
    public BufferedStreamByteOutput<T> target(byte[] target) {
        return (BufferedStreamByteOutput<T>) super.target(target);
    }

    // ------------------------------------------------------------------- index
    @Override
    @SuppressWarnings("unchecked")
    public BufferedStreamByteOutput<T> index(int index) {
        return (BufferedStreamByteOutput<T>) super.index(index);
    }

    // ------------------------------------------------------------------- limit
    @Override
    @SuppressWarnings("unchecked")
    public BufferedStreamByteOutput<T> limit(int limit) {
        return (BufferedStreamByteOutput<T>) super.limit(limit);
    }

    // ------------------------------------------------------------------ stream
    public T getStream() {
        return stream;
    }

    public void setStream(final T stream) {
        this.stream = stream;
    }

    public BufferedStreamByteOutput<T> stream(final T stream) {
        setStream(stream);
        return this;
    }

    // -------------------------------------------------------------------------
    protected T stream;
}
