/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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
import java.util.LinkedList;
import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitIoTest {


    private static void test(final BitIoType type) throws IOException {

        final byte[] array = new byte[1048576];
        final List<Object> params = new LinkedList<>();

        final BitOutput output = new DefaultBitOutput<>(
            new ArrayOutput(array, array.length, 0));
        final Object expected = type.write(params, output);
        final long padded = output.align(1);

        final BitInput input = new DefaultBitInput<>(
            new ArrayInput(array, array.length, 0));
        final Object actual = type.read(params, input);
        final long discarded = input.align(1);

        assertEquals(discarded, padded);
        assertEquals(actual, expected, "type: " + type);
    }


//    @Test(invocationCount = 1024)
//    public void fbytes() throws IOException {
//
//        test(BitIoType.FBYTES);
//    }
//    @Test(invocationCount = 1024)
//    public void vbytes() throws IOException {
//
//        test(BitIoType.VBYTES);
//    }
    @Test(enabled = false, invocationCount = 1024)
    public void test() throws IOException {

        final BitIoType[] types = BitIoType.values();
        final BitIoType type = types[current().nextInt(types.length)];

        final byte[] array = new byte[1048576];
        final List<Object> params = new LinkedList<>();

        final BitOutput output = new DefaultBitOutput(
            new ArrayOutput(array, array.length, 0));
        final Object expected = type.write(params, output);
        final long padded = output.align(1);

        final BitInput input = new DefaultBitInput(
            new ArrayInput(array, array.length, 0));
        final Object actual = type.read(params, input);
        assertEquals(actual, expected, "type: " + type);
        final long discarded = input.align(1);

        assertEquals(discarded, padded);
    }


    @Test(enabled = false)
    public void test1() throws IOException {

        final BitIoType[] types = BitIoType.values();

        final byte[] array = new byte[1048576];

        final int count = current().nextInt(512, 1024);
        final List<Object> params = new LinkedList<>(); // type, param+, value

        final BitOutput output = new DefaultBitOutput(
            new ArrayOutput(array, array.length, 0));
        for (int i = 0; i < count; i++) {
            final BitIoType type = types[current().nextInt(types.length)];
            params.add(type);
            final Object value = type.write(params, output);
            params.add(value);
        }
        final long padded = output.align(1);

        final BitInput input = new DefaultBitInput(
            new ArrayInput(array, array.length, 0));
        for (int i = 0; i < count; i++) {
            final BitIoType type = (BitIoType) params.remove(0);
            final Object actual = type.read(params, input);
            final Object expected = params.remove(0);
            assertEquals(actual, expected, "type: " + type);
        }
        final long discarded = input.align(1);

        assertEquals(discarded, padded);
    }


    private transient final Logger logger = getLogger(getClass());

}

