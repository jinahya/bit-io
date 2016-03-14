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

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class BitIoRandoms {

    public static int size(final boolean unsigned, final int exponent) {

        final int origin = 1;
        final int bound = (int) Math.pow(2, exponent) + (unsigned ? 0 : 1);

        final int size = current().nextInt(origin, bound);

        return BitIoConstraints.requireValidSize(unsigned, exponent, size);
    }

    public static long value(final boolean unsigned, final int exponent,
            final int size) {

        BitIoConstraints.requireValidSize(unsigned, exponent, size);

        final long value = current().nextLong();
        final int shift = Long.SIZE - size;

        return unsigned ? value >>> shift : value >> shift;
    }

    private BitIoRandoms() {

        super();
    }

}
