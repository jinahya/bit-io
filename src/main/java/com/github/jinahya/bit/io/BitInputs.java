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

import java.io.DataInput;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Static factory methods for creating {@link BitInput} instances over various byte sources.
 *
 * <p>Each method wraps the source in the appropriate {@link ByteInput} implementation and returns a
 * {@link DefaultBitInput} bound to it, hiding the two-level construction
 * ({@code new DefaultBitInput(new XxxByteInput(...))}).</p>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutputs
 */
public final class BitInputs {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new bit input reading from specified byte input.
     *
     * @param source the byte input from which bytes are read; must not be {@code null}.
     * @return a new bit input.
     * @throws NullPointerException if {@code source} is {@code null}.
     */
    public static BitInput from(final ByteInput source) {
        return new DefaultBitInput(source);
    }

    /**
     * Creates a new bit input reading from specified input stream.
     *
     * @param source the input stream from which bytes are read; must not be {@code null}.
     * @return a new bit input.
     * @throws NullPointerException if {@code source} is {@code null}.
     */
    public static BitInput from(final InputStream source) {
        return from(new StreamByteInput(source));
    }

    /**
     * Creates a new bit input reading from specified byte array.
     *
     * @param source the byte array from which bytes are read; must not be {@code null}.
     * @return a new bit input.
     * @throws NullPointerException if {@code source} is {@code null}.
     */
    public static BitInput from(final byte[] source) {
        return from(new ArrayByteInput(source));
    }

    /**
     * Creates a new bit input reading from specified byte buffer.
     *
     * @param source the byte buffer from which bytes are read; must not be {@code null}.
     * @return a new bit input.
     * @throws NullPointerException if {@code source} is {@code null}.
     */
    public static BitInput from(final ByteBuffer source) {
        return from(new BufferByteInput(source));
    }

    /**
     * Creates a new bit input reading from specified data input.
     *
     * @param source the data input from which bytes are read; must not be {@code null}.
     * @return a new bit input.
     * @throws NullPointerException if {@code source} is {@code null}.
     */
    public static BitInput from(final DataInput source) {
        return from(new DataByteInput(source));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private BitInputs() {
        throw new AssertionError("instantiation is not allowed");
    }
}
