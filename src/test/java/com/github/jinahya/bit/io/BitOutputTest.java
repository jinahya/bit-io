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


import com.github.jinahya.bit.io.BitOutput;
import static com.github.jinahya.bit.io.BitIoRandoms.lengthInt;
import static com.github.jinahya.bit.io.BitIoRandoms.lengthIntUnsigned;
import static com.github.jinahya.bit.io.BitIoRandoms.lengthLong;
import static com.github.jinahya.bit.io.BitIoRandoms.lengthLongUnsigned;
import static com.github.jinahya.bit.io.BitIoRandoms.valueFloat32;
import static com.github.jinahya.bit.io.BitIoRandoms.valueFloat32Raw;
import static com.github.jinahya.bit.io.BitIoRandoms.valueInt;
import static com.github.jinahya.bit.io.BitIoRandoms.valueIntUnsigned;
import static com.github.jinahya.bit.io.BitIoRandoms.valueLong;
import static com.github.jinahya.bit.io.BitIoRandoms.valueLongUnsigned;
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

        output.writeBoolean(BitIoRandoms.valueBoolean());
    }


    @Test(invocationCount = 128)
    public void writeUnsignedInt() throws IOException {

        output.writeUnsignedInt(lengthIntUnsigned(), valueIntUnsigned());
    }


    @Test(invocationCount = 128)
    public void writeInt() throws IOException {

        output.writeInt(lengthInt(), valueInt());
    }


//    @Test(invocationCount = 128)
//    public void writeFloat32() throws IOException {
//
//        output.writeFloat32(valueFloat32());
//    }
//    @Test(invocationCount = 128)
//    public void writeFloat32Raw() throws IOException {
//
//        output.writeFloat32(valueFloat32Raw());
//    }
    @Test(invocationCount = 128)
    public void writeUnsignedLong() throws IOException {

        output.writeUnsignedLong(lengthLongUnsigned(), valueLongUnsigned());
    }


    @Test(invocationCount = 128)
    public void writeLong() throws IOException {

        output.writeLong(lengthLong(), valueLong());
    }


//    @Test(invocationCount = 128)
//    public void writeDouble64() throws IOException {
//
//        output.writeDouble64(BitIoTests.valueDouble());
//    }
//    @Test(invocationCount = 128)
//    public void writeDouble64Raw() throws IOException {
//
//        output.writeDouble64Raw(BitIoTests.valueDoubleRaw());
//    }
    /**
     * logger.
     */
    private transient final Logger logger = getLogger(BitOutputTest.class);


    @Inject
    private BitOutput output;


}

