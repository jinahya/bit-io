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


/**
 * An abstract class for implementing {@code ByteInput}s.
 *
 * @param <T> underlying byte source type parameter
 */
public abstract class AbstractByteInput<T> implements ByteInput {


    /**
     * Creates a new instance built on top of the specified underlying byte
     * source.
     *
     * @param source the underlying byte source, or {@code null} if is is
     * intended to be lazily initialized and set.
     */
    public AbstractByteInput(final T source) {

        super();

        this.source = source;
    }


    /**
     * Returns the current value of {@link #source}.
     *
     * @return the current value of {@link #source}.
     */
    public T getSource() {

        return source;
    }


    /**
     * Replaces the value of {@link #source} with given.
     *
     * @param source new value for {@link #source}.
     */
    public void setSource(final T source) {

        this.source = source;
    }


    T requireNonNullSource() {

        if (source == null) {
            throw new IllegalStateException("#source is currently null");
        }

        return source;
    }


    /**
     * The underlying byte source.
     *
     * @see #getSource()
     * @see #setSource(java.lang.Object)
     */
    protected T source;


}

