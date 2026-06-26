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
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
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
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#writeByte(boolean, int, byte)
     */
    byte readByte(boolean unsigned, int size) throws IOException;

    /**
     * Reads an {@value java.lang.Byte#SIZE}-bit signed {@code byte} value.
     *
     * @return an {@value java.lang.Byte#SIZE}-bit signed {@code byte} value.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeByte8(byte)
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
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#writeShort(boolean, int, short)
     */
    short readShort(boolean unsigned, int size) throws IOException;

    /**
     * Reads a {@value java.lang.Short#SIZE}-bit signed {@code short} value.
     *
     * @return a {@value java.lang.Short#SIZE}-bit signed {@code short} value.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeShort16(short)
     */
    short readShort16() throws IOException;

    /**
     * Reads a {@value java.lang.Short#SIZE}-bit signed {@code short} value encoded in little endian byte order.
     *
     * @return a signed {@value java.lang.Short#SIZE}-bit {@code short} value encoded in little endian byte order.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeShort16Le(short)
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
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#writeInt(boolean, int, int)
     */
    int readInt(boolean unsigned, int size) throws IOException;

    /**
     * Reads a {@value java.lang.Integer#SIZE}-bit signed {@code int} value.
     *
     * @return a {@value java.lang.Integer#SIZE}-bit signed {@code int} value.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeInt32(int)
     */
    int readInt32() throws IOException;

    /**
     * Reads a {@value java.lang.Integer#SIZE}-bit signed {@code int} value encoded in little endian byte order.
     *
     * @return a {@value java.lang.Integer#SIZE}-bit signed {@code int} value encoded in little endian byte order.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeInt32Le(int)
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
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#writeLong(boolean, int, long)
     */
    long readLong(boolean unsigned, int size) throws IOException;

    /**
     * Reads a {@value java.lang.Long#SIZE}-bit signed {@code long} value.
     *
     * @return a {@value java.lang.Long#SIZE}-bit signed {@code long} value.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeLong64(long)
     */
    long readLong64() throws IOException;

    /**
     * Reads a {@value java.lang.Long#SIZE}-bit signed {@code long} value encoded in little endian byte order.
     *
     * @return a {@value java.lang.Long#SIZE}-bit signed {@code long} value encoded in little endian byte order.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeLong64Le(long)
     */
    long readLong64Le() throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a {@code char} value of specified number of bits.
     *
     * @param size the number of bits to read; between {@code 1} and {@value java.lang.Character#SIZE}, both inclusive.
     * @return a {@code char} value of specified {@code size}.
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#writeChar(int, char)
     */
    char readChar(int size) throws IOException;

    /**
     * Reads a {@value java.lang.Character#SIZE}-bit {@code char} value.
     *
     * @return a {@value java.lang.Character#SIZE}-bit {@code char} value.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeChar16(char)
     */
    char readChar16() throws IOException;

    /**
     * Reads a {@value java.lang.Character#SIZE}-bit {@code char} value encoded in little endian byte order.
     *
     * @return a {@value java.lang.Character#SIZE}-bit {@code char} value encoded in little endian byte order.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeChar16Le(char)
     */
    char readChar16Le() throws IOException;

    // ----------------------------------------------------------------------------------------------------------- float

    /**
     * Reads a {@code float} value packed into {@code 1 + exponentSize + fractionSize} bits by
     * {@link BitOutput#writeFloat(int, int, float)}. The same {@code (exponentSize, fractionSize)} pair must be used as
     * when writing.
     *
     * @param exponentSize the number of bits for the exponent; between {@code 2} and {@code 8}, both inclusive.
     * @param fractionSize the number of bits for the fraction(significand); between {@code 2} and {@code 23}, both
     *                     inclusive.
     * @return a {@code float} value.
     * @throws IllegalArgumentException if {@code exponentSize} or {@code fractionSize} is not valid.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#writeFloat(int, int, float)
     */
    float readFloat(int exponentSize, int fractionSize) throws IOException;

    /**
     * Reads a {@code float} value from a {@value java.lang.Float#SIZE}-bit pattern written by
     * {@link BitOutput#writeFloat32(float)}.
     *
     * @return a {@code float} value.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeFloat32(float)
     */
    float readFloat32() throws IOException;

    // ---------------------------------------------------------------------------------------------------------- double

    /**
     * Reads a {@code double} value packed into {@code 1 + exponentSize + fractionSize} bits by
     * {@link BitOutput#writeDouble(int, int, double)}. The same {@code (exponentSize, fractionSize)} pair must be used
     * as when writing.
     *
     * @param exponentSize the number of bits for the exponent; between {@code 2} and {@code 11}, both inclusive.
     * @param fractionSize the number of bits for the fraction(significand); between {@code 2} and {@code 52}, both
     *                     inclusive.
     * @return a {@code double} value.
     * @throws IllegalArgumentException if {@code exponentSize} or {@code fractionSize} is not valid.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#writeDouble(int, int, double)
     */
    double readDouble(int exponentSize, int fractionSize) throws IOException;

    /**
     * Reads a {@code double} value from a {@value java.lang.Double#SIZE}-bit pattern written by
     * {@link BitOutput#writeDouble64(double)}.
     *
     * @return a {@code double} value.
     * @throws IOException if an I/O error occurs.
     * @see BitOutput#writeDouble64(double)
     */
    double readDouble64() throws IOException;

    // ---------------------------------------------------------------------------------------------------------- object

    /**
     * Reads a value using specified reader.
     *
     * @param reader the reader; must not be {@code null}.
     * @param <T>    value type parameter
     * @return the value read by {@code reader}.
     * @throws NullPointerException if {@code reader} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @see BitOutput#writeObject(BitWriter, Object)
     */
    <T> T readObject(BitReader<? extends T> reader) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Skips specified number of bits by discarding bits.
     *
     * @param bits the number of bit to skip; must be positive.
     * @throws IllegalArgumentException if {@code bits} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    void skip(int bits) throws IOException;

    /**
     * Aligns to specified number of bytes by discarding bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits discarded while aligning.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     * @see BitOutput#align(int)
     */
    long align(int bytes) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the number of bytes read so far. Only <em>complete</em> octets are counted; bits of the current
     * partially-read octet are not included until the octet is completed (e.g. by {@link #align(int)}).
     *
     * @return the number of bytes read so far.
     * @see BitOutput#getCount()
     */
    long getCount();
}
