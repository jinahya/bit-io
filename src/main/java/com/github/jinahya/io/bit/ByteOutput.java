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
 * @author Jin Kwon <onacit at gmail.com>
 * @param <T>
 */
public abstract class ByteOutput<T> implements Closeable {


    /**
     * Creates a new instance built on top of the specified underlying byte
     * target.
     *
     * @param target the underlying byte target to be assigned to the field
     * {@link #target} for later use, or {@code null} if this instance is to be
     * created without an underlying byte target.
     */
    public ByteOutput(final T target) {

        super();

        this.target = target;
    }


    /**
     * Writes an unsigned 8-bit integer.
     *
     * @param value an unsigned 8-bit integer.
     *
     * @throws IOException if an I/O error occurs.
     */
    public abstract void writeUnsignedByte(final int value) throws IOException;


    /**
     * Closes this input and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs.
     */
    public abstract void close() throws IOException;


    /**
     * Returns the current value of {@link #target}.
     *
     * @return the current value of {@link #target}.
     */
    public T getTarget() {

        return target;
    }


    /**
     * Replaces the value of {@link #target} with given.
     *
     * @param target new value for {@link #target}.
     */
    public void setTarget(final T target) {

        this.target = target;
    }


    /**
     * The target byte consumer.
     */
    protected T target;


}

