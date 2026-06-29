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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BitIoUtilsTest {

    @Test
    void constructorThrowsAssertionError() throws Exception {
        final Constructor<?> constructor = BitIoUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        final InvocationTargetException exception =
                assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertSame(AssertionError.class, exception.getCause().getClass());
    }

    @Test
    void highestOneBitIndexInt() {
        assertEquals(-1, BitIoUtils.highestOneBitIndex(0));
        assertEquals(0, BitIoUtils.highestOneBitIndex(1));
        assertEquals(30, BitIoUtils.highestOneBitIndex(1 << 30));
        assertEquals(31, BitIoUtils.highestOneBitIndex(Integer.MIN_VALUE));
    }

    @Test
    void highestOneBitIndexLong() {
        assertEquals(-1, BitIoUtils.highestOneBitIndex(0L));
        assertEquals(0, BitIoUtils.highestOneBitIndex(1L));
        assertEquals(62, BitIoUtils.highestOneBitIndex(1L << 62));
        assertEquals(63, BitIoUtils.highestOneBitIndex(Long.MIN_VALUE));
    }

    @Test
    void requireValidSizeForSignedByteAcceptsMinAndMax() {
        assertEquals(1, BitIoUtils.requireValidSizeForSignedByte(1));
        assertEquals(Byte.SIZE, BitIoUtils.requireValidSizeForSignedByte(Byte.SIZE));
    }

    @Test
    void requireValidSizeForSignedByteRejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidSizeForSignedByte(0));
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidSizeForSignedByte(Byte.SIZE + 1));
    }
}
