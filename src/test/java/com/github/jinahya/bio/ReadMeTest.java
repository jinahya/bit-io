/*
 * Copyright 2014 Jin Kwon.
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


package com.github.jinahya.bio;


import java.io.IOException;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon
 */
public class ReadMeTest {


    /**
     * logger.
     */
    private static final Logger logger
            = LoggerFactory.getLogger(ReadMeTest.class);


    @Test
    public void read1_() throws IOException {

        final ByteBuffer source = ByteBuffer.allocate(7);

        final BitInput input = new BitInput(() -> source.get() & 0xFF);

        input.readBoolean();
        input.readUnsignedInt(6);
        input.readLong(47);

        final int discarded = input.align(1);
        Assert.assertEquals(discarded, 2);

        Assert.assertEquals(source.remaining(), 0);
    }


    @Test
    public void write1_() throws IOException {

        final ByteBuffer target = ByteBuffer.allocate(8);

        final BitOutput output = new BitOutput((a) -> target.put((byte) a));

        output.writeBoolean(true);
        output.writeInt(7, -1);
        output.writeUnsignedLong(33, 1L);

        final int padded = output.align(4);
        Assert.assertEquals(padded, 23);

        Assert.assertEquals(target.remaining(), 0);
    }


}

