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


import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
final class BitIoTests {


    static int assertValueIntUnsigned(final int length, final int value) {

        RandomLengths.assertLengthIntUnsigned(length);

        Assert.assertTrue((value >> length) == 0);

        return value;
    }


    static int newValueIntUnsigned(Integer length) {

        if (length == null) {
            length = RandomLengths.newLengthIntUnsigned();
        }

        final Random random = ThreadLocalRandom.current();

        final int value = random.nextInt() >>> (32 - length);

        return assertValueIntUnsigned(length, value);
    }


    static int assertValueInt(final int length, final int value) {

        RandomLengths.assertLengthInt(length);

        if (length != 32) {
            if (value < 0L) {
                Assert.assertTrue((value >> length) == ~0);
            } else {
                Assert.assertTrue((value >> length) == 0);
            }
        }

        return value;
    }


    static int newValueInt(Integer length) {

        if (length == null) {
            length = RandomLengths.newLengthInt();
        }

        final Random random = ThreadLocalRandom.current();

        final int value = random.nextInt() >> (32 - length); // length == 32 ?

        return assertValueInt(length, value);
    }


    static long assertValueLongUnsigned(final int length, final long value) {

        RandomLengths.assertLengthLongUnsigned(length);

        Assert.assertTrue((value >> length) == 0L);

        return value;
    }


    static long newValueLongUnsigned(Integer length) {

        if (length == null) {
            length = RandomLengths.newLengthLongUnsigned();
        }

        final Random random = ThreadLocalRandom.current();

        final long value = random.nextLong() >>> (64 - length);

        return assertValueLongUnsigned(length, value);
    }


    static long assertValueLong(final int length, final long value) {

        RandomLengths.assertLengthLong(length);

        if (length != 64) {
            if (value < 0L) {
                Assert.assertTrue((value >> length) == ~0L);
            } else {
                Assert.assertTrue((value >> length) == 0L);
            }
        }

        return value;
    }


    static long newValueLong(Integer length) {

        if (length == null) {
            length = RandomLengths.newLengthLong();
        }

        final Random random = ThreadLocalRandom.current();

        final long value = random.nextLong() >> (64 - length);

        return assertValueLong(length, value);
    }


    static byte[] newValueBytes(Integer scale, Integer range) {

        if (scale == null) {
            scale = RandomLengths.newScaleBytes();
        }

        RandomLengths.assertScaleBytes(scale);

        if (range == null) {
            range = RandomLengths.newRangeBytes();
        }

        RandomLengths.assertRangeBytes(range);

        final Random random = ThreadLocalRandom.current();

        final byte[] bytes = new byte[random.nextInt() >>> (32 - scale)];
        random.nextBytes(bytes);

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ((bytes[i] & 0xFF) >> (8 - range));
        }

        return bytes;
    }


    static String newValueUtf8String() {

        final Random random = ThreadLocalRandom.current();

        String string;

        do {
            final int count = random.nextInt(32768); // = 65536 / 2
            string = RandomStringUtils.random(count);
        } while (string.getBytes(StandardCharsets.UTF_8).length >= 65536);

        return string;
    }


    static String newValueUsAsciiString() {

        final Random random = ThreadLocalRandom.current();

        final int count = random.nextInt(65536);

        return RandomStringUtils.randomAscii(count);

    }


    private BitIoTests() {
        super();
    }


}

