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
 * An interface for reading values of arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitOutput
 */
public interface BitInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code 1}-bit {@code boolean} value. This method reads a {@code 1}-bit unsigned {@code int} and returns
     * {@code true} for {@code 0b1} and {@code false} for {@code 0b0}.
     *
     * @return {@code true} for {@code 0b1}, {@code false} for {@code 0b0}
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeBoolean(boolean)
     */
    boolean readBoolean() throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits for value; between {@code 1} and ({@code 7 + (unsigned ? 0 : 1)}), both
     *                 inclusive.
     * @return a {@code byte} value of given {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readSignedByte(int)
     * @see #readUnsignedByte(int)
     * @see BitOutput#writeByte(boolean, int, byte)
     */
    byte readByte(boolean unsigned, int size) throws IOException;

    /**
     * Reads a signed {@code byte} value of specified number of bits.
     *
     * @param size the number of bits for value; between {@code 1} and {@value Byte#SIZE}, both inclusive.
     * @return a signed {@code byte} value of given {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readByte(boolean, int)
     * @see #readUnsignedByte(int)
     * @see BitOutput#writeSignedByte(int, byte)
     */
    byte readSignedByte(int size) throws IOException;

    /**
     * Reads an unsigned {@code byte} value of specified number of bits.
     *
     * @param size the number of bits for value; between {@code 1} and {@code 7}, both inclusive.
     * @return an unsigned {@code byte} value of given {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readByte(boolean, int)
     * @see #readSignedByte(int)
     * @see BitOutput#writeUnsignedByte(int, byte)
     */
    byte readUnsignedByte(int size) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code short} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits for value; between {@code 1} and ({@code 15 + (unsigned ? 0 : 1)}), both
     *                 inclusive.
     * @return a {@code short} value of given {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readSignedShort(int)
     * @see #readUnsignedShort(int)
     * @see BitOutput#writeShort(boolean, int, short)
     */
    short readShort(boolean unsigned, int size) throws IOException;

    /**
     * Reads a signed {@code short} value of specified number of bits.
     *
     * @param size the number of bits for value; between {@code 1} and {@value Short#SIZE}, both inclusive.
     * @return a signed {@code short} value of given {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readShort(boolean, int)
     * @see #readUnsignedShort(int)
     * @see BitOutput#writeSignedShort(int, short)
     */
    short readSignedShort(int size) throws IOException;

    /**
     * Reads an unsigned {@code short} value of specified number of bits.
     *
     * @param size the number of bits for value; between {@code 1} and {@code 15}, both inclusive.
     * @return a {@code short} value of given {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readShort(boolean, int)
     * @see #readSignedShort(int)
     * @see BitOutput#writeUnsignedShort(int, short)
     */
    short readUnsignedShort(int size) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits for value; between {@code 1} and ({@code 31 + (unsigned ? 0 : 1)}), both
     *                 inclusive.
     * @return an int value.
     * @throws IOException if an I/O error occurs.
     * @see #readSignedInt(int)
     * @see #readUnsignedInt(int)
     * @see BitOutput#writeInt(boolean, int, int)
     */
    int readInt(boolean unsigned, int size) throws IOException;

    /**
     * Reads a signed {@code int} value of specified number of bits.
     *
     * @param size the number of bits for value; between {@code 1} and {@value Integer#SIZE}, both inclusive.
     * @return a signed {@code int} value of specified bit size.
     * @throws IOException if an I/O error occurs.
     * @see #readInt(boolean, int)
     * @see #readUnsignedInt(int)
     * @see BitOutput#writeSignedInt(int, int)
     */
    int readSignedInt(int size) throws IOException;

    /**
     * Reads an unsigned {@code int} value of specified bit size.
     *
     * @param size number of bits for value; between {@code 1} and {@code 31}, both inclusive.
     * @return an unsigned {@code int} value of specified bit size.
     * @throws IOException if an I/O error occurs.
     * @see #readInt(boolean, int)
     * @see #readSignedInt(int)
     * @see BitOutput#writeUnsignedInt(int, int)
     */
    int readUnsignedInt(int size) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@code 63 + (unsigned ? 0 : 1)}), both
     *                 inclusive.
     * @return a {@code long} value of specified bit size.
     * @throws IOException if an I/O error occurs.
     * @see #readSignedLong(int)
     * @see #readUnsignedLong(int)
     * @see BitOutput#writeLong(boolean, int, long)
     */
    long readLong(boolean unsigned, int size) throws IOException;

    /**
     * Reads a signed {@code long} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value Long#SIZE}, both inclusive.
     * @return an unsigned {@code long} value of specified bit size.
     * @throws IOException if an I/O error occurs.
     * @see #readLong(boolean, int)
     * @see #readUnsignedLong(int)
     * @see BitOutput#writeSignedLong(int, long)
     */
    long readSignedLong(int size) throws IOException;

    /**
     * Reads an unsigned {@code long} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@code 63}, both inclusive.
     * @return a {@code long} value of specified bit size.
     * @throws IOException if an I/O error occurs.
     * @see #readLong(boolean, int)
     * @see #readSignedLong(int)
     * @see BitOutput#writeUnsignedLong(int, long)
     */
    long readUnsignedLong(int size) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code char} value of specified bit size.
     *
     * @param size the number of bits for value; between {@code 1} and {@value Character#SIZE}, both inclusive.
     * @return a {@code char} value
     * @throws IOException if an I/O error occurs.
     * @see #readUnsignedInt(int)
     * @see BitOutput#writeChar(int, char)
     */
    char readChar(int size) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Aligns to given number of bytes by discarding bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded while aligning
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#align(int)
     */
    long align(int bytes) throws IOException;
}
