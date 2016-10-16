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
 * An interface for reading arbitrary length of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface BitInput {

    /**
     * Reads a 1-bit boolean value. This method read a 1-bit unsigned int and
     * returns {@code true} for {@code 1} and {@code false} for {@code 0}.
     *
     * @return {@code true} for {@code 1}, {@code false} for {@code 0}
     * @throws IOException if an I/O error occurs.
     */
    boolean readBoolean() throws IOException;

    /**
     * Reads a byte value.
     *
     * @param unsigned a flag for unsigned value.
     * @param size number of bits for value; between {@code 1} and
     * {@code 7 + (unsigned ? 0 : 1)}, both inclusive.
     * @return a byte value
     * @throws IOException if an I/O error occurs.
     */
    byte readByte(boolean unsigned, int size) throws IOException;

    /**
     * Reads a short value.
     *
     * @param unsigned a flag for unsigned value
     * @param size number of bits for value; between {@code 1} and
     * {@code 15 + (unsigned ? 0 : 1)}, both inclusive.
     * @return a short value
     * @throws IOException if an I/O error occurs.
     */
    short readShort(boolean unsigned, int size) throws IOException;

    /**
     * Reads an int value.
     *
     * @param unsigned a flag for unsigned value
     * @param size number of bits for value; between {@code 1} and
     * {@code 31 + (unsigned ? 0 : 1)}, both inclusive.
     * @return an int value
     * @throws IOException if an I/O error occurs.
     */
    int readInt(boolean unsigned, int size) throws IOException;

    /**
     * Reads a long value.
     *
     * @param unsigned a flag for unsigned value
     * @param size number of valid bits for value between {@code 1} and
     * {@code 63 + (unsigned ? 0 : 1)}, both inclusive.
     * @return a long value
     * @throws IOException if an I/O error occurs.
     */
    long readLong(boolean unsigned, int size) throws IOException;

    /**
     * Reads a char value.
     *
     * @param size the number of bits for value; between {@code 1} and
     * {@code 16}, both inclusive.
     * @return a char value
     * @throws IOException if an I/O error occurs.
     */
    char readChar(int size) throws IOException;

    /**
     * Aligns to given number of bytes.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded while aligning
     * @throws IllegalArgumentException if {@code bytes} is less than {@code 1}.
     * @throws IOException if an I/O error occurs.
     */
    long align(int bytes) throws IOException;
}
