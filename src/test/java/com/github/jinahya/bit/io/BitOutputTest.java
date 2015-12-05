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


import static com.github.jinahya.bit.io.BitIoRandoms.randomBooleanValue;
import static com.github.jinahya.bit.io.BitIoRandoms.randomIntSize;
import static com.github.jinahya.bit.io.BitIoRandoms.randomUnsignedIntSize;
import static com.github.jinahya.bit.io.BitIoRandoms.randomUnsignedIntValue;
import static com.github.jinahya.bit.io.BitIoRandoms.randomUnsignedLongSize;
import static com.github.jinahya.bit.io.BitIoRandoms.unsignedLongValue;
import java.io.IOException;
import javax.inject.Inject;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
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

        final boolean value = randomBooleanValue();

        output.writeBoolean(value);
    }


    @Test(invocationCount = 128)
    public void writeUnsignedInt() throws IOException {

        final int size = randomUnsignedIntSize();
        final int value = randomUnsignedIntValue(size);

        output.writeUnsignedInt(size, value);
    }


    @Test(invocationCount = 128)
    public void writeInt() throws IOException {

        final int size = randomIntSize();
        final int value = BitIoRandoms.randomIntValue(size);

        output.writeInt(size, value);
    }


    @Test(invocationCount = 128)
    public void writeUnsignedLong() throws IOException {

        final int size = randomUnsignedLongSize();
        final long value = unsignedLongValue(size);

        output.writeUnsignedLong(size, value);
    }


    @Test(invocationCount = 128)
    public void writeLong() throws IOException {

        final int size = BitIoRandoms.randomLongSize();
        final long value = BitIoRandoms.randomLongValue(size);

        output.writeLong(size, value);
    }


    /**
     * logger.
     */
    private transient final Logger logger = getLogger(BitOutputTest.class);


    @Inject
    private transient BitOutput output;

}

