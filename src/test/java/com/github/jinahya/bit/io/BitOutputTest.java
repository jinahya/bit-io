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


package com.github.jinahya.bit.io;


import java.io.IOException;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = {BlackBitOutputModule.class})
public class BitOutputTest {


    @Test(invocationCount = 128)
    public void writeBoolean() throws IOException {

        final boolean value = BitIoRandoms.randomBooleanValue();

        output.writeBoolean(value);
    }


    @Test(invocationCount = 128)
    public void writeUnsignedInt() throws IOException {

        final int size = BitIoRandoms.randomUnsignedIntSize();
        final int value = BitIoRandoms.randomUnsignedIntValue(size);

        output.writeUnsignedInt(size, value);
    }


    @Test(invocationCount = 128)
    public void writeInt() throws IOException {

        final int size = BitIoRandoms.randomIntSize();
        final int value = BitIoRandoms.randomIntValue(size);

        output.writeInt(size, value);
    }


    @Test(invocationCount = 128)
    public void writeUnsignedLong() throws IOException {

        final int size = BitIoRandoms.randomUnsignedLongSize();
        final long value = BitIoRandoms.unsignedLongValue(size);

        output.writeUnsignedLong(size, value);
    }


    @Test(invocationCount = 128)
    public void writeLong() throws IOException {

        final int size = BitIoRandoms.randomLongSize();
        final long value = BitIoRandoms.randomLongValue(size);

        output.writeLong(size, value);
    }


//    @Test
//    public void writeObject() throws IOException {
//
//        output.writeObject(
//            Person.newRandomInstance(),
//            (o, v) -> {
//                o.writeUnsignedInt(7, v.getAge());
//                o.writeBoolean(v.isMarried());
//            }
//        );
//    }
    /**
     * logger.
     */
    private transient final Logger logger
        = LoggerFactory.getLogger(BitOutputTest.class
        );


    @Inject
    private transient BitOutput output;

}

