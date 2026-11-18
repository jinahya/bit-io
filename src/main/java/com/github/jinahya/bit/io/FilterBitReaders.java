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

import java.util.Objects;
import java.util.function.Function;

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

    /**
     * Returns a new filter reader that maps values read by specified delegate with specified function.
     * <pre>{@code
     * BitReader<Integer> source = input -> input.readUnsignedInt(3);
     * FilterBitReader<String, Integer> reader = FilterBitReaders.mapping(source, value -> Integer.toString(value));
     * }</pre>
     * <p>Mappings compose by nesting calls; e.g., {@code mapping(mapping(source, v -> v + 4), String::valueOf)}.</p>
     *
     * @param delegate the reader to wrap; must not be {@code null}.
     * @param mapper   the function mapping each value read by {@code delegate}; must not be {@code null}.
     * @param <T>      mapped value type parameter
     * @param <U>      original value type parameter
     * @return a new mapping filter reader.
     * @throws NullPointerException if {@code delegate} or {@code mapper} is {@code null}.
     * @see FilterBitWriters#mapping(BitWriter, Function)
     */
    public static <T, U> FilterBitReader<T, U> mapping(final BitReader<? extends U> delegate,
                                                       final Function<? super U, ? extends T> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return new FilterBitReader<T, U>(delegate) {
            @Override
            protected T apply(final U value) {
                return mapper.apply(value);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    private FilterBitReaders() {
        throw new AssertionError("instantiation is not allowed");
    }
}
