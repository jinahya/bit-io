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

import static com.github.jinahya.bit.io.BitIoRandom.nextSize;
import static com.github.jinahya.bit.io.BitIoRandom.nextValue;
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
@Guice(modules = {BlackBitOutputModule.class})
public class BitOutputTest {

    public static void writeBoolean(final BitOutput output) throws IOException {
        final boolean value = current().nextBoolean();
        output.writeBoolean(value);
    }

    public static void writeByte(final BitOutput output) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = nextSize(unsigned, 3);
        final byte value = (byte) nextValue(unsigned, 3, size);
        output.writeByte(unsigned, size, value);
    }

    public static void writeShort(final BitOutput output) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = nextSize(unsigned, 4);
        final short value = (short) nextValue(unsigned, 4, size);
        output.writeShort(unsigned, size, value);
    }

    public static void writeInt(final BitOutput output) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = nextSize(unsigned, 5);
        final int value = (int) nextValue(unsigned, 5, size);
        output.writeInt(unsigned, size, value);
    }

    public static void writeLong(final BitOutput output) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = nextSize(unsigned, 6);
        final long value = nextValue(unsigned, 6, size);
        output.writeLong(unsigned, size, value);
    }

    public static void writeChar(final BitOutput output) throws IOException {
        final int size = nextSize(true, 4);
        final char value = (char) nextValue(true, 4, size);
        output.writeChar(size, value);
    }

    public static void test(final BitOutput output) throws IOException {
        writeBoolean(output);
        writeByte(output);
        writeShort(output);
        writeInt(output);
        writeLong(output);
        writeChar(output);
    }

    @Test(invocationCount = 128)
    public void writeBoolean() throws IOException {
        writeBoolean(output);
    }

    @Test(invocationCount = 128)
    public void writeByte() throws IOException {
        writeByte(output);
    }

    @Test(invocationCount = 128)
    public void writeShort() throws IOException {
        writeShort(output);
    }

    @Test(invocationCount = 128)
    public void writeInt() throws IOException {
        writeInt(output);
    }

    @Test(invocationCount = 128)
    public void writeLong() throws IOException {
        writeLong(output);
    }

    @Test(invocationCount = 128)
    public void writeChar() throws IOException {
        writeChar(output);
    }

    /**
     * logger.
     */
    private transient final Logger logger = getLogger(getClass());

    @Inject
    private transient BitOutput output;
}
