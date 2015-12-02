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
import org.testng.annotations.Test;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BitIoTest {


    @Test
    public void test1() throws IOException {

        final BitType[] types = BitType.values();

        final byte[] array = new byte[1048576];

        final int count = current().nextInt(128);
        final List<Object> list = new LinkedList<>(); // (type, size, value)+

        final BitOutput output = new DelegatedBitOutput(
            new ArrayOutput(array, 0, array.length));
        for (int i = 0; i < count; i++) {
            final BitType type = types[current().nextInt(types.length)];
            final int size = type.length();
            final Object value = type.value(size);
            list.add(type);
            list.add(size);
            list.add(value);
            type.write(size, output, value);
        }
        final int padded = output.align(1);
        logger.debug("padded: {}", padded);

        final BitInput input = new DelegatedBitInput(
            new ArrayInput(array, 0, array.length));
        for (int i = 0; i < count; i++) {
            final BitType type = (BitType) list.remove(0);
            final int size = (int) list.remove(0);
            final Object expected = list.remove(0);
            final Object actual = type.read(size, input);
            assertEquals(actual, expected);
        }
        final int discarded = input.align(1);
        logger.debug("discarded: {}", discarded);

        assertEquals(discarded, padded);
    }


    private transient final Logger logger = getLogger(getClass());

}

