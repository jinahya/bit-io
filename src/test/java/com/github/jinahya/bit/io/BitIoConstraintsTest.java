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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing {@link BitIoConstraints}.
 */
class BitIoConstraintsTest {

    @MethodSource("integralSizeValidators")
    @ParameterizedTest
    void splitIntegralValidatorsAcceptMinAndMax(final Validator validator, final int min, final int max) {
        assertEquals(min, validator.requireValidSize(min));
        assertEquals(max, validator.requireValidSize(max));
    }

    @MethodSource("integralSizeValidators")
    @ParameterizedTest
    void splitIntegralValidatorsRejectOutOfRangeSizes(final Validator validator, final int min, final int max) {
        assertThrows(IllegalArgumentException.class, () -> validator.requireValidSize(min - 1));
        assertThrows(IllegalArgumentException.class, () -> validator.requireValidSize(-1));
        assertThrows(IllegalArgumentException.class, () -> validator.requireValidSize(max + 1));
    }

    @CsvSource({
            "1, 16"
    })
    @ParameterizedTest
    void charValidatorAcceptsMinAndMax(final int min, final int max) {
        assertEquals(min, BitIoUtils.requireValidSizeChar(min));
        assertEquals(max, BitIoUtils.requireValidSizeChar(max));
    }

    @CsvSource({
            "0",
            "-1",
            "17"
    })
    @ParameterizedTest
    void charValidatorRejectsOutOfRangeSizes(final int size) {
        assertThrows(IllegalArgumentException.class, () -> BitIoUtils.requireValidSizeChar(size));
    }

    @CsvSource({
            "true,  byte,  1,  7",
            "false, byte,  1,  8",
            "true,  short, 1,  15",
            "false, short, 1,  16",
            "true,  int,   1,  31",
            "false, int,   1,  32",
            "true,  long,  1,  63",
            "false, long,  1,  64"
    })
    @ParameterizedTest
    void dispatchersAcceptMinAndMax(final boolean unsigned, final String type, final int min, final int max) {
        assertEquals(min, dispatch(type, unsigned, min));
        assertEquals(max, dispatch(type, unsigned, max));
    }

    @CsvSource({
            "true,  byte,  1,  7",
            "false, byte,  1,  8",
            "true,  short, 1,  15",
            "false, short, 1,  16",
            "true,  int,   1,  31",
            "false, int,   1,  32",
            "true,  long,  1,  63",
            "false, long,  1,  64"
    })
    @ParameterizedTest
    void dispatchersRejectOutOfRangeSizes(final boolean unsigned, final String type, final int min, final int max) {
        assertThrows(IllegalArgumentException.class, () -> dispatch(type, unsigned, min - 1));
        assertThrows(IllegalArgumentException.class, () -> dispatch(type, unsigned, -1));
        assertThrows(IllegalArgumentException.class, () -> dispatch(type, unsigned, max + 1));
    }

    @CsvSource({
            "true,  byte,  1",
            "true,  byte,  7",
            "false, byte,  1",
            "false, byte,  7",
            "false, byte,  8",
            "true,  short, 1",
            "true,  short, 15",
            "false, short, 1",
            "false, short, 15",
            "false, short, 16",
            "true,  int,   1",
            "true,  int,   31",
            "false, int,   1",
            "false, int,   31",
            "false, int,   32",
            "true,  long,  1",
            "true,  long,  63",
            "false, long,  1",
            "false, long,  63",
            "false, long,  64"
    })
    @ParameterizedTest
    void dispatchersMatchSplitValidatorsForValidSizes(final boolean unsigned, final String type, final int size) {
        assertEquals(split(type, unsigned, size), dispatch(type, unsigned, size));
    }

    @CsvSource({
            "true,  byte,  0",
            "true,  byte,  -1",
            "true,  byte,  8",
            "false, byte,  0",
            "false, byte,  -1",
            "false, byte,  9",
            "true,  short, 0",
            "true,  short, -1",
            "true,  short, 16",
            "false, short, 0",
            "false, short, -1",
            "false, short, 17",
            "true,  int,   0",
            "true,  int,   -1",
            "true,  int,   32",
            "false, int,   0",
            "false, int,   -1",
            "false, int,   33",
            "true,  long,  0",
            "true,  long,  -1",
            "true,  long,  64",
            "false, long,  0",
            "false, long,  -1",
            "false, long,  65"
    })
    @ParameterizedTest
    void dispatchersAndSplitValidatorsThrowSameTypeForInvalidSizes(final boolean unsigned, final String type,
                                                                   final int size) {
        assertThrows(IllegalArgumentException.class, () -> split(type, unsigned, size));
        assertThrows(IllegalArgumentException.class, () -> dispatch(type, unsigned, size));
    }

