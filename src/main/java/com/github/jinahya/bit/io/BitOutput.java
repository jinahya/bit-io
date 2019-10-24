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
 * An interface for writing values of arbitrary number of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see BitInput
 */
public interface BitOutput {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code 1}-bit {@code boolean} value. This method writes {@code 0b1} for {@code true} and {@code 0b0} for
     * {@code false}.
     *
     * @param value the value to write.
     * @throws IOException if an I/O error occurs
     * @see BitInput#readBoolean()
     */
    void writeBoolean(boolean value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code byte} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits for value; between {@code 1} and {@code 7 + (unsigned ? 0 : 1)}, both
     *                 inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeSignedByte(int, byte)
     * @see #writeUnsignedByte(int, byte)
     * @see BitInput#readByte(boolean, int)
     */
    void writeByte(boolean unsigned, int size, byte value) throws IOException;

    /**
     * Writes a signed {@code byte} value of specified number of bits.
     *
     * @param size  the number of bits for value; between {@code 1} and {@value Byte#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeByte(boolean, int, byte)
     * @see #writeUnsignedByte(int, byte)
     * @see BitInput#readSignedByte(int)
     */
    void writeSignedByte(int size, byte value) throws IOException;

    /**
     * Writes an unsigned {@code byte} value of specified number of bits.
     *
     * @param size  the number of bits for value; between {@code 1} and {@code 7}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeByte(boolean, int, byte)
     * @see #writeUnsignedByte(int, byte)
     * @see BitInput#readUnsignedByte(int)
     */
    void writeUnsignedByte(int size, byte value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code short} value of specified number of bits.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits for value; between {@code 1} and {@code 15 + (unsigned ? 0 : 1)}, both
     *                 inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeSignedShort(int, short)
     * @see #writeUnsignedShort(int, short)
     * @see BitInput#readShort(boolean, int)
     */
    void writeShort(boolean unsigned, int size, short value) throws IOException;

    /**
     * Writes a signed {@code short} value of specified number of bits.
     *
     * @param size  the number of bits for value; between {@code 1} and {@value Short#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeShort(boolean, int, short)
     * @see #writeUnsignedShort(int, short)
     * @see BitInput#readSignedShort(int)
     */
    void writeSignedShort(int size, short value) throws IOException;

    /**
     * Writes an unsigned {@code short} value of specified number of bits.
     *
     * @param size  the number of bits for value; between {@code 1} and {@code 15}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeShort(boolean, int, short)
     * @see #writeSignedShort(int, short)
     * @see BitInput#readUnsignedShort(int)
     */
    void writeUnsignedShort(int size, short value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes an {@code int} value of specified number of bits. Only the lower number of specified bits are written.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of bits for value; between {@code 1} and {@code 31 + (unsigned ? 0 : 1)}, both
     *                 inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeSignedInt(int, int)
     * @see #writeUnsignedInt(int, int)
     * @see BitInput#readInt(boolean, int)
     */
    void writeInt(boolean unsigned, int size, int value) throws IOException;

    /**
     * Writes a signed {@code int} value of specified number of bits. Only the lower number of specified bits are
     * written.
     *
     * @param size  the number of bits for value; between {@code 1} and {@value Integer#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeInt(boolean, int, int)
     * @see #writeUnsignedInt(int, int)
     * @see BitInput#readSignedInt(int)
     */
    void writeSignedInt(int size, int value) throws IOException;

    /**
     * Writes an unsigned {@code int} value of specified number of bits. Only the lower number of specified bits are
     * written.
     *
     * @param size  the number of bits for value; between {@code 1} and {@code 31}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeInt(boolean, int, int)
     * @see #writeSignedInt(int, int)
     * @see BitInput#readUnsignedInt(int)
     */
    void writeUnsignedInt(int size, int value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code long} value of specified number of bits. Only the lower number of specified bits are written.
     *
     * @param unsigned a flag for indicating unsigned value; {@code true} for unsigned, {@code false} for signed.
     * @param size     the number of valid bits for value; between {@code 1} and {@code 63 + (unsigned ? 0 : 1)}, both
     *                 inclusive.
     * @param value    the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeSignedLong(int, long)
     * @see #writeUnsignedLong(int, long)
     * @see BitInput#readLong(boolean, int)
     */
    void writeLong(boolean unsigned, int size, long value) throws IOException;

    /**
     * Writes a signed {@code long} value of specified number of bits. Only the lower number of specified bits are
     * written.
     *
     * @param size  the number of valid bits for value; between {@code 1} and {@value Long#SIZE}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeLong(boolean, int, long)
     * @see #writeUnsignedLong(int, long)
     * @see BitInput#readSignedLong(int)
     */
    void writeSignedLong(int size, long value) throws IOException;

    /**
     * Writes an unsigned {@code long} value of specified number of bits. Only the lower number of specified bits are
     * written.
     *
     * @param size  the number of valid bits for value; between {@code 1} and {@code 63}, both inclusive.
     * @param value the value to write.
     * @throws IOException if an I/O error occurs.
     * @see #writeLong(boolean, int, long)
     * @see #writeSignedLong(int, long)
     * @see BitInput#readUnsignedLong(int)
     */
    void writeUnsignedLong(int size, long value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes a {@code char} value of specified number of bits.
     *
     * @param size  the number of bits for value; between {@code 1} and {@value Character#SIZE}, both inclusive.
     * @param value the value to write
     * @throws IOException if an I/O error occurs.
     * @see #writeUnsignedInt(int, int)
     * @see BitInput#readChar(int)
     */
    void writeChar(int size, char value) throws IOException;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Aligns to specified number of bytes by padding zero bits.
     *
     * @param bytes the number of bytes to align; must be positive.
     * @return the number of zero bits padded while aligning.
     * @throws IOException if an I/O error occurs.
     * @see BitInput#align(int)
     */
    long align(int bytes) throws IOException;
}
