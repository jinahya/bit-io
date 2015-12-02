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
 * A abstract class partially implementing {@link BitInput}.
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

        final int quotient = size / 8;
        final int remainder = size % 8;

        if (remainder > 0) {
            writeUnsignedByte(remainder, value >> (quotient * 8));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedByte(8, value >> (8 * i));
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

        final int quotient = size / 16;
        final int remainder = size % 16;

        if (remainder > 0) {
            writeUnsignedShort(remainder, value >> (quotient * 16));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedShort(16, value >> (16 * i));
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

        final int quotient = size / 31;
        final int remainder = size % 31;

        if (remainder > 0) {
            writeUnsignedInt(remainder, (int) (value >> (quotient * 31)));
        }

        for (int i = quotient - 1; i >= 0; i--) {
            writeUnsignedInt(31, (int) (value >> (i * 31)));
        }
    }


    @Override
    public void writeLong(final int size, final long value) throws IOException {

        BitIoConstraints.requireValidLongSize(size);

        final int unsigned = size - 1;
        writeUnsignedByte(1, (int) (value >> unsigned));
        writeUnsignedLong(unsigned, value);
    }


    @Override
    public byte[] writeBytes(final byte[] bytes, final int offset,
                             final int length)
        throws IOException {

        final int limit = offset + length;
        for (int i = offset; i < limit; i++) {
            writeUnsignedByte(8, bytes[i]);
        }

        return bytes;
    }


    @Override
    public byte[] writeBytes(final byte[] bytes, final int offset)
        throws IOException {

        return writeBytes(bytes, offset, bytes.length - offset);
    }


    @Override
    public void writeBytes(final int scale, final int range, final byte[] value)
        throws IOException {

        BitIoConstraints.requireValidBytesScale(scale);

        BitIoConstraints.requireValidBytesRange(range);

        if (value == null) {
            throw new NullPointerException("null value");
        }
        if (value.length >> scale != 0) {
            throw new IllegalArgumentException(
                "value.length(" + value.length + ") >> scale(" + scale
                + " != 0");
        }

        writeUnsignedInt(scale, value.length);

        for (int i = 0; i < value.length; i++) {
            writeUnsignedByte(range, value[i]);
        }
    }


    @Override
    public void writeString(final String value, final String charsetName)
        throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        if (charsetName == null) {
            throw new NullPointerException("null charsetName");
        }

        final byte[] bytes = value.getBytes(charsetName);

        writeBytes(BitIoConstants.SCALE_SIZE_MAX, BitIoConstants.RANGE_SIZE_MAX,
                   bytes);
    }


    @Override
    public void writeAscii(final String value) throws IOException {

        if (value == null) {
            throw new NullPointerException("null value");
        }

        final byte[] bytes = value.getBytes("US-ASCII");

        writeBytes(BitIoConstants.SCALE_SIZE_MAX, 7, bytes);
    }


    @Override
    public int align(final int bytes) throws IOException {

        BitIoConstraints.requireValidAlighBytes(bytes);

        int bits = 0; // number of bits to be padded

        // pad remained bits into current byte
        if (index > 0) {
            bits += (8 - index);
            writeUnsignedByte(bits, 0x00); // count incremented
        }

        long octets = count % bytes;

        if (octets == 0) {
            return bits;
        }

        if (octets > 0) {
            octets = bytes - octets;
        } else {
            octets = 0 - octets;
        }

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

