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
 * Static factory methods for composing {@link BitReader} instances.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitWriters
 */
public final class BitReaders {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a new reader that reads a {@code 1}-bit nullability flag and, when the flag is set, delegates to
     * specified reader; otherwise returns {@code null}.
     *
     * @param reader the reader to delegate to when the value is present; must not be {@code null}.
     * @param <T>    value type parameter
     * @return a new {@code null}-aware reader.
     * @throws NullPointerException if {@code reader} is {@code null}.
     */
    public static <T> BitReader<T> nullable(final BitReader<? extends T> reader) {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        // not a FilterBitReader: a nullability wrapper reads its own flag bit and bypasses the apply(...) mapping,
        // so it
        // implements the interface directly rather than the value-mapping skeletal type.
        return new BitReader<T>() {
            @Override
            public T read(final BitInput input) throws IOException {
                if (!BitIoUtils.readPresenceFlag(input)) {
                    return null;
                }
                return reader.read(input);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    private BitReaders() {
        throw new AssertionError("instantiation is not allowed");
    }
}
