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
 * @see AbstractBitOutput
 */
public abstract class AbstractBitInput implements BitInput {

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
     * Reads an unsigned 8-bit integer.
     *
     * @return an unsigned 8-bit integer.
     * @throws IOException if an I/O error occurs.
     */
    protected abstract int read() throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an unsigned value whose maximum size is {@value Byte#SIZE}.
     *
     * @param size the number of bits for the value; between {@code 1} and {@value Byte#SIZE}, both inclusive.
     * @return an unsigned byte value.
     * @throws IOException if an I/O error occurs.
     */
    protected int unsigned8(final int size) throws IOException {
        requireValidSizeUnsigned8(size);
        if (available == 0) {
            octet = read();
            count++;
            available = Byte.SIZE;
        }
        final int required = size - available;
        if (required > 0) {
            return (unsigned8(available) << required) | unsigned8(required);
        }
        return (octet >> (available -= size)) & ((1 << size) - 1);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an unsigned value whose maximum size is {@value Short#SIZE}.
     *
     * @param size the number of bits for the value; between {@code 1} and {@value Short#SIZE}, both inclusive.
     * @return an unsigned short value.
     * @throws IOException if an I/O error occurs.
     */
    protected int unsigned16(final int size) throws IOException {
        requireValidSizeUnsigned16(size);
        int value = 0x00;
        final int quotient = size / Byte.SIZE;
        for (int i = 0; i < quotient; i++) {
            value <<= Byte.SIZE;
            value |= unsigned8(Byte.SIZE);
        }
        final int remainder = size % Byte.SIZE;
        if (remainder > 0) {
            value <<= remainder;
            value |= unsigned8(remainder);
        }
        return value;
    }

    // --------------------------------------------------------------------------------------------------------- boolean
    @Override
    public boolean readBoolean() throws IOException {
        return readInt(true, 1) == 1;
    }

    // ------------------------------------------------------------------------------------------------------------ byte
    @Override
    public byte readByte(final boolean unsigned, final int size) throws IOException {
        return (byte) readInt(unsigned, requireValidSizeByte(unsigned, size));
    }

//    @Override
//    public byte readSignedByte(final int size) throws IOException {
//        return readByte(false, size);
//    }
//
//    @Override
//    public byte readUnsignedByte(final int size) throws IOException {
//        return readByte(true, size);
//    }

    // ----------------------------------------------------------------------------------------------------------- short
    @Override
    public short readShort(final boolean unsigned, final int size) throws IOException {
        return (short) readInt(unsigned, requireValidSizeShort(unsigned, size));
    }

//    @Override
//    public short readSignedShort(final int size) throws IOException {
//        return readShort(false, size);
//    }
//
//    @Override
//    public short readUnsignedShort(final int size) throws IOException {
//        return readShort(true, size);
//    }

    // ------------------------------------------------------------------------------------------------------------- int
    @Override
    public int readInt(final boolean unsigned, final int size) throws IOException {
        requireValidSizeInt(unsigned, size);
        if (!unsigned) {
            int value = 0 - readInt(true, 1);
            final int usize = size - 1;
            if (usize > 0) {
                value <<= usize;
                value |= readInt(true, usize);
            }
            return value;
        }
        int value = 0x00;
        final int quotient = size / Short.SIZE;
        for (int i = 0; i < quotient; i++) {
            value <<= Short.SIZE;
            value |= unsigned16(Short.SIZE);
        }
        final int remainder = size % Short.SIZE;
        if (remainder > 0) {
            value <<= remainder;
            value |= unsigned16(remainder);
        }
        return value;
    }

//    @Override
//    public int readSignedInt(final int size) throws IOException {
//        return readInt(false, size);
//    }
//
//    @Override
//    public int readUnsignedInt(final int size) throws IOException {
//        return readInt(true, size);
//    }

    // ------------------------------------------------------------------------------------------------------------ long
    @Override
    public long readLong(final boolean unsigned, final int size) throws IOException {
        requireValidSizeLong(unsigned, size);
        if (!unsigned) {
            long value = 0L - readLong(true, 1);
            final int usize = size - 1;
            if (usize > 0) {
                value <<= usize;
                value |= readLong(true, usize);
            }
            return value;
        }
        long value = 0x00L;
        final int quotient = size / Integer.SIZE;
        for (int i = 0; i < quotient; i++) {
            value <<= Integer.SIZE;
            value |= readInt(false, Integer.SIZE) & 0xFFFFFFFFL;
        }
        final int remainder = size % Integer.SIZE;
        if (remainder > 0) {
            value <<= remainder;
            value |= readInt(true, remainder);
        }
        return value;
    }

//    @Override
//    public long readSignedLong(final int size) throws IOException {
//        return readLong(false, size);
//    }
//
//    @Override
//    public long readUnsignedLong(final int size) throws IOException {
//        return readLong(true, size);
//    }

    // ------------------------------------------------------------------------------------------------------------ char
    @Override
    public char readChar(final int size) throws IOException {
        return (char) readInt(true, requireValidSizeChar(size));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public long align(final int bytes) throws IOException {
        if (bytes <= 0) {
            throw new IllegalArgumentException("bytes(" + bytes + ") <= 0");
        }
        long bits = 0; // number of bits to be discarded
        if (available > 0) {
            bits += available;
            readInt(true, available);
        }
        for (; count % bytes > 0; bits += Byte.SIZE) {
            readInt(true, Byte.SIZE);
        }
        return bits;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the number bytes read so far.
     *
     * @return the number of bytes read so far.
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
     * The number of available bits in {@link #octet} for reading..
     */
    private int available;

    /**
     * The number of bytes read so far.
     */
    private long count;
}
