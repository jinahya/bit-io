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
 * An interface for writing bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see ByteInput
 */
@FunctionalInterface
public interface ByteOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes specified unsigned {@value java.lang.Byte#SIZE}-bit value.
     *
     * <p>How a full target is signalled is implementation-defined. Most targets throw an {@link IOException} when they
     * can accept no more bytes. A <em>buffer-backed</em> target ({@link BufferByteOutput}) instead throws an unchecked
     * {@link java.nio.BufferOverflowException} when its buffer is full; such a buffer is a caller-managed window, so
     * callers should {@linkplain java.nio.Buffer#hasRemaining() pre-check} and purge it to the ultimate target rather
     * than rely on that exception.</p>
     *
     * @param value the unsigned {@value java.lang.Byte#SIZE}-bit value to write; between {@code 0} and {@code 255},
     *              both inclusive.
     * @throws IOException if an I/O error occurs.
     * @see ByteInput#read()
     */
    void write(int value) throws IOException;
}
