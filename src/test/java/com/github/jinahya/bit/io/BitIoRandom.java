/*
 * Copyright 2013 Jin Kwon <jinahya at gmail.com>.
 *
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
 */
package com.github.jinahya.bit.io;

import static com.github.jinahya.bit.io.AbstractBitBase.requireValidSize;
import static java.lang.Long.SIZE;
import static java.lang.Math.pow;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoRandom {

    /**
     * Generates a random size for given argument.
     *
     * @param unsigned the flag for unsigned
     * @param exponent the exponent
     * @return a random size.
     */
    public static int nextSize(final boolean unsigned, final int exponent) {
        final int origin = 1;
        final int bound = (int) pow(2, exponent) + (unsigned ? 0 : 1);
        final int size = current().nextInt(origin, bound);
        return requireValidSize(unsigned, exponent, size);
    }

    /**
     * Generates a random value for given arguments.
     *
     * @param unsigned the flag for unsigned
     * @param exponent the exponent
     * @param size the size
     * @return a random value.
     */
    public static long nextValue(final boolean unsigned, final int exponent,
                                 final int size) {
        requireValidSize(unsigned, exponent, size);
        final long value = current().nextLong();
        final int shift = SIZE - size;
        return unsigned ? (value >>> shift) : (value >> shift);
    }

    private BitIoRandom() {
        super();
    }
}
