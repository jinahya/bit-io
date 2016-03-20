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
     * Writes a 1-bit boolean value. This method writes {@code 0b1} for
     * {@code true} and {@code 0b0} for {@code false}.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs
     */
    void writeBoolean(boolean value) throws IOException;

    /**
     * Writes a {@code byte} value.
     *
     * @param unsigned a flag indicating unsigned value.
     * @param size the number of bits for value ranged
     * {@code [1..(7 + (unsigned ? 0 : 1))]}
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeByte(boolean unsigned, int size, byte value) throws IOException;

    @Deprecated
    void writeUnsignedByte(int size, byte value) throws IOException;

    @Deprecated
    void writeByte(int size, byte value) throws IOException;

    /**
     * Writes a {@code short} value.
     *
     * @param unsigned a flag for unsigned value
     * @param size the number of bits for value ranged
     * {@code [1..(15 + (unsigned ? 0 : 1))]}
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeShort(boolean unsigned, int size, short value) throws IOException;

    @Deprecated
    void writeUnsignedShort(int size, short value) throws IOException;

    @Deprecated
    void writeShort(int size, short value) throws IOException;

    /**
     * Writes an {@code int} value. Only the lower specified number of bits are
     * written.
     *
     * @param unsigned a flag for unsigned value.
     * @param size the number of bits for value range
     * {@code [1..(31 + (unsigned ? 0 : 1))]}
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeInt(boolean unsigned, int size, int value) throws IOException;

    @Deprecated
    void writeUnsignedInt(int size, int value) throws IOException;

    @Deprecated
    void writeInt(int size, int value) throws IOException;

    /**
     * Writes a {@code long} value. Only the lower specified number of bits are
     * written.
     *
     * @param unsigned a flag for unsigned value
     * @param size the number of valid bits for value ranged
     * {@code [1..(63 + unsigned ? 0 : 1))]}
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeLong(boolean unsigned, int size, long value) throws IOException;

    @Deprecated
    void writeUnsignedLong(int size, long value) throws IOException;

    @Deprecated
    void writeLong(int size, long value) throws IOException;

    /**
     * Writes a {@code char} value.
     *
     * @param size the number of bits for value; between {@code 1} (inclusive)
     * and {@code 16} (inclusive)
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     */
    void writeChar(int size, char value) throws IOException;

    /**
     * Writes a {@code float} value. This method converts given {@code value} to
     * a 32-bit signed int value using {@link Float#floatToRawIntBits(float)}
     * and writes the result using {@link #writeInt(boolean, int, int)} with
     * {@code false}, {@code 32}, and the {@code result}.
     *
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     * @see Float#floatToRawIntBits(float)
     * @see #writeInt(boolean, int, int)
     * @deprecated Use {@link #writeInt(boolean, int, int)} with {@code true}
     * and {@value Integer#SIZE} and the value converted with
     * {@link Float#floatToRawIntBits(float)}.
     */
    @Deprecated
    void writeFloat(float value) throws IOException;

    /**
     * Writes a {@code double} value. This method converts given {@code value}
     * to a 64-bit signed long value using
     * {@link Double#doubleToRawLongBits(double)} and writes the result using
     * {@link #writeLong(boolean, int, long)} with {@code false}, {@code 64},
     * and the {@code result}.
     *
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     * @see Double#doubleToRawLongBits(double)
     * @see #writeLong(boolean, int, long)
     * @deprecated Use {@link #writeLong(boolean, int, long)} with {@code true}
     * and {@value Long#SIZE} and the value converted with
     * {@link Double#doubleToRawLongBits(double)}.
     */
    @Deprecated
    void writeDouble(double value) throws IOException;

//    /**
//     * Returns the number of bytes written so far.
//     *
//     * @return number of byte written so far.
//     */
//    long getCount();
//    /**
//     * Returns the bit index to write in current octet.
//     *
//     * @return bit index to write in current octet.
//     */
//    int getIndex();
    /**
     * Aligns to specified number of bytes.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits padded while aligning
     * @throws IllegalArgumentException if {@code bytes} is less than {@code 1}.
     * @throws IOException if an I/O error occurs.
     */
    long align(int bytes) throws IOException;
}
