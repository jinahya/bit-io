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


package com.github.jinahya.bio;


/**
 * A base class.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public final class Bytes {


    /**
     * The minimum value for {@code scale} of bytes.
     */
    protected static final int BYTES_SCALE_MIN = 0x01;


    /**
     * The maximum value for {@code scale} of bytes.
     */
    protected static final int BYTES_SCALE_MAX = 0x10;


    /**
     * The minimum value for {@code range} of bytes.
     */
    protected static final int BYTES_RANGE_MIN = 0x01;


    /**
     * The maximum value for {@code range} of bytes.
     */
    protected static final int BYTES_RANGE_MAX = 0x08;


    /**
     * Checks that the specified value is valid.
     *
     * @param scale the value to check
     *
     * @return given value if it's valid.
     *
     * @throws IllegalArgumentException if given value is not valid.
     *
     * @see #BYTES_SCALE_MIN
     * @see #BYTES_SCALE_MAX
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
     * @see #BYTES_RANGE_MIN
     * @see #BYTES_RANGE_MAX
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

