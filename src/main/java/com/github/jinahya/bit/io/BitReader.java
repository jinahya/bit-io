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
 * An interface for reading a value of a specific type from a {@link BitInput}.
 *
 * <p>A reader is the unit of <em>deserialization</em>: it knows how to reconstruct one value of type {@code T} from
 * the bit stream. Implement it (as a named class, since this library targets Java 1.6 and has no lambdas) and pass it
 * to {@link AbstractBitInput#readObject(BitReader)}, or invoke {@link #read(BitInput)} directly.</p>
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitWriter
 * @see FilterBitReader
 */
public interface BitReader<T> {

    /**
     * Reads a value from specified bit input.
     *
     * @param input the bit input from which the value is read; must not be {@code null}.
     * @return a value read from {@code input}; may be {@code null} only when this reader explicitly supports it (e.g. a
     *         {@link FilterBitReader#nullable(BitReader) nullable} reader).
     * @throws IOException if an I/O error occurs.
     * @see BitWriter#write(BitOutput, Object)
     */
    T read(BitInput input) throws IOException;
}
