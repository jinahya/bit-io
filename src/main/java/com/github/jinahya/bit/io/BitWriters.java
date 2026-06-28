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
 * Static factory methods for composing {@link BitWriter} instances.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitReaders
 */
public final class BitWriters {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a new writer that writes a {@code 1}-bit nullability flag and, when the value is non-{@code null},
     * delegates to specified writer.
     *
     * @param writer the writer to delegate to when the value is present; must not be {@code null}.
     * @param <T>    value type parameter
     * @return a new {@code null}-aware writer.
     * @throws NullPointerException if {@code writer} is {@code null}.
     */
    public static <T> BitWriter<T> nullable(final BitWriter<? super T> writer) {
        if (writer == null) {
            throw new NullPointerException("writer is null");
        }
        // not a FilterBitWriter: a nullability wrapper writes its own flag bit and bypasses the apply(...) mapping, so it
        // implements the interface directly rather than the value-mapping skeletal type.
        return new BitWriter<T>() {
            @Override
            public void write(final BitOutput output, final T value) throws IOException {
                if (BitIoUtils.writePresenceFlag(output, value)) {
                    writer.write(output, value);
                }
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    private BitWriters() {
        throw new AssertionError("instantiation is not allowed");
    }
}
