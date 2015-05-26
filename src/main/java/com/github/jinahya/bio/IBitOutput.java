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
 * A class for writing arbitrary length of bits.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public interface IBitOutput {


    /**
     * Writes a 1-bit boolean value. {@code 0b1} for {@code true} and
     * {@code 0b0} for {@code false}.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs
     */
    void writeBoolean(final boolean value) throws IOException;


    /**
     * Writes an unsigned int value. Only the lower specified number of bits in
     * {@code value} are written.
     *
     * @param length the number of lower bits to write; between {@code 1}
     * inclusive and {@code 32} exclusive.
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeUnsignedInt(final int length, final int value) throws IOException;


    /**
     * Writes a signed int value. Only the number of specified bits in
     * {@code value} are written including the sign bit at {@code length}.
     *
     * @param length the number of lower bits in {@code value} to write; between
     * {@code 1} exclusive and {@code 32} inclusive.
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeInt(final int length, final int value) throws IOException;


    /**
     * Writes a float value.
     *
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Float#floatToIntBits(float)
     */
    void writeFloat32(final float value) throws IOException;


    /**
     * Writes a float value.
     *
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Float#floatToRawIntBits(float)
     */
    void writeFloat32Raw(final float value) throws IOException;


    /**
     * Writes an unsigned long value. Only the number of specified bits in
     * {@code value} are writtern.
     *
     * @param length the number of lower valid bits to write; between 1
     * (inclusive) and 64 (exclusive).
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #requireValidUnsignedLongLength(int)
     */
    void writeUnsignedLong(final int length, final long value)
        throws IOException;


    /**
     * Writes a signed long value. Only the number of specified bits in
     * {@code value} are written including the sign bit at {@code length}.
     *
     * @param length the number of lower valid bits to write; between 1
     * (exclusive) and 64 (inclusive).
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeLong(final int length, final long value) throws IOException;


    /**
     * Writes a double value.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Double#doubleToLongBits(double)
     */
    void writeDouble64(final double value) throws IOException;


    /**
     * Writes a double value.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Double#doubleToRawLongBits(double)
     */
    void writeDouble64Raw(final double value) throws IOException;


    void writeBytes(final int scale, final int range, final byte[] value)
        throws IOException;


    /**
     * Writes a string value. This method writes the decoded byte array with
     * {@code scale} of {@code 16} and {@code range} of {@code 8}.
     *
     * @param value the string value to write.
     * @param charsetName the character set name to decode the string
     *
     * @throws NullPointerException if {@code value} or {@code charsetName} is
     * {@code null}
     * @throws IOException if an I/O error occurs.
     *
     * @see #writeBytes(int, int, byte[])
     */
    void writeString(final String value, final String charsetName)
        throws IOException;


    /**
     * Writes a {@code US-ASCII} encoded string value. This method writes the
     * decoded byte array with {@code scale} of {@code 16} and {@code range} of
     * {@code 7}.
     *
     * @param value the string value to write.
     *
     * @throws NullPointerException if {@code value} is {@code null}.
     * @throws IOException if an I/O error occurs.
     *
     * @see #writeBytes(int, int, byte[])
     */
    void writeUsAsciiString(final String value) throws IOException;


    /**
     * Aligns to specified number of bytes.
     *
     * @param length the number of bytes to align; between 0 (exclusive) and
     * {@value java.lang.Short#MAX_VALUE} (inclusive).
     *
     * @return the number of bits padded for alignment
     *
     * @throws IllegalArgumentException if {@code length} is not valid.
     * @throws IOException if an I/O error occurs.
     */
    int align(final int length) throws IOException;


}

