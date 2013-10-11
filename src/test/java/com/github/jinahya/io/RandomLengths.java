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


package com.github.jinahya.io;


import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.testng.Assert;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
final class RandomLengths {


    static int assertLengthIntUnsigned(final int length) {

        Assert.assertTrue(length >= 1);
        Assert.assertTrue(length < 32);

        return length;
    }


    static int newLengthIntUnsigned() {

        final Random random = ThreadLocalRandom.current();

        final int length = random.nextInt(31) + 1; // (0 ~ 30) + 1 = (1 ~ 31)

        return assertLengthIntUnsigned(length);
    }


    static int assertLengthInt(final int length) {

        Assert.assertTrue(length > 1);
        Assert.assertTrue(length <= 32);

        return length;
    }


    static int newLengthInt() {

        final Random random = ThreadLocalRandom.current();

        final int length = random.nextInt(31) + 2; // (0 ~ 30) + 2 = (2 ~ 32)

        return assertLengthInt(length);
    }


    static int assertLengthLongUnsigned(final int length) {

        Assert.assertTrue(length >= 1);
        Assert.assertTrue(length < 64);

        return length;
    }


    /**
     * Generates a valid bit length for unsigned long value.
     *
     * @return a bit length for unsigned long value
     */
    static int newLengthLongUnsigned() {

        final Random random = ThreadLocalRandom.current();

        final int length = random.nextInt(63) + 1; // (0 ~ 62) + 1 = (1 ~ 63)

        return assertLengthLongUnsigned(length);
    }


    static int assertLengthLong(final int length) {

        Assert.assertTrue(length > 1);
        Assert.assertTrue(length <= 64);

        return length;
    }


    static int newLengthLong() {

        final Random random = ThreadLocalRandom.current();

        final int length = random.nextInt(63) + 2; // (0 ~ 62) + 2 = (2 ~ 64)

        return assertLengthLong(length);
    }


    static int assertScaleBytes(final int sacle) {

        Assert.assertTrue(sacle > 0);
        Assert.assertTrue(sacle <= 16);

        return sacle;
    }


    static int newScaleBytes() {

        final Random random = ThreadLocalRandom.current();

        final int scale = random.nextInt(16) + 1; // (0 ~ 15) + 1 = (1 ~ 16)

        return assertScaleBytes(scale);
    }


    static int assertRangeBytes(final int range) {

        Assert.assertTrue(range > 0);
        Assert.assertTrue(range <= 8);

        return range;
    }


    static int newRangeBytes() {

        final Random random = ThreadLocalRandom.current();

        final int scale = random.nextInt(8) + 1; // (0 ~ 7) + 1 = (1 ~ 8)

        return assertRangeBytes(scale);
    }


    private RandomLengths() {
        super();
    }


}
