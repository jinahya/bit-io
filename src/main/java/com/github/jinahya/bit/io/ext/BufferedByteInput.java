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
public class BufferedByteInput<T extends InputStream> extends ArrayByteInput {

    public BufferedByteInput(final byte[] source, final int offset,
                             final int limit, final T stream) {
        super(source, offset, limit);
        this.stream = stream;
    }

    @Override
    public int read() throws IOException {
        if (getIndex() == getLimit()) {
            setLimit(getStream().read(getSource()));
            if (getLimit() == -1) {
                throw new EOFException("unexpected end-of-streame");
            }
            setIndex(0);
        }
        return super.read();
    }

    // -------------------------------------------------------------------------
    public T getStream() {
        return stream;
    }

    public void setStream(final T stream) {
        this.stream = stream;
    }

    public BufferedByteInput<T> stream(final T stream) {
        setStream(stream);
        return this;
    }

    // -------------------------------------------------------------------------
    protected T stream;
}
