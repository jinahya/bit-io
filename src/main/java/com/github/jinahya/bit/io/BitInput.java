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
import java.util.List;
import java.util.function.Function;


/**
 * An interface for reading arbitrary length of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface BitInput {


    /**
     * Reads a 1-bit boolean value. This method reads {@code true} for
     * {@code 0b1} and {@code false} for {@code 0b0}.
     *
     * @return {@code true} for {@code 0b1}, {@code false} for {@code 0b0}
     *
     * @throws IOException if an I/O error occurs.
     */
    boolean readBoolean() throws IOException;


    /**
     * Reads an unsigned int value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#UINT_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#UINT_SIZE_MAX}
     * (inclusive).
     *
     * @return the unsigned int value
     *
     * @throws IOException if an I/O error occurs
     */
    int readUnsignedInt(int size) throws IOException;


    /**
     * Reads a signed int value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#INT_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#INT_SIZE_MAX}
     * (inclusive).
     *
     * @return a signed int value.
     *
     * @throws IOException if an I/O error occurs.
     */
    int readInt(int size) throws IOException;


    /**
     * Reads an unsigned long value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#ULONG_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#ULONG_SIZE_MAX}
     * (inclusive).
     *
     * @return an unsigned long value.
     *
     * @throws IOException if an I/O error occurs
     */
    long readUnsignedLong(int size) throws IOException;


    /**
     * Reads a signed long value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#LONG_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#LONG_SIZE_MAX}
     * (inclusive).
     *
     * @return a signed long value
     *
     * @throws IOException if an I/O error occurs.
     */
    long readLong(int size) throws IOException;


    <T> T readObject(Function<BitInput, T> reader) throws IOException;


    <T> T[] readArray(int scale, Function<BitInput, T> reader)
        throws IOException;


    <T> List<T> readList(int scale, Function<BitInput, T> reader)
        throws IOException;


    /**
     * Reads specified number of bytes and set on given array starting from
     * specified offset.
     *
     * @param array the array
     * @param offset the start offset of array
     * @param length number of bytes to read
     * @param range valid number of lower bits in each byte
     *
     * @throws IOException if an I/O error occurs.
     */
    void readBytes(byte[] array, int offset, int length, int range)
        throws IOException;


    /**
     * Reads a variable length of bytes.
     *
     * @param scale number of bits for length;
     * @param range valid number of lower bits in each byte.
     *
     * @return an array of bytes.
     *
     * @throws IOException if an I/O error occurs.
     */
    byte[] readBytes(int scale, int range) throws IOException;


    /**
     * Aligns to given number of bytes.
     *
     * @param bytes the number of bytes to align; must be positive.
     *
     * @return the number of bits discarded for alignment
     *
     * @throws IOException if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

}

