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


/**
 * A class for constraints.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoConstraints {


    public static int requireValidAlighBytes(final int bytes) {

        if (bytes < BitIoConstants.ALIGN_BYTES_MIN) {
            throw new IllegalArgumentException(
                "bytes(" + bytes + ") < " + BitIoConstants.ALIGN_BYTES_MIN);
        }

        if (bytes > BitIoConstants.ALIGN_BYTES_MAX) {
            throw new IllegalArgumentException(
                "bytes(" + bytes + ") > " + BitIoConstants.ALIGN_BYTES_MAX);
        }

        return bytes;
    }


    public static int requireValidUnsignedByteSize(final int size) {

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


    public static int requireValidUnsignedByteValue(final int value,
                                                    final int size) {

        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }

        if ((value >> size) > 0) {
            throw new IllegalArgumentException(
                "(value(" + value + ") >> size(" + size + ")) > 0");
        }

        return value;
    }


    public static int requireValidUnsignedShortSize(final int size) {

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


    public static int requireValidUnsignedShortValue(final int value,
                                                     final int size) {

        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }

        if ((value >> requireValidUnsignedShortSize(size)) > 0) {
            throw new IllegalArgumentException(
                "(value(" + value + ") >> size(" + size + ")) < 0");
        }

        return value;
    }


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


    public static int requireValidUnsignedIntValue(final int value,
                                                   final int size) {

        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }

        if ((value >> requireValidUnsignedIntSize(size)) > 0) {
            throw new IllegalArgumentException(
                "(value(" + value + ") >> size(" + size + ")) > 0");
        }

        return value;
    }


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


    public static int requireValidIntValue(final int value, final int size) {

        requireValidIntSize(size);

        if (size == BitIoConstants.INT_SIZE_MAX) {
            return value;
        }

        if (value < 0) {
            if ((value >> size) != -1) {
                throw new IllegalArgumentException(
                    "(value(" + value + " >> size(" + size + ")) != -1");
            }
        } else if ((value >> size) != 0) {
            throw new IllegalArgumentException(
                "(value(" + value + " >> size(" + size + ")) != 0");
        }

        return value;
    }


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


    public static long requireValidUnsignedLongValue(final long value,
                                                     final int size) {

        if (value < 0L) {
            throw new IllegalArgumentException("value(" + value + ") < 0L");
        }

        if ((value >> size) > 0) {
            throw new IllegalArgumentException(
                "(value(" + value + ") >> size(" + size + ")) > 0");
        }

        return value;
    }


    public static int requireValidLongSize(final int size) {

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


    public static long requireValidLongValue(final long value, final int size) {

        requireValidLongSize(size);

        if (size == BitIoConstants.LONG_SIZE_MAX) {
            return value;
        }

        if (value < 0) {
            if ((value >> size) != -1L) {
                throw new IllegalArgumentException(
                    "(value(" + value + ") >> size(" + size + ")) != -1L");
            }
        } else if ((value >> size) != 0L) {
            throw new IllegalArgumentException(
                "(value(" + value + ") >> size(" + size + ")) != 0L");
        }

        return value;
    }


    /**
     * Checks that the specified value is valid.
     *
     * @param size the value to check
     *
     * @return given value if it's valid.
     */
    public static int requireValidLengthSize(final int size) {

        if (size < BitIoConstants.LENGTH_SIZE_MIN) {
            throw new IllegalArgumentException(
                "scale(" + size + ") < " + BitIoConstants.LENGTH_SIZE_MIN);
        }

        if (size > BitIoConstants.LENGTH_SIZE_MAX) {
            throw new IllegalArgumentException(
                "scale(" + size + ") > " + BitIoConstants.LENGTH_SIZE_MAX);
        }

        return size;
    }


    public static int requireValidLengthValue(final int value, final int size) {

        if (value < 0) {
            throw new IllegalArgumentException("value(" + value + ") < 0");
        }

        requireValidLengthSize(size);

        if ((value >> size) > 0) {
            throw new IllegalArgumentException(
                "(value(" + value + ") >> size(" + size + ")) > 0");
        }

        return value;
    }


    /**
     * Checks that the specified value is valid.
     *
     * @param range the value to check
     *
     * @return given value if it's valid.
     *
     * @throws IllegalArgumentException if given value is not valid.
     */
    public static int requireValidBytesRange(final int range) {

        if (range < BitIoConstants.UBYTE_SIZE_MIN) {
            throw new IllegalArgumentException(
                "range(" + range + ") < " + BitIoConstants.UBYTE_SIZE_MIN);
        }

        if (range > BitIoConstants.UBYTE_SIZE_MAX) {
            throw new IllegalArgumentException(
                "range(" + range + ") > " + BitIoConstants.UBYTE_SIZE_MAX);
        }

        return range;
    }

}

