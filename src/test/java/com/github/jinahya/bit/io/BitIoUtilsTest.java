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

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static com.github.jinahya.bit.io.BitIoTests.randomSizeForInt;
import static com.github.jinahya.bit.io.BitIoTests.randomSizeForLong;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForInt;
import static com.github.jinahya.bit.io.BitIoTests.randomValueForLong;
import static com.github.jinahya.bit.io.BitIoUtils.reverseInt;
import static com.github.jinahya.bit.io.BitIoUtils.reverseLong;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A class for unit-testing {@link BitIoUtils} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class BitIoUtilsTest {

    // ------------------------------------------------------------------------------------------------------ reverseInt

    /**
     * Tests {@link BitIoUtils#reverseInt(int, int)} for {@value java.lang.Integer#MIN_VALUE}.
     */
    @Test
    void testReverseIntWithMinValue() {
        final int value = Integer.MIN_VALUE;
        final int expected = 1;
        final int actual = reverseInt(Integer.SIZE, value);
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitIoUtils#reverseInt(int, int)} for {@code 0}.
     */
    @Test
    void testReverseIntWithZero() {
        final int value = 0;
        final int expected = 0;
        final int actual = reverseInt(Integer.SIZE, value);
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitIoUtils#reverseInt(int, int)} for {@code -1}.
     */
    @Test
    void testReverseIntWithNegativeOne() {
        final int value = -1;
        final int expected = -1;
        final int actual = reverseInt(Integer.SIZE, value);
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitIoUtils#reverseInt(int, int)} for {@code 1}.
     */
    @Test
    void testReverseIntWithPositiveOne() {
        final int value = 1;
        final int expected = Integer.MIN_VALUE;
        final int actual = reverseInt(Integer.SIZE, value);
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitIoUtils#reverseInt(int, int)} for {@value java.lang.Integer#MAX_VALUE}.
     */
    @Test
    void testReverseIntWithMaxValue() {
        final int value = Integer.MAX_VALUE;
        final int expected = -2;
        final int actual = reverseInt(Integer.SIZE, value);
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitIoUtils#reverseInt(int, int)} for random values.
     */
    @RepeatedTest(128)
    void testReverseInt() {
        final boolean unsigned = false;
        final int size = randomSizeForInt(unsigned);
        final int value = randomValueForInt(unsigned, size);
        final int reversed = reverseInt(size, value);
        if (size < Integer.SIZE) {
            assertTrue(reversed >= 0L);
        }
        {
            final int shifted = reversed << (Integer.SIZE - size);
            if ((value & 0x01) == 1) {
                assertTrue(shifted < 0);
            } else {
                assertTrue(shifted >= 0);
            }
        }
        if (value >= 0) {
            final int actual = reverseInt(size, reversed);
            assertEquals(value, actual);
        }
    }

    /**
     * Tests {@link BitIoUtils#reverseInt(int)} for random values.
     */
    @RepeatedTest(128)
    void testReverseInt32() {
        final int value = current().nextInt();
        final int reversed = reverseInt(value);
        final int actual = reverseInt(reversed);
        assertEquals(value, actual);
    }

    // ----------------------------------------------------------------------------------------------------- reverseLong
    @Test
    void testReverseLongWithMinValue() {
        final long value = Long.MIN_VALUE;
        final long expected = 1L;
        final long actual = reverseLong(Long.SIZE, value);
        assertEquals(expected, actual);
    }

    @Test
    void testReverseLongWithNegativeOne() {
        final long value = -1L;
        final long expected = -1L;
        final long actual = reverseLong(Long.SIZE, value);
        assertEquals(expected, actual);
    }

    @Test
    void testReverseLongWithZero() {
        final long value = 0L;
        final long expected = 0L;
        final long actual = reverseLong(Long.SIZE, value);
        assertEquals(expected, actual);
    }

    @Test
    void testReverseLongWithPositiveOne() {
        final long value = 1L;
        final long expected = Long.MIN_VALUE;
        final long actual = reverseLong(Long.SIZE, value);
        assertEquals(expected, actual);
    }

    @Test
    void testReverseLongWithMaxValue() {
        final long value = Long.MAX_VALUE;
        final long expected = -2;
        final long actual = reverseLong(Long.SIZE, value);
        assertEquals(expected, actual);
    }

    @RepeatedTest(128)
    void testReverseLong() {
        final boolean unsigned = false;
        final int size = randomSizeForLong(unsigned);
        final long value = randomValueForLong(unsigned, size);
        final long reversed = reverseLong(size, value);
        if (size < Long.SIZE) {
            assertTrue(reversed >= 0L);
        }
        {
            final long shifted = reversed << (Long.SIZE - size);
            if ((value & 0x01L) == 1L) {
                assertTrue(shifted < 0L);
            } else {
                assertTrue(shifted >= 0L);
            }
        }
        if (value >= 0L) {
            final long actual = reverseLong(size, reversed);
            assertEquals(value, actual);
        }
    }

    @RepeatedTest(128)
    void testReverseInt64() {
        final long value = current().nextLong();
        final long reversed = reverseLong(value);
        final long actual = reverseLong(reversed);
        assertEquals(value, actual);
    }
}
