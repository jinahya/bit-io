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
 * An interface for reading arbitrary length of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface BitInput {


    /**
     * Reads a 1-bit boolean value. This method reads {@code true} for
     * {@code 0b1} and {@code false} for {@code 0b0}.
     *
     * @return {@code true} for {@code 0b1}, {@code false} for {@code 0b0}
     *
     * @throws IOException if an I/O error occurs.
     */
    boolean readBoolean() throws IOException;


    /**
     * Reads an unsigned int value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#UINT_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#UINT_SIZE_MAX}
     * (inclusive).
     *
     * @return the unsigned int value
     *
     * @throws IOException if an I/O error occurs
     */
    int readUnsignedInt(int size) throws IOException;


    /**
     * Reads a signed int value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#INT_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#INT_SIZE_MAX}
     * (inclusive).
     *
     * @return a signed int value.
     *
     * @throws IOException if an I/O error occurs.
     */
    int readInt(int size) throws IOException;


    /**
     * Reads an unsigned long value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#ULONG_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#ULONG_SIZE_MAX}
     * (inclusive).
     *
     * @return an unsigned long value.
     *
     * @throws IOException if an I/O error occurs
     */
    long readUnsignedLong(int size) throws IOException;


    /**
     * Reads a signed long value.
     *
     * @param size the number of bits for the value; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#LONG_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#LONG_SIZE_MAX}
     * (inclusive).
     *
     * @return a signed long value
     *
     * @throws IOException if an I/O error occurs.
     */
    long readLong(int size) throws IOException;


    byte[] readBytes(byte[] bytes, int offset, int length) throws IOException;


    byte[] readBytes(byte[] bytes, int offset) throws IOException;


    /**
     * Reads a byte array.
     *
     * @param scale the length scale between
     * {@value com.github.jinahya.bit.io.BitIoConstants#SCALE_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#SCALE_SIZE_MAX}
     * (inclusive).
     * @param range the number of bits for each byte between
     * {@value com.github.jinahya.bit.io.BitIoConstants#RANGE_SIZE_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#RANGE_SIZE_MAX}
     * (inclusive).
     *
     * @return a byte array.
     *
     * @throws IOException if an I/O error occurs.
     */
    byte[] readBytes(int scale, int range) throws IOException;


    /**
     * Reads a string. This method reads a byte array via
     * {@link #readBytes(int, int)} with {@code scale} of
     * {@value com.github.jinahya.bit.io.BitIoConstants#SCALE_SIZE_MAX} and
     * {@code range} of
     * {@value com.github.jinahya.bit.io.BitIoConstants#RANGE_SIZE_MAX} and
     * returns the output string created via
     * {@link String#String(byte[], java.lang.String)} with the byte array and
     * given {@code charsetName}.
     *
     * @param charsetName the character set name to encode output string.
     *
     * @return a string value.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #readBytes(int, int)
     * @see String#String(byte[], java.lang.String)
     */
    String readString(String charsetName) throws IOException;


    /**
     * Reads a {@code US-ASCII} encoded string. This method reads a byte array
     * via {@link #readBytes(int, int)} with {@code scale} of
     * {@value com.github.jinahya.bit.io.BitIoConstants#SCALE_SIZE_MAX} and
     * {@code range} of {@code 0x07} and returns the output string created by
     * {@link String#String(byte[], java.lang.String)} with the byte array and
     * {@code US-ASCII}.
     *
     * @return a {@code US-ASCII} encoded string.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #readBytes(int, int)
     * @see String#String(byte[], java.lang.String)
     */
    String readAscii() throws IOException;


    /**
     * Aligns to given number of bytes.
     *
     * @param bytes the number of bytes to align; between
     * {@value com.github.jinahya.bit.io.BitIoConstants#ALIGN_BYTES_MIN}
     * (inclusive) and
     * {@value com.github.jinahya.bit.io.BitIoConstants#ALIGN_BYTES_MAX}
     * (inclusive).
     *
     * @return the number of bits discarded for alignment
     *
     * @throws IOException if an I/O error occurs.
     */
    int align(int bytes) throws IOException;

}

