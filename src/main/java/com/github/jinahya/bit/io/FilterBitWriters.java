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

import java.util.function.Function;

/**
 * Static factory methods for creating {@link FilterBitWriter} instances.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see FilterBitReaders
 */
public final class FilterBitWriters {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a new filter writer that writes specified values to specified delegate unchanged.
     *
     * @param delegate the writer to wrap; must not be {@code null}.
     * @param <T>      value type parameter
     * @return a new identity filter writer.
     * @throws NullPointerException if {@code delegate} is {@code null}.
     */
    public static <T> FilterBitWriter<T, T> identity(final BitWriter<? super T> delegate) {
        return new FilterBitWriter<T, T>(delegate) {
            @Override
            protected T apply(final T value) {
                return value;
            }
        };
    }

    /**
     * Returns a new filter writer that maps values with specified function before writing them to specified
     * delegate.
     * <pre>{@code
     * BitWriter<Integer> sink = (output, value) -> output.writeUnsignedInt(8, value);
     * FilterBitWriter<String, Integer> writer = FilterBitWriters.mapping(sink, String::length);
     * }</pre>
     * <p>Mappings compose by nesting calls; the inner mapping is closest to the delegate; e.g.,
     * {@code mapping(mapping(sink, String::length), Object::toString)}.</p>
     *
     * @param delegate the writer to wrap; must not be {@code null}.
     * @param mapper   the function mapping each value before {@code delegate} writes it; must not be {@code null}.
     * @param <T>      original value type parameter
     * @param <U>      mapped value type parameter
     * @return a new mapping filter writer.
     * @throws NullPointerException if {@code delegate} or {@code mapper} is {@code null}.
     * @see FilterBitReaders#mapping(BitReader, Function)
     */
    public static <T, U> FilterBitWriter<T, U> mapping(final BitWriter<? super U> delegate,
                                                       final Function<? super T, ? extends U> mapper) {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        return new FilterBitWriter<T, U>(delegate) {
            @Override
            protected U apply(final T value) {
                return mapper.apply(value);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    private FilterBitWriters() {
        throw new AssertionError("instantiation is not allowed");
    }
}
