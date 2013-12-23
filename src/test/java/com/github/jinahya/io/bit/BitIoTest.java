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
        final int count = random.nextInt(1024);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteOutput<OutputStream> byteOutput = new StreamOutput(target);
        final BitOutput<OutputStream> bitOutput = new BitOutput<>(byteOutput);

        final List<Boolean> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final boolean value = random.nextBoolean();
            expected.add(value);
            bitOutput.writeBoolean(value);
        }
        bitOutput.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final ByteInput<InputStream> byteInput = new StreamInput(source);
        final BitInput<InputStream> bitInput = new BitInput<>(byteInput);

        final List<Boolean> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            actual.add(bitInput.readBoolean());
        }
        bitInput.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void int_unsigned_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(1024);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthIntUnsigned();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteOutput<OutputStream> byteOutput = new StreamOutput(target);
        final BitOutput<OutputStream> bitOutput = new BitOutput<>(byteOutput);

        final List<Integer> expected = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final int value = BitIoTests.valueIntUnsigned(length);
            expected.add(value);
            bitOutput.writeUnsignedInt(length, value);
        }
        bitOutput.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final ByteInput<InputStream> byteInput = new StreamInput(source);
        final BitInput<InputStream> bitInput = new BitInput<>(byteInput);

        final List<Integer> actual = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final int value = bitInput.readUnsignedInt(length);
            actual.add(value);
        }
        bitInput.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void int_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(1024);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthInt();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteOutput<OutputStream> byteOutput = new StreamOutput(target);
        final BitOutput<OutputStream> bitOutput = new BitOutput<>(byteOutput);

        final List<Integer> expected = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final int value = BitIoTests.valueInt(length);
            expected.add(value);
            bitOutput.writeInt(length, value);
        }
        bitOutput.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final ByteInput<InputStream> byteInput = new StreamInput(source);
        final BitInput<InputStream> bitInput = new BitInput<>(byteInput);

        final List<Integer> actual = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final int value = bitInput.readInt(length);
            actual.add(value);
        }
        bitInput.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void long_unsigned_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(1024);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthLongUnsigned();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteOutput<OutputStream> byteOutput = new StreamOutput(target);
        final BitOutput<OutputStream> bitOutput = new BitOutput<>(byteOutput);

        final List<Long> expected = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final long value = BitIoTests.valueLongUnsigned(length);
            expected.add(value);
            bitOutput.writeUnsignedLong(length, value);
        }
        bitOutput.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final ByteInput<InputStream> byteInput = new StreamInput(source);
        final BitInput<InputStream> bitInput = new BitInput<>(byteInput);

        final List<Long> actual = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final long value = bitInput.readUnsignedLong(length);
            actual.add(value);
        }
        bitInput.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void long_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(1024);

        final List<Integer> lengths = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int length = BitIoTests.lengthInt();
            lengths.add(length);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteOutput<OutputStream> byteOutput = new StreamOutput(target);
        final BitOutput<OutputStream> bitOutput = new BitOutput<>(byteOutput);

        final List<Long> expected = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final long value = BitIoTests.valueLong(length);
            expected.add(value);
            bitOutput.writeLong(length, value);
        }
        bitOutput.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final ByteInput<InputStream> byteInput = new StreamInput(source);
        final BitInput<InputStream> bitInput = new BitInput<>(byteInput);

        final List<Long> actual = new ArrayList<>(lengths.size());
        for (int i = 0; i < lengths.size(); i++) {
            final int length = lengths.get(i);
            final long value = bitInput.readLong(length);
            actual.add(value);
        }
        bitInput.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void bytes_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(1024);

        final List<Integer> scales = new ArrayList<>();
        final List<Integer> ranges = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int scale = BitIoTests.scaleBytes();
            scales.add(scale);
            final int range = BitIoTests.rangeBytes();
            ranges.add(range);
        }

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteOutput<OutputStream> byteOutput = new StreamOutput(target);
        final BitOutput<OutputStream> bitOutput = new BitOutput<>(byteOutput);

        final List<byte[]> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int scale = scales.get(i);
            final int range = ranges.get(i);
            final byte[] value = BitIoTests.valueBytes(scale, range);
            expected.add(value);
            bitOutput.writeBytes(scale, range, value);
        }
        bitOutput.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final ByteInput<InputStream> byteInput = new StreamInput(source);
        final BitInput<InputStream> bitInput = new BitInput<>(byteInput);

        final List<byte[]> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final int scale = scales.get(i);
            final int range = ranges.get(i);
            actual.add(bitInput.readBytes(scale, range));
        }
        bitInput.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void string_utf8_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(1024);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteOutput<OutputStream> byteOutput = new StreamOutput(target);
        final BitOutput<OutputStream> bitOutput = new BitOutput<>(byteOutput);

        final List<String> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = BitIoTests.valueStringUtf8();
            expected.add(value);
            bitOutput.writeString(value, "UTF-8");
        }
        bitOutput.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final ByteInput<InputStream> byteInput = new StreamInput(source);
        final BitInput<InputStream> bitInput = new BitInput<>(byteInput);

        final List<String> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = bitInput.readString("UTF-8");
            actual.add(value);
        }
        bitInput.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test(invocationCount = 1)
    public static void string_usAscii_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int count = random.nextInt(1024);

        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ByteOutput<OutputStream> byteOutput = new StreamOutput(target);
        final BitOutput<OutputStream> bitOutput = new BitOutput<>(byteOutput);

        final List<String> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = BitIoTests.valueStringUsAscii();
            expected.add(value);
            bitOutput.writeUsAsciiString(value);
        }
        bitOutput.align(1);

        final ByteArrayInputStream source
            = new ByteArrayInputStream(target.toByteArray());
        final ByteInput<InputStream> byteInput = new StreamInput(source);
        final BitInput<InputStream> bitInput = new BitInput<>(byteInput);

        final List<String> actual = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final String value = bitInput.readUsAsciiString();
            actual.add(value);
        }
        bitInput.align(1);

        Assert.assertEquals(actual, expected);
    }


    @Test
    public static void random_() throws IOException {

        final ThreadLocalRandom random = ThreadLocalRandom.current();

        final int count = random.nextInt(1024);

        final RandomLengthPack ls[] = new RandomLengthPack[count];
        for (int i = 0; i < ls.length; i++) {
            ls[i] = new RandomLengthPack();
        }

        final RandomValuePack evs[] = new RandomValuePack[ls.length];
        for (int i = 0; i < evs.length; i++) {
            evs[0] = new RandomValuePack(ls[i]);
        }

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final ByteOutput<OutputStream> byt = new StreamOutput(baos)) {
            try (final BitOutput<OutputStream> bit = new BitOutput<>(byt)) {
                for (int i = 0; i < evs.length; i++) {
                    evs[i] = new RandomValuePack(ls[i]);
                    evs[i].write(bit);
                }
                bit.align((short) 1);
            }
        }

        final RandomValuePack[] avs = new RandomValuePack[ls.length];

        final ByteArrayInputStream bais
            = new ByteArrayInputStream(baos.toByteArray());
        try (final ByteInput<InputStream> byt = new StreamInput(bais)) {
            try (final BitInput<InputStream> bit = new BitInput<>(byt)) {
                for (int i = 0; i < avs.length; i++) {
                    avs[i] = new RandomValuePack(ls[i]);
                    avs[i].read(bit);
                }
                bit.align((short) 1);
            }
        }

        Assert.assertEquals(avs, evs);
    }


}

