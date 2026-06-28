package com.github.jinahya.bit.io.miscellaneous;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
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

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;

final class _Utils {

    static byte[] fromHex(final String value) {
        requireNonNullValue(value);
        final String hex;
        if ((value.length() & 1) == 0) {
            hex = value;
        } else {
            hex = "0" + value;
        }
        final byte[] bytes = new byte[hex.length() >> 1];
        for (int i = 0; i < bytes.length; i++) {
            final int high = Character.digit(hex.charAt(i << 1), 16);
            final int low = Character.digit(hex.charAt((i << 1) + 1), 16);
            if (high < 0 || low < 0) {
                throw new IllegalArgumentException("invalid hexadecimal string: " + value);
            }
            bytes[i] = (byte) ((high << 4) | low);
        }
        return bytes;
    }

    static String toHex(final byte[] value) {
        requireNonNullValue(value);
        final char[] chars = new char[value.length << 1];
        for (int i = 0; i < value.length; i++) {
            chars[i << 1] = HEX_DIGITS[(value[i] & 0xF0) >>> 4];
            chars[(i << 1) + 1] = HEX_DIGITS[value[i] & 0x0F];
        }
        return new String(chars);
    }

    static <T extends BitInput> T requireNonNullInput(final T input) {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        return input;
    }

    static <T extends BitOutput> T requireNonNullOutput(final T output) {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        return output;
    }

    static <T> T requireNonNullValue(final T value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return value;
    }

    /**
     * Returns the zero-based index of the highest set bit in specified value. A {@code 1.4}-safe replacement for
     * {@code 63 - }{@link Long#numberOfLeadingZeros(long)} ({@code Long.numberOfLeadingZeros} is a Java {@code 5}
     * method that the retro-translated {@code 1.3}/{@code 1.4} artifacts cannot rely on).
     *
     * @param value the value.
     * @return the index of the highest set bit, between {@code 0} and {@code 63}, both inclusive; {@code -1} when
     *         {@code value} is {@code 0}.
     */
    static int highestOneBitIndex(final long value) {
        int index = -1;
        for (long v = value; v != 0L; v >>>= 1) {
            index++;
        }
        return index;
    }

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private _Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
