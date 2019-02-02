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
package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@link ByteOutput} writes bytes to an {@link OutputStream}.
 *
 * @param <T> stream type parameter
 * @see StreamByteInput
 */
public class StreamByteOutput<T extends OutputStream> extends AbstractByteOutput<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance built on top of the specified output stream.
     *
     * @param target the output stream; {@code null} if it's supposed to be lazily initialized and set.
     */
    public StreamByteOutput(final T target) {
        super(target);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code StreamByteOutput} class invokes {@link
     * OutputStream#write(int)}, on what {@link #getTarget()} gives, with given {@code value}. Override this method if
     * the {@link #target} is supposed to be lazily initialized and set.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #getTarget()
     * @see OutputStream#write(int)
     */
    @Override
    public void write(final int value) throws IOException {
        getTarget().write(value);
    }

    // ---------------------------------------------------------------------------------------------------------- target

    /**
     * {@inheritDoc}
     *
     * @param target {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public StreamByteOutput<T> target(final T target) {
        return (StreamByteOutput<T>) super.target(target);
    }
}
