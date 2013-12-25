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
import java.nio.ByteBuffer;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public class BufferInputTest extends ByteInputTest<BufferInput, ByteBuffer> {


    @Test
    public void readUnsignedByte_() throws IOException {

        final ByteBuffer source = ByteBuffer.wrap(new byte[]{0x00});

        final ByteInput<ByteBuffer> input = new BufferInput(source);

        Assert.assertEquals(input.readUnsignedByte(), 0x00);
    }


    @Test
    public void readUnsignedByte_eof() throws IOException {

        final ByteBuffer source = ByteBuffer.wrap(new byte[0]);

        final ByteInput<ByteBuffer> input = new BufferInput(source);

        Assert.assertEquals(input.readUnsignedByte(), -1);
    }


    @Test
    public void close() throws IOException {

        new BufferInput(ByteBuffer.wrap(new byte[]{0x00})).close();

        new BufferInput(ByteBuffer.wrap(new byte[0])).close();

        new BufferInput(null).close();
    }


}

