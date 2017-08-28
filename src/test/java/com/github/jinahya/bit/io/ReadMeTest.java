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
import static java.lang.Integer.toBinaryString;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.StringUtils.leftPad;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ReadMeTest {

    private static final Logger logger = getLogger(ReadMeTest.class);

    @Test
    public void read() throws IOException {
        final byte[] array = new byte[8];
        final BitInput input = new DefaultBitInput<>(
                new ArrayByteInput(array, 0, array.length));
        input.readBoolean();
        input.readInt(true, 6);
        input.readLong(false, 47);
        final long discarded = input.align(1);
        assertEquals(discarded, 2L);
    }

    @Test
    public void write() throws IOException {
        final byte[] array = new byte[8];
        final BitOutput output = new DefaultBitOutput<>(
                new ArrayByteOutput(array, 0, array.length));
        output.writeBoolean(false);
        output.writeInt(false, 9, -72);
        output.writeBoolean(true);
        output.writeLong(true, 33, 99L);
        final long padded = output.align(4);
        assertEquals(padded, 20L);
        final String w = range(0, array.length)
                .mapToObj(v -> leftPad(toBinaryString(array[v] & 0xFF), 8, '0'))
                .collect(joining(" "));
        assertEquals(w, "01101110 00100000 00000000 00000000"
                        + " 00000110 00110000 00000000 00000000");
    }
}
