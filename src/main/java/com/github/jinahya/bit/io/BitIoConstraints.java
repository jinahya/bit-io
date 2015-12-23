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


import java.lang.reflect.Array;


/**
 * A class for constraints.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class BitIoConstraints {


    static int requireValidUnsignedByteSize(final int size) {

        if (size < BitIoConstants.UBYTE_SIZE_MIN) {
            throw new IllegalArgumentException(
                "size(" + size + ") < " + BitIoConstants.UBYTE_SIZE_MIN);
        }

        if (size > BitIoConstants.UBYTE_SIZE_MAX) {
            throw new IllegalArgumentException(
                "size(" + size + ") > " + BitIoConstants.UBYTE_SIZE_MAX);
        }

        return size;
    }


    static int requireValidUnsignedByteValue(final int size, final int value) {

        requireValidUnsignedByteSize(size);

        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }

        final int shifted = value >> size;
        if (shifted > 0) {
            throw new IllegalArgumentException(
                shifted + "(" + value + " >> " + size + ") > 0");
        }

        return value;
    }


    // ---------------------------------------------------------- unsigned short
    static int requireValidUnsignedShortSize(final int size) {

        if (size < BitIoConstants.USHORT_SIZE_MIN) {
            throw new IllegalArgumentException(
                "size(" + size + ") < " + BitIoConstants.USHORT_SIZE_MIN);
        }

        if (size > BitIoConstants.USHORT_SIZE_MAX) {
            throw new IllegalArgumentException(
                "size(" + size + ") > " + BitIoConstants.USHORT_SIZE_MAX);
        }

        return size;
    }


    static int requireValidUnsignedShortValue(final int size, final int value) {

        requireValidUnsignedShortSize(size);

        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }

        final int shifted = value >> size;
        if (shifted > 0) {
            throw new IllegalArgumentException(
                shifted + "(" + value + " >> " + size + ") < 0");
        }

        return value;
    }


    // ------------------------------------------------------------ unsigned int
    public static int requireValidUnsignedIntSize(final int size) {

        if (size < BitIoConstants.UINT_SIZE_MIN) {
            throw new IllegalArgumentException(
                "size(" + size + ") < " + BitIoConstants.UINT_SIZE_MIN);
        }

        if (size > BitIoConstants.UINT_SIZE_MAX) {
            throw new IllegalArgumentException(
                "size(" + size + ") > " + BitIoConstants.UINT_SIZE_MAX);
        }

        return size;
    }


    public static int requireValidUnsignedIntValue(final int size,
                                                   final int value) {

        requireValidUnsignedIntSize(size);

        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }

        final int shifted = value >> size;
        if (shifted > 0) {
            throw new IllegalArgumentException(
                shifted + "(" + value + " >> " + size + ") > 0");
        }

        return value;
    }


    // --------------------------------------------------------------------- int
    public static int requireValidIntSize(final int size) {

        if (size < BitIoConstants.INT_SIZE_MIN) {
            throw new IllegalArgumentException(
                "size(" + size + ") < " + BitIoConstants.INT_SIZE_MIN);
        }

        if (size > BitIoConstants.INT_SIZE_MAX) {
            throw new IllegalArgumentException(
                "size(" + size + ") > " + BitIoConstants.INT_SIZE_MAX);
        }

        return size;
    }


    static int requireValidIntValue(final int size, final int value) {

        requireValidIntSize(size);

        if (size == BitIoConstants.INT_SIZE_MAX) {
            return value;
        }

        final int shifted = value >> size;
        if (value < 0) {
            if (shifted != -1) {
                throw new IllegalArgumentException(
                    shifted + "(" + value + " >> " + size + ") != -1");
            }
        } else if (shifted != 0) {
            throw new IllegalArgumentException(
                shifted + "(" + value + " >> " + size + ") != 0");
        }

        return value;
    }


    // ----------------------------------------------------------- unsigned long
    public static int requireValidUnsignedLongSize(final int size) {

        if (size < BitIoConstants.ULONG_SIZE_MIN) {
            throw new IllegalArgumentException(
                "size(" + size + ") < " + BitIoConstants.ULONG_SIZE_MIN);
        }

        if (size > BitIoConstants.ULONG_SIZE_MAX) {
            throw new IllegalArgumentException(
                "size(" + size + ") > " + BitIoConstants.ULONG_SIZE_MAX);
        }

        return size;
    }


    public static long requireValidUnsignedLongValue(final int size,
                                                     final long value) {

        requireValidUnsignedLongSize(size);

        if (value < 0L) {
            throw new IllegalArgumentException("value(" + value + ") < 0L");
        }

        final long shifted = value >> size;
        if (shifted > 0L) {
            throw new IllegalArgumentException(
                shifted + "(" + value + " >> " + size + ") > 0");
        }

        return value;
    }


    // -------------------------------------------------------------------- long
    static int requireValidLongSize(final int size) {

        if (size < BitIoConstants.LONG_SIZE_MIN) {
            throw new IllegalArgumentException(
                "size(" + size + ") < " + BitIoConstants.LONG_SIZE_MIN);
        }

        if (size > BitIoConstants.LONG_SIZE_MAX) {
            throw new IllegalArgumentException(
                "size(" + size + ") > " + BitIoConstants.LONG_SIZE_MAX);
        }

        return size;
    }


    static long requireValidLongValue(final int size, final long value) {

        requireValidLongSize(size);

        if (size == BitIoConstants.LONG_SIZE_MAX) {
            return value;
        }

        final long shifted = value >> size;
        if (value < 0) {
            if (shifted != -1L) {
                throw new IllegalArgumentException(
                    shifted + "(" + value + " >> " + size + ") != -1");
            }
        } else if (shifted != 0L) {
            throw new IllegalArgumentException(
                shifted + "(" + value + " >> " + size + ") != 0");
        }

        return value;
    }


    // ----------------------------------------------------- array/offset/length
    static void requireValidArrayOffsetLength(
        final Object array, final int offset, final int length) {

        if (array == null) {
            throw new NullPointerException("null array");
        }

        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException(
                "array(" + array + ") is not an array");
        }

        if (offset < 0) {
            throw new IndexOutOfBoundsException("offset(" + offset + ") < 0");
        }

        if (length < 0) {
            throw new IndexOutOfBoundsException("length(" + length + ") < 0");
        }

        final int arrayLength = Array.getLength(array);

        final int limit = offset + length;
        if (limit > arrayLength) {
            throw new IndexOutOfBoundsException(
                limit + "(" + offset + " + " + length + ") > array.length("
                + arrayLength + ")");
        }
    }


    // -------------------------------------------------------------- scale/size
    static int requireValidSize(final boolean unsigned, final int exponent,
                                final int size) {

        if (exponent <= 0) {
            throw new IllegalArgumentException(
                "exponent(" + exponent + ") <= 0");
        }

        if (exponent >= 31) {
            throw new IllegalArgumentException(
                "exponent(" + exponent + ") >= 31");
        }

        final int min = unsigned ? 1 : 2;
        final int max = ((int) Math.pow(2, exponent)) - (unsigned ? 1 : 0);

        if (size < min) {
            throw new IllegalArgumentException(
                "size(" + size + ") < min(" + min + ")");
        }

        if (size > max) {
            throw new IllegalArgumentException(
                "size(" + size + ") > max(" + max + ")");
        }

        return size;
    }


    public static int requireValidIntSize(final boolean unsigned,
                                          final int exponent, final int size) {

        if (exponent > 5) {
            throw new IllegalArgumentException(
                "exponent(" + exponent + ") > 5");
        }

        return requireValidSize(unsigned, exponent, size);
    }


    public static int requireValidLongSize(final boolean unsigned,
                                           final int exponent, final int size) {

        if (exponent > 6) {
            throw new IllegalArgumentException(
                "exponent(" + exponent + ") > 6");
        }

        return requireValidSize(unsigned, exponent, size);
    }


    // ------------------------------------------------------------ scale/length
    public static int requireValidScale(final int scale) {

        return requireValidUnsignedIntSize(scale);
    }


    public static int requireValidLength(final int scale, final int length) {

        return requireValidUnsignedIntValue(requireValidScale(scale), length);
    }


    public BitIoConstraints() {

        super();
    }

}

