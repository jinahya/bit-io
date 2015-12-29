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


import com.github.jinahya.bit.io.codec.BitDecoder;
import java.io.IOException;


/**
 * An interface for reading arbitrary length of bits.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface BitInput {


    /**
     * Reads a 1-bit boolean value. This method read a 1-bit unsigned int and
     * return {@code true} for {@code 1} and {@code false} for {@code 0}.
     *
     * @return {@code true} for {@code 1}, {@code false} for {@code 0}
     *
     * @throws IOException if an I/O error occurs.
     */
    boolean readBoolean() throws IOException;


    byte readByte(boolean unsigned, int size) throws IOException;


    short readShort(boolean unsigned, int size) throws IOException;


    int readInt(boolean unsigned, int size) throws IOException;


    long readLong(boolean unsigned, int size) throws IOException;


    /**
     * Reads a char value.
     *
     * @param size the number of bits for value; between {@code 1} (inclusive)
     * and {@code 16} (inclusive)
     *
     * @return a char value
     *
     * @throws IOException if an I/O error occurs.
     */
    char readChar(int size) throws IOException;


    float readFloat() throws IOException;


    double readDouble() throws IOException;


    <T extends BitReadable> T readObject(T value) throws IOException;


    /**
     * Reads an object reference value which is possibly {@code null}. This
     * method reads preceding 1-bit boolean flag and, if it is {@code true},
     * reads an instance of specified type.
     *
     * @param <T> value type parameter
     * @param nullable flat for nullability
     * @param type value type.
     *
     * @return an object reference value; may be {@code null}.
     *
     * @throws IOException if an I/O error occurs.
     */
    <T extends BitReadable> T readObject(boolean nullable,
                                         Class<? extends T> type)
        throws IOException;


    /**
     * Decodes an object reference value using specified decoder.
     *
     * @param <T> value type parameter
     * @param decoder the decoder
     *
     * @return decoded value.
     *
     * @throws IOException if an I/O error occurs.
     */
    <T> T decodeObject(BitDecoder<? extends T> decoder) throws IOException;


    /**
     * Returns the number of bytes read so far.
     *
     * @return number of byte read so far.
     */
    long getCount();


    /**
     * Returns the bit index to read in current octet.
     *
     * @return bit index to read in current octet.
     */
    int getIndex();


    /**
     * Aligns to given number of bytes.
     *
     * @param bytes the number of bytes to align; must be positive.
     *
     * @return the number of bits discarded while aligning
     *
     * @throws IOException if an I/O error occurs.
     */
    long align(int bytes) throws IOException;

}

