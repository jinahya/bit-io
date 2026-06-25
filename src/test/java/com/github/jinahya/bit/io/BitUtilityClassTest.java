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

class BitUtilityClassTest {

    @Test
    void bitIoConstantsExposesAsciiNameAndRejectsInstantiation() throws Exception {
        assertEquals("US-ASCII", BitIoConstants.US_ASCII);

        assertPrivateConstructorThrowsAssertionError(BitIoConstants.class);
    }

    @Test
    void bitIoConstraintsRejectsInstantiation() throws Exception {
        assertPrivateConstructorThrowsAssertionError(BitIoConstraints.class);
    }

    private static void assertPrivateConstructorThrowsAssertionError(final Class<?> type) throws Exception {
        final Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);

        final InvocationTargetException exception =
                assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertSame(AssertionError.class, exception.getCause().getClass());
    }
}
