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

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    // -----------------------------------------------------------------------------------------------------------------
    static <T extends Comparable<? super T>, X extends Exception> T requireGreaterThanOrEqualsTo2(
            final T value, final T minimum, final _Supplier<? extends X> exceptionSupplier)
            throws X {
        if (value.compareTo(minimum) >= 0) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    static <T extends Comparable<? super T>, X extends Exception> T requireLessThanOrEqualsTo2(
            final T value, final T maximum, final _Supplier<? extends X> exceptionSupplier)
            throws X {
        if (value.compareTo(maximum) <= 0) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    static <X extends Exception> int requireGreaterThanOrEqualsTo(final int value, final int minimum,
                                                                  final _Supplier<? extends X> exceptionSupplier)
            throws X {
        if (value >= minimum) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    static <X extends Exception> int requireLessThanOrEqualsTo(final int value, final int maximum,
                                                               final _Supplier<? extends X> exceptionSupplier)
            throws X {
        if (value <= maximum) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    static <X extends Exception> long requireGreaterThanOrEqualsTo(final long value, final long minimum,
                                                                   final _Supplier<? extends X> exceptionSupplier)
            throws X {
        if (value >= minimum) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    static <X extends Exception> long requireLessThanOrEqualsTo(final long value, final long maximum,
                                                                final _Supplier<? extends X> exceptionSupplier)
            throws X {
        if (value <= maximum) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private _Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
