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


    static int assertLengthIntUnsigned(final int length) {

        assert length > 0;
        assert length < 32;

        return length;
    }


    static int newLengthIntUnsigned() {

        final int length = ThreadLocalRandom.current().nextInt(1, 32);

        return assertLengthIntUnsigned(length);
    }


    static int assertValueIntUnsigned(final int length, final int value) {

        assertLengthIntUnsigned(length);

        assert (value >> length) == 0;

        return value;
    }


    static int newValueIntUnsigned(final int length) {

        assertLengthIntUnsigned(length);

        final Random random = ThreadLocalRandom.current();

        final int value = random.nextInt() >>> (32 - length);

        return assertValueIntUnsigned(length, value);
    }


    static int assertLengthInt(final int length) {

        assert length > 1;
        assert length <= 32;

        return length;
    }


    static int newLengthInt() {

        final int length = ThreadLocalRandom.current().nextInt(2, 33);

        return assertLengthInt(length);
    }


    static int assertValueInt(final int length, final int value) {

        assertLengthInt(length);

        if (length < 32) {
            if (value < 0L) {
                Assert.assertTrue((value >> length) == ~0);
            } else {
                Assert.assertTrue((value >> length) == 0);
            }
        }

        return value;
    }


    static int newValueInt(final int length) {

        assertLengthInt(length);

        final Random random = ThreadLocalRandom.current();

        final int value = random.nextInt() >> (32 - length); // length == 32 ?

        return assertValueInt(length, value);
    }


    static int assertLengthLongUnsigned(final int length) {

        assert length >= 1;
        assert length < 64;

        return length;
    }


    static int newLengthLongUnsigned() {

        final int length = ThreadLocalRandom.current().nextInt(1, 64);

        return assertLengthLongUnsigned(length);
    }


    static long assertValueLongUnsigned(final int length, final long value) {

        assertLengthLongUnsigned(length);

        Assert.assertTrue((value >> length) == 0L);

        return value;
    }


    static long newValueLongUnsigned(final int length) {

        assertLengthLongUnsigned(length);

        final long value =
            ThreadLocalRandom.current().nextLong() >>> (64 - length);

        return assertValueLongUnsigned(length, value);
    }


    static int assertLengthLong(final int length) {

        assert length > 1;
        assert length <= 64;

        return length;
    }


    static int newLengthLong() {

        final int length = ThreadLocalRandom.current().nextInt(2, 65);

        return assertLengthLong(length);
    }


    static long assertValueLong(final int length, final long value) {

        assertLengthLong(length);

        if (length < 64) {
            if (value < 0L) {
                Assert.assertTrue((value >> length) == ~0L);
            } else {
                Assert.assertTrue((value >> length) == 0L);
            }
        }

        return value;
    }


    static long newValueLong(final int length) {

        assertLengthLong(length);

        final long value =
            ThreadLocalRandom.current().nextLong() >> (64 - length);

        return assertValueLong(length, value);
    }


    static int assertByteArrayScale(final int scale) {

        assert scale > 0 : "scale(" + scale + ") <= 0";
        assert scale <= 16 : "scale(" + scale + ") > 16";

        return scale;
    }


    static int newByteArrayScale() {

        final int scale = ThreadLocalRandom.current().nextInt(1, 17);

        return assertByteArrayScale(scale);
    }


    static int assertByteArrayRange(final int range) {

        assert range > 0 : "range(" + range + ") <= 0";
        assert range <= 8 : " range(" + range + ") > 8";

        return range;
    }


    static int newByteArrayRange() {

        final int range = ThreadLocalRandom.current().nextInt(1, 9);

        return assertByteArrayRange(range);
    }


    static byte[] newValueBytes(final int scale, final int range) {

        assertByteArrayScale(scale);
        assertByteArrayRange(range);

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

