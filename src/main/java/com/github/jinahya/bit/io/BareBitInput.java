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
 * A bare interface for reading values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitOutput
 */
interface BareBitInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value Integer#SIZE} - (unsigned ? {@code 1}
     *                 : {@code 0})), both inclusive.
     * @return an {@code int} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeInt(boolean, int, int)
     */
    int readInt(boolean unsigned, int size) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value Long#SIZE} - (unsigned ? {@code 1} :
     *                 {@code 0})), both inclusive.
     * @return a {@code long} value of specified bit size.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeLong(boolean, int, long)
     */
    long readLong(boolean unsigned, int size) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Aligns to specified number of bytes by discarding bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded while aligning.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#align(int)
     */
    long align(int bytes) throws IOException;
}
