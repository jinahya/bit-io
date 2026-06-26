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

import java.io.DataOutput;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Static factory methods for creating {@link BitOutput} instances over various byte targets.
 *
 * <p>Each method wraps the target in the appropriate {@link ByteOutput} implementation and returns a
 * {@link DefaultBitOutput} bound to it, hiding the two-level construction
 * ({@code new DefaultBitOutput(new XxxByteOutput(...))}).</p>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInputs
 */
public final class BitOutputs {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new bit output writing to specified byte output.
     *
     * @param target the byte output to which bytes are written; must not be {@code null}.
     * @return a new bit output.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public static BitOutput from(final ByteOutput target) {
        return new DefaultBitOutput(target);
    }

    /**
     * Creates a new bit output writing to specified output stream.
     *
     * @param target the output stream to which bytes are written; must not be {@code null}.
     * @return a new bit output.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public static BitOutput from(final OutputStream target) {
        return from(new StreamByteOutput(target));
    }

    /**
     * Creates a new bit output writing to specified byte array.
     *
     * @param target the byte array to which bytes are written; must not be {@code null}.
     * @return a new bit output.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public static BitOutput from(final byte[] target) {
        return from(new ArrayByteOutput(target));
    }

    /**
     * Creates a new bit output writing to specified byte buffer.
     *
     * @param target the byte buffer to which bytes are written; must not be {@code null}.
     * @return a new bit output.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public static BitOutput from(final ByteBuffer target) {
        return from(new BufferByteOutput(target));
    }

    /**
     * Creates a new bit output writing to specified data output.
     *
     * @param target the data output to which bytes are written; must not be {@code null}.
     * @return a new bit output.
     * @throws NullPointerException if {@code target} is {@code null}.
     */
    public static BitOutput from(final DataOutput target) {
        return from(new DataByteOutput(target));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private BitOutputs() {
        throw new AssertionError("instantiation is not allowed");
    }
}
