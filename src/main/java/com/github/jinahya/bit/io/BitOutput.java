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
     * Writes a 1-bit boolean value. This method writes {@code 1} for
     * {@code true} and {@code 0} for {@code false}.
     *
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs
     */
    void writeBoolean(boolean value) throws IOException;


    /**
     * Writes a {@code byte} value.
     *
     * @param unsigned a flag indicating unsigned value.
     * @param size the number of bits for value.
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeByte(boolean unsigned, int size, byte value) throws IOException;


    void writeShort(boolean unsigned, int size, short value) throws IOException;


    void writeChar(int size, char value) throws IOException;


    void writeInt(boolean unsigned, int size, int value) throws IOException;


    void writeLong(boolean unsigned, int size, long value) throws IOException;


    void writeFloat(float value) throws IOException;


    void writeDouble(double value) throws IOException;


    /**
     * Writes given object reference value.
     *
     * @param <T> value type parameter
     * @param value the value to write.
     *
     * @throws IOException if an I/O error occurs.
     */
    <T extends BitWritable> void writeObject(T value) throws IOException;


    /**
     * Writes given object reference value which is possibly {@code null}. This
     * method writes preceding 1-bit boolean flag which representing the
     * nullability of the value and invokes
     * {@link #writeObject(com.github.jinahya.bit.io.BitWritable)} with
     * specified value if it is not {@code null}.
     *
     * @param <T> value type parameter
     * @param value the value to write
     *
     * @throws IOException if an I/O error occurs.
     */
    <T extends BitWritable> void writeNullable(T value) throws IOException;


    /**
     * Returns the number of bytes written so far.
     *
     * @return number of byte written so far.
     */
    long getCount();


    /**
     * Returns the bit index to write in current octet.
     *
     * @return bit index to write in current octet.
     */
    int getIndex();


    /**
     * Aligns to specified number of bytes.
     *
     * @param bytes the number of bytes to align; must be positive.
     *
     * @return the number of bits padded while aligning
     *
     * @throws IOException if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

}

