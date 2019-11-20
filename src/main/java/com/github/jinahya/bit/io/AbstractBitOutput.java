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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeChar;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeShort;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeUnsigned16;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeUnsigned8;

/**
 * An abstract class for implementing {@link BitInput} interface.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DefaultBitOutput
 * @see AbstractByteInput
 */
public abstract class AbstractBitOutput implements BitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public AbstractBitOutput() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return super.toString() + "{"
               + "count=" + count
               + "}";
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes given {@value java.lang.Byte#SIZE}-bit unsigned integer.
     *
     * @param value the {@value java.lang.Byte#SIZE}-bit unsigned integer to write.
     * @throws IOException if an I/O error occurs.
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
     * @see AbstractBitInput#unsigned8(int)
     */
    protected void unsigned8(final int size, int value) throws IOException {
        requireValidSizeUnsigned8(size);
        final int required = size - available;
        if (required > 0) {
            unsigned8(available, value >> required);
            unsigned8(required, value);
            return;
        }
        octet <<= size;
        octet |= (value & ((1 << size) - 1));
        available -= size;
        if (available == 0) {
            assert octet >= 0 && octet < 256;
            write(octet);
            count++;
            octet = 0x00;
            available = Byte.SIZE;
        }
    }

    /**
     * Writes an unsigned {@code int} value of specified bit size which is, in maximum, {@value java.lang.Short#SIZE}.
     *
     * @param size  the number of lower bits to write; between {@code 1} and {@value java.lang.Short#SIZE}, both
     *              inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs
     * @see #unsigned8(int, int)
     * @see AbstractBitInput#unsigned16(int)
     */
    protected void unsigned16(final int size, final int value) throws IOException {
        requireValidSizeUnsigned16(size);
        final int quotient = size / Byte.SIZE;
        final int remainder = size % Byte.SIZE;
        if (remainder > 0) {
            unsigned8(remainder, value >> (quotient * Byte.SIZE));
        }
        for (int i = quotient - 1; i >= 0; i--) {
            unsigned8(Byte.SIZE, value >> (Byte.SIZE * i));
        }
    }

    // --------------------------------------------------------------------------------------------------------- boolean
    @Override
    public void writeBoolean(final boolean value) throws IOException {
        writeInt(true, 1, value ? 1 : 0);
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
    public void writeInt(final boolean unsigned, final int size, final int value) throws IOException {
        requireValidSizeInt(unsigned, size);
        if (!unsigned) {
            writeInt(true, 1, value < 0 ? 1 : 0);
            final int usize = size - 1;
            if (usize > 0) {
                writeInt(true, usize, value);
            }
            return;
        }
        final int quotient = size / Short.SIZE;
        final int remainder = size % Short.SIZE;
        if (remainder > 0) {
            unsigned16(remainder, value >> (quotient * Short.SIZE));
        }
        for (int i = Short.SIZE * (quotient - 1); i >= 0; i -= Short.SIZE) {
            unsigned16(Short.SIZE, value >> i);
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
    public void writeLong(final boolean unsigned, final int size, final long value) throws IOException {
        requireValidSizeLong(unsigned, size);
        if (!unsigned) {
            writeLong(true, 1, value < 0L ? 1L : 0L);
            final int usize = size - 1;
            if (usize > 0) {
                writeLong(true, usize, value);
            }
            return;
        }
        final int divisor = Integer.SIZE - 1;
        final int quotient = size / divisor;
        final int remainder = size % divisor;
        if (remainder > 0) {
            writeInt(true, remainder, (int) (value >> (quotient * divisor)));
        }
        for (int i = divisor * (quotient - 1); i >= 0; i -= divisor) {
            writeInt(true, divisor, (int) (value >> i));
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

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long align(final int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }
        long bits = 0; // the number of bits padded
        if (available < Byte.SIZE) {
            bits += available;
            writeInt(true, available, 0x00);
        }
        for (; count % bytes > 0; bits += Byte.SIZE) {
            writeInt(true, Byte.SIZE, 0x00);
        }
        return bits;
    }

    // ----------------------------------------------------------------------------------------------------------- count

    /**
     * Returns the number of bytes written so far.
     *
     * @return the number of bytes written so far.
     * @see #write(int)
     */
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
