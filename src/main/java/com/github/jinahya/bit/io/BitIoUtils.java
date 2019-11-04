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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

/**
 * A utility class for bit io.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class BitIoUtils {

    // -----------------------------------------------------------------------------------------------------------------
    public static int reverseInt(final int size, int value) {
        requireValidSizeInt(false, size);
        if (value == 0) {
            return 0;
        }
        if (value == -1) {
            return -1 >>> (Integer.SIZE - size);
        }
        if (value > 0) {
            final int numberOfLeadingZeros = Integer.numberOfLeadingZeros(value);
            final int shift = numberOfLeadingZeros - (Integer.SIZE - size);
            if (shift > 0) {
                return reverseInt(Integer.SIZE - numberOfLeadingZeros, value) << shift;
            }
        }
        int result = 0;
        for (int i = 0; i < size; i++) {
            result <<= 1;
            result |= value & 1;
            value >>= 1;
        }
        return result;
    }

    public static int reverseInt(final int value) {
        return reverseInt(Integer.SIZE, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static long reverseLong(int size, long value) {
        requireValidSizeLong(false, size);
        if (value == 0L) {
            return 0L;
        }
        if (value == -1L) {
            return -1L >>> (Long.SIZE - size);
        }
        if (value > 0) {
            final int numberOfLeadingZeros = Long.numberOfLeadingZeros(value);
            final int shift = numberOfLeadingZeros - (Long.SIZE - size);
            if (shift > 0) {
                return reverseLong(Long.SIZE - numberOfLeadingZeros, value) << shift;
            }
        }
        long result = 0L;
        if (size > Integer.SIZE) {
            result = (reverseInt(Integer.SIZE, (int) value) & 0xFFFFFFFFL) << (size - Integer.SIZE);
            size -= Integer.SIZE;
            value >>= Integer.SIZE;
        }
        result |= reverseInt(size, (int) value) & 0xFFFFFFFFL;
        return result;
    }

    public static long reverseLong(final long value) {
        return reverseLong(Long.SIZE, value);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private BitIoUtils() {
        super();
    }
}
