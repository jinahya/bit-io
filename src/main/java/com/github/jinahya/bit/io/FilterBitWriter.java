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
 * A skeletal {@link BitWriter} that maps a value through {@link #apply(Object)} before writing it with another writer
 * (the {@link #delegate delegate}), for composing writers without modifying them.
 *
 * @param <T> value type parameter
 * @param <U> delegate value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see FilterBitReader
 * @see BitWriters
 */
public abstract class FilterBitWriter<T, U>
        implements BitWriter<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance wrapping specified delegate.
     *
     * @param delegate the writer to wrap; must not be {@code null}.
     * @throws NullPointerException if {@code delegate} is {@code null}.
     */
    protected FilterBitWriter(final BitWriter<? super U> delegate) {
        super();
        if (delegate == null) {
            throw new NullPointerException("delegate is null");
        }
        this.delegate = delegate;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final BitOutput output, final T value) throws IOException {
        delegate.write(output, apply(value));
    }

    protected abstract U apply(T value);

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The writer wrapped by this filter.
     */
    protected final BitWriter<? super U> delegate;
}
