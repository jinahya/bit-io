package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A class for unit-testing {@link ByteOutput} class and {@link BitInput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class ByteIoTest {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link ByteOutput#write(int)} method and {@link ByteInput#read()} method.
     *
     * @param output a byte output.
     * @param input  a byte input.
     * @throws IOException if an I/O error occurs.
     */
    @MethodSource({"com.github.jinahya.bit.io.ByteIoSource#sourceByteIo"})
    @ParameterizedTest
    void test(final ByteOutput output, final ByteInput input) throws IOException {
        final int expected = current().nextInt(256);
        output.write(expected);
        final int actual = input.read();
        assertEquals(expected, actual);
    }
}
