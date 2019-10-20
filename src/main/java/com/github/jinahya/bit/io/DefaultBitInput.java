package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import java.io.IOException;

/**
 * A default implementation of {@link ByteInput} which reads bytes from an instance of {@link ByteInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class DefaultBitInput extends AbstractBitInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Create a new instance with specified delegate.
     *
     * @param delegate the delegate on which {@link #read()} is invoked; may be {@code null} if it is intended to be
     *                 lazily initialized and set.
     */
    public DefaultBitInput(final ByteInput delegate) {
        super();
        this.delegate = delegate;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code DefaultBitInput} class invokes {@link ByteInput#read()} ,on
     * what {@link #getDelegate()} gives, and returns the result.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    protected int read() throws IOException {
        return getDelegate().read();
    }

    // -------------------------------------------------------------------------------------------------------- delegate

    /**
     * Returns the current value of {@link #delegate}.
     *
     * @return current value of {@link #delegate}
     */
    public ByteInput getDelegate() {
        return delegate;
    }

    /**
     * Replaces the value of {@link #delegate} with given.
     *
     * @param delegate new value of {@link #delegate}.
     */
    public void setDelegate(final ByteInput delegate) {
        this.delegate = delegate;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The delegate whose {@link ByteInput#read()} method is invoked via {@link #read()} method.
     */
    private ByteInput delegate;
}
