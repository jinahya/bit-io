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


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class BitInputTest {


    @Test
    public static void newInstance_() {

        BitInput.newInstance((ByteBuffer) null);
        BitInput.newInstance((InputStream) null);
        BitInput.newInstance(null, null);
    }


    public static BitInput<Void> mockedInstance(final long limit) {

        return new BitInput<>(new MockedByteInput(limit));
    }


    @Test
    public void readUnsignedByte() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        try {
            input.readUnsignedByte(0);
            Assert.fail("passed: readUnsignedByte(0)");
        } catch (final IllegalArgumentException iae) {
            // expected
        }

        for (int length = 1; length <= 8; length++) {
            input.readUnsignedByte(length);
        }

        try {
            input.readUnsignedByte(9);
            Assert.fail("passed: readUnsignedByte(9)");
        } catch (final IllegalArgumentException iae) {
            // expected
        }
    }


    @Test
    public void readUnsignedShort() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        try {
            input.readUnsignedShort(0);
            Assert.fail("passed: readUnsignedShort(0)");
        } catch (final IllegalArgumentException iae) {
            // expected
        }

        for (int length = 1; length <= 16; length++) {
            input.readUnsignedShort(length);
        }

        try {
            input.readUnsignedShort(17);
            Assert.fail("passed: readUnsignedShort(17)");
        } catch (final IllegalArgumentException iae) {
            // expected
        }
    }


    @Test(expectedExceptions = {NullPointerException.class})
    public void createWithNullByteInput() {

        final BitInput<?> input = new BitInput<>(null);
    }


    @Test(invocationCount = 128)
    public void readBoolean() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        final boolean value = input.readBoolean();
    }


    @Test(invocationCount = 128)
    public void readUnsignedInt() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        final int length = BitIoTests.lengthIntUnsigned();

        final int value = input.readUnsignedInt(length);
    }


    @Test(invocationCount = 128)
    public void readInt() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        final int length = BitIoTests.lengthInt();

        final int value = input.readInt(length);
    }


    @Test(invocationCount = 128)
    public void readFloat() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        final float value = input.readFloat();
    }


    @Test(invocationCount = 128)
    public void readUnsignedLong() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        final int length = BitIoTests.lengthLongUnsigned();

        final long value = input.readUnsignedLong(length);
    }


    @Test(invocationCount = 128)
    public void readLong() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        final int length = BitIoTests.lengthLong();

        final long value = input.readLong(length);
    }


    @Test(invocationCount = 128)
    public void readDouble() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        final double value = input.readDouble();
    }


    @Test(invocationCount = 128)
    public void readBytes() throws IOException {

        final BitInput<?> input = mockedInstance(-1L);

        final int scale = BitIoTests.scaleBytes();
        final int range = BitIoTests.rangeBytes();

        final byte[] value = input.readBytes(scale, range);
    }


    @Test
    public void getCount_() {

        final BitInput<?> input = mockedInstance(-1L);

        Assert.assertEquals(input.getCount(), 0L);
    }


}

