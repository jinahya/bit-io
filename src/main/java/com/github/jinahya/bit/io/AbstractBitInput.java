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
 * @see AbstractBitOutput
 * @see DefaultBitInput
 */
public abstract class AbstractBitInput implements BitInput {

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
            assert octet >= 0 && octet < 256;
            count++;
            available = Byte.SIZE;
        }
        final int required = size - available;
        if (required > 0) {
            return (unsigned8(available) << required) | unsigned8(required);
        }
        available -= size;
        return (octet >> available) & mask(size);
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

    @Override
    public byte readByte8() throws IOException {
        return readByte(false, Byte.SIZE);
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @Override
    public short readShort(final boolean unsigned, final int size) throws IOException {
        return (short) readInt(unsigned, requireValidSizeShort(unsigned, size));
    }

    @Override
    public short readShort16() throws IOException {
        return readShort(false, Short.SIZE);
    }

    @Override
    public short readShort16Le() throws IOException {
        return (short) (readByte8() & 0xFF | readByte8() << Byte.SIZE);
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @Override
    public int readInt(final boolean unsigned, int size) throws IOException {
        requireValidSizeInt(unsigned, size);
        int value = 0;
        if (!unsigned) {
            value -= readInt(true, 1);
            if (--size > 0) {
                value <<= size;
                value |= readInt(true, size);
            }
            return value;
        }
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
    public int readInt32() throws IOException {
        return readInt(false, Integer.SIZE);
    }

    @Override
    public int readInt32Le() throws IOException {
        return readShort16Le() & 0xFFFF | readShort16Le() << Short.SIZE;
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @Override
    public long readLong(final boolean unsigned, int size) throws IOException {
        requireValidSizeLong(unsigned, size);
        long value = 0L;
        if (!unsigned) {
            value -= readLong(true, 1);
            if (--size > 0) {
                value <<= size;
                value |= readLong(true, size);
            }
            return value;
        }
        if (size >= Integer.SIZE) {
            value = (readInt(false, Integer.SIZE) & 0xFFFFFFFFL);
            size -= Integer.SIZE;
        }
        if (size > 0) {
            value <<= size;
            value |= readInt(true, size);
        }
        return value;
    }

    @Override
    public long readLong64() throws IOException {
        return readLong(false, Long.SIZE);
    }

    @Override
    public long readLong64Le() throws IOException {
        return readInt32Le() & 0xFFFFFFFFL | ((long) readInt32Le()) << Integer.SIZE;
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @Override
    public char readChar(final int size) throws IOException {
        return (char) readInt(true, requireValidSizeChar(size));
    }

    @Override
    public char readChar16() throws IOException {
        return readChar(Character.SIZE);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void skip(int bits) throws IOException {
        if (bits <= 0) {
            throw new IllegalArgumentException("bits(" + bits + ") <= 0");
        }
        for (; bits >= Integer.SIZE; bits -= Integer.SIZE) {
            readInt(false, Integer.SIZE);
        }
        if (bits > 0) {
            readInt(true, bits);
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
            readInt(true, available);
        }
        if (bytes == 1) {
            return bits;
        }
        for (bytes = bytes - (int) (count % bytes); bytes > 0L; bytes--) {
            readInt(true, Byte.SIZE);
            bits += Byte.SIZE;
        }
        return bits;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The current octet.
     */
    private int octet;

    /**
     * The number of available bits in {@link #octet} for reading..
     */
    private int available = 0;

    /**
     * The number of bytes read so far.
     */
    private long count;
}
