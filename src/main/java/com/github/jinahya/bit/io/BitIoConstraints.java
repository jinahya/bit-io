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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class for constraints.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class BitIoConstraints {

    private static final int MIN_EXPONENT = 3;

    private static final int MAX_EXPONENT = 6;

    static {
        assert MAX_EXPONENT >= MIN_EXPONENT;
        assert MIN_EXPONENT > 0;
    }

    private static final List<Integer> MAX_SIGNED_SIZES;

    public static int requireValidExponent(final int exponent) {
        if (exponent < MIN_EXPONENT) {
            throw new IllegalArgumentException(
                    "exponent(" + exponent + ") < " + MIN_EXPONENT);
        }
        if (exponent > MAX_EXPONENT) {
            throw new IllegalArgumentException(
                    "exponent(" + exponent + ") > " + MAX_EXPONENT);
        }
        return exponent;
    }

    static {
        final List<Integer> maxSignedSizes
                = new ArrayList<Integer>(MAX_EXPONENT - MIN_EXPONENT + 1);
        for (int i = MIN_EXPONENT; i <= MAX_EXPONENT; i++) {
            maxSignedSizes.add((int) Math.pow(2, i));
        }
        MAX_SIGNED_SIZES = Collections.unmodifiableList(maxSignedSizes);
    }

    public static int requireValidSize(final boolean unsigned,
                                       final int exponent, final int size) {
        requireValidExponent(exponent);
        //final int minSize = 1 + (unsigned ? 0 : 1);
        final int minSize = 1;
        if (size < minSize) {
            throw new IllegalArgumentException(
                    "size(" + size + ") < " + minSize + ";unsigned=" + unsigned
                    + ";exponent=" + exponent);
        }
        final int index = exponent - MIN_EXPONENT;
        final int maxSize = MAX_SIGNED_SIZES.get(index) - (unsigned ? 1 : 0);
        if (size > maxSize) {
            throw new IllegalArgumentException(
                    "size(" + size + ") > " + maxSize + ";unsigned=" + unsigned
                    + ";exponent=" + exponent);
        }
        return size;
    }

    public static int requireValidSizeByte(final boolean unsigned,
                                           final int size) {
        return requireValidSize(unsigned, 3, size);
    }

    public static int requireValidSizeShort(final boolean unsigned,
                                            final int size) {
        return requireValidSize(unsigned, 4, size);
    }

    public static int requireValidSizeInt(final boolean unsigned,
                                          final int size) {
        return requireValidSize(unsigned, 5, size);
    }

    public static int requireValidSizeLong(final boolean unsigned,
                                           final int size) {
        return requireValidSize(unsigned, 6, size);
    }

    static int requireValidSizeUnsigned8(final int size) {
        if (size < 1) {
            throw new IllegalArgumentException(
                    "unsigned8.size(" + size + ") < 1");
        }
        if (size > 8) {
            throw new IllegalArgumentException(
                    "unsigned8.size(" + size + ") > 8");
        }
        return size;
    }

    static int requireValidSizeUnsigned16(final int size) {
        if (size < 1) {
            throw new IllegalArgumentException(
                    "unsigned16.size(" + size + ") < 1");
        }
        if (size > 16) {
            throw new IllegalArgumentException(
                    "unsigned16.size(" + size + ") > 16");
        }
        return size;
    }

    public static int requireValidSizeChar(final int size) {
        return requireValidSizeUnsigned16(size);
    }

    public BitIoConstraints() {
        super();
    }
}
