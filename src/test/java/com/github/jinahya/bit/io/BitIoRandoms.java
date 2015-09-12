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


    static int sizeIntUnsigned() {

        final int size = random().nextInt(
            BitIoConstants.UINT_SIZE_MIN, BitIoConstants.UINT_SIZE_MAX + 1);

        return BitIoConstraints.requireValidUnsignedIntSize(size);
    }


    static int valueIntUnsigned(final int size) {

        BitIoConstraints.requireValidUnsignedIntSize(size);

        final int value = random().nextInt() >>> (Integer.SIZE - size);

        return value;
    }


    static int valueIntUnsigned() {

        return valueIntUnsigned(sizeIntUnsigned());
    }


    static int sizeInt() {

        final int size = random().nextInt(
            BitIoConstants.INT_SIZE_MIN, BitIoConstants.INT_SIZE_MAX + 1);

        return BitIoConstraints.requireValidIntSize(size);
    }


    static int valueInt(final int size) {

        BitIoConstraints.requireValidIntSize(size);

        final int value = random().nextInt() >> (Integer.SIZE - size);

        return value;
    }


    static int valueInt() {

        return valueInt(sizeInt());
    }


    static int sizeLongUnsigned() {

        final int size = random().nextInt(
            BitIoConstants.ULONG_SIZE_MIN, BitIoConstants.ULONG_SIZE_MAX + 1);

        return BitIoConstraints.requireValidUnsignedLongSize(size);
    }


    static long valueLongUnsigned(final int size) {

        BitIoConstraints.requireValidUnsignedLongSize(size);

        final long value = random().nextLong() >>> (Long.SIZE - size);

        return value;
    }


    static long valueLongUnsigned() {

        return valueLongUnsigned(sizeLongUnsigned());
    }


    static long valueLongUnsigned(final Collection<Integer> lengths) {

        final int length = sizeLongUnsigned();
        lengths.add(length);

        return valueLongUnsigned(length);
    }


    static int sizeLong() {

        final int size = random().nextInt(
            BitIoConstants.LONG_SIZE_MIN, BitIoConstants.LONG_SIZE_MAX + 1);

        return BitIoConstraints.requireValidLongSize(size);
    }


    static long valueLong(final int size) {

        BitIoConstraints.requireValidLongSize(size);

        final long value = random().nextLong() >> (Long.SIZE - size);

        return value;
    }


    static long valueLong() {

        return valueLong(sizeLong());
    }


    private static int assertScaleBytes(final int scale) {

        assert scale > 0 : "scale(" + scale + ") <= 0";
        assert scale <= 16 : "scale(" + scale + ") > 16";

        return scale;
    }


    static int scaleBytes() {

        final int scale = random().nextInt(
            BitIoConstants.SCALE_SIZE_MIN, BitIoConstants.SCALE_SIZE_MAX + 1);

        return BitIoConstraints.requireValidBytesScale(scale);
    }


    static int rangeBytes() {

        final int range = random().nextInt(
            BitIoConstants.RANGE_SIZE_MIN, BitIoConstants.RANGE_SIZE_MAX + 1);

        return BitIoConstraints.requireValidBytesRange(range);
    }


    static byte[] valueBytes(final int scale, final int range) {

        BitIoConstraints.requireValidBytesScale(scale);
        BitIoConstraints.requireValidBytesRange(range);

        final byte[] value
            = new byte[random().nextInt() >>> (Integer.SIZE - scale)];
        random().nextBytes(value);

        for (int i = 0; i < value.length; i++) {
            value[i] = (byte) ((value[i] & 0xFF) >> (Byte.SIZE - range));
        }

        return value;
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

