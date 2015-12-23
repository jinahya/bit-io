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
@Guice(modules = {WhiteBitInputModule.class})
public class BitInputTest {


    @Test(invocationCount = 128)
    public void readBoolean() throws IOException {

        final boolean value = input.readBoolean();
    }


    @Test(invocationCount = 128)
    public void readUnsignedInt() throws IOException {

        final int size = BitIoRandoms.randomUnsignedIntSize();

        final int value = input.readUnsignedInt(size);
    }


    @Test(invocationCount = 128)
    public void readInt() throws IOException {

        final int size = BitIoRandoms.randomIntSize();

        final int value = input.readInt(size);
    }


    @Test(invocationCount = 128)
    public void readUnsingedLong() throws IOException {

        final int size = BitIoRandoms.randomUnsignedLongSize();

        final long value = input.readUnsignedLong(size);
    }


    @Test(invocationCount = 128)
    public void readLong() throws IOException {

        final int size = BitIoRandoms.randomLongSize();

        final long value = input.readLong(size);
    }


//    @Test(invocationCount = 128)
//    public void readFixedBytes() throws IOException {
//
//        final byte[] array = new byte[current().nextInt(1024)];
//        final int offset = array.length == 0
//                           ? 0 : current().nextInt(array.length);
//        final int length = array.length == 0
//                           ? 0 : current().nextInt(array.length - offset);
//        final int byteSize = BitIoRandoms.randomUnsignedByteSize();
//
//        input.readBytes(array, offset, length, byteSize);
//    }
//    @Test(invocationCount = 128)
//    public void readVariableBytes() throws IOException {
//
//        final byte[] array = new byte[current().nextInt(1024)];
//        final int offset = array.length == 0
//                           ? 0 : current().nextInt(array.length);
//        final int length = array.length == 0
//                           ? 0 : current().nextInt(array.length - offset);
//        final int byteSize = BitIoRandoms.randomUnsignedByteSize();
//
//        input.readBytes(array, offset, length, byteSize);
//    }
//    @Test
//    public void readObject() throws IOException {
//
//        final Person v = input.readObject(
//            i -> {
//                try {
//                    return new Person().age(i.readUnsignedInt(7));
//                } catch (final IOException ioe) {
//                    throw new UncheckedIOException(ioe);
//                }
//            });
//    }
    /**
     * logger.
     */
    private transient final Logger logger
        = LoggerFactory.getLogger(BitInputTest.class);


    @Inject
    private transient BitInput input;

}

