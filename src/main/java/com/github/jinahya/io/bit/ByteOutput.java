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
     * Creates a new instance with given target.
     *
     * @param target the target byte consumer.
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


    public T getTarget() {

        return target;
    }


    public void setTarget(final T target) {

        this.target = target;
    }


    /**
     * The target byte consumer.
     */
    protected T target;


}

