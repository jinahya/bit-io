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


import static com.github.jinahya.bit.io.BioConstants.ALIGH_MAX;
import static com.github.jinahya.bit.io.BioConstants.ALIGN_MIN;
import static com.github.jinahya.bit.io.BioConstants.RANGE_MAX;
import static com.github.jinahya.bit.io.BioConstants.RANGE_MIN;
import static com.github.jinahya.bit.io.BioConstants.SCALE_MAX;
import static com.github.jinahya.bit.io.BioConstants.SCALE_MIN;


/**
 * A base class.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public final class BioConstraints {


    protected static int requireValidUnsignedByteLength(final int length) {

        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length > 8) {
            throw new IllegalArgumentException("length(" + length + ") > 8");
        }

        return length;
    }


    protected static int requireValidUnsignedShortLength(final int length) {

        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length > 16) {
            throw new IllegalArgumentException("length(" + length + ") > 16");
        }

        return length;
    }


    protected static int requireValidUnsignedIntLength(final int length) {

        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length >= 32) {
            throw new IllegalArgumentException("length(" + length + ") >= 32");
        }

        return length;
    }


    protected static int requireValidIntLength(final int length) {

        if (true) {
            return requireValidUnsignedLongLength(length - 1) + 1;
        }

        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        }

        if (length > 32) {
            throw new IllegalArgumentException("length(" + length + ") > 32");
        }

        return length;
    }


    protected static int requireValidUnsignedLongLength(final int length) {

        if (length < 1) {
            throw new IllegalArgumentException("length(" + length + ") < 1");
        }

        if (length >= 64) {
            throw new IllegalArgumentException("length(" + length + ") >= 64");
        }

        return length;
    }


    protected static int requireValidLongLength(final int length) {

        if (true) {
            return requireValidUnsignedLongLength(length - 1) + 1;
        }

        if (length <= 1) {
            throw new IllegalArgumentException("length(" + length + ") <= 1");
        }

        if (length > 64) {
            throw new IllegalArgumentException("length(" + length + ") > 64");
        }

        return length;
    }


    protected static int requireValidAlighLength(final int length) {

        if (length <= ALIGN_MIN) {
            throw new IllegalArgumentException(
                "length(" + length + ") <= " + ALIGN_MIN);
        }

        if (length > ALIGH_MAX) {
            throw new IllegalArgumentException(
                "length(" + length + ") > " + ALIGH_MAX);
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

        if (scale < SCALE_MIN) {
            throw new IllegalArgumentException(
                "scale(" + scale + ") <= " + SCALE_MIN);
        }

        if (scale > SCALE_MAX) {
            throw new IllegalArgumentException(
                "scale(" + scale + ") > " + SCALE_MAX);
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

        if (range < RANGE_MIN) {
            throw new IllegalArgumentException(
                "range(" + range + ") < " + RANGE_MIN);
        }

        if (range > RANGE_MAX) {
            throw new IllegalArgumentException(
                "range(" + range + ") > " + RANGE_MAX);
        }

        return range;
    }


}

