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
 * An interface for writing arbitrary length of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface BitOutput {


    /**
     * Writes a 1-bit boolean value. This method writes {@code 0b1} for
     * {@code true} and {@code 0b0} for {@code false}.
     *
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs
     */
    void writeBoolean(boolean value) throws IOException;


    /**
     * Writes an unsigned int value. Only the lower specified number of bits in
     * {@code value} are written.
     *
     * @param size the number of lower bits to write; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#UINT_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#UINT_SIZE_MAX}
     * (inclusive).
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeUnsignedInt(int size, int value) throws IOException;


    /**
     * Writes a signed int value. Only the lower number of specified bits in
     * {@code value} are written.
     *
     * @param size the number of lower bits to write; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#INT_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#INT_SIZE_MAX}
     * (inclusive).
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeInt(int size, int value) throws IOException;


    /**
     * Writes an unsigned long value. Only the lower specified number of bits in
     * {@code value} are written.
     *
     * @param size the number of bits to write; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#ULONG_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#ULONG_SIZE_MAX}
     * (inclusive}.
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeUnsignedLong(int size, long value) throws IOException;


    /**
     * Writes a signed long value. Only the lower {@code size} bits in
     * {@code value} are written.
     *
     * @param size the number of bits to write; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#LONG_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#LONG_SIZE_MAX}
     * (inclusive). (inclusive).
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeLong(int size, long value) throws IOException;


    /**
     * Writes a specified number of bytes in given array starting from specified
     * offset.
     *
     * @param array the array
     * @param offset the start offset
     * @param length number of bytes to write.
     * @param range required bits in each byte
     *
     * @return given array
     *
     * @throws IOException if an I/O error occurs.
     */
    byte[] writeBytes(byte[] array, int offset, int length, int range)
        throws IOException;


    byte[] writeBytes(byte[] array, int offset, int range) throws IOException;


    byte[] writeBytes(byte[] array, int range) throws IOException;


    byte[] writeBytes(int scale, byte[] array, int offset, int range)
        throws IOException;


    byte[] writeBytes(int scale, byte[] array, int range) throws IOException;


//    /**
//     * Writes an array of bytes.
//     *
//     * @param scale the number of bits to present the length of array between
//     * {@value com.github.jinahya.bit.io.BitIoConstants#LENGTH_SIZE_MIN}
//     * (inclusive) and
//     * {@value com.github.jinahya.bit.io.BitIoConstants#LENGTH_SIZE_MAX}
//     * (inclusive).
//     * @param range the number of valid bits in each byte; between
//     * {@value com.github.jinahya.bit.io.BitIoConstants#UBYTE_SIZE_MIN}
//     * (inclusive) and
//     * {@value com.github.jinahya.bit.io.BitIoConstants#UBYTE_SIZE_MAX}
//     * (inclusive).
//     * @param value the array to write.
//     *
//     * @throws IOException if an I/O error occurs.
//     */
//    void writeBytes(int scale, int range, byte[] value) throws IOException;
    /**
     * Writes a string value.
     *
     * @param value the string value to write.
     * @param charsetName the character set name to decode the string
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see String#getBytes(java.lang.String)
     */
    void writeString(String value, String charsetName) throws IOException;


    void writeString(int scale, String value, String charsetName)
        throws IOException;


    /**
     * Writes a {@code US-ASCII} decoded string value.
     *
     * @param value the string value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see String#getBytes(java.lang.String)
     */
    void writeAscii(String value) throws IOException;


    void writeAscii(int scale, String value) throws IOException;


    /**
     * Aligns to specified number of bytes.
     *
     * @param bytes the number of bytes to align; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#ALIGN_BYTES_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#ALIGN_BYTES_MAX}
     * (inclusive).
     *
     * @return the number of bits padded for alignment
     *
     * @throws IOException if an I/O error occurs.
     */
    int align(int bytes) throws IOException;

}

