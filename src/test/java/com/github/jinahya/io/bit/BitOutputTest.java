/*
 * Copyright 2013 <a href="mailto:onacit@gmail.com">Jin Kwon</a>.
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
import java.util.concurrent.ThreadLocalRandom;
import org.testng.annotations.Test;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public class BitOutputTest {


    public static BitOutput<Void> mockedInstance(final long limit) {

        return new BitOutput<>(new MockedByteWriter(limit));
    }


    static ThreadLocalRandom random() {

        return ThreadLocalRandom.current();
    }


    @Test(invocationCount = 128)
    public void writeBoolean() throws IOException {

        final BitOutput<?> output = mockedInstance(-1L);

        output.writeBoolean(BitIoTests.valueBoolean());
    }


    @Test(invocationCount = 128)
    public void writeUnsignedInt() throws IOException {

        final BitOutput<?> output = mockedInstance(-1L);

        final int length = BitIoTests.lengthIntUnsigned();
        final int value = BitIoTests.valueIntUnsigned(length);

        output.writeUnsignedInt(length, value);
    }


    @Test(invocationCount = 128)
    public void readInt() throws IOException {

        final BitOutput<?> output = mockedInstance(-1L);

        final int length = BitIoTests.lengthInt();
        final int value = BitIoTests.valueInt(length);

        output.writeInt(length, value);
    }


    @Test(invocationCount = 128)
    public void readUnsignedLong() throws IOException {

        final BitOutput<?> output = mockedInstance(-1L);

        final int length = BitIoTests.lengthLongUnsigned();
        final long value = BitIoTests.valueLongUnsigned(length);

        output.writeUnsignedLong(length, value);
    }


    @Test(invocationCount = 128)
    public void readLong() throws IOException {

        final BitOutput<?> output = mockedInstance(-1L);

        final int length = BitIoTests.lengthLong();
        final long value = BitIoTests.valueLong(length);

        output.writeLong(length, value);
    }


    @Test(invocationCount = 128)
    public void readBytes() throws IOException {

        final BitOutput<?> output = mockedInstance(-1L);

        final int scale = BitIoTests.scaleBytes();
        final int range = BitIoTests.rangeBytes();
        final byte[] value = BitIoTests.valueBytes(scale, range);

        output.writeBytes(scale, range, value);
    }


}

