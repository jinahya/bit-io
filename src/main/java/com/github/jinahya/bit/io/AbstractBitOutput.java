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

import static com.github.jinahya.bit.io.BitIoConstants.FLAG_SIZE;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidExponentSizeDouble;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidExponentSizeFloat;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidExponentSizeHalf;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidFractionSizeDouble;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidFractionSizeFloat;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidFractionSizeHalf;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeChar;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeShort;

/**
 * An abstract class for implementing {@link BitOutput} interface.
 *
 * <p>Instances are <strong>not</strong> thread-safe: they hold mutable bit-position state ({@code octet},
 * {@code available}, {@code count}) that is updated without synchronization, so an instance must be confined to a
 * single thread.</p>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see AbstractBitInput
 * @see DefaultBitOutput
 */
public abstract class AbstractBitOutput
        implements BitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    protected AbstractBitOutput() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes given {@value java.lang.Byte#SIZE}-bit unsigned integer.
     *
     * @param value the {@value java.lang.Byte#SIZE}-bit unsigned integer to write.
     * @throws IOException if an I/O error occurs.
     * @see AbstractBitInput#read()
     */
    protected abstract void write(int value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes an unsigned {@code int} value of specified bit size which is, in maximum, {@value java.lang.Byte#SIZE}.
     *
     * @param size  the number of lower bits to write; between {@code 1} and {@value java.lang.Byte#SIZE}, both
     *              inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #write(int)
     */
    private void unsigned8(final int size, final int value) throws IOException {
        final int required = size - available;
        if (required > 0) {
            unsigned8(available, value >> required);
            unsigned8(required, value);
            return;
        }
        octet <<= size;
        octet |= value & ((1 << size) - 1);
        available -= size;
        if (available == 0) {
            assert octet >= 0 && octet < 256;
            write(octet);
            count++;
            octet = 0x00;
            available = Byte.SIZE;
        }
    }

    // --------------------------------------------------------------------------------------------------------- boolean
    @Override
    public void writeBoolean(final boolean value) throws IOException {
        writeInt(true, FLAG_SIZE, value ? 1 : 0);
    }

    // ------------------------------------------------------------------------------------------------------------ byte
    @Override
    public void writeByte(final boolean unsigned, final int size, final byte value) throws IOException {
        writeInt(unsigned, requireValidSizeByte(unsigned, size), value);
    }

    @Override
    public void writeByte8(final byte value) throws IOException {
        writeByte(false, Byte.SIZE, value);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @Override
    public void writeShort(final boolean unsigned, final int size, final short value) throws IOException {
        writeInt(unsigned, requireValidSizeShort(unsigned, size), value);
    }

    @Override
    public void writeShort16(final short value) throws IOException {
        writeShort(false, Short.SIZE, value);
    }

    @Override
    public void writeShort16Le(final short value) throws IOException {
        writeByte8((byte) value);
        writeByte8((byte) (value >> Byte.SIZE));
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @Override
    public void writeInt(final boolean unsigned, int size, final int value) throws IOException {
        requireValidSizeInt(unsigned, size);
        if (!unsigned) {
            writeInt(true, FLAG_SIZE, value < 0 ? 1 : 0);
            if (--size > 0) {
                writeInt(true, size, value);
            }
            return;
        }
        final int quotient = size >> 3;
        final int remainder = size & 7;
        if (remainder > 0) {
            unsigned8(remainder, value >> (quotient << 3));
        }
        for (int i = Byte.SIZE * (quotient - 1); i >= 0; i -= Byte.SIZE) {
            unsigned8(Byte.SIZE, value >> i);
        }
    }

    @Override
    public void writeInt32(final int value) throws IOException {
        writeInt(false, Integer.SIZE, value);
    }

    @Override
    public void writeInt32Le(final int value) throws IOException {
        writeShort16Le((short) value);
        writeShort16Le((short) (value >> Short.SIZE));
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @Override
    public void writeLong(final boolean unsigned, int size, final long value) throws IOException {
        requireValidSizeLong(unsigned, size);
        if (!unsigned) {
            writeInt(true, FLAG_SIZE, value < 0L ? 0x01 : 0x00);
            if (--size > 0) {
                writeLong(true, size, value);
            }
            return;
        }
        if (size >= Integer.SIZE) {
            writeInt(false, Integer.SIZE, (int) (value >> (size - Integer.SIZE)));
            size -= Integer.SIZE;
        }
        if (size > 0) {
            writeInt(true, size, (int) value);
        }
    }

    @Override
    public void writeLong64(final long value) throws IOException {
        writeLong(false, Long.SIZE, value);
    }

    @Override
    public void writeLong64Le(final long value) throws IOException {
        writeInt32Le((int) value);
        writeInt32Le((int) (value >> Integer.SIZE));
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @Override
    public void writeChar(final int size, final char value) throws IOException {
        writeInt(true, requireValidSizeChar(size), value);
    }

    @Override
    public void writeChar16(final char value) throws IOException {
        writeChar(Character.SIZE, value);
    }

    @Override
    public void writeChar16Le(final char value) throws IOException {
        writeShort16Le((short) value);
    }

    // ------------------------------------------------------------------------------------------------------------ half
    @Override
    public void writeHalf(final int exponentSize, final int fractionSize, final float value) throws IOException {
        requireValidExponentSizeHalf(exponentSize);
        requireValidFractionSizeHalf(fractionSize);
        writeFloat(exponentSize, fractionSize, value); // reduced encoding over the float carrier, bounded to binary16
    }

    @Override
    public void writeHalf16(final float value) throws IOException {
        writeShort16((short) halfBits(value));
    }

    @Override
    public void writeHalf16Le(final float value) throws IOException {
        writeShort16Le((short) halfBits(value));
    }

    /**
     * Encodes specified {@code float} into a conformant IEEE 754 {@code binary16} bit pattern (RNE rounding, subnormal
     * generation, saturation, quiet/signaling-preserving NaN).
     *
     * @param value the value to encode.
     * @return the {@code binary16} bit pattern, in the low {@value java.lang.Short#SIZE} bits.
     */
    private static int halfBits(final float value) {
        final int bits = Float.floatToRawIntBits(value);
        final int sign = (bits >>> 16) & 0x8000;     // half sign bit (bit 15)
        final int rawExp = (bits >>> 23) & 0xFF;     // native float exponent
        int rawFrac = bits & 0x7FFFFF;               // native float fraction
        int half;
        if (rawExp == 0xFF) {                         // Infinity / NaN
            if (rawFrac == 0) {                       // Infinity
                half = sign | 0x7C00;
            } else {                                  // NaN: preserve quiet/signaling (consistent with writeFloat)
                int frac = rawFrac >>> 13;            // top 10 bits
                if ((rawFrac & 0x400000) != 0) {      // qNaN: native quiet bit (bit 22) set
                    frac |= 0x0200;                   // 10...
                } else {                              // sNaN
                    frac &= ~0x0200;                  // keep quiet bit clear
                    if (frac == 0) {                  // payload truncated away -> force 01...
                        frac = 0x0100;
                    }
                }
                half = sign | 0x7C00 | frac;
            }
        } else {
            final int e = (rawExp - 127) + 15;        // candidate half biased exponent
            if (e >= 0x1F) {                          // overflow -> Infinity
                half = sign | 0x7C00;
            } else if (e <= 0) {                      // subnormal range or underflow
                if (e < -10) {                        // smaller than half of the least subnormal -> ±0
                    half = sign;
                } else {                              // subnormal, round to nearest even
                    rawFrac |= 0x800000;              // restore the implicit leading 1 (24-bit significand)
                    final int shift = 14 - e;         // 14 .. 24
                    int frac = rawFrac >>> shift;
                    final int rem = rawFrac & ((1 << shift) - 1);
                    final int halfway = 1 << (shift - 1);
                    if (rem > halfway || (rem == halfway && (frac & 1) != 0)) {
                        frac++;                       // carry may reach the least normal (0x400)
                    }
                    half = sign | frac;
                }
            } else {                                  // normal, round to nearest even
                int frac = rawFrac >>> 13;            // top 10 bits
                final int rem = rawFrac & 0x1FFF;     // low 13 bits dropped
                half = sign | (e << 10) | frac;
                if (rem > 0x1000 || (rem == 0x1000 && (frac & 1) != 0)) {
                    half++;                           // carry bumps exponent; e==30 may round to Infinity
                }
            }
        }
        return half;
    }

    // ----------------------------------------------------------------------------------------------------------- float
    @Override
    public void writeFloat(final int exponentSize, final int fractionSize, final float value) throws IOException {
        requireValidExponentSizeFloat(exponentSize);
        requireValidFractionSizeFloat(fractionSize);
        final int bits = Float.floatToRawIntBits(value);
        final int sign = (bits >>> 31) & 0x01;
        final int rawExp = (bits >>> 23) & 0xFF;
        final int rawFrac = bits & 0x7FFFFF;
        final int expMask = (1 << exponentSize) - 1;
        final int newBias = (1 << (exponentSize - 1)) - 1;
        final int fracShift = 23 - fractionSize;
        int storedFrac = rawFrac >>> fracShift; // keep the high fractionSize bits
        final int storedExp;
        if (rawExp == 0xFF) {                    // Infinity / NaN
            storedExp = expMask;
            if (rawFrac != 0) {                  // NaN: keep it a NaN, preserve quiet/signaling
                final int msb = 1 << (fractionSize - 1);
                if ((rawFrac & 0x400000) != 0) { // qNaN: native bit 22 (the quiet bit) is set
                    storedFrac |= msb;
                } else {                         // sNaN: keep the quiet bit clear, guard against collapsing to Infinity
                    storedFrac &= ~msb;
                    if (storedFrac == 0) {       // payload truncated away
                        storedFrac = msb >> 1;   // force the next-highest bit (fractionSize >= 2 guaranteed)
                    }
                }
            }
        } else if (rawExp == 0) {                // signed zero / native subnormal
            storedExp = 0;
        } else {
            final int stored = (rawExp - 127) + newBias;
            if (stored >= expMask) {             // overflow -> Infinity
                storedExp = expMask;
                storedFrac = 0;
            } else if (stored <= 0) {            // underflow -> signed zero
                storedExp = 0;
                storedFrac = 0;
            } else {                             // finite normal
                storedExp = stored;
            }
        }
        writeInt(true, FLAG_SIZE, sign);
        writeInt(true, exponentSize, storedExp);
        writeInt(true, fractionSize, storedFrac);
    }

    @Override
    public void writeFloat32(final float value) throws IOException {
        writeInt32(Float.floatToRawIntBits(value));
    }

    @Override
    public void writeFloat32Le(final float value) throws IOException {
        writeInt32Le(Float.floatToRawIntBits(value));
    }

    // ---------------------------------------------------------------------------------------------------------- double
    @Override
    public void writeDouble(final int exponentSize, final int fractionSize, final double value) throws IOException {
        requireValidExponentSizeDouble(exponentSize);
        requireValidFractionSizeDouble(fractionSize);
        final long bits = Double.doubleToRawLongBits(value);
        final int sign = (int) ((bits >>> 63) & 0x01L);
        final int rawExp = (int) ((bits >>> 52) & 0x7FFL);
        final long rawFrac = bits & 0x000FFFFFFFFFFFFFL;
        final int expMask = (1 << exponentSize) - 1;
        final int newBias = (1 << (exponentSize - 1)) - 1;
        final int fracShift = 52 - fractionSize;
        long storedFrac = rawFrac >>> fracShift; // keep the high fractionSize bits
        final int storedExp;
        if (rawExp == 0x7FF) {                   // Infinity / NaN
            storedExp = expMask;
            if (rawFrac != 0L) {                 // NaN: keep it a NaN, preserve quiet/signaling
                final long msb = 1L << (fractionSize - 1);
                if ((rawFrac & 0x0008000000000000L) != 0L) { // qNaN: native bit 51 (the quiet bit) is set
                    storedFrac |= msb;
                } else {                         // sNaN: keep the quiet bit clear, guard against collapsing to Infinity
                    storedFrac &= ~msb;
                    if (storedFrac == 0L) {      // payload truncated away
                        storedFrac = msb >> 1;   // force the next-highest bit (fractionSize >= 2 guaranteed)
                    }
                }
            }
        } else if (rawExp == 0) {                // signed zero / native subnormal
            storedExp = 0;
        } else {
            final int stored = (rawExp - 1023) + newBias;
            if (stored >= expMask) {             // overflow -> Infinity
                storedExp = expMask;
                storedFrac = 0L;
            } else if (stored <= 0) {            // underflow -> signed zero
                storedExp = 0;
                storedFrac = 0L;
            } else {                             // finite normal
                storedExp = stored;
            }
        }
        writeInt(true, FLAG_SIZE, sign);
        writeInt(true, exponentSize, storedExp);
        writeLong(true, fractionSize, storedFrac);
    }

    @Override
    public void writeDouble64(final double value) throws IOException {
        writeLong64(Double.doubleToRawLongBits(value));
    }

    @Override
    public void writeDouble64Le(final double value) throws IOException {
        writeLong64Le(Double.doubleToRawLongBits(value));
    }

    // ---------------------------------------------------------------------------------------------------------- object

    /**
     * Writes specified value using specified writer.
     *
     * @param writer the writer; must not be {@code null}.
     * @param value  the value to write.
     * @param <T>    value type parameter
     * @throws NullPointerException if {@code writer} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @see AbstractBitInput#readObject(BitReader)
     */
    @Override
    public <T> void writeObject(final BitWriter<? super T> writer, final T value) throws IOException {
        if (writer == null) {
            throw new NullPointerException("writer is null");
        }
        writer.write(this, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void skip(int bits) throws IOException {
        if (bits <= 0) {
            throw new IllegalArgumentException("bits(" + bits + ") <= 0");
        }
        for (; bits >= Integer.SIZE; bits -= Integer.SIZE) {
            writeInt(false, Integer.SIZE, 0);
        }
        if (bits > 0) {
            writeInt(true, bits, 0);
        }
    }

    @Override
    public long align(int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }
        long bits = 0L; // number of bits padded
        if (available < Byte.SIZE) {
            bits += available; // must be prior to the below
            writeInt(true, available, 0x00);
        }
        if (bytes == 1) {
            return bits;
        }
        for (bytes = (bytes - (int) (count % bytes)) % bytes; bytes > 0; bytes--) {
            writeInt(true, Byte.SIZE, 0x00);
            bits += Byte.SIZE;
        }
        return bits;
    }

    // ----------------------------------------------------------------------------------------------------------- count

    /**
     * Returns the number of bytes written so far.
     *
     * @return the number of bytes written so far.
     * @see #write(int)
     * @see AbstractBitInput#getCount()
     */
    @Override
    public long getCount() {
        return count;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The current octet.
     */
    private int octet;

    /**
     * The number of available bits in {@link #octet} for writing.
     */
    private int available = Byte.SIZE;

    /**
     * The number of bytes written so far.
     */
    private long count;
}
