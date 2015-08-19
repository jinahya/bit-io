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


import static com.github.jinahya.bit.io.BitIoConstants.ALIGN_LENGTH_MAX;
import static com.github.jinahya.bit.io.BitIoConstants.ALIGN_LENGTH_MIN;
import static com.github.jinahya.bit.io.BitIoConstants.BYTES_RANGE_MAX;
import static com.github.jinahya.bit.io.BitIoConstants.BYTES_RANGE_MIN;
import static com.github.jinahya.bit.io.BitIoConstants.BYTES_SCALE_MAX;
import static com.github.jinahya.bit.io.BitIoConstants.BYTES_SCALE_MIN;


/**
 * A base class.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public final class BitIoConstraints {


    protected static int requireValidAlighLength(final int align) {

        if (align < ALIGN_LENGTH_MIN) {
            throw new IllegalArgumentException(
                "align(" + align + ") < " + ALIGN_LENGTH_MIN);
        }

        if (align > ALIGN_LENGTH_MAX) {
            throw new IllegalArgumentException(
                "align(" + align + ") > " + ALIGN_LENGTH_MAX);
        }

        return align;
    }


    protected static int requireValidUnsignedByteLength(final int length) {

        if (length < 0x01) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length > 0x08) {
            throw new IllegalArgumentException("length(" + length + ") > 8");
        }

        return length;
    }


    protected static int requireValidUnsignedShortLength(final int length) {

        if (length < 0x01) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length > 0x10) {
            throw new IllegalArgumentException("length(" + length + ") > 16");
        }

        return length;
    }


    protected static int requireValidUnsignedIntLength(final int length) {

        if (length < 0x01) {
            throw new IllegalArgumentException("length(" + length + ") < 0x01");
        }

        if (length >= 0x20) {
            throw new IllegalArgumentException(
                "length(" + length + ") >= 0x20");
        }

        return length;
    }


    protected static int requireValidIntLength(final int length) {

        if (length <= 0x01) {
            throw new IllegalArgumentException(
                "length(" + length + ") <= 0x01");
        }

        if (length > 0x20) {
            throw new IllegalArgumentException("length(" + length + ") > 0x20");
        }

        return length;
    }


    protected static int requireValidUnsignedLongLength(final int length) {

        if (length < 0x01) {
            throw new IllegalArgumentException("length(" + length + ") < 0x01");
        }

        if (length >= 0x40) {
            throw new IllegalArgumentException(
                "length(" + length + ") >= 0x40");
        }

        return length;
    }


    protected static int requireValidLongLength(final int length) {

        if (length <= 0x01) {
            throw new IllegalArgumentException(
                "length(" + length + ") <= 0x01");
        }

        if (length > 0x40) {
            throw new IllegalArgumentException("length(" + length + ") > 0x40");
        }

        return length;
    }


    /**
     * Checks that the specified value is valid.
     *
     * @param scale the value to check
     *
     * @return given value if it's valid.
     *
     * @throws IllegalArgumentException if given value is not valid.
     *
     * @see #SCALE_MIN
     * @see #SCALE_MAX
     */
    protected static int requireValidBytesScale(final int scale) {

        if (scale < BYTES_SCALE_MIN) {
            throw new IllegalArgumentException(
                "scale(" + scale + ") <= " + BYTES_SCALE_MIN);
        }

        if (scale > BYTES_SCALE_MAX) {
            throw new IllegalArgumentException(
                "scale(" + scale + ") > " + BYTES_SCALE_MAX);
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
    protected static int requireValidBytesRange(final int range) {

        if (range < BYTES_RANGE_MIN) {
            throw new IllegalArgumentException(
                "range(" + range + ") < " + BYTES_RANGE_MIN);
        }

        if (range > BYTES_RANGE_MAX) {
            throw new IllegalArgumentException(
                "range(" + range + ") > " + BYTES_RANGE_MAX);
        }

        return range;
    }


}

