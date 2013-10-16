/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
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


import com.github.jinahya.io.BitInput.StreamInput;
import com.github.jinahya.io.BitOutput.StreamOutput;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
public class BitIoTest {


    @Test(invocationCount = 128)
    public void testBytes() throws IOException {

        final int scale = RandomLengths.newScaleBytes();
        final int range = RandomLengths.newRangeBytes();
        final byte[] expected = BitIoTests.newValueBytes(scale, range);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new BitOutput(new StreamOutput(baos));

        output.writeBytes(scale, range, expected);
        output.align((short) 1);

        final ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = new BitInput(new StreamInput(bais));

        final byte[] actual = input.readBytes(scale, range);

        Assert.assertEquals(actual, expected);
    }


    @Test(enabled = true, invocationCount = 128)
    public void testUsAsciiString() throws IOException {

        final String expected = BitIoTests.newValueUsAsciiString();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new BitOutput(new StreamOutput(baos));

        output.writeUsAsciiString(expected);
        output.align((short) 1);
        output.close();

        final ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = new BitInput(new StreamInput(bais));

        final String actual = input.readUsAsciiString();
        input.close();

        Assert.assertEquals(actual, expected);
    }


    @Test(enabled = true, invocationCount = 128)
    public void testUtf8String() throws IOException {

        final String expected = BitIoTests.newValueUtf8String();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new BitOutput(new StreamOutput(baos));

        output.writeString(expected, "UTF-8");
        output.align((short) 1);
        output.close();

        final ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = new BitInput(new StreamInput(bais));

        final String actual = input.readString("UTF-8");
        input.close();

        Assert.assertEquals(actual, expected);
    }


}

