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
 * An abstract class partially implementing {@link BitInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class AbstractBitOutput implements BitOutput, ByteOutput {


    /**
     * Consumes given unsigned byte value while incrementing {@code count}.
     *
     * @param value the unsigned byte value
     *
     * @throws IOException if an I/O error occurs.
     */
    protected void octet(final int value) throws IOException {

        writeUnsignedByte(value);

        count++;
    }


    /**
     * Writes an unsigned byte value. Only the lower {@code size} bits in given
     * {@code value} are written.
     *
     * @param size the number of lower bits to write; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#UBYTE_SIZE_MIN}
     * (exclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#UBYTE_SIZE_MAX}
     * (inclusive).
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    protected void writeUnsignedByte(final int size, int value)
        throws IOException {

        BitIoConstraints.requireValidUnsignedByteSize(size);

        if (size == 8 && index == 0) {
            octet(value);
            return;
        }

        final int required = size - (8 - index);
        if (required > 0) {
            writeUnsignedByte(size - required, value >> required);
            writeUnsignedByte(required, value);
            return;
        }

        for (int i = index + size - 1; i >= index; i--) {
            flags[i] = (value & 0x01) == 0x01;
            value >>= 1;
        }
        index += size;

        if (index == 8) {
            int octet = 0x00;
            for (int i = 0; i < 8; i++) {
                octet <<= 1;
                octet |= (flags[i] ? 0x01 : 0x00);
            }
            octet(octet);
            index = 0;
        }
    }


    /**
     * Writes an unsigned short value. Only the lower {@code length} bits in
     * {@code value} are written.
     *
     * @param size the number of lower bits to write; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#USHORT_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#USHORT_SIZE_MAX}
     * (inclusive).
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs
     */
    protected void writeUnsignedShort(final int size, final int value)
        throws IOException {

        BitIoConstraints.requireValidUnsignedShortSize(size);

        final int quotient = size / BitIoConstants.UBYTE_SIZE_MAX;
        final int remainder = size % BitIoConstants.UBYTE_SIZE_MAX;

        if (remainder > 0) {
            writeUnsignedByte(
                remainder, value >> (quotient * BitIoConstants.UBYTE_SIZE_MAX));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedByte(BitIoConstants.UBYTE_SIZE_MAX,
                              value >> (BitIoConstants.UBYTE_SIZE_MAX * i));
        }
    }


    @Override
    public void writeBoolean(final boolean value) throws IOException {

        writeUnsignedByte(1, value ? 0x01 : 0x00);
    }


    @Override
    public void writeUnsignedInt(final int size, final int value)
        throws IOException {

        BitIoConstraints.requireValidUnsignedIntSize(size);

        final int quotient = size / BitIoConstants.USHORT_SIZE_MAX;
        final int remainder = size % BitIoConstants.USHORT_SIZE_MAX;

        if (remainder > 0) {
            writeUnsignedShort(
                remainder,
                value >> (quotient * BitIoConstants.USHORT_SIZE_MAX));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedShort(BitIoConstants.USHORT_SIZE_MAX,
                               value >> (BitIoConstants.USHORT_SIZE_MAX * i));
        }
    }


    @Override
    public void writeInt(final int size, final int value) throws IOException {

        BitIoConstraints.requireValidIntSize(size);

        final int usize = size - 1;
        writeUnsignedByte(1, value >> usize);
        writeUnsignedInt(usize, value);
    }


    @Override
    public void writeUnsignedLong(final int size, final long value)
        throws IOException {

        BitIoConstraints.requireValidUnsignedLongSize(size);

        final int quotient = size / BitIoConstants.UINT_SIZE_MAX;
        final int remainder = size % BitIoConstants.UINT_SIZE_MAX;

        if (remainder > 0) {
            writeUnsignedInt(
                remainder,
                (int) (value >> (quotient * BitIoConstants.UINT_SIZE_MAX)));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedInt(
                BitIoConstants.UINT_SIZE_MAX,
                (int) (value >> (BitIoConstants.UINT_SIZE_MAX * i)));
        }
    }


    @Override
    public void writeLong(final int size, final long value) throws IOException {

        BitIoConstraints.requireValidLongSize(size);

        final int usize = size - 1;
        writeUnsignedByte(1, (int) (value >> usize));
        writeUnsignedLong(usize, value);
    }


    @Override
    public long align(final int bytes) throws IOException {

        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }

        long bits = 0; // number of bits to be padded

        // pad remained bits into current octet
        if (index > 0) {
            bits += (8 - index);
            writeUnsignedByte((int) bits, 0x00); // count incremented
        }

        final long remainder = count % bytes;
        long octets = (remainder > 0 ? bytes : 0) - remainder;
        for (; octets > 0; octets--) {
            writeUnsignedByte(8, 0x00);
            bits += 8;
        }

        return bits;
    }


    /**
     * bit flags.
     */
    private final boolean[] flags = new boolean[8];


    /**
     * bit index to write.
     */
    private int index = 0;


    /**
     * number of bytes written so far.
     */
    private long count = 0;

}

