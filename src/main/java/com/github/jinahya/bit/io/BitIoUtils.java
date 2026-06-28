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
 * Internal utilities shared by the reader/writer layer.
 */
final class BitIoUtils {

    /**
     * Reads a single-bit presence flag from specified input, as a {@link BitInput#readBoolean() boolean}.
     *
     * @param input the input from which the flag is read; must not be {@code null}.
     * @return {@code true} if a value is present and follows (flag set); {@code false} if the value is {@code null}
     * (flag clear).
     * @throws NullPointerException if {@code input} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    static boolean readPresenceFlag(final BitInput input) throws IOException {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        return input.readBoolean();
    }

    /**
     * Writes a single-bit presence flag for specified value to specified output, as a
     * {@link BitOutput#writeBoolean(boolean) boolean}.
     *
     * @param output the output to which the flag is written; must not be {@code null}.
     * @param value  the value whose presence the flag represents; may be {@code null}.
     * @return {@code true} if {@code value} is present (non-{@code null}) and should be written; {@code false} otherwise.
     * @throws NullPointerException if {@code output} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    static boolean writePresenceFlag(final BitOutput output, final Object value) throws IOException {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        final boolean present = value != null;
        output.writeBoolean(present);
        return present;
    }

    private BitIoUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
