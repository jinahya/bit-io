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

import static com.github.jinahya.bit.io.BitIoConstants.MIN_SIZE;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_BYTE;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_CHAR;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_INTEGER;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_LONG;
import static com.github.jinahya.bit.io.BitIoConstants.SIZE_EXPONENT_SHORT;
import static java.lang.Math.pow;

/**
 * Constraints for bit-io.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoConstraints {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Checks whether given size is valid for unsigned 8 bit integer. An {@code IllegalArgumentException} will be thrown
     * if given value is not valid.
     *
     * @param size the size to check; must between {@code 1} and {@value java.lang.Byte#SIZE}, both inclusive.
     * @return given size.
     */
    static int requireValidSizeUnsigned8(final int size) {
        if (size < 1) {
            throw new IllegalArgumentException("unsigned8.size(" + size + ") < 1");
        }
        if (size > Byte.SIZE) {
            throw new IllegalArgumentException("unsigned8.size(" + size + ") > " + Byte.SIZE);
        }
        return size;
    }

    /**
     * Checks whether given size is valid for unsigned 16 bit integer. An {@code IllegalArgumentException} will be
     * thrown if given value is not valid.
     *
     * @param size the size to check; must between {@code 1} and {@value java.lang.Short#SIZE}, both inclusive.
     * @return given size.
     */
    static int requireValidSizeUnsigned16(final int size) {
        if (size < 1) {
            throw new IllegalArgumentException("unsigned16.size(" + size + ") < 1");
        }
        if (size > Short.SIZE) {
            throw new IllegalArgumentException("unsigned16.size(" + size + ") > " + Short.SIZE);
        }
        return size;
    }

    // -------------------------------------------------------------------------------------------------------- exponent
    static final int MIN_EXPONENT = SIZE_EXPONENT_BYTE;

    static final int MAX_EXPONENT = SIZE_EXPONENT_LONG;

    /**
     * Validates given exponent.
     *
     * @param exponent the exponent to validate.
     * @return given exponent.
     */
    static int requireValidExponent(final int exponent) {
        if (exponent < MIN_EXPONENT) {
            throw new IllegalArgumentException("exponent(" + exponent + ") < " + MIN_EXPONENT);
        }
        if (exponent > MAX_EXPONENT) {
            throw new IllegalArgumentException("exponent(" + exponent + ") > " + MAX_EXPONENT);
        }
        return exponent;
    }

    // ------------------------------------------------------------------------------------------------------------ size
    private static final int[] MAX_SIZE = new int[MAX_EXPONENT - MIN_EXPONENT + 1];

    static {
        MAX_SIZE[0] = (int) pow(2, MIN_EXPONENT);
        for (int i = 1; i < MAX_SIZE.length; i++) {
            MAX_SIZE[i] = MAX_SIZE[i - 1] << 2;
        }
    }

    /**
     * Returns the maximum size for given arguments.
     *
     * @param unsigned the value for unsigned.
     * @param exponent the value for exponent.
     * @return the maximum size.
     */
    static int maxSize(final boolean unsigned, final int exponent) {
        return MAX_SIZE[requireValidExponent(exponent) - MIN_EXPONENT] - (unsigned ? 1 : 0);
    }

    static int requireValidSize(final boolean unsigned, final int exponent, final int size) {
        if (size < MIN_SIZE) {
            throw new IllegalArgumentException("size(" + size + ") < " + MIN_SIZE);
        }
        final int max = maxSize(unsigned, exponent);
        if (size > max) {
            throw new IllegalArgumentException("size(" + size + ") > " + max + "; unsigned=" + unsigned
                                               + "; exponent=" + exponent);
        }
        return size;
    }

    static int requireValidSizeByte(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, SIZE_EXPONENT_BYTE, size);
    }

    static int requireValidSizeShort(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, SIZE_EXPONENT_SHORT, size);
    }

    static int requireValidSizeInt(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, SIZE_EXPONENT_INTEGER, size);
    }

    static int requireValidSizeLong(final boolean unsigned, final int size) {
        return requireValidSize(unsigned, SIZE_EXPONENT_LONG, size);
    }

    static int requireValidSizeChar(final int size) {
        return requireValidSize(true, SIZE_EXPONENT_CHAR, size);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private BitIoConstraints() {
        super();
    }
}
