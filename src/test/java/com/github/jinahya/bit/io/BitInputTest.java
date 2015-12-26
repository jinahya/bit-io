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
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Inject;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = {WhiteBitInputModule.class})
public class BitInputTest {


    @Test(invocationCount = 128)
    public void readBoolean() throws IOException {

        final boolean value = input.readBoolean();
    }


    @Test(invocationCount = 128)
    public void readByte() throws IOException {

        final boolean unsigned = current().nextBoolean();
        final int size = BitIoRandoms.size(unsigned, 3);

        final byte value = input.readByte(unsigned, size);
    }


    @Test(invocationCount = 128)
    public void readShort() throws IOException {

        final boolean unsigned = current().nextBoolean();
        final int size = BitIoRandoms.size(unsigned, 4);

        final short value = input.readShort(unsigned, size);
    }


    @Test(invocationCount = 128)
    public void readInt() throws IOException {

        final boolean unsigned = current().nextBoolean();
        final int size = BitIoRandoms.size(unsigned, 5);

        final int value = input.readInt(unsigned, size);
    }


    @Test(invocationCount = 128)
    public void readLong() throws IOException {

        final boolean unsigned = current().nextBoolean();
        final int size = BitIoRandoms.size(unsigned, 6);

        final long value = input.readLong(unsigned, size);
    }


    @Test(invocationCount = 128)
    public void readChar() throws IOException {

        final int size = BitIoRandoms.size(true, 4);

        final long value = input.readChar(size);
    }


    /**
     * logger.
     */
    private transient final Logger logger = getLogger(getClass());


    @Inject
    private transient BitInput input;

}