    @MethodSource("floatingPointSizeValidators")
    @ParameterizedTest
    void floatingPointValidatorsAcceptMinAndMax(final Validator validator, final int min, final int max) {
        assertEquals(min, validator.requireValidSize(min));
        assertEquals(max, validator.requireValidSize(max));
    }

    @MethodSource("floatingPointSizeValidators")
    @ParameterizedTest
    void floatingPointValidatorsRejectOutOfRangeSizes(final Validator validator, final int min, final int max) {
        assertThrows(IllegalArgumentException.class, () -> validator.requireValidSize(1));
        assertThrows(IllegalArgumentException.class, () -> validator.requireValidSize(-1));
        assertThrows(IllegalArgumentException.class, () -> validator.requireValidSize(max + 1));
    }

    private static Stream<Object[]> integralSizeValidators() {
        return Stream.of(
                new Object[]{validator(BitIoUtils::requireValidSizeForUnsignedByte), 1, 7},
                new Object[]{validator(BitIoUtils::requireValidSizeForSignedByte), 1, 8},
                new Object[]{validator(BitIoUtils::requireValidSizeForUnsignedShort), 1, 15},
                new Object[]{validator(BitIoUtils::requireValidSizeForSignedShort), 1, 16},
                new Object[]{validator(BitIoUtils::requireValidSizeForUnsignedInt), 1, 31},
                new Object[]{validator(BitIoUtils::requireValidSizeForSignedInt), 1, 32},
                new Object[]{validator(BitIoUtils::requireValidSizeForUnsignedLong), 1, 63},
                new Object[]{validator(BitIoUtils::requireValidSizeForSignedLong), 1, 64}
        );
    }

    private static Stream<Object[]> floatingPointSizeValidators() {
        return Stream.of(
                new Object[]{validator(BitIoUtils::requireValidExponentSizeFloat), 2, 8},
                new Object[]{validator(BitIoUtils::requireValidFractionSizeFloat), 2, 23},
                new Object[]{validator(BitIoUtils::requireValidExponentSizeDouble), 2, 11},
                new Object[]{validator(BitIoUtils::requireValidFractionSizeDouble), 2, 52}
        );
    }

    private static Validator validator(final Validator validator) {
        return validator;
    }

    private static int dispatch(final String type, final boolean unsigned, final int size) {
        if ("byte".equals(type)) {
            return BitIoUtils.requireValidSizeByte(unsigned, size);
        }
        if ("short".equals(type)) {
            return BitIoUtils.requireValidSizeShort(unsigned, size);
        }
        if ("int".equals(type)) {
            return BitIoUtils.requireValidSizeInt(unsigned, size);
        }
        if ("long".equals(type)) {
            return BitIoUtils.requireValidSizeLong(unsigned, size);
        }
        throw new AssertionError("unknown type: " + type);
    }

    private static int split(final String type, final boolean unsigned, final int size) {
        if ("byte".equals(type)) {
            return unsigned ? BitIoUtils.requireValidSizeForUnsignedByte(size)
                    : BitIoUtils.requireValidSizeForSignedByte(size);
        }
        if ("short".equals(type)) {
            return unsigned ? BitIoUtils.requireValidSizeForUnsignedShort(size)
                    : BitIoUtils.requireValidSizeForSignedShort(size);
        }
        if ("int".equals(type)) {
            return unsigned ? BitIoUtils.requireValidSizeForUnsignedInt(size)
                    : BitIoUtils.requireValidSizeForSignedInt(size);
        }
        if ("long".equals(type)) {
            return unsigned ? BitIoUtils.requireValidSizeForUnsignedLong(size)
                    : BitIoUtils.requireValidSizeForSignedLong(size);
        }
        throw new AssertionError("unknown type: " + type);
    }

    private interface Validator {

        int requireValidSize(int size);
    }
}
