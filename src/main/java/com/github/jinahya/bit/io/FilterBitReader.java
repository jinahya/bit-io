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

import static com.github.jinahya.bit.io.BitIoConstants.FLAG_SIZE;

/**
 * A skeletal {@link BitReader} that wraps another reader (the {@link #delegate delegate}), for composing readers
 * without modifying them.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see FilterBitWriter
 */
public abstract class FilterBitReader<T>
        implements BitReader<T> {

    /**
     * Returns a new reader that reads a {@code 1}-bit nullability flag and, when the flag is set, delegates to
     * specified reader; otherwise returns {@code null}.
     *
     * @param reader the reader to delegate to when the value is present; must not be {@code null}.
     * @param <T>    value type parameter
     * @return a new {@code null}-aware reader.
     * @throws NullPointerException if {@code reader} is {@code null}.
     * @see FilterBitWriter#nullable(BitWriter)
     */
    public static <T> BitReader<T> nullable(final BitReader<? extends T> reader) {
        return new FilterBitReader<T>(reader) {
            @Override
            public T read(final BitInput input) throws IOException {
                if (input == null) {
                    throw new NullPointerException("input is null");
                }
                if (input.readInt(true, FLAG_SIZE) == 0) {
                    return null;
                }
                return delegate.read(input);
            }
        };
    }

    /**
     * Creates a new instance wrapping specified delegate.
     *
     * @param delegate the reader to wrap; must not be {@code null}.
     * @throws NullPointerException if {@code delegate} is {@code null}.
     */
    protected FilterBitReader(final BitReader<? extends T> delegate) {
        super();
        if (delegate == null) {
            throw new NullPointerException("delegate is null");
        }
        this.delegate = delegate;
    }

    /**
     * The reader wrapped by this filter.
     */
    protected final BitReader<? extends T> delegate;
}
