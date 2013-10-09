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


package com.googlecode.jinahya.io;


import com.googlecode.jinahya.io.BitOutput.StreamOutput;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class BitOutputTest {


    @Test
    public void testWriteBoolean() throws IOException {

        final BitOutput output = new BitOutput(new StreamOutput(
            new ByteArrayOutputStream()));

        output.writeBoolean(ThreadLocalRandom.current().nextBoolean());
    }


    @Test
    public void testAlign()
        throws IOException, NoSuchFieldException, IllegalAccessException {

        final BitOutput output =
            new BitOutput(new StreamOutput(new FilterOutputStream(null) {


            @Override
            public void write(final int b) throws IOException {
                // does nothin'
            }


        }));


        Assert.assertEquals(output.align(1), 0);

        output.writeUnsignedByte(6, 0x00);
        Assert.assertEquals(output.align(1), 2);

        output.writeUnsignedByte(5, 0x00);
        final Field field = BitOutput.class.getDeclaredField("count");
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(output, -2);
        Assert.assertEquals(output.align(1), 3);

    }


}

