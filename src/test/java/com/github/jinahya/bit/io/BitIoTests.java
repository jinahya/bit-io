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
import org.junit.jupiter.params.provider.Arguments;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeByte;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeChar;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeShort;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Utilities for testing classes.
 */
@Slf4j
final class BitIoTests {

    // ------------------------------------------------------------------------------------------------------------ byte
    static int randomSizeForByte(final boolean unsigned) {
        return requireValidSizeByte(unsigned, current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1)));
    }

    static byte randomValueForByte(final boolean unsigned, final int size) {
        final byte value;
        if (unsigned) {
            value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
            assertTrue(value >= 0);
        } else {
            value = (byte) (current().nextInt() >> (Integer.SIZE - size));
            assertEquals(value >> size, value >= 0 ? 0 : -1);
        }
        return value;
    }

    static <R> R applyRandomSizeForByte(final boolean unsigned,
                                        final BiFunction<? super Boolean, ? super Integer, ? extends R> function) {
        return function.apply(unsigned, randomSizeForByte(unsigned));
    }

    static <R> R applyRandomSizeValueForByte(final boolean unsigned,
                                             final BiFunction<? super Integer, ? super Byte, ? extends R> function) {
        return applyRandomSizeForByte(unsigned, (u, s) -> function.apply(s, randomValueForByte(u, s)));
    }

    static Stream<Arguments> sourceSizeAndValueForByteSigned() {
        final Stream.Builder<Arguments> builder = Stream.builder();
        builder.add(Arguments.of(1, (byte) -1));
        builder.add(Arguments.of(1, (byte) 0));
        builder.add(Arguments.of(2, (byte) 1));
        builder.add(Arguments.of(Byte.SIZE, Byte.MIN_VALUE));
        builder.add(Arguments.of(Byte.SIZE, (byte) -1));
        builder.add(Arguments.of(Byte.SIZE, (byte) 0));
        builder.add(Arguments.of(Byte.SIZE, (byte) 1));
        builder.add(Arguments.of(Byte.SIZE, Byte.MAX_VALUE));
        range(0, 128).forEach(i -> applyRandomSizeValueForByte(false, (s, v) -> builder.add(Arguments.of(s, v))));
        return builder.build();
    }

    static Stream<Arguments> sourceSizeAndValueForByteUnsigned() {
        final Stream.Builder<Arguments> builder = Stream.builder();
        builder.add(Arguments.of(1, (byte) 0));
        builder.add(Arguments.of(1, (byte) 1));
        builder.add(Arguments.of(Byte.SIZE - 1, (byte) 0));
        builder.add(Arguments.of(Byte.SIZE - 1, (byte) 1));
        builder.add(Arguments.of(Byte.SIZE - 1, Byte.MAX_VALUE));
        range(0, 128).forEach(i -> applyRandomSizeValueForByte(true, (s, v) -> builder.add(Arguments.of(s, v))));
        return builder.build();
    }

    // ----------------------------------------------------------------------------------------------------------- short
    static int randomSizeForShort(final boolean unsigned) {
        return requireValidSizeShort(unsigned, current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1)));
    }

    static short randomValueForShort(final boolean unsigned, final int size) {
        final short value;
        if (unsigned) {
            value = (short) (current().nextInt() >>> (Integer.SIZE - size));
            assertTrue(value >= 0);
        } else {
            value = (short) (current().nextInt() >> (Integer.SIZE - size));
            assertEquals(value >> size, value >= 0 ? 0 : -1);
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------------------------- int
    static int randomSizeForInt(final boolean unsigned) {
        return requireValidSizeInt(unsigned, current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1)));
    }

    static int randomValueForInt(final boolean unsigned, final int size) {
        final int value;
        if (unsigned) {
            value = current().nextInt() >>> (Integer.SIZE - size);
            assertTrue(value >= 0);
        } else {
            value = current().nextInt() >> (Integer.SIZE - size);
            assertEquals(value >> size, value >= 0 ? 0 : -1);
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------------------------ long
    static int randomSizeForLong(final boolean unsigned) {
        return requireValidSizeLong(unsigned, current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1)));
    }

    static long randomValueForLong(final boolean unsigned, final int size) {
        final long value;
        if (unsigned) {
            value = current().nextLong() >>> (Long.SIZE - size);
            assertTrue(value >= 0);
        } else {
            value = current().nextLong() >> (Long.SIZE - size);
            assertEquals(value >> size, value >= 0L ? 0L : -1L);
        }
        return value;
    }

    // ------------------------------------------------------------------------------------------------------------ char
    static int randomSizeForChar() {
        return requireValidSizeChar(current().nextInt(1, Character.SIZE));
    }

    static char randomValueForChar(final int size) {
        return (char) (current().nextInt(Character.MAX_VALUE + 1) >> (Integer.SIZE - size));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoTests() {
        super();
    }
}
