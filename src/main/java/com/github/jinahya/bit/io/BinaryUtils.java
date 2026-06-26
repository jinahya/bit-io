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

/**
 * Utilities for low-level binary manipulation.
 */
final class BinaryUtils {

    /**
     * Returns the value obtained by reversing the order of both bytes of specified {@code short} value; that is,
     * converts between big-endian and little-endian byte order.
     *
     * <p>The operation is its own inverse: {@code reverseBytes(reverseBytes(v)) == v} for every {@code v}.</p>
     *
     * @param value the value whose bytes are to be reversed.
     * @return the value obtained by reversing the bytes of {@code value}.
     */
    public static short reverseBytes(final short value) {
        // equivalent to Short#reverseBytes(short), open-coded so the retrotranslated (1.3/1.4) artifacts do not depend
        // on the Java-5 Short#reverseBytes, which the backport runtime does not provide.
        return (short) ((value << 8) | ((value & 0xFFFF) >>> 8));
    }

    /**
     * Returns the value obtained by reversing the order of all {@value java.lang.Integer#BYTES} bytes of specified
     * {@code int} value; that is, converts between big-endian and little-endian byte order.
     *
     * <p>The operation is its own inverse: {@code reverseBytes(reverseBytes(v)) == v} for every {@code v}.</p>
     *
     * @param value the value whose bytes are to be reversed.
     * @return the value obtained by reversing the bytes of {@code value}.
     */
    public static int reverseBytes(final int value) {
        // equivalent to Integer#reverseBytes(int), open-coded so the retrotranslated (1.3/1.4) artifacts do not depend
        // on the Java-5 Integer#reverseBytes, which the backport runtime does not provide.
        return ((value & 0x000000FF) << 24)
               | ((value & 0x0000FF00) << 8)
               | ((value >>> 8) & 0x0000FF00)
               | ((value >>> 24) & 0x000000FF);
    }

    /**
     * Returns the value obtained by reversing the order of all {@value java.lang.Long#BYTES} bytes of specified
     * {@code long} value; that is, converts between big-endian and little-endian byte order.
     *
     * <p>The operation is its own inverse: {@code reverseBytes(reverseBytes(v)) == v} for every {@code v}.</p>
     *
     * @param value the value whose bytes are to be reversed.
     * @return the value obtained by reversing the bytes of {@code value}.
     */
    public static long reverseBytes(final long value) {
        // equivalent to Long#reverseBytes(long), open-coded with shifts/masks only so the retrotranslated (1.3/1.4)
        // artifacts do not depend on the Java-5 Long#reverseBytes, which the backport runtime does not provide.
        return ((value & 0x00000000000000FFL) << 56)
               | ((value & 0x000000000000FF00L) << 40)
               | ((value & 0x0000000000FF0000L) << 24)
               | ((value & 0x00000000FF000000L) << 8)
               | ((value >>> 8) & 0x00000000FF000000L)
               | ((value >>> 24) & 0x0000000000FF0000L)
               | ((value >>> 40) & 0x000000000000FF00L)
               | ((value >>> 56) & 0x00000000000000FFL);
    }

    /**
     * Returns the value obtained by reversing the order of both bytes of specified {@code char} value; that is,
     * converts between big-endian and little-endian byte order.
     *
     * <p>The operation is its own inverse: {@code reverseBytes(reverseBytes(v)) == v} for every {@code v}.</p>
     *
     * @param value the value whose bytes are to be reversed.
     * @return the value obtained by reversing the bytes of {@code value}.
     */
    public static char reverseBytes(final char value) {
        // equivalent to Character#reverseBytes(char), open-coded so the retrotranslated (1.3/1.4) artifacts do not
        // depend on the Java-5 Character#reverseBytes, which the backport runtime does not provide.
        return (char) ((value << 8) | (value >>> 8));
    }

    private BinaryUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
