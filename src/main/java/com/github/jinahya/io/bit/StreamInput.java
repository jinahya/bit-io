/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
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
 * A {@link ByteInput} implementation for {@link InputStream}s.
 */
public class StreamInput extends ByteInput<InputStream> {


    /**
     * Creates a new instance.
     *
     * @param source the stream on which this byte input is built.
     */
    public StreamInput(final InputStream source) {
        super(source);
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The {@code readUnsignedByte()} method of {@code StreamInput} class
     * calls {@link InputStream#read()} on {@link #source} and returns the
     * result.
     *
     * @return {@inheritDoc}
     *
     * @throws IllegalStateException if {@link #source} is currently
     * {@code null}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int readUnsignedByte() throws IOException {
        if (source == null) {
            throw new IllegalStateException("null source");
        }
        return source.read();
    }


    /**
     * {@inheritDoc}
     * <p/>
     * The {@code close()} method of {@code StreamInput} class calls
     * {@link InputStream#close()} on {@link #source} if it is not
     * {@code null}.
     *
     * @throws IOException {@inheritDoc }
     */
    @Override
    public void close() throws IOException {
        if (source != null) {
            source.close();
        }
    }
    
}

