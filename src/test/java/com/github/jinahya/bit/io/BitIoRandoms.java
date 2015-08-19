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


import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoRandoms {


    static ThreadLocalRandom random() {

        return ThreadLocalRandom.current();
    }


    static boolean valueBoolean() {

        return random().nextBoolean();
    }


    private static int assertLengthIntUnsigned(final int length) {

        assert length > 0;
        assert length < 32;

        return length;
    }


    static int lengthIntUnsigned() {

        final int length = random().nextInt(1, 32);

        return assertLengthIntUnsigned(length);
    }


    private static int assertValueIntUnsigned(final int value,
                                              final int length) {

        assertLengthIntUnsigned(length);

        assert (value >> length) == 0;

        return value;
    }


    static int valueIntUnsigned(final int length) {

        assertLengthIntUnsigned(length);

        final int value = random().nextInt() >>> (32 - length);

        return assertValueIntUnsigned(value, length);
    }


    static int valueIntUnsigned() {

        return random().nextInt();
    }


    private static int assertLengthInt(final int length) {

        assert length > 1;
        assert length <= 32;

        return length;
    }


    static int lengthInt() {

        final int length = random().nextInt(2, 33);

        return assertLengthInt(length);
    }


    static int lengthInt32() {

        return assertLengthInt(Integer.BYTES);
    }


    private static int assertValueInt(final int value, final int length) {

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


    static int valueInt(final int length) {

        assertLengthInt(length);

        final int value = random().nextInt() >> (32 - length); // length == 32 ?

        return assertValueInt(value, length);
    }


    static int valueInt() {

        return random().nextInt();
    }


    static float valueFloat32() {

        float value = random().nextFloat() * Float.MAX_VALUE;
        if (random().nextBoolean()) {
            value = Float.MAX_VALUE;
        }
        if (random().nextBoolean()) {
            value = 0 - value;
        }

        return value;
        //return (float) random().nextLong() / random().nextInt();
    }


    static float valueFloat32Raw() {

        return valueFloat32();
        //return (float) random().nextLong() / random().nextInt();
    }


    private static int assertLengthLongUnsigned(final int length) {

        assert length >= 1;
        assert length < 64;

        return length;
    }


    static int lengthLongUnsigned() {

        final int length = random().nextInt(1, 64);

        return assertLengthLongUnsigned(length);
    }


    private static long assertValueLongUnsigned(final long value,
                                                final int length) {

        assertLengthLongUnsigned(length);

        Assert.assertTrue((value >> length) == 0L);

        return value;
    }


    static long valueLongUnsigned(final int length) {

        assertLengthLongUnsigned(length);

        final long value = random().nextLong() >>> (64 - length);

        return assertValueLongUnsigned(value, length);
    }


    static long valueLongUnsigned() {

        return random().nextLong();
    }


    static long valueLongUnsigned(final Collection<Integer> lengths) {

        final int length = lengthLongUnsigned();
        lengths.add(length);

        return valueLongUnsigned(length);
    }


    static int assertLengthLong(final int length) {

        assert length > 1;
        assert length <= 64;

        return length;
    }


    static int lengthLong() {

        final int length = random().nextInt(2, 65);

        return assertLengthLong(length);
    }


    static int lengthLong64() {

        return assertLengthLong(Long.SIZE);
    }


    private static long assertValueLong(final long value, final int length) {

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


    static long valueLong(final int length) {

        assertLengthLong(length);

        final long value = random().nextLong() >> (64 - length);

        return assertValueLong(value, length);
    }


    static long valueLong() {

        return random().nextLong();
    }


    static double valueDouble() {

        double value = random().nextDouble() * Double.MAX_VALUE;
        if (random().nextBoolean()) {
            value = Double.MAX_VALUE;
        }
        if (random().nextBoolean()) {
            value = 0 - value;
        }

        return value;
        //return (double) random().nextLong() / (double) random().nextInt();
    }


    static double valueDoubleRaw() {

        return valueDouble();
    }


    private static int assertScaleBytes(final int scale) {

        assert scale > 0 : "scale(" + scale + ") <= 0";
        assert scale <= 16 : "scale(" + scale + ") > 16";

        return scale;
    }


    static int scaleBytes() {

        final int scale = random().nextInt(1, 17);

        return assertScaleBytes(scale);
    }


    private static int assertRangeBytes(final int range) {

        assert range > 0 : "range(" + range + ") <= 0";
        assert range <= 8 : " range(" + range + ") > 8";

        return range;
    }


    static int rangeBytes() {

        final int range = random().nextInt(1, 9);

        return assertRangeBytes(range);
    }


    static byte[] valueBytes(final int scale, final int range) {

        assertScaleBytes(scale);
        assertRangeBytes(range);

        final byte[] bytes = new byte[random().nextInt() >>> (32 - scale)];
        random().nextBytes(bytes);

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ((bytes[i] & 0xFF) >> (8 - range));
        }

        return bytes;
    }


    static String valueStringUtf8() {

        String string;

        do {
            final int count = random().nextInt(32768); // = 65536 / 2
            string = RandomStringUtils.random(count);
        } while (string.getBytes(StandardCharsets.UTF_8).length >= 65536);

        return string;
    }


    static String valueStringUsAscii() {

        final int count = random().nextInt(65536);

        return RandomStringUtils.randomAscii(count);
    }


    static String toBinaryString(final byte[] bytes, final int word) {

        final StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            final int k = builder.length();
            for (int i = 0; i < Byte.SIZE; i++) {
                builder.insert(k, b & 0x01);
                b >>= 1;
            }
        }

        if (word > 0) {
            for (int i = builder.length() - 1; i > 0; i--) {
                if (i % word == 0) {
                    builder.insert(i, " ");
                }
            }
        }

        return builder.toString();
    }


    private BitIoRandoms() {
        super();
    }


}

