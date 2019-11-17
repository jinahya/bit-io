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
 * A default implementation of {@link BitInput} which reads bytes from an instance of {@link ByteInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DefaultBitOutput
 */
public class DefaultBitInput extends AbstractBitInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Create a new instance with specified Adelegate.
     *
     * @param delegate the delegate from which bytes are read; may be {@code null} if it is intended to be lazily
     *                 initialized and set.
     */
    public DefaultBitInput(final ByteInput delegate) {
        super();
        this.delegate = delegate;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return super.toString() + "{"
               + "delegate=" + delegate
               + "}";
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@code DefaultBitInput} class invokes {@link ByteInput#read()} method,
     * on what {@link #getDelegate()} returns, and returns the result.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #getDelegate()
     * @see ByteInput#read()
     */
    @Override
    protected int read() throws IOException {
        return getDelegate().read();
    }

    // -------------------------------------------------------------------------------------------------------- delegate

    /**
     * Returns the current value of {@code delegate} attribute.
     *
     * @return current value of {@code delegate} attribute.
     */
    protected ByteInput getDelegate() {
        return delegate;
    }

    /**
     * Replaces the value of {@code delegate} attribute with given.
     *
     * @param delegate new value for {@code delegate} attribute.
     */
    protected void setDelegate(final ByteInput delegate) {
        this.delegate = delegate;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The delegate whose {@link ByteInput#read()} method is invoked via {@link #read()} method.
     */
    private ByteInput delegate;
}
