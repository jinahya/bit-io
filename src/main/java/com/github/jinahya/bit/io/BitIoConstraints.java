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


    static int requireValidUnsigned8Size(final int size) {

        if (size < 1) {
            throw new IllegalArgumentException("size(" + size + ") < 1");
        }

        if (size > 8) {
            throw new IllegalArgumentException("size(" + size + ") > 8");
        }

        return size;
    }


    static int requireValidUnsigned16Size(final int size) {

        if (size < 1) {
            throw new IllegalArgumentException("size(" + size + ") < 1");
        }

        if (size > 16) {
            throw new IllegalArgumentException("size(" + size + ") > 16");
        }

        return size;
    }


    private static final int MIN_EXPONENT = 3;

    private static final int MAX_EXPONENT = 6;

    private static final List<Integer> MAXES;


    static {
        final List<Integer> maxes = new ArrayList<Integer>(4);
        for (int i = MIN_EXPONENT; i <= MAX_EXPONENT; i++) {
            maxes.add((int) Math.pow(2, i));
        }
        MAXES = Collections.unmodifiableList(maxes);
    }


    public static int requireValidSize(final boolean unsigned,
                                       final int exponent, final int size) {

        if (exponent < MIN_EXPONENT) {
            throw new IllegalArgumentException(
                "exponent(" + exponent + ") < " + MIN_EXPONENT);
        }

        if (exponent > MAX_EXPONENT) {
            throw new IllegalArgumentException(
                "exponent(" + exponent + ") > " + MAX_EXPONENT);
        }

        final int min = unsigned ? 1 : 2;
        final int max = MAXES.get(exponent - MIN_EXPONENT) - (unsigned ? 1 : 0);

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


    public static int requireValidByteSize(final boolean unsigned,
                                           final int size) {

        return requireValidSize(unsigned, 3, size);
    }


    public static int requireValidShortSize(final boolean unsigned,
                                            final int size) {

        return requireValidSize(unsigned, 4, size);
    }


    public static int requireValidIntSize(final boolean unsigned,
                                          final int size) {

        return requireValidSize(unsigned, 5, size);
    }


    public static int requireValidLongSize(final boolean unsigned,
                                           final int size) {

        return requireValidSize(unsigned, 6, size);
    }


    public static int requireValidCharSize(final int size) {

        return requireValidUnsigned16Size(size);
    }


    public BitIoConstraints() {

        super();
    }


}

