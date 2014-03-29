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


import java.io.Closeable;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;


/**
 *
 * @param <T> underlying byte source type parameter
 */
public abstract class CloseableByteInput<T> implements ByteInput, Closeable {


    public static CloseableByteInput<ByteBuffer> newInstance(final ByteBuffer source) {

        return new BufferInput(source);
    }


    public static CloseableByteInput<InputStream> newInstance(final InputStream source) {

        return new StreamInput(source);
    }


    public static CloseableByteInput<ReadableByteChannel> newInstance(
            final ReadableByteChannel source, final ByteBuffer buffer) {

        return new ChannelInput(source, buffer);
    }


    /**
     * Creates a new instance built on top of the specified underlying byte
     * source.
     *
     * @param source the underlying byte source to be assigned to the field
     * {@link #source} for later use, or {@code null} if this instance is
     * intended to be created without an underlying byte source.
     */
    public CloseableByteInput(final T source) {

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


    /**
     * The underlying byte source.
     *
     * @see #getSource()
     * @see #setSource(java.lang.Object)
     */
    protected T source;


}

