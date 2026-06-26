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
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
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
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
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
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
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
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
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
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
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
     * @throws IllegalArgumentException if {@code size} is not valid.
     * @throws IOException              if an I/O error occurs.
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

    /**
     * Writes specified {@value java.lang.Character#SIZE}-bit {@code char} value in little endian byte order.
     *
     * @param value a {@value java.lang.Character#SIZE}-bit {@code char} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readChar16Le()
     */
    void writeChar16Le(char value) throws IOException;

    // ----------------------------------------------------------------------------------------------------------- float

    /**
     * Writes specified {@code float} value packed into {@code 1 + exponentSize + fractionSize} bits using an IEEE-like
     * <em>reduced</em> encoding (sign, rebiased exponent, high-bit-kept fraction).
     *
     * <p>The encoding is a deterministic, mode-less projection that depends only on the raw bits observed from
     * {@link Float#floatToRawIntBits(float)} and the two widths: {@code ±0}, {@code ±Infinity} and NaN round-trip as
     * packed-format states; quiet/signaling NaN is preserved for the raw NaN bits observable at the write point, since
     * both widths are {@code >= 2}. A magnitude too large to represent saturates to {@code ±Infinity}, one too small
     * underflows to {@code ±0}, and an over-wide fraction is truncated toward zero (not rounded). At the full native
     * widths ({@code exponentSize} of {@code 8}, {@code fractionSize} of {@code 23}) the packed bits are lossless. See
     * {@link #writeFloat32(float)} for the unconditionally lossless full-width path.
     *
     * @param exponentSize the number of bits for the exponent; between {@code 2} and {@code 8}, both inclusive.
     * @param fractionSize the number of bits for the fraction(significand); between {@code 2} and {@code 23}, both
     *                     inclusive.
     * @param value        the {@code float} value to write.
     * @throws IllegalArgumentException if {@code exponentSize} or {@code fractionSize} is not valid.
     * @throws IOException              if an I/O error occurs.
     * @see BitInput#readFloat(int, int)
     */
    void writeFloat(int exponentSize, int fractionSize, float value) throws IOException;

    /**
     * Writes specified {@code float} value as a {@value java.lang.Float#SIZE}-bit pattern, losslessly. The value is
     * reinterpreted via {@link Float#floatToRawIntBits(float)} and written with {@link #writeInt32(int)}; no
     * exponent/fraction reduction is applied.
     *
     * @param value the {@code float} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readFloat32()
     */
    void writeFloat32(float value) throws IOException;

    // ---------------------------------------------------------------------------------------------------------- double

    /**
     * Writes specified {@code double} value packed into {@code 1 + exponentSize + fractionSize} bits using an
     * IEEE-like
     * <em>reduced</em> encoding (sign, rebiased exponent, high-bit-kept fraction).
     *
     * <p>The encoding is a deterministic, mode-less projection that depends only on the raw bits observed from
     * {@link Double#doubleToRawLongBits(double)} and the two widths: {@code ±0}, {@code ±Infinity} and NaN round-trip
     * as packed-format states; quiet/signaling NaN is preserved for the raw NaN bits observable at the write point,
     * since both widths are {@code >= 2}. A magnitude too large to represent saturates to {@code ±Infinity}, one too
     * small underflows to {@code ±0}, and an over-wide fraction is truncated toward zero (not rounded). At the full
     * native widths ({@code exponentSize} of {@code 11}, {@code fractionSize} of {@code 52}) the packed bits are
     * lossless. See {@link #writeDouble64(double)} for the unconditionally lossless full-width path.
     *
     * @param exponentSize the number of bits for the exponent; between {@code 2} and {@code 11}, both inclusive.
     * @param fractionSize the number of bits for the fraction(significand); between {@code 2} and {@code 52}, both
     *                     inclusive.
     * @param value        the {@code double} value to write.
     * @throws IllegalArgumentException if {@code exponentSize} or {@code fractionSize} is not valid.
     * @throws IOException              if an I/O error occurs.
     * @see BitInput#readDouble(int, int)
     */
    void writeDouble(int exponentSize, int fractionSize, double value) throws IOException;

    /**
     * Writes specified {@code double} value as a {@value java.lang.Double#SIZE}-bit pattern, losslessly. The value is
     * reinterpreted via {@link Double#doubleToRawLongBits(double)} and written with {@link #writeLong64(long)}; no
     * exponent/fraction reduction is applied.
     *
     * @param value the {@code double} value to write.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#readDouble64()
     */
    void writeDouble64(double value) throws IOException;

    // ---------------------------------------------------------------------------------------------------------- object

    /**
     * Writes specified value using specified writer.
     *
     * @param writer the writer; must not be {@code null}.
     * @param value  the value to write.
     * @param <T>    value type parameter
     * @throws NullPointerException if {@code writer} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @see BitInput#readObject(BitReader)
     */
    <T> void writeObject(BitWriter<? super T> writer, T value) throws IOException;

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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the number of bytes written so far. Only <em>complete</em> octets are counted; bits of the current
     * partially-written octet are not included until the octet is completed (e.g. by {@link #align(int)}).
     *
     * @return the number of bytes written so far.
     * @see BitInput#getCount()
     */
    long getCount();
}
