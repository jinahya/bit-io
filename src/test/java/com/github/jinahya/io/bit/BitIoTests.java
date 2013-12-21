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


package com.github.jinahya.io.bit;


import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
final class BitIoTests {


    static ThreadLocalRandom random() {

        return ThreadLocalRandom.current();
    }


    static boolean valueBoolean() {

        return random().nextBoolean();
    }


    static int assertLengthIntUnsigned(final int length) {

        assert length > 0;
        assert length < 32;

        return length;
    }


    static int lengthIntUnsigned() {

        final int length = random().nextInt(1, 32);

        return assertLengthIntUnsigned(length);
    }


    static int assertValueIntUnsigned(final int value, final int length) {

        assertLengthIntUnsigned(length);

        assert (value >> length) == 0;

        return value;
    }


    static int valueIntUnsigned(final int length) {

        assertLengthIntUnsigned(length);

        final int value = random().nextInt() >>> (32 - length);

        return assertValueIntUnsigned(value, length);
    }


    static int valueIntUnsigned(final Collection<Integer> lengths) {

        final int length = lengthIntUnsigned();
        lengths.add(length);

        return valueIntUnsigned(length);
    }


    static int assertLengthInt(final int length) {

        assert length > 1;
        assert length <= 32;

        return length;
    }


    static int lengthInt() {

        final int length = random().nextInt(2, 33);

        return assertLengthInt(length);
    }


    static int assertValueInt(final int value, final int length) {

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


    static int valueInt(final Collection<Integer> lengths) {

        final int length = lengthInt();
        lengths.add(length);

        return valueInt(length);
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


    static long assertValueLong(final long value, final int length) {

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


    static long valueLong(final Collection<Integer> lengths) {

        final int length = lengthLong();
        lengths.add(length);

        return valueLong(lengths);
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


    static void random(final List<Class<?>> types, final List<Boolean> signes,
                       final List<Integer> lengths, final List<Object> values) {

        Objects.requireNonNull(types, "null types");

        Objects.requireNonNull(signes, "null signes");

        Objects.requireNonNull(lengths, "null lengths");

        Objects.requireNonNull(values, "null values");

        final ThreadLocalRandom random = ThreadLocalRandom.current();

        final int count = random.nextInt(128, 1024);
        for (int i = 0; i < count; i++) {

            switch (random.nextInt(10)) {
                case 0: // boolean
                    types.add(Boolean.TYPE);
                    signes.add(null);
                    lengths.add(null);
                    values.add(random.nextBoolean());
                    break;
                case 1: // unsigned int
                    types.add(Integer.TYPE);
                    signes.add(Boolean.FALSE);
                     {
                        final int length = BitIoTests.lengthIntUnsigned();
                        lengths.add(length);
                        final int value = BitIoTests.valueIntUnsigned(length);
                        values.add(value);
                    }
                    break;
                case 2: // int
                    types.add(Integer.TYPE);
                    signes.add(Boolean.TRUE);
                     {
                        final int length = BitIoTests.lengthInt();
                        lengths.add(length);
                        final int value = BitIoTests.valueInt(length);
                        values.add(value);
                    }
                    break;
                case 3: // unsigned long
                    types.add(Long.TYPE);
                    signes.add(Boolean.FALSE);
                    values.add(BitIoTests.valueLongUnsigned(lengths));
                    break;
                case 4: // long
                    types.add(Long.TYPE);
                    signes.add(Boolean.TRUE);
                    values.add(BitIoTests.valueLong(lengths));
            }
        }
    }


    private BitIoTests() {
        super();
    }


}

