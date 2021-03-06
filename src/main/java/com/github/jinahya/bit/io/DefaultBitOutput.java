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
 * A default implementation of {@link BitOutput} writes bytes to an instance of {@link ByteOutput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DefaultBitInput
 */
public class DefaultBitOutput extends AbstractBitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates new instance with specified delegate.
     *
     * @param delegate the delegate to which bytes are written; {@code null} if if is supposed to be lazily initialized
     *                 and set.
     */
    public DefaultBitOutput(final ByteOutput delegate) {
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
     * {@inheritDoc} The {@code write(int)} method of {@code DefaultBitOutput} class invokes {@link
     * ByteOutput#write(int)} method, on what {@link #getDelegate()} method returns, with given value.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     * @see #getDelegate()
     * @see ByteOutput#write(int)
     */
    @Override
    protected void write(final int value) throws IOException {
        getDelegate().write(value);
    }

    // -------------------------------------------------------------------------------------------------------- delegate

    /**
     * Returns the current value of {@code delegate} attribute.
     *
     * @return current value of {@code delegate} attribute.
     */
    protected ByteOutput getDelegate() {
        return delegate;
    }

    /**
     * Replaces the value of {@code delegate} attribute with given.
     *
     * @param delegate new value for {@code delegate} attribute.
     */
    protected void setDelegate(final ByteOutput delegate) {
        this.delegate = delegate;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The delegate whose {@link ByteOutput#write(int)} method is invoked via {@link #write(int)} method.
     */
    private ByteOutput delegate;
}
