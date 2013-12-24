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
import java.io.InputStream;
import java.io.OutputStream;
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
        final int count = random.nextInt(128);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteWriter<OutputStream> writer = new StreamWriter(target);
        final BitOutput<OutputStream> output = new BitOutput<>(writer);

        final List<Boolean> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final boolean value = random.nextBoolean();
            expected.add(value);
            output.writeBoolean(value);
        }
        output.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final ByteReader<InputStream> reader = new StreamReader(source);
        final BitInput<InputStream> input = new BitInput<>(reader);

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
        final int count = random.nextInt(128);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteWriter<OutputStream> writer = new StreamWriter(target);
        final BitOutput<OutputStream> output = new BitOutput<>(writer);

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
        final ByteReader<InputStream> reader = new StreamReader(source);
        final BitInput<InputStream> input = new BitInput<>(reader);

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
        final int count = random.nextInt(128);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthIntUnsigned();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteWriter<OutputStream> writer = new StreamWriter(target);
        final BitOutput<OutputStream> output = new BitOutput<>(writer);

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
        final ByteReader<InputStream> reader = new StreamReader(source);
        final BitInput<InputStream> input = new BitInput<>(reader);

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
        final int count = random.nextInt(128);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthInt();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteWriter<OutputStream> writer = new StreamWriter(target);
        final BitOutput<OutputStream> output = new BitOutput<>(writer);

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
        final ByteReader<InputStream> reader = new StreamReader(source);
        final BitInput<InputStream> input = new BitInput<>(reader);

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
    public static void long_unsigned_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(128);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthLongUnsigned();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteWriter<OutputStream> writer = new StreamWriter(target);
        final BitOutput<OutputStream> output = new BitOutput<>(writer);

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
        final ByteReader<InputStream> reader = new StreamReader(source);
        final BitInput<InputStream> input = new BitInput<>(reader);

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
        final int count = random.nextInt(128);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthInt();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteWriter<OutputStream> writer = new StreamWriter(target);
        final BitOutput<OutputStream> output = new BitOutput<>(writer);

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
        final ByteReader<InputStream> reader = new StreamReader(source);
        final BitInput<InputStream> input = new BitInput<>(reader);

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
    public static void bytes_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(128);

        final List<Integer> scales = new ArrayList<>();
        final List<Integer> ranges = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int scale = BitIoTests.scaleBytes();
            scales.add(scale);
            final int range = BitIoTests.rangeBytes();
            ranges.add(range);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteWriter<OutputStream> writer = new StreamWriter(target);
        final BitOutput<OutputStream> output = new BitOutput<>(writer);

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
        final ByteReader<InputStream> reader = new StreamReader(source);
        final BitInput<InputStream> input = new BitInput<>(reader);

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
        final int count = random.nextInt(128);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteWriter<OutputStream> writer = new StreamWriter(target);
        final BitOutput<OutputStream> output = new BitOutput<>(writer);

        final List<String> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = BitIoTests.valueStringUtf8();
            expected.add(value);
            output.writeString(value, "UTF-8");
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final ByteReader<InputStream> reader = new StreamReader(source);
        final BitInput<InputStream> input = new BitInput<>(reader);

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
        final int count = random.nextInt(128);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteWriter<OutputStream> writer = new StreamWriter(target);
        final BitOutput<OutputStream> output = new BitOutput<>(writer);

        final List<String> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = BitIoTests.valueStringUsAscii();
            expected.add(value);
            output.writeUsAsciiString(value);
        }
        output.align(1);

        final byte[] bytes = target.toByteArray();

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        final ByteReader<InputStream> reader = new StreamReader(source);
        final BitInput<InputStream> input = new BitInput<>(reader);

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

        final int count = random.nextInt(128);

        final RandomLengthPack lengths[] = new RandomLengthPack[count];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = new RandomLengthPack();
        }

        final RandomValuePack expected[] = new RandomValuePack[lengths.length];
        for (int i = 0; i < expected.length; i++) {
            expected[0] = new RandomValuePack(lengths[i]);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (final ByteWriter<?> writer = new StreamWriter(target)) {
            try (final BitOutput<?> output = new BitOutput<>(writer)) {
                for (int i = 0; i < expected.length; i++) {
                    expected[i] = new RandomValuePack(lengths[i]);
                    expected[i].write(output);
                }
                output.align((short) 1);
            }
        }

        final byte[] bytes = target.toByteArray();

        final RandomValuePack[] actual = new RandomValuePack[lengths.length];

        final ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        try (final ByteReader<?> reader = new StreamReader(source)) {
            try (final BitInput<?> input = new BitInput<>(reader)) {
                for (int i = 0; i < actual.length; i++) {
                    actual[i] = new RandomValuePack(lengths[i]);
                    actual[i].read(input);
                }
                input.align((short) 1);
            }
        }

        Assert.assertEquals(actual, expected);
    }


}

