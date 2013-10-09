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


import com.googlecode.jinahya.io.BitInput.StreamInput;
import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class BitInputTest {


    @Test
    public void testReadBoolean() throws IOException {

        final BitInput input = new BitInput(new StreamInput(
            new ByteArrayInputStream(new byte[]{(byte) 0x80}))); // 1000 0000

        Assert.assertTrue(input.readBoolean());
        for (int i = 0; i < 7; i++) {
            Assert.assertFalse(input.readBoolean());
        }
    }


    @Test
    public void testAlign()
        throws IOException, NoSuchFieldException, IllegalAccessException {

        final BitInput input = new BitInput(new StreamInput(
            new FilterInputStream(null) {


            @Override
            public int read() throws IOException {
                return ThreadLocalRandom.current().nextInt(0, 256);
            }


        }));
        Assert.assertEquals(input.align(1), 0);

        input.readUnsignedByte(6);
        Assert.assertEquals(input.align(1), 2);

        input.readUnsignedByte(5);

        final Field field = BitInput.class.getDeclaredField("count");
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(input, -2);

        Assert.assertEquals(input.align(1), 3);

    }


}

