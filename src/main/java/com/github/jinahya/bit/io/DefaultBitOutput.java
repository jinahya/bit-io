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


import com.github.jinahya.bit.io.octet.ByteOutput;
import java.io.IOException;


/**
 * A default implementation writes bytes to {@link #delegate}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> byte output type parameter
 *
 * @see BitOutputFactory#newInstance(com.github.jinahya.bit.io.ByteOutput)
 * @see BitOutputFactory#newInstance(java.util.function.Supplier)
 */
public class DefaultBitOutput<T extends ByteOutput> extends AbstractBitOutput {


    /**
     * Creates new instance with specified byte output.
     *
     * @param delegate the byte output, or {@code null} if if is supposed to
     * lazily initialized and set.
     */
    public DefaultBitOutput(final T delegate) {

        super();

        this.delegate = delegate;
    }


    /**
     * {@inheritDoc} The {@code write(int)} method of {@code DefaultBitOutput}
     * class invokes {@link ByteOutput#write(int)} with given value. Override
     * this method if {@link #delegate} is supposed to be lazily initialized and
     * set.
     *
     * @param value {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void write(final int value) throws IOException {

        delegate.write(value);
    }


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
     * The delegate on which {@link #write(int)} is invoked.
     */
    protected T delegate;

}

