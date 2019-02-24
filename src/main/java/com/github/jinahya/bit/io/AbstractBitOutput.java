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
 * An abstract class for implementing {@link BitInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see AbstractBitInput
 */
public abstract class AbstractBitOutput implements BitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes given unsigned 8-bit integer.
     *
     * @param value an unsigned 8-bit integer to write
     * @throws IOException if an I/O error occurs.
     */
    protected abstract void write(int value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes an unsigned value whose size is, in maximum, {@value Byte#SIZE}.
     *
     * @param size  the number of lower bits to write; between {@code 1} and {@value Byte#SIZE}, both inclusive.
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     */
    protected void unsigned8(final int size, int value) throws IOException {
        BitIoConstraints.requireValidSizeUnsigned8(size);
        final int required = size - (Byte.SIZE - index);
        if (required > 0) {
            unsigned8(size - required, value >> required);
            unsigned8(required, value);
            return;
        }
        octet <<= size;
        octet |= ((1 << size) - 1) & value;
        index += size;
        if (index == Byte.SIZE) {
            write(octet);
            count++;
            octet = 0x00;
            //octet(octet);
            index = 0;
        }
    }

    /**
     * Writes an unsigned value whose size is max {@value Short#SIZE}.
     *
     * @param size  the number of lower bits to write; between {@code 1} and {@value Short#SIZE}, both inclusive.
     * @param value the value to write
     * @throws IOException if an I/O error occurs
     */
    protected void unsigned16(final int size, final int value) throws IOException {
        BitIoConstraints.requireValidSizeUnsigned16(size);
        final int quotient = size / Byte.SIZE;
        final int remainder = size % Byte.SIZE;
        if (remainder > 0) {
            unsigned8(remainder, value >> (quotient * Byte.SIZE));
        }
        for (int i = quotient - 1; i >= 0; i--) {
            unsigned8(Byte.SIZE, value >> (Byte.SIZE * i));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void writeBoolean(final boolean value) throws IOException {
        writeInt(true, 1, value ? 1 : 0);
    }

    @Override
    public void writeByte(final boolean unsigned, final int size, final byte value) throws IOException {
        BitIoConstraints.requireValidSizeByte(unsigned, size);
        writeInt(unsigned, size, value);
    }

    @Override
    public void writeShort(final boolean unsigned, final int size, final short value) throws IOException {
        BitIoConstraints.requireValidSizeShort(unsigned, size);
        writeInt(unsigned, size, value);
    }

    @Override
    public void writeInt(final boolean unsigned, final int size, final int value) throws IOException {
        BitIoConstraints.requireValidSizeInt(unsigned, size);
        if (!unsigned) {
            final int usize = size - 1;
            writeInt(true, 1, value >> usize);
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
        for (int i = quotient - 1; i >= 0; i--) {
            unsigned16(Short.SIZE, value >> (Short.SIZE * i));
        }
    }

    @Override
    public void writeLong(final boolean unsigned, final int size, final long value) throws IOException {
        BitIoConstraints.requireValidSizeLong(unsigned, size);
        if (!unsigned) {
            final int usize = size - 1;
            writeLong(true, 1, value >> usize);
            if (usize > 0) {
                writeLong(true, usize, value);
            }
            return;
        }
        final int quotient = size / 31;
        final int remainder = size % 31;
        if (remainder > 0) {
            writeInt(true, remainder, (int) (value >> (quotient * 31)));
        }
        for (int i = quotient - 1; i >= 0; i--) {
            writeInt(true, 31, (int) (value >> (i * 31)));
        }
    }

    @Override
    public void writeChar(final int size, final char value) throws IOException {
        BitIoConstraints.requireValidSizeChar(size);
        unsigned16(size, value);
    }

    @Override
    public long align(final int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }
        long bits = 0; // number of bits to be padded
        // pad remained bits into current octet
        if (index > 0) {
            bits += Byte.SIZE - index;
            unsigned8((int) bits, 0x00); // count incremented
        }
        final long remainder = count % bytes;
        long octets = (remainder > 0 ? bytes : 0) - remainder;
        for (; octets > 0; octets--) {
            unsigned8(Byte.SIZE, 0x00);
            bits += Byte.SIZE;
        }
        return bits;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The current value of written bits.
     */
    private int octet;

    /**
     * The bit index in {@link #octet} to write.
     */
    private int index = 0;

    /**
     * The number of bytes written so far.
     */
    private long count = 0L;

    // -----------------------------------------------------------------------------------------------------------------
}
