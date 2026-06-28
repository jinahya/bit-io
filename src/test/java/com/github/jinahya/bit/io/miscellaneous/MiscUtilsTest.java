package com.github.jinahya.bit.io.miscellaneous;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class MiscUtilsTest {

    @Test
    void fromHexDecodesEvenLengthValue() {
        assertArrayEquals(new byte[]{0x00, 0x0F, (byte) 0xFF}, _Utils.fromHex("000fff"));
    }

    @Test
    void fromHexDecodesOddLengthValueWithLeadingZero() {
        assertArrayEquals(new byte[]{0x0A}, _Utils.fromHex("a"));
    }

    @Test
    void fromHexAcceptsUpperCaseDigits() {
        assertArrayEquals(new byte[]{0x0A, (byte) 0xBC}, _Utils.fromHex("0ABC"));
    }

    @Test
    void fromHexRejectsInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> _Utils.fromHex("0x"));
    }

    @Test
    void fromHexRejectsNullValue() {
        assertThrows(NullPointerException.class, () -> _Utils.fromHex(null));
    }

    @Test
    void toHexEncodesLowerCaseValue() {
        assertEquals("000fff", _Utils.toHex(new byte[]{0x00, 0x0F, (byte) 0xFF}));
    }

    @Test
    void toHexRejectsNullValue() {
        assertThrows(NullPointerException.class, () -> _Utils.toHex(null));
    }
}
