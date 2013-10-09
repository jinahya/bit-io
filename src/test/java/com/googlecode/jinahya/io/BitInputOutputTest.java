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


package com.googlecode.jinahya.io;


import com.googlecode.jinahya.io.BitInput.StreamInput;
import com.googlecode.jinahya.io.BitOutput.StreamOutput;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
public class BitInputOutputTest {


    @Test(invocationCount = 128)
    public void testBytes() throws IOException {

        final int scale = RandomLengths.newScaleBytes();
        final int range = RandomLengths.newRangeBytes();
        final byte[] expected = RandomValues.newValueBytes(scale, range);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BitOutput output = new BitOutput(new StreamOutput(baos));

        output.writeBytes(scale, range, expected);
        output.align(1);

        final ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        final BitInput input = new BitInput(new StreamInput(bais));

        final byte[] actual = input.readBytes(scale, range);

        Assert.assertEquals(actual, expected);
    }


}
