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
 * An interface for writing values of an arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitInput
 */
public interface BitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code 1}-bit {@code boolean} value. This method writes {@code 0b1} for {@code true} and {@code 0b0} for
     * {@code false}.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readBoolean()
     */
    void writeBoolean(boolean value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Byte#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readByte(boolean, int)
     */
    void writeByte(boolean unsigned, int size, byte value) throws IOException;

    /**
     * Writes specified {@value java.lang.Byte#SIZE}-bit signed {@code byte} value.
     *
     * @param value the {@value java.lang.Byte#SIZE}-bit signed {@code byte} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readByte8()
     */
    void writeByte8(byte value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code short} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Short#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readShort(boolean, int)
     */
    void writeShort(boolean unsigned, int size, short value) throws IOException;

    /**
     * Writes specified {@value java.lang.Short#SIZE}-bit signed {@code short} value.
     *
     * @param value the {@value java.lang.Short#SIZE}-bit signed {@code short} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readShort16()
     */
    void writeShort16(short value) throws IOException;

    /**
     * Writes specified {@value java.lang.Short#SIZE}-bit signed {@code short} value in little endian byte order.
     *
     * @param value the {@value java.lang.Short#SIZE}-bit signed {@code short} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readShort16Le()
     */
    void writeShort16Le(short value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes an {@code int} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Integer#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readInt(boolean, int)
     */
    void writeInt(boolean unsigned, int size, int value) throws IOException;

    /**
     * Writes specified {@value java.lang.Integer#SIZE}-bit signed {@code int} value.
     *
     * @param value the {@value java.lang.Integer#SIZE}-bit signed {@code int} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readInt32()
     */
    void writeInt32(int value) throws IOException;

    /**
     * Writes specified {@value java.lang.Integer#SIZE}-bit signed {@code int} value in little endian byte order.
     *
     * @param value the {@value java.lang.Integer#SIZE}-bit signed {@code int} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readInt32Le()
     */
    void writeInt32Le(int value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code long} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits to write; between {@code 1} and ({@value java.lang.Long#SIZE} - (unsigned ?
     *                 {@code 1} : {@code 0})), both inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readLong(boolean, int)
     */
    void writeLong(boolean unsigned, int size, long value) throws IOException;

    /**
     * Writes specified {@value java.lang.Long#SIZE}-bit signed {@code long} value.
     *
     * @param value the {@value java.lang.Long#SIZE}-bit signed {@code long} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readLong64()
     */
    void writeLong64(long value) throws IOException;

    /**
     * Writes specified {@value java.lang.Long#SIZE}-bit signed {@code long} value in little endian byte order.
     *
     * @param value the {@value java.lang.Long#SIZE}-bit signed {@code long} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readLong64Le()
     */
    void writeLong64Le(long value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code char} value of specified number of bits.
     *
     * @param size  the number of bits to write; between {@code 1} and {@value java.lang.Character#SIZE}, both
     *              inclusive.
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     * @see #writeInt(boolean, int, int)
     * @see BitInput#readChar(int)
     */
    void writeChar(int size, char value) throws IOException;

    /**
     * Writes specified {@value java.lang.Character#SIZE}-bit {@code char} value.
     *
     * @param value a {@value java.lang.Character#SIZE}-bit {@code char} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readChar16()
     */
    void writeChar16(char value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Skips specified number of bits by padding zero bits.
     *
     * @param bits the number of bit to skip; must be positive.
     * @throws IllegalArgumentException if {@code bits} is not positive.
     * @throws IOException              if an I/O error occurs.
     */
    void skip(int bits) throws IOException;

    /**
     * Aligns to specified number of bytes by padding zero bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of bits padded while aligning.
     * @throws IllegalArgumentException if {@code bytes} is not positive.
     * @throws IOException              if an I/O error occurs.
     * @see BitInput#align(int)
     */
    long align(int bytes) throws IOException;
}
