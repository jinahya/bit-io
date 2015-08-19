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


import static com.github.jinahya.bit.io.BitIoRandoms.lengthInt;
import static com.github.jinahya.bit.io.BitIoRandoms.lengthInt32;
import static com.github.jinahya.bit.io.BitIoRandoms.lengthIntUnsigned;
import static com.github.jinahya.bit.io.BitIoRandoms.lengthLong;
import static com.github.jinahya.bit.io.BitIoRandoms.lengthLong64;
import static com.github.jinahya.bit.io.BitIoRandoms.lengthLongUnsigned;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
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


    private static ThreadLocalRandom random() {

        return ThreadLocalRandom.current();
    }


    @Test(invocationCount = 128)
    public void readBoolean() throws IOException {

        input.readBoolean();
    }


    @Test(invocationCount = 128)
    public void readUnsignedInt() throws IOException {

        input.readUnsignedInt(lengthIntUnsigned());
    }


    @Test(invocationCount = 128)
    public void readInt() throws IOException {

        input.readInt(lengthInt());
    }


    @Test(invocationCount = 128)
    public void readInt32() throws IOException {

        input.readInt(lengthInt32());
    }


//    @Test(invocationCount = 128)
//    public void readFloat32() throws IOException {
//
//        input.readFloat32();
//    }
    @Test(invocationCount = 128)
    public void readUnsingedLong() throws IOException {

        input.readUnsignedLong(lengthLongUnsigned());
    }


    @Test(invocationCount = 128)
    public void readLong() throws IOException {

        input.readLong(lengthLong());
    }


    @Test(invocationCount = 128)
    public void readLong64() throws IOException {

        input.readLong(lengthLong64());
    }


//    @Test(invocationCount = 128)
//    public void readDouble() throws IOException {
//
//        input.readDouble64();
//    }
    @Test(invocationCount = 128)
    public void readBytesFully() {

    }


    /**
     * logger.
     */
    private transient final Logger logger = getLogger(BitInputTest.class);


    @Inject
    private BitInput input;


}

