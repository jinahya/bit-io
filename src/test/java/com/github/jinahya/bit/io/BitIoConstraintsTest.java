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

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing {@link BitIoConstraints}.
 */
class BitIoConstraintsTest {

    // ------------------------------------------------------------------------------------------------------------ byte
    @Test
    void testRequireValidSizeByte_throwsOnZero() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeByte(false, 0));
    }

    @Test
    void testRequireValidSizeByte_throwsOnNegative() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeByte(false, -1));
    }

    @Test
    void testRequireValidSizeByte_throwsOnTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeByte(false, Byte.SIZE + 1));
    }

    @Test
    void testRequireValidSizeByte_throwsOnUnsignedMaxSize() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeByte(true, Byte.SIZE));
    }

    // ----------------------------------------------------------------------------------------------------------- short
    @Test
    void testRequireValidSizeShort_throwsOnZero() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeShort(false, 0));
    }

    @Test
    void testRequireValidSizeShort_throwsOnNegative() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeShort(false, -1));
    }

    @Test
    void testRequireValidSizeShort_throwsOnTooLarge() {
        assertThrows(IllegalArgumentException.class,
                     () -> BitIoConstraints.requireValidSizeShort(false, Short.SIZE + 1));
    }

    @Test
    void testRequireValidSizeShort_throwsOnUnsignedMaxSize() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeShort(true, Short.SIZE));
    }

    // ------------------------------------------------------------------------------------------------------------- int
    @Test
    void testRequireValidSizeInt_throwsOnZero() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeInt(false, 0));
    }

    @Test
    void testRequireValidSizeInt_throwsOnNegative() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeInt(false, -1));
    }

    @Test
    void testRequireValidSizeInt_throwsOnTooLarge() {
        assertThrows(IllegalArgumentException.class,
                     () -> BitIoConstraints.requireValidSizeInt(false, Integer.SIZE + 1));
    }

    @Test
    void testRequireValidSizeInt_throwsOnUnsignedMaxSize() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeInt(true, Integer.SIZE));
    }

    // ------------------------------------------------------------------------------------------------------------ long
    @Test
    void testRequireValidSizeLong_throwsOnZero() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeLong(false, 0));
    }

    @Test
    void testRequireValidSizeLong_throwsOnNegative() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeLong(false, -1));
    }

    @Test
    void testRequireValidSizeLong_throwsOnTooLarge() {
        assertThrows(IllegalArgumentException.class,
                     () -> BitIoConstraints.requireValidSizeLong(false, Long.SIZE + 1));
    }

    @Test
    void testRequireValidSizeLong_throwsOnUnsignedMaxSize() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeLong(true, Long.SIZE));
    }

    // ------------------------------------------------------------------------------------------------------------ char
    @Test
    void testRequireValidSizeChar_throwsOnZero() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeChar(0));
    }

    @Test
    void testRequireValidSizeChar_throwsOnNegative() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeChar(-1));
    }

    @Test
    void testRequireValidSizeChar_throwsOnTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> BitIoConstraints.requireValidSizeChar(Character.SIZE + 1));
    }
}
