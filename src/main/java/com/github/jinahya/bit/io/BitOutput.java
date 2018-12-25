/*
 *  Copyright 2010 Jin Kwon.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.jinahya.bit.io;

import java.io.IOException;

/**
 * An interface for writing arbitrary length of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface BitOutput {

    /**
     * Writes a 1-bit boolean value. This method writes {@code 0b1} for {@code true} and {@code 0b0} for {@code false}.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs
     */
    void writeBoolean(boolean value) throws IOException;

    /**
     * Writes a {@code byte} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits for value; between {@code 1} and {@code 7 + (unsigned ? 0 : 1)}, both
     *                 inclusive.
     * @param value    the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeByte(boolean unsigned, int size, byte value) throws IOException;

    /**
     * Writes a {@code short} value.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits for value; between {@code 1} and {@code 15 + (unsigned ? 0 : 1)}, both
     *                 inclusive.
     * @param value    the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeShort(boolean unsigned, int size, short value) throws IOException;

    /**
     * Writes an {@code int} value. Only the lower specified number of bits are written.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits for value; between {@code 1} and {@code 31 + (unsigned ? 0 : 1)}, both
     *                 inclusive.
     * @param value    the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeInt(boolean unsigned, int size, int value) throws IOException;

    /**
     * Writes a {@code long} value. Only the lower specified number of bits are written.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of valid bits for value; between {@code 1} and {@code 63 + (unsigned ? 0 : 1)}, both
     *                 inclusive.
     * @param value    the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeLong(boolean unsigned, int size, long value) throws IOException;

    /**
     * Writes a {@code char} value.
     *
     * @param size  the number of bits for value; between {@code 1} and {@code 16}, both inclusive.
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeChar(int size, char value) throws IOException;

    /**
     * Aligns to specified number of bytes by padding zeros.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of zero bits padded while aligning.
     * @throws IllegalArgumentException if {@code bytes} is less than {@code 1}.
     * @throws IOException              if an I/O error occurs.
     */
    long align(int bytes) throws IOException;
}
