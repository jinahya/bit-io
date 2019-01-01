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
 * A default implementation writes bytes to an instance of {@link ByteOutput}.
 *
 * @param <T> byte output type parameter
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class DefaultBitOutput<T extends ByteOutput> extends AbstractBitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates new instance with specified delegate.
     *
     * @param delegate the delegate; {@code null} if if is supposed to be lazily initialized and set.
     */
    public DefaultBitOutput(final T delegate) {
        super();
        this.delegate = delegate;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code DefaultBitOutput} class invokes {@link
     * ByteOutput#write(int)} on what {@link #getDelegate()} returns with given value. Override this method if the
     * {@link #delegate} is supposed to be lazily initialized and set.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    protected void write(final int value) throws IOException {
        getDelegate().write(value);
    }

    // -------------------------------------------------------------------------------------------------------- delegate

    /**
     * returns the current value of {@link #delegate}.
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
     * Replaces {@link #delegate} with given and returns self.
     *
     * @param delegate new value for {@link #delegate}
     * @return this instance
     */
    public DefaultBitOutput<T> delegate(final T delegate) {
        setDelegate(delegate);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The delegate on which {@link #write(int)} is invoked.
     */
    protected T delegate;
}
