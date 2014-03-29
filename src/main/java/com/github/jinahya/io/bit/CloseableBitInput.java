/*
 *  Copyright 2010 Jin Kwon.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package com.github.jinahya.io.bit;


import java.io.Closeable;
import java.io.IOException;


/**
 * A class for reading arbitrary length of bits.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 * @param <T> closeable byte input type parameter
 * @param <U> byte source type parameter
 */
public class CloseableBitInput<T extends CloseableByteInput<U>, U>
        extends BitInput<T>
        implements Closeable {


    /**
     * Creates a new instance built on top of the specified byte input.
     *
     * @param input the byte input on which this bit input is built.
     *
     * @throws NullPointerException if the specified {@code input} is
     * {@code null}.
     */
    public CloseableBitInput(final T input) {

        super(input);
    }


    /**
     * Closes this bit input. This method aligns to a single byte and closes the
     * underlying byte input.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #align(int)
     * @see CloseableByteInput#close()
     */
    @Override
    public void close() throws IOException {

        align(1);
        input.close();
    }


}

