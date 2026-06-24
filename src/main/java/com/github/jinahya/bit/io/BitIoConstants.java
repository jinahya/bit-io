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

/**
 * Constants for bit-io.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class BitIoConstants {

    /**
     * Precomputed bit masks indexed by {@code (size - 1)}, where {@code MASKS[size - 1]} has its lowest {@code size}
     * bits set.
     */
    private static final int[] MASKS = new int[8];

    static {
        int p = 2;
        for (int i = 0; i < MASKS.length; i++) {
            MASKS[i] = p - 1;
            p <<= 1;
        }
    }

    /**
     * Returns a bit mask whose lowest {@code size} bits are set.
     *
     * @param size the number of lowest bits to set; between {@code 1} and {@value java.lang.Byte#SIZE}, both
     *             inclusive.
     * @return a bit mask with its lowest {@code size} bits set.
     */
    static int mask(final int size) {
        return MASKS[size - 1];
    }

    /**
     * Creates a new instance.
     */
    private BitIoConstants() {
        throw new AssertionError("initialization is not allowed");
    }
}
