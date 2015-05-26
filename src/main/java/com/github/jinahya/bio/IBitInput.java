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


package com.github.jinahya.bio;


import java.io.IOException;


/**
 * An interface for reading arbitrary length of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface IBitInput {


    boolean readBoolean() throws IOException;


    /**
     * Reads an unsigned int value.
     *
     * @param length the number of bits for the value; between 1 (inclusive) and
     * 32 (exclusive).
     *
     * @return the unsigned int value
     *
     * @throws IllegalArgumentException if {@code length} is not valid.
     * @throws IOException if an I/O error occurs
     */
    int readUnsignedInt(int length) throws IOException;


    /**
     * Reads a signed int value.
     *
     * @param length the number of bits for the value; between 1 (exclusive) and
     * 32 (inclusive).
     *
     * @return a signed int value.
     *
     * @throws IllegalArgumentException if {@code length} is not valid.
     * @throws IOException if an I/O error occurs.
     */
    int readInt(int length) throws IOException;


    /**
     * Reads a float value.
     *
     * @return a float value.
     *
     * @throws IOException if an I/O error occurs
     */
    float readFloat32() throws IOException;


    /**
     * Reads an unsigned long value.
     *
     * @param length the number of bits for the value; between 1 (inclusive) and
     * 64 (exclusive).
     *
     * @return an unsigned long value.
     *
     * @throws IllegalArgumentException if {@code length} is not valid
     * @throws IOException if an I/O error occurs
     */
    long readUnsignedLong(int length) throws IOException;


    /**
     * Reads a signed long value.
     *
     * @param length the number of bits for the value; between 1 (exclusive) and
     * 64 (inclusive).
     *
     * @return a signed long value
     *
     * @throws IllegalArgumentException if {@code length} is not valid
     * @throws IOException if an I/O error occurs.
     */
    long readLong(int length) throws IOException;


    /**
     * Reads a 64-bit double value.
     *
     * @return a 64-bit double value
     *
     * @throws IOException if an I/O error occurs.
     */
    double readDouble64() throws IOException;


    /**
     * Reads a byte array.
     *
     * @param scale the length scale between {@value Bytes#BYTES_SCALE_MIN}
     * (inclusive) and {@value Bytes#BYTES_SCALE_MAX} (inclusive).
     * @param range the number of bits for each byte between
     * {@value Bytes#BYTES_RANGE_MIN} (inclusive) and
     * {@value Bytes#BYTES_RANGE_MAX} (inclusive).
     *
     * @return a byte array.
     *
     * @throws IOException if an I/O error occurs.
     */
    byte[] readBytes(int scale, int range) throws IOException;


    /**
     * Reads a string. This method reads a byte array via
     * {@link #readBytes(int, int)} with {@code scale} of
     * {@value Bytes#BYTES_SCALE_MAX} and {@code range} of
     * {@value Bytes#BYTES_RANGE_MAX} and returns the output string created by
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
     * {@value Bytes#BYTES_SCALE_MAX} and {@code range} of {@code 7} and returns
     * the output string created by
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
    String readUsAsciiString() throws IOException;


    /**
     * Aligns to given number of bytes.
     *
     * @param length the number of bytes to align; between 0 (exclusive) and
     * {@value java.lang.Short#MAX_VALUE} (inclusive).
     *
     * @return the number of bits discarded for alignment
     *
     * @throws IOException if an I/O error occurs.
     */
    int align(int length) throws IOException;


    /**
     * Returns the number of bytes read from the underlying byte input so far.
     *
     * @return the number of bytes read so far.
     */
    long getCount();


}

