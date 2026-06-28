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

/**
 * Static factory methods for creating {@link FilterBitReader} instances.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see FilterBitWriters
 */
public final class FilterBitReaders {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a new filter reader that returns values read by specified delegate unchanged.
     *
     * @param delegate the reader to wrap; must not be {@code null}.
     * @param <T>      value type parameter
     * @return a new identity filter reader.
     * @throws NullPointerException if {@code delegate} is {@code null}.
     */
    public static <T> FilterBitReader<T, T> identity(final BitReader<? extends T> delegate) {
        return new FilterBitReader<T, T>(delegate) {
            @Override
            protected T apply(final T value) {
                return value;
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    private FilterBitReaders() {
        throw new AssertionError("instantiation is not allowed");
    }
}
