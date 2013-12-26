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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class BitIoTest {


    @Test(invocationCount = 1)
    public static void boolean_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<Boolean> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final boolean value = random.nextBoolean();
            expected.add(value);
            output.writeBoolean(value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<Boolean> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            actual.add(input.readBoolean());
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void boolean_nullable() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<Boolean> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final Boolean value
                = random.nextBoolean() ? null : random.nextBoolean();
            expected.add(value);
            if (output.isNotNull(value)) {
                output.writeBoolean(value);
            }
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<Boolean> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            if (input.isNull()) {
                actual.add(null);
            } else {
                actual.add(input.readBoolean());
            }
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void int_unsigned_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthIntUnsigned();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<Integer> expected = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final int value = BitIoTests.valueIntUnsigned(length);
            expected.add(value);
            output.writeUnsignedInt(length, value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<Integer> actual = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final int value = input.readUnsignedInt(length);
            actual.add(value);
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void int_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthInt();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<Integer> expected = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final int value = BitIoTests.valueInt(length);
            expected.add(value);
            output.writeInt(length, value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<Integer> actual = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final int value = input.readInt(length);
            actual.add(value);
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void float_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<Float> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final float value = BitIoTests.valueFloat();
            expected.add(value);
            output.writeFloat(value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<Float> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final float value = input.readFloat();
            actual.add(value);
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    private static void float_(final float expected) throws IOException {

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        output.writeFloat(expected);
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final float actual = input.readFloat();
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void float_constants() throws IOException {

        float_(Float.MAX_EXPONENT);
        float_(Float.MAX_VALUE);
        float_(Float.MIN_EXPONENT);
        float_(Float.MIN_NORMAL);
        float_(Float.MIN_VALUE);
        float_(Float.NaN);
        float_(Float.NEGATIVE_INFINITY);
        float_(Float.POSITIVE_INFINITY);
    }


    @Test(invocationCount = 1)
    public static void long_unsigned_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthLongUnsigned();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<Long> expected = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final long value = BitIoTests.valueLongUnsigned(length);
            expected.add(value);
            output.writeUnsignedLong(length, value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<Long> actual = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final long value = input.readUnsignedLong(length);
            actual.add(value);
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void long_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthInt();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<Long> expected = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final long value = BitIoTests.valueLong(length);
            expected.add(value);
            output.writeLong(length, value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<Long> actual = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final long value = input.readLong(length);
            actual.add(value);
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void double_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<Double> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final double value = BitIoTests.valueDouble();
            expected.add(value);
            output.writeDouble(value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<Double> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final double value = input.readDouble();
            actual.add(value);
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    private static void double_(final double expected) throws IOException {

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        output.writeDouble(expected);
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final double actual = input.readDouble();
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void double_constants() throws IOException {

        double_(Double.MAX_EXPONENT);
        double_(Double.MAX_VALUE);
        double_(Double.MIN_EXPONENT);
        double_(Double.MIN_NORMAL);
        double_(Double.MIN_VALUE);
        double_(Double.NaN);
        double_(Double.NEGATIVE_INFINITY);
        double_(Double.POSITIVE_INFINITY);
    }


    @Test(invocationCount = 1)
    public static void bytes_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final List<Integer> scales = new ArrayList<>();
        final List<Integer> ranges = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int scale = BitIoTests.scaleBytes();
            scales.add(scale);
            final int range = BitIoTests.rangeBytes();
            ranges.add(range);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<byte[]> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int scale = scales.get(i);
            final int range = ranges.get(i);
            final byte[] value = BitIoTests.valueBytes(scale, range);
            expected.add(value);
            output.writeBytes(scale, range, value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<byte[]> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int scale = scales.get(i);
            final int range = ranges.get(i);
            actual.add(input.readBytes(scale, range));
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void string_utf8_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<String> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = BitIoTests.valueStringUtf8();
            expected.add(value);
            output.writeString(value, "UTF-8");
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<String> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = input.readString("UTF-8");
            actual.add(value);
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void string_usAscii_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(32);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final BitOutput<?> output = new BitOutput<>(new StreamOutput(target));

        final List<String> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = BitIoTests.valueStringUsAscii();
            expected.add(value);
            output.writeUsAsciiString(value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final BitInput<?> input = new BitInput<>(new StreamInput(source));

        final List<String> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = input.readUsAsciiString();
            actual.add(value);
        }
        input.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test
    public static void random_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();

        final int count = random.nextInt(32);

        final RandomLengthPack lengths[] = new RandomLengthPack[count];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = new RandomLengthPack();
        }

        final RandomValuePack expected[] = new RandomValuePack[lengths.length];
        for (int i = 0; i < expected.length; i++) {
            expected[0] = new RandomValuePack(lengths[i]);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (final BitOutput<?> output
            = new BitOutput<>(new StreamOutput(target))) {
            for (int i = 0; i < expected.length; i++) {
                expected[i] = new RandomValuePack(lengths[i]);
                expected[i].write(output);
            }
            output.align((short) 1);
        }

        final byte[] bytes = target.toByteArray();

        final RandomValuePack[] actual = new RandomValuePack[lengths.length];

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        try (final BitInput<?> input
            = new BitInput<>(new StreamInput(source))) {
            for (int i = 0; i < actual.length; i++) {
                actual[i] = new RandomValuePack(lengths[i]);
                actual[i].read(input);
            }
            input.align((short) 1);
        }

        Assert.assertEquals(actual, expected);
    }


}

