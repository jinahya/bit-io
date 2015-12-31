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
import java.io.UncheckedIOException;
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
public class BitValueTest {


    private static void test(final BitValue type) throws IOException {

        final List<Object> params = new LinkedList<>();
        final Object[] w = new Object[1];
        final long[] p = new long[1];
        final Object[] r = new Object[1];
        final long[] d = new long[1];

        BitIoTests.all(
            o -> {
                try {
                    w[0] = type.write(params, o);
                    p[0] = o.align(1);
                } catch (final IOException ioe) {
                    throw new UncheckedIOException(ioe);
                }
            },
            i -> {
                try {
                    r[0] = type.read(params, i);
                    d[0] = i.align(1);
                } catch (final IOException ioe) {
                    throw new UncheckedIOException(ioe);
                }
            });

        assertEquals(d[0], p[0]);
        assertEquals(r[0], w[0], "type: " + type);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _boolean() throws IOException {

        test(BitValue.BOOLEAN);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _byte() throws IOException {

        test(BitValue.BYTE);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _short() throws IOException {

        test(BitValue.SHORT);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _int() throws IOException {

        test(BitValue.INT);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _long() throws IOException {

        test(BitValue.LONG);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _char() throws IOException {

        test(BitValue.CHAR);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _float() throws IOException {

        test(BitValue.FLOAT);
    }


    @Test(enabled = true, invocationCount = 1024)
    public void _double() throws IOException {

        test(BitValue.DOUBLE);
    }


    private transient final Logger logger = getLogger(getClass());

}

