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

import static java.lang.Math.pow;

/**
 * A class for constraints.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoConstraints {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Checks whether given size is valid for unsigned 8 bit integer. An {@code IllegalArgumentException} will be thrown
     * if given value is not valid.
     *
     * @param size the size to check; must between {@code 1} and {@value Byte#SIZE}, both inclusive.
     * @return given size.
     */
    static int requireValidSizeUnsigned8(final int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size(" + size + ") < 1");
        }
        if (size > Byte.SIZE) {
            throw new IllegalArgumentException("size(" + size + ") > " + Byte.SIZE);
        }
        return size;
    }

    /**
     * Checks whether given size is valid for unsigned 16 bit integer. An {@code IllegalArgumentException} will be
     * thrown if given value is not valid.
     *
     * @param size the size to check; must between {@code 1} and {@value Short#SIZE}, both inclusive.
     * @return given size.
     */
    static int requireValidSizeUnsigned16(final int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size(" + size + ") < 1");
        }
        if (size > Short.SIZE) {
            throw new IllegalArgumentException("size(" + size + ") > " + Short.SIZE);
        }
        return size;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static final int MIN_EXPONENT = 3;

    static final int MAX_EXPONENT = 6;

    static final int MIN_SIZE = 1;

    private static final int[] MAX_SIZES = new int[MAX_EXPONENT - MIN_EXPONENT + 1];

    static {
        for (int i = 0; i < MAX_SIZES.length; i++) {
            MAX_SIZES[i] = (int) pow(2.0d, (double) i + MIN_EXPONENT);
        }
    }

    static int requireValidExponent(final int exponent) {
        if (exponent < MIN_EXPONENT) {
            throw new IllegalArgumentException("exponent(" + exponent + ") < " + MIN_EXPONENT);
        }
        if (exponent > MAX_EXPONENT) {
            throw new IllegalArgumentException("exponent(" + exponent + ") > " + MAX_EXPONENT);
        }
        return exponent;
    }

    static int maxSize(final boolean unsigned, final int exponent) {
        return MAX_SIZES[requireValidExponent(exponent) - MIN_EXPONENT] - (unsigned ? 1 : 0);
    }

    static int requireValidSize(final boolean unsigned, final int exponent, final int size) {
        requireValidExponent(exponent);
        if (size < MIN_SIZE) {
            throw new IllegalArgumentException("size(" + size + ") < " + MIN_SIZE);
        }
        final int maxSize = maxSize(unsigned, exponent);
        if (size > maxSize) {
            throw new IllegalArgumentException("size(" + size + ") > " + maxSize + "; unsigned=" + unsigned
                                               + "; exponent=" + exponent);
        }
        return size;
    }

    static int requireValidSizeByte(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, 3, size);
    }

    static int requireValidSizeShort(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, 4, size);
    }

    static int requireValidSizeInt(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, 5, size);
    }

    static int requireValidSizeLong(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, 6, size);
    }

    static int requireValidSizeChar(final int size) {
        return requireValidSizeUnsigned16(size);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoConstraints() {
        super();
    }
}
