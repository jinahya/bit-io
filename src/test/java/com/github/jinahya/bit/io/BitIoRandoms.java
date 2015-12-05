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
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import org.apache.commons.lang3.RandomStringUtils;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoRandoms {


    static boolean randomBooleanValue() {

        return current().nextBoolean();
    }


    static int randomUnsignedIntSize() {

        final int size = current().nextInt(
            BitIoConstants.UINT_SIZE_MIN, BitIoConstants.UINT_SIZE_MAX + 1);

        return BitIoConstraints.requireValidUnsignedIntSize(size);
    }


    static int randomUnsignedIntValue(final int size) {

        BitIoConstraints.requireValidUnsignedIntSize(size);

        final int value = current().nextInt() >>> (Integer.SIZE - size);

        return BitIoConstraints.requireValidUnsignedIntValue(value, size);
    }


    static int randomIntSize() {

        final int size = current().nextInt(
            BitIoConstants.INT_SIZE_MIN, BitIoConstants.INT_SIZE_MAX + 1);

        return BitIoConstraints.requireValidIntSize(size);
    }


    static int randomIntValue(final int size) {

        BitIoConstraints.requireValidIntSize(size);

        final int value = current().nextInt() >> (Integer.SIZE - size);

        return BitIoConstraints.requireValidIntValue(value, size);
    }


    static int randomUnsignedLongSize() {

        final int size = current().nextInt(
            BitIoConstants.ULONG_SIZE_MIN, BitIoConstants.ULONG_SIZE_MAX + 1);

        return BitIoConstraints.requireValidUnsignedLongSize(size);
    }


    static long unsignedLongValue(final int size) {

        BitIoConstraints.requireValidUnsignedLongSize(size);

        final long value = current().nextLong() >>> (Long.SIZE - size);

        return BitIoConstraints.requireValidUnsignedLongValue(value, size);
    }


    static int randomLongSize() {

        final int size = current().nextInt(
            BitIoConstants.LONG_SIZE_MIN, BitIoConstants.LONG_SIZE_MAX + 1);

        return BitIoConstraints.requireValidLongSize(size);
    }


    static long randomLongValue(final int size) {

        BitIoConstraints.requireValidLongSize(size);

        final long value = current().nextLong() >> (Long.SIZE - size);

        return BitIoConstraints.requireValidLongValue(value, size);
    }


    static int randomLengthSize(Integer min, Integer max,
                                final IntConsumer consumer) {

        if (min == null) {
            min = BitIoConstants.LENGTH_SIZE_MIN;
        }

        if (max == null) {
            max = BitIoConstants.LENGTH_SIZE_MAX;
        }

        final int size = current().nextInt(min, max + 1);
        if (consumer != null) {
            consumer.accept(size);
        }

        return size;
    }


    static int randomLengthValue(IntSupplier sizeSupplier,
                                 final IntConsumer valueConsumer) {

        if (sizeSupplier == null) {
            sizeSupplier = () -> randomLengthSize(null, null, null);
        }

        final int size = sizeSupplier.getAsInt();
        BitIoConstraints.requireValidLengthSize(size);

        final int value = current().nextInt() >>> (Integer.SIZE - size);
        if (valueConsumer != null) {
            valueConsumer.accept(value);
        }

        return BitIoConstraints.requireValidLengthValue(value, size);
    }


    static int rangeBytes() {

        final int range = current().nextInt(
            BitIoConstants.UBYTE_SIZE_MIN, BitIoConstants.UBYTE_SIZE_MAX + 1);

        return BitIoConstraints.requireValidBytesRange(range);
    }


    static byte[] valueBytes(final int scale, final int range) {

        BitIoConstraints.requireValidLengthSize(scale);
        BitIoConstraints.requireValidBytesRange(range);

        final byte[] value
            = new byte[current().nextInt() >>> (Integer.SIZE - scale)];
        current().nextBytes(value);

        for (int i = 0; i < value.length; i++) {
            value[i] = (byte) ((value[i] & 0xFF) >> (Byte.SIZE - range));
        }

        return value;
    }


    static String valueStringUtf8() {

        String string;

        do {
            final int count = current().nextInt(32768); // = 65536 / 2
            string = RandomStringUtils.random(count);
        } while (string.getBytes(StandardCharsets.UTF_8).length >= 65536);

        return string;
    }


    static String valueStringUsAscii() {

        final int count = current().nextInt(65536);

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

