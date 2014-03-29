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
 * A class for writing arbitrary length of bits.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 * @param <T> underlying byte target type parameter
 * @param <U>
 */
public class CloseableBitOutput<T extends CloseableByteOutput<T>, U>
        extends BitOutput<T>
        implements Closeable {


    /**
     * Creates a new instance built on top of the specified byte input.
     *
     * @param output the byte input on which this bit output is built.
     *
     * @throws NullPointerException if the specified {@code output} is
     * {@code null}.
     */
    public CloseableBitOutput(final T output) {

        super(output);
    }


    /**
     * Closes this instance. This method, if {@link #output} is not
     * {@code null}, aligns to a single byte and closes the {@link #output}.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #align(int)
     * @see ByteInput#close()
     */
    @Override
    public void close() throws IOException {

        align(1);
        output.close();
    }


}

