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
 * An interface for reading bytes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see ByteOutput
 */
public interface ByteInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an unsigned {@value java.lang.Byte#SIZE}-bit value.
     *
     * <p>Implementations <em>must</em> return a value between {@code 0} and {@code 255}, both inclusive, and must
     * <em>not</em> return a negative value (such as the {@code -1} returned by {@link java.io.InputStream#read()}) to
     * signal the end of the input; doing so corrupts the bit stream read by {@link BitInput}.</p>
     *
     * <p>How exhaustion is signalled is implementation-defined. <em>Terminal</em> sources (input stream, data input,
     * channel) throw an {@link java.io.EOFException} when the end is reached. A <em>buffer-backed</em> source
     * ({@link BufferByteInput}) instead throws an unchecked {@link java.nio.BufferUnderflowException} when its buffer
     * is exhausted; such a buffer is a caller-managed window, so callers should
     * {@linkplain java.nio.Buffer#hasRemaining() pre-check} and refill it from the ultimate source rather than rely on
     * that exception.</p>
     *
     * @return an unsigned {@value java.lang.Byte#SIZE}-bit value; between {@code 0} and {@code 255}, both inclusive.
     * @throws IOException if an I/O error occurs.
     * @see ByteOutput#write(int)
     */
    int read() throws IOException;
}
