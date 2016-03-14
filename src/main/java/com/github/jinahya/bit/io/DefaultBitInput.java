/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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

/**
 * A default implementation read bytes from {@link #delegate}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> byte input type parameter
 */
public class DefaultBitInput<T extends ByteInput> extends AbstractBitInput {

    /**
     * Create a new instance with specified delegate.
     *
     * @param delegate the delegate; may be {@code null} if it is intended to be
     * lazily initialized and set.
     */
    public DefaultBitInput(final T delegate) {
        super();
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc} The {@code read()} method of {@code DefaultBitInput} class
     * invokes {@link ByteInput#read()} on {@link #delegate} and returns the
     * result. Override this method if {@link #delegate} is supposed to be
     * lazily initialized and set.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    /**
     * Returns the current value of {@link #delegate}.
     *
     * @return current value of {@link #delegate}
     */
    public T getDelegate() {
        return delegate;
    }

    /**
     * Replaces the value of {@link #delegate} with given.
     *
     * @param delegate new value of {@link #delegate}.
     */
    public void setDelegate(final T delegate) {
        this.delegate = delegate;
    }

    /**
     * The delegate on which {@link #read()} is invoked.
     */
    protected T delegate;
}
