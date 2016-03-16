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
import java.io.IOException;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Inject;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * Test class tests {@link BitInput}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = {WhiteBitInputModule.class})
public class BitInputTest {

    public static void readBoolean(final BitInput input) throws IOException {
        final boolean value = input.readBoolean();
    }

    public static void readByte(final BitInput input) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = nextSize(unsigned, 3);
        final byte value = input.readByte(unsigned, size);
    }

    public static void readShort(final BitInput input) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = nextSize(unsigned, 4);
        final short value = input.readShort(unsigned, size);
    }

    public static void readInt(final BitInput input) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = nextSize(unsigned, 5);
        final int value = input.readInt(unsigned, size);
    }

    public static void readLong(final BitInput input) throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = nextSize(unsigned, 6);
        final long value = input.readLong(unsigned, size);
    }

    public static void readChar(final BitInput input) throws IOException {
        final int size = current().nextInt(1, 17);
        final char value = input.readChar(size);
    }

    public static void test(final BitInput input) throws IOException {
        readBoolean(input);
        readByte(input);
        readShort(input);
        readInt(input);
        readLong(input);
        readChar(input);
    }

    @Test(invocationCount = 128)
    public void readBoolean() throws IOException {
        readBoolean(input);
    }

    @Test(invocationCount = 128)
    public void readByte() throws IOException {
        readByte(input);
    }

    @Test(invocationCount = 128)
    public void readShort() throws IOException {
        readShort(input);
    }

    @Test(invocationCount = 128)
    public void readInt() throws IOException {
        readInt(input);
    }

    @Test(invocationCount = 128)
    public void readLong() throws IOException {
        readLong(input);
    }

    @Test(invocationCount = 128)
    public void readChar() throws IOException {
        readChar(input);
    }

    private transient final Logger logger = getLogger(getClass());

    @Inject
    private transient BitInput input;
}
