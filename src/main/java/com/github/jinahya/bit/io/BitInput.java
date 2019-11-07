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
 * An interface for reading values of an arbitrary number of bits.
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
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Byte#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return a {@code byte} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeByte(boolean, int, byte)
     */
    byte readByte(boolean unsigned, int size) throws IOException;

    /**
     * Reads a {@value java.lang.Byte#SIZE}-bit signed {@code byte} value. This method is equivalent to {@code
     * writeByte(true, Byte.SIZE)}.
     *
     * @return an {@value java.lang.Byte#SIZE}-bit signed {@code byte} value.
     * @throws IOException if an I/O error occurs.
     */
    byte readByte8() throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code short} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Short#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return a {@code short} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeShort(boolean, int, short)
     */
    short readShort(boolean unsigned, int size) throws IOException;

    /**
     * Reads a {@value java.lang.Short#SIZE}-bit signed {@code short} value. This method is equivalent to {@code
     * writeShort(false, Short.SIZE)}.
     *
     * @return a {@value java.lang.Short#SIZE}-bit signed {@code short} value.
     * @throws IOException if an I/O error occurs.
     */
    short readShort16() throws IOException;

    /**
     * Reads a {@value java.lang.Short#SIZE}-bit signed {@code short} value encoded in little endian byte order.
     *
     * @return a signed {@value java.lang.Short#SIZE}-bit {@code short} value encoded in little endian byte order.
     * @throws IOException if an I/O error occurs.
     */
    short readShort16Le() throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Integer#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return an {@code int} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeInt(boolean, int, int)
     */
    int readInt(boolean unsigned, int size) throws IOException;

    /**
     * Reads a {@value java.lang.Integer#SIZE}-bit signed {@code int} value. This method is equivalent to {@code
     * readInt(false, Integer.SIZE)}.
     *
     * @return a {@value java.lang.Integer#SIZE}-bit signed {@code int} value.
     * @throws IOException if an I/O error occurs.
     */
    int readInt32() throws IOException;

    /**
     * Reads a {@value java.lang.Integer#SIZE}-bit signed {@code int} value encoded in little endian byte order.
     *
     * @return a {@value java.lang.Integer#SIZE}-bit signed {@code int} value encoded in little endian byte order.
     * @throws IOException if an I/O error occurs.
     */
    int readInt32Le() throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to read; between {@code 1} and ({@value java.lang.Long#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @return a {@code long} value of specified bit size.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeLong(boolean, int, long)
     */
    long readLong(boolean unsigned, int size) throws IOException;

    /**
     * Reads a {@value java.lang.Long#SIZE}-bit signed {@code long} value. This method is equivalent to {@code
     * readLong(false, Long.SIZE)}.
     *
     * @return a {@value java.lang.Long#SIZE}-bit signed {@code long} value.
     * @throws IOException if an I/O error occurs.
     */
    long readLong64() throws IOException;

    /**
     * Reads a {@value java.lang.Long#SIZE}-bit signed {@code long} value encoded in little endian byte order.
     *
     * @return a {@value java.lang.Long#SIZE}-bit signed {@code long} value encoded in little endian byte order.
     * @throws IOException if an I/O error occurs.
     */
    long readLong64Le() throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code char} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Character#SIZE}, both inclusive.
     * @return a {@code char} value of specified {@code size}.
     * @throws IOException if an I/O error occurs.
     * @see #readInt(boolean, int)
     * @see BitOutput#writeChar(int, char)
     */
    char readChar(int size) throws IOException;

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
