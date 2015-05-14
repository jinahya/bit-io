/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
 *
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
 */


package com.github.jinahya.bio;


import java.io.IOException;


/**
 * An interface for consuming bytes.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
//@FunctionalInterface
public interface ByteOutput {


    /**
     * Writes an unsigned 8-bit integer.
     *
     * @param value an unsigned 8-bit byte value between {@code 0} (inclusive)
     * and {@code 256} (exclusive).
     *
     * @throws IOException if an I/O error occurs.
     */
    void writeUnsignedByte(final int value) throws IOException;


}

