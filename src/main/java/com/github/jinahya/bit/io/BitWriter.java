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
 * An interface for writing a value of a specific type to a {@link BitOutput}.
 *
 * <p>A writer is the unit of <em>serialization</em>: it knows how to encode one value of type {@code T} onto the bit
 * stream. Implement it (as a named class, since this library targets Java 1.6 and has no lambdas) and pass it to
 * {@link AbstractBitOutput#writeObject(BitWriter, Object)}, or invoke {@link #write(BitOutput, Object)} directly.</p>
 *
 * @param <T> value type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitReader
 * @see FilterBitWriter
 */
public interface BitWriter<T> {

    /**
     * Writes specified value to specified bit output.
     *
     * @param output the bit output to which the value is written; must not be {@code null}.
     * @param value  the value to write; {@code null} is accepted only when this writer explicitly supports it (e.g. a
     *               {@link FilterBitWriter#nullable(BitWriter) nullable} writer).
     * @throws IOException if an I/O error occurs.
     * @see BitReader#read(BitInput)
     */
    void write(BitOutput output, T value) throws IOException;
}
