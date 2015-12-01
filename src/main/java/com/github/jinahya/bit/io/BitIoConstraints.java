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


import static com.github.jinahya.bit.io.BitIoConstants.ALIGN_BYTES_MAX;
import static com.github.jinahya.bit.io.BitIoConstants.ALIGN_BYTES_MIN;
import static com.github.jinahya.bit.io.BitIoConstants.RANGE_SIZE_MAX;
import static com.github.jinahya.bit.io.BitIoConstants.RANGE_SIZE_MIN;
import static com.github.jinahya.bit.io.BitIoConstants.SCALE_SIZE_MAX;
import static com.github.jinahya.bit.io.BitIoConstants.SCALE_SIZE_MIN;


/**
 * A class for constraints.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoConstraints {


    static int requireValidAlighBytes(final int bytes) {

        if (bytes < ALIGN_BYTES_MIN) {
            throw new IllegalArgumentException(
                "align(" + bytes + ") < " + ALIGN_BYTES_MIN);
        }

        if (bytes > ALIGN_BYTES_MAX) {
            throw new IllegalArgumentException(
                "bytes(" + bytes + ") > " + ALIGN_BYTES_MAX);
        }

        return bytes;
    }


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


    static int requireValidUnsignedIntSize(final int size) {

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


    static int requireValidIntSize(final int size) {

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


    static int requireValidUnsignedLongSize(final int size) {

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


    /**
     * Checks that the specified value is valid.
     *
     * @param scale the value to check
     *
     * @return given value if it's valid.
     */
    static int requireValidBytesScale(final int scale) {

        if (scale < SCALE_SIZE_MIN) {
            throw new IllegalArgumentException(
                "scale(" + scale + ") < " + SCALE_SIZE_MIN);
        }

        if (scale > SCALE_SIZE_MAX) {
            throw new IllegalArgumentException(
                "scale(" + scale + ") > " + SCALE_SIZE_MAX);
        }

        return scale;
    }


    /**
     * Checks that the specified value is valid.
     *
     * @param range the value to check
     *
     * @return given value if it's valid.
     *
     * @throws IllegalArgumentException if given value is not valid.
     *
     * @see #RANGE_MIN
     * @see #RANGE_MAX
     */
    static int requireValidBytesRange(final int range) {

        if (range < RANGE_SIZE_MIN) {
            throw new IllegalArgumentException(
                "range(" + range + ") < " + RANGE_SIZE_MIN);
        }

        if (range > RANGE_SIZE_MAX) {
            throw new IllegalArgumentException(
                "range(" + range + ") > " + RANGE_SIZE_MAX);
        }

        return range;
    }

}

