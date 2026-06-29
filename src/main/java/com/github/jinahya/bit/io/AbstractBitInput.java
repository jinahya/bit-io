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
import static com.github.jinahya.bit.io.BitIoUtils.requireValidExponentSizeDouble;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidExponentSizeFloat;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidExponentSizeHalf;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidFractionSizeDouble;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidFractionSizeFloat;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidFractionSizeHalf;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeChar;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForSignedByte;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForSignedInt;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForSignedLong;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForSignedShort;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForUnsignedByte;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForUnsignedInt;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForUnsignedLong;
import static com.github.jinahya.bit.io.BitIoUtils.requireValidSizeForUnsignedShort;

/**
 * An abstract class for implementing {@link BitInput} interface.
 *
 * <p>Instances are <strong>not</strong> thread-safe: they hold mutable bit-position state ({@code octet},
 * {@code available}, {@code count}) that is updated without synchronization, so an instance must be confined to a
 * single thread.</p>
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see AbstractBitOutput
 * @see DefaultBitInput
 */
public abstract class AbstractBitInput
        implements BitInput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    protected AbstractBitInput() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an {@value java.lang.Byte#SIZE}-bit unsigned integer.
     *
     * @return an {@value java.lang.Byte#SIZE}-bit unsigned integer.
     * @throws IOException if an I/O error occurs.
     * @see AbstractBitOutput#write(int)
     */
    protected abstract int read() throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an unsigned {@code int} value of specified bit size which is, in maximum, {@value java.lang.Byte#SIZE}.
     *
     * @param size the number of bits for the value; between {@code 1} and {@value java.lang.Byte#SIZE}, both
     *             inclusive.
     * @return an unsigned byte value.
     * @throws IOException if an I/O error occurs.
     * @see #read()
     */
    private int unsigned8(final int size) throws IOException {
        if (available == 0) {
            octet = read();
            if (octet < 0 || octet > 255) {
                throw new IOException("read() returned an out-of-range value: " + octet
                                      + "; must be between 0 and 255, both inclusive");
            }
            count++;
            available = Byte.SIZE;
        }
        final int required = size - available;
        if (required > 0) {
            return (unsigned8(available) << required) | unsigned8(required);
        }
        available -= size;
        return (octet >> available) & ((1 << size) - 1);
    }

    // --------------------------------------------------------------------------------------------------------- boolean
    @Override
    public boolean readBoolean() throws IOException {
        return readUnsignedInt(FLAG_SIZE) == 1;
    }

    // ------------------------------------------------------------------------------------------------------------ byte
    @Deprecated
    @Override
    public byte readByte(final boolean unsigned, final int size) throws IOException {
        return unsigned ? readUnsignedByte(size) : readByte(size);
    }

    @Override
    public byte readUnsignedByte(final int size) throws IOException {
        return (byte) readUnsignedInt(requireValidSizeForUnsignedByte(size));
    }

    @Override
    public byte readByte(final int size) throws IOException {
        return (byte) readInt(requireValidSizeForSignedByte(size));
    }

    @Override
    public byte readByte8() throws IOException {
        return readByte(Byte.SIZE);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @Deprecated
    @Override
    public short readShort(final boolean unsigned, final int size) throws IOException {
        return unsigned ? readUnsignedShort(size) : readShort(size);
    }

    @Override
    public short readUnsignedShort(final int size) throws IOException {
        return (short) readUnsignedInt(requireValidSizeForUnsignedShort(size));
    }

    @Override
    public short readShort(final int size) throws IOException {
        return (short) readInt(requireValidSizeForSignedShort(size));
    }

    @Override
    public short readShort16() throws IOException {
        return readShort(Short.SIZE);
    }

    @Override
    public short readShort16Le() throws IOException {
        return (short) (readByte8() & 0xFF | readByte8() << Byte.SIZE);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @Deprecated
    @Override
    public int readInt(final boolean unsigned, int size) throws IOException {
        return unsigned ? readUnsignedInt(size) : readInt(size);
    }

    @Override
    public int readUnsignedInt(int size) throws IOException {
        requireValidSizeForUnsignedInt(size);
        int value = 0;
        for (; size >= Byte.SIZE; size -= Byte.SIZE) {
            value <<= Byte.SIZE;
            value |= unsigned8(Byte.SIZE);
        }
        if (size > 0) {
            value <<= size;
            value |= unsigned8(size);
        }
        return value;
    }

    @Override
    public int readInt(int size) throws IOException {
        requireValidSizeForSignedInt(size);
        int value = 0;
        value -= readUnsignedInt(FLAG_SIZE);
        if (--size > 0) {
            value <<= size;
            value |= readUnsignedInt(size);
        }
        return value;
    }

    @Override
    public int readInt32() throws IOException {
        return readInt(Integer.SIZE);
    }

    @Override
    public int readInt32Le() throws IOException {
        return readShort16Le() & 0xFFFF | readShort16Le() << Short.SIZE;
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @Deprecated
    @Override
    public long readLong(final boolean unsigned, int size) throws IOException {
        return unsigned ? readUnsignedLong(size) : readLong(size);
    }

    @Override
    public long readUnsignedLong(int size) throws IOException {
        requireValidSizeForUnsignedLong(size);
        long value = 0L;
        if (size >= Integer.SIZE) {
            value = (readInt(Integer.SIZE) & 0xFFFFFFFFL);
            size -= Integer.SIZE;
        }
        if (size > 0) {
            value <<= size;
            value |= readUnsignedInt(size);
        }
        return value;
    }

    @Override
    public long readLong(int size) throws IOException {
        requireValidSizeForSignedLong(size);
        long value = 0L;
        value -= readUnsignedLong(FLAG_SIZE);
        if (--size > 0) {
            value <<= size;
            value |= readUnsignedLong(size);
        }
        return value;
    }

    @Override
    public long readLong64() throws IOException {
        return readLong(Long.SIZE);
    }

    @Override
    public long readLong64Le() throws IOException {
        return readInt32Le() & 0xFFFFFFFFL | ((long) readInt32Le()) << Integer.SIZE;
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @Override
    public char readChar(final int size) throws IOException {
        return (char) readUnsignedInt(requireValidSizeChar(size));
    }

    @Override
    public char readChar16() throws IOException {
        return readChar(Character.SIZE);
    }

    @Override
    public char readChar16Le() throws IOException {
        return (char) readShort16Le();
    }

    // ------------------------------------------------------------------------------------------------------------ half
    @Override
    public float readHalf(final int exponentSize, final int fractionSize) throws IOException {
        requireValidExponentSizeHalf(exponentSize);
        requireValidFractionSizeHalf(fractionSize);
        return readFloat(exponentSize, fractionSize); // reduced encoding over the float carrier, bounded to binary16
    }

    @Override
    public float readHalf16() throws IOException {
        return halfValue(readShort16() & 0xFFFF);
    }

    @Override
    public float readHalf16Le() throws IOException {
        return halfValue(readShort16Le() & 0xFFFF);
    }

    /**
     * Decodes a {@code 16}-bit IEEE 754 {@code binary16} pattern into an exact {@code float}.
     *
     * @param bits16 the {@code binary16} bit pattern, in the low {@value java.lang.Short#SIZE} bits.
     * @return the decoded value, as a {@code float}.
     */
    private static float halfValue(final int bits16) {
        final int sign = (bits16 >>> 15) & 0x01;
        final int exponent = (bits16 >>> 10) & 0x1F;
        final int significand = bits16 & 0x3FF;
        final int bits;
        if (exponent == 0) {                          // zero / subnormal
            if (significand == 0) {                   // ±0
                bits = sign << 31;
            } else {                                  // subnormal -> renormalize into a float normal (exact)
                final int p = BitIoUtils.highestOneBitIndex(significand); // highest set bit, 0 .. 9
                final int floatExp = p + 103;                                 // (p - 24) + 127
                final int floatFrac = (significand & ((1 << p) - 1)) << (23 - p);
                bits = (sign << 31) | (floatExp << 23) | floatFrac;
            }
        } else if (exponent == 0x1F) {                // Infinity / NaN
            bits = (sign << 31) | 0x7F800000 | (significand << 13);
        } else {                                      // finite normal
            bits = (sign << 31) | ((exponent - 15 + 127) << 23) | (significand << 13);
        }
        return Float.intBitsToFloat(bits);
    }

    // ----------------------------------------------------------------------------------------------------------- float
    @Override
    public float readFloat(final int exponentSize, final int fractionSize) throws IOException {
        requireValidExponentSizeFloat(exponentSize);
        requireValidFractionSizeFloat(fractionSize);
        final int sign = readUnsignedInt(FLAG_SIZE);
        final int storedExp = readUnsignedInt(exponentSize);
        final int storedFrac = readUnsignedInt(fractionSize);
        final int expMask = (1 << exponentSize) - 1;
        final int newBias = (1 << (exponentSize - 1)) - 1;
        final int fracShift = 23 - fractionSize;
        final int rawExp;
        if (storedExp == 0) {                    // signed zero / native subnormal
            rawExp = 0;
        } else if (storedExp == expMask) {       // Infinity / NaN
            rawExp = 0xFF;
        } else {                                 // finite normal
            rawExp = (storedExp - newBias) + 127;
        }
        final int rawFrac = storedFrac << fracShift;
        final int bits = (sign << 31) | ((rawExp & 0xFF) << 23) | (rawFrac & 0x7FFFFF);
        return Float.intBitsToFloat(bits);
    }

    @Override
    public float readFloat32() throws IOException {
        return Float.intBitsToFloat(readInt32());
    }

    @Override
    public float readFloat32Le() throws IOException {
        return Float.intBitsToFloat(readInt32Le());
    }

    // ---------------------------------------------------------------------------------------------------------- double
    @Override
    public double readDouble(final int exponentSize, final int fractionSize) throws IOException {
        requireValidExponentSizeDouble(exponentSize);
        requireValidFractionSizeDouble(fractionSize);
        final long sign = readUnsignedInt(FLAG_SIZE);
        final int storedExp = readUnsignedInt(exponentSize);
        final long storedFrac = readUnsignedLong(fractionSize);
        final int expMask = (1 << exponentSize) - 1;
        final int newBias = (1 << (exponentSize - 1)) - 1;
        final int fracShift = 52 - fractionSize;
        final long rawExp;
        if (storedExp == 0) {                    // signed zero / native subnormal
            rawExp = 0L;
        } else if (storedExp == expMask) {       // Infinity / NaN
            rawExp = 0x7FFL;
        } else {                                 // finite normal
            rawExp = (storedExp - newBias) + 1023;
        }
        final long rawFrac = storedFrac << fracShift;
        final long bits = (sign << 63) | ((rawExp & 0x7FFL) << 52) | (rawFrac & 0x000FFFFFFFFFFFFFL);
        return Double.longBitsToDouble(bits);
    }

    @Override
    public double readDouble64() throws IOException {
        return Double.longBitsToDouble(readLong64());
    }

    @Override
    public double readDouble64Le() throws IOException {
        return Double.longBitsToDouble(readLong64Le());
    }

    // ------------------------------------------------------------------------------------------------------- primitive
    @Override
    public boolean readBoolean(final BooleanBitReader reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        return reader.readBoolean(this);
    }

    @Override
    public int readInt(final IntBitReader reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        return reader.readInt(this);
    }

    @Override
    public long readLong(final LongBitReader reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        return reader.readLong(this);
    }

    @Override
    public float readFloat(final FloatBitReader reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        return reader.readFloat(this);
    }

    @Override
    public double readDouble(final DoubleBitReader reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        return reader.readDouble(this);
    }

    // ---------------------------------------------------------------------------------------------------------- object

    /**
     * Reads a value using specified reader.
     *
     * @param reader the reader; must not be {@code null}.
     * @param <T>    value type parameter
     * @return the value read by {@code reader}.
     * @throws NullPointerException if {@code reader} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @see AbstractBitOutput#writeObject(BitWriter, Object)
     */
    @Override
    public <T> T readObject(final BitReader<? extends T> reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException("reader is null");
        }
        return reader.read(this);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void skip(int bits) throws IOException {
        if (bits <= 0) {
            throw new IllegalArgumentException("bits(" + bits + ") <= 0");
        }
        for (; bits >= Integer.SIZE; bits -= Integer.SIZE) {
            readInt(Integer.SIZE);
        }
        if (bits > 0) {
            readUnsignedInt(bits);
        }
    }

    @Override
    public long align(int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }
        long bits = 0; // number of bits to discard
        if (available > 0) {
            bits += available;
            readUnsignedInt(available);
        }
        if (bytes == 1) {
            return bits;
        }
        for (bytes = (bytes - (int) (count % bytes)) % bytes; bytes > 0L; bytes--) {
            readUnsignedInt(Byte.SIZE);
            bits += Byte.SIZE;
        }
        return bits;
    }

    // ----------------------------------------------------------------------------------------------------------- count

    /**
     * Returns the number of bytes read so far.
     *
     * @return the number of bytes read so far.
     * @see #read()
     * @see AbstractBitOutput#getCount()
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
     * The number of available bits in {@link #octet} for reading.
     */
    private int available = 0;

    /**
     * The number of bytes read so far.
     */
    private long count;
}
