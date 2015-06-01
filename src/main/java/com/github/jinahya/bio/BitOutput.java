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
public interface BitOutput {


    /**
     * Writes a 1-bit boolean value. {@code 0b1} for {@code true} and
     * {@code 0b0} for {@code false}.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs
     */
    void writeBoolean(boolean value) throws IOException;


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
    void writeUnsignedInt(int length, int value) throws IOException;


    /**
     * Writes a signed int value. Only the number of specified bits in
     * {@code value} are written.
     *
     * @param length the number of lower bits to write; between {@code 1}
     * (exclusive) and {@code 32} (inclusive).
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeInt(int length, int value) throws IOException;


    /**
     * Writes a 32-bit int value resulting from
     * {@link Float#floatToIntBits(float)} with specified value.
     *
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Float#floatToIntBits(float)
     */
    void writeFloat32(float value) throws IOException;


    /**
     * Writes a 32-bit int value resulting from
     * {@link Float#floatToRawIntBits(float)} with specified value.
     *
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Float#floatToRawIntBits(float)
     */
    void writeFloat32Raw(float value) throws IOException;


    /**
     * Writes an unsigned long value. Only the number of specified lower bits in
     * {@code value} are written.
     *
     * @param length the number of lower bits to write; between 1 (inclusive)
     * and 64 (exclusive).
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeUnsignedLong(int length, long value) throws IOException;


    /**
     * Writes a signed long value. Only the number of specified lower bits in
     * {@code value} are written.
     *
     * @param length the number of lower valid bits to write; between 1
     * (exclusive) and 64 (inclusive).
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeLong(int length, long value) throws IOException;


    /**
     * Writes a 64-bit long value resulting from
     * {@link Double#doubleToLongBits(double)} with specified value.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Double#doubleToLongBits(double)
     */
    void writeDouble64(double value) throws IOException;


    /**
     * Writes a 64-bit long value resulting from
     * {@link Double#doubleToRawLongBits(double)} with specified value.
     *
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Double#doubleToRawLongBits(double)
     */
    void writeDouble64Raw(double value) throws IOException;


    void writeBytesFully(int length, int range, ByteInput input)
        throws IOException;


    /**
     * Writes an array of bytes.
     *
     * @param scale the number of bits to present the length of array between
     * {@value Bytes#SCALE_MIN} (inclusive) and {@value Bytes#SCALE_MAX}
     * (inclusive).
     * @param range the number of lower bits valid in each byte in array;
     * between {@value Bytes#RANGE_MIN} (inclusive) and {@value Bytes#RANGE_MAX}
     * (inclusive).
     * @param value the array to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see Bytes#SCALE_MIN
     * @see Bytes#SCALE_MAX
     * @see Bytes#RANGE_MIN
     * @see Bytes#RANGE_MAX
     */
    void writeBytes(int scale, int range, byte[] value) throws IOException;


    /**
     * Writes a string value. This method writes the decoded byte array via
     * {@link #writeBytes(int, int, byte[])} with {@code scale} of
     * {@value Bytes#SCALE_MAX} and {@code range} of {@value Bytes#RANGE_MAX}.
     *
     * @param value the string value to write.
     * @param charsetName the character set name to decode the string
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #writeBytes(int, int, byte[])
     */
    void writeString(String value, String charsetName) throws IOException;


    /**
     * Writes a {@code US-ASCII} encoded string value. This method writes the
     * decoded byte array via {@link #writeBytes(int, int, byte[])} with
     * {@code scale} of {@value Bytes#SCALE_MAX} and {@code range} of {@code 7}.
     *
     * @param value the string value to write.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @see #writeBytes(int, int, byte[])
     */
    void writeUsAsciiString(String value) throws IOException;


    /**
     * Aligns to specified number of bytes.
     *
     * @param length the number of bytes to align; between
     * {@value Bytes#ALIGN_MIN} (exclusive) and {@value Bytes#ALIGH_MAX}
     * (inclusive).
     *
     * @return the number of bits padded for alignment
     *
     * @throws IOException if an I/O error occurs.
     */
    int align(int length) throws IOException;


}

