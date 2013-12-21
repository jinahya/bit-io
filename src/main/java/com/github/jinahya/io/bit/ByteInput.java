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


import java.io.Closeable;
import java.io.IOException;


/**
 *
 * @param <T>
 */
public abstract class ByteInput<T> implements Closeable {


    /**
     * Creates a new instance with given underlying input source.
     *
     * @param input the underlying input source.
     */
    public ByteInput(final T input) {
        super();
        this.source = input;
    }


    /**
     * Reads the next unsigned 8-bit byte.
     *
     * @return the next unsigned 8-bit byte, or {@code -1} if the end of the
     * stream is reached.
     *
     * @throws IOException if an I/O error occurs.
     */
    public abstract int readUnsignedByte() throws IOException;


    public T getSource() {

        return source;
    }


    public void setSource(final T source) {

        this.source = source;
    }


    /**
     * The underlying byte source.
     */
    protected T source;


}

