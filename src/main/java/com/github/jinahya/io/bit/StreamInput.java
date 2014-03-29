/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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


package com.github.jinahya.io.bit;


import java.io.IOException;
import java.io.InputStream;


/**
 * A {@link AbstractByteInput} implementation for {@link InputStream}s.
 */
public class StreamInput extends AbstractByteInput<InputStream> {


    /**
     * Creates a new instance built on top of the specified input stream.
     *
     * @param source {@inheritDoc}
     */
    public StreamInput(final InputStream source) {

        super(source);
    }


    /**
     * {@inheritDoc} The {@code readUnsignedByte()} method of
     * {@code StreamReader} class calls {@link InputStream#read()} on
     * {@link #source} and returns the result. Override this method if
     * {@link #source} is intended to be lazily initialized and set.
     *
     * @return {@inheritDoc}
     *
     * @throws IllegalStateException id {@link #source} is currently
     * {@code null}.
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int readUnsignedByte() throws IOException {

        if (source == null) {
            throw new IllegalStateException("#source is currently null");
        }

        return source.read();
    }


}

