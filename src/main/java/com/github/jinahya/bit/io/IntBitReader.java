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
 * An interface for reading an {@code int} value from a {@link BitInput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see IntBitWriter
 */
public interface IntBitReader {

    /**
     * Reads an {@code int} value from specified bit input.
     *
     * @param input the bit input from which the value is read; must not be {@code null}.
     * @return an {@code int} value read from {@code input}.
     * @throws IOException if an I/O error occurs.
     * @see IntBitWriter#writeInt(BitOutput, int)
     */
    int readInt(BitInput input) throws IOException;
}
