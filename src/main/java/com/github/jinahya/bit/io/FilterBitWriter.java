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
 * A skeletal {@link BitWriter} that wraps another writer (the {@link #delegate delegate}), for composing writers
 * without modifying them.
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see FilterBitReader
 */
public abstract class FilterBitWriter<T>
        implements BitWriter<T> {

    /**
     * Returns a new writer that writes a {@code 1}-bit nullability flag and, when the value is non-{@code null},
     * delegates to specified writer.
     *
     * @param writer the writer to delegate to when the value is present; must not be {@code null}.
     * @param <T>    value type parameter
     * @return a new {@code null}-aware writer.
     * @throws NullPointerException if {@code writer} is {@code null}.
     * @see FilterBitReader#nullable(BitReader)
     */
    public static <T> BitWriter<T> nullable(final BitWriter<? super T> writer) {
        return new FilterBitWriter<T>(writer) {
            @Override
            public void write(final BitOutput output, final T value) throws IOException {
                if (output == null) {
                    throw new NullPointerException("output is null");
                }
                if (value == null) {
                    output.writeInt(true, FLAG_SIZE, 0);
                    return;
                }
                output.writeInt(true, FLAG_SIZE, 1);
                delegate.write(output, value);
            }
        };
    }

    /**
     * Creates a new instance wrapping specified delegate.
     *
     * @param delegate the writer to wrap; must not be {@code null}.
     * @throws NullPointerException if {@code delegate} is {@code null}.
     */
    protected FilterBitWriter(final BitWriter<? super T> delegate) {
        super();
        if (delegate == null) {
            throw new NullPointerException("delegate is null");
        }
        this.delegate = delegate;
    }

    /**
     * The writer wrapped by this filter.
     */
    protected final BitWriter<? super T> delegate;
}
