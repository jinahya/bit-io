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


import com.github.jinahya.bit.io.octet.ArrayInput;
import com.github.jinahya.bit.io.octet.ArrayOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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

        final byte[] array = new byte[8];
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


    @Test(enabled = true, invocationCount = 1024)
    public void _boolean() throws IOException {

        test(BitIoType.BOOLEAN);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _byte() throws IOException {

        test(BitIoType.BYTE);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _short() throws IOException {

        test(BitIoType.SHORT);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _int() throws IOException {

        test(BitIoType.INT);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _long() throws IOException {

        test(BitIoType.LONG);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _char() throws IOException {

        test(BitIoType.CHAR);
    }


    private transient final Logger logger = getLogger(getClass());

}

