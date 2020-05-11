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

import static com.github.jinahya.bit.io.BitIoConstants.mask;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeChar;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeShort;

/**
 * An abstract class for implementing {@link BitInput} interface.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see AbstractBitInput
 * @see DefaultBitOutput
 */
public abstract class AbstractBitOutput implements BitOutput {

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
        octet |= value & mask(size);
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
    public void writeInt(final boolean unsigned, int size, final int value) throws IOException {
        requireValidSizeInt(unsigned, size);
        if (!unsigned) {
            writeInt(true, 1, value < 0 ? 1 : 0);
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
            writeInt(true, 1, value < 0L ? 0x01 : 0x00);
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
        for (bytes = bytes - (int) (count % bytes); bytes > 0; bytes--) {
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
