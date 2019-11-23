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
 * A bare interface for writing values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitInput
 */
interface BareBitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes an {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value Integer#SIZE} - (unsigned ? {@code 1}
     *                 : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readInt(boolean, int)
     */
    void writeInt(boolean unsigned, int size, int value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value Long#SIZE} - (unsigned ? {@code 1} :
     *                 {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readLong(boolean, int)
     */
    void writeLong(boolean unsigned, int size, long value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Aligns to specified number of bytes by padding zero bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits padded while aligning.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#align(int)
     */
    long align(int bytes) throws IOException;
}
