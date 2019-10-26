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

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Utilities for testing classes.
 */
@Slf4j
final class BitIoTests {

    // ------------------------------------------------------------------------------------------------------------ byte
    static int randomSizeForByte(final boolean unsigned) {
        return current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
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

    // ----------------------------------------------------------------------------------------------------------- short
    static int randomSizeForShort(final boolean unsigned) {
        return current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
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
        return current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1));
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
        return current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1));
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
        return current().nextInt(1, Character.SIZE);
    }

    static char randomValueForChar(final int size) {
        return (char) (current().nextInt(Character.MAX_VALUE + 1) >> (Integer.SIZE - size));
    }
//
//    // -----------------------------------------------------------------------------------------------------------------
//
//    /**
//     * Applies a random {@code size}, based on given {@code unsigned} flag, to speicifed {@code function}.
//     *
//     * @param unsigned the flag for {@code unsigned}.
//     * @param function the function to which the {@code size} is applied.
//     * @param <R>      function result type parameter
//     * @return the value the function results.
//     */
//    static <R> R applyRandomSizeByte(final boolean unsigned, final IntFunction<R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
//        return function.apply(requireValidSizeByte(unsigned, size));
//    }
//
//    static void acceptRandomSizeByte(final boolean unsigned, final IntConsumer consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        applyRandomSizeByte(unsigned, size -> {
//            consumer.accept(size);
//            return null;
//        });
//    }
//
//    /**
//     * Applies a unsigned flag and a bit size to given function.
//     *
//     * @param function the function.
//     * @param <R>      result type parameter.
//     * @return the value the function results.
//     */
//    static <R> R applyRandomSizeByte(final BiFunction<Boolean, Integer, R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        final boolean unsigned = current().nextBoolean();
//        return applyRandomSizeByte(unsigned, size -> function.apply(unsigned, size));
//    }
//
//    static void acceptRandomSizeByte(final BiConsumer<Boolean, Integer> consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeByte((unsigned, size) -> {
//            consumer.accept(unsigned, size);
//            return null;
//        }));
//    }
//
//    static <R> R applyRandomSizeValueByte(final BiFunction<Pair<Boolean, Integer>, Byte, R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        return applyRandomSizeByte((unsigned, size) -> {
//            final byte value;
//            if (unsigned) {
//                value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
//                assertTrue(value >= 0);
//            } else {
//                value = (byte) (current().nextInt() >> (Integer.SIZE - size));
//                assertEquals(value >> size, value >= 0 ? 0 : -1);
//            }
//            return function.apply(Pair.of(unsigned, size), value);
//        });
//    }
//
//    static void acceptRandomSizeValueByte(final BiConsumer<Pair<Boolean, Integer>, Byte> consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeValueByte((pair, value) -> {
//            consumer.accept(pair, value);
//            return null;
//        }));
//    }
//
//    // ----------------------------------------------------------------------------------------------------------- short
//    static <R> R applyRandomSizeShort(final boolean unsigned, final IntFunction<R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        final int size = current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
//        return function.apply(requireValidSizeShort(unsigned, size));
//    }
//
//    static void acceptRandomSizeShort(final boolean unsigned, final IntConsumer consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeShort(unsigned, size -> {
//            consumer.accept(size);
//            return null;
//        }));
//    }
//
//    static <R> R applyRandomSizeShort(final BiFunction<Boolean, Integer, R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        final boolean unsigned = current().nextBoolean();
//        return applyRandomSizeShort(unsigned, size -> function.apply(unsigned, size));
//    }
//
//    static void acceptRandomSizeShort(final BiConsumer<Boolean, Integer> consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeShort((unsigned, size) -> {
//            consumer.accept(unsigned, size);
//            return null;
//        }));
//    }
//
//    static <R> R applyRandomSizeValueShort(final BiFunction<Pair<Boolean, Integer>, Short, R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        return applyRandomSizeShort((unsigned, size) -> {
//            final short value;
//            if (unsigned) {
//                value = (short) (current().nextInt() >>> (Integer.SIZE - size));
//                assertTrue(value >= 0);
//            } else {
//                value = (short) (current().nextInt() >> (Integer.SIZE - size));
//                assertEquals(value >> size, value >= 0 ? 0 : -1);
//            }
//            return function.apply(Pair.of(unsigned, size), value);
//        });
//    }
//
//    /**
//     * Accepts randomly generated {@code unsigned}, {@code size}, and {@code value} to specified consumer.
//     *
//     * @param consumer the consumer to which random values are accepted.
//     */
//    static void acceptRandomSizeValueShort(final BiConsumer<Pair<Boolean, Integer>, Short> consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeValueShort((pair, value) -> {
//            consumer.accept(pair, value);
//            return null;
//        }));
//    }
//
//    // ------------------------------------------------------------------------------------------------------------- int
//    static <R> R applyRandomSizeInt(final boolean unsigned, final IntFunction<R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        final int size = current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1));
//        return function.apply(requireValidSizeInt(unsigned, size));
//    }
//
//    static void acceptRandomSizeInt(final boolean unsigned, final IntConsumer consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeInt(unsigned, size -> {
//            consumer.accept(size);
//            return null;
//        }));
//    }
//
//    /**
//     * Applies randomly generated {@code unsigned} and {@code size} to specified function and returns the result.
//     *
//     * @param function the function to which values are applied.
//     * @param <R>      result type parameter.
//     * @return the value the function results.
//     */
//    static <R> R applyRandomSizeInt(final BiFunction<Boolean, Integer, R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        final boolean unsigned = current().nextBoolean();
//        return applyRandomSizeInt(unsigned, size -> function.apply(unsigned, size));
//    }
//
//    /**
//     * Accepts randomly generated unsigned flag and bit size to given consumer.
//     *
//     * @param consumer the consumer.
//     */
//    static void acceptRandomSizeInt(final BiConsumer<Boolean, Integer> consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeInt((unsigned, size) -> {
//            consumer.accept(unsigned, size);
//            return null;
//        }));
//    }
//
//    /**
//     * Asserts given value is valid against specified unsigned flag and bit size.
//     *
//     * @param unsigned the unsigned flag.
//     * @param size     the bit size.
//     * @param value    the value to validate.
//     * @return given value.
//     */
//    static int assertValidValueInt(final boolean unsigned, final int size, final int value) {
//        if (unsigned) {
//            assertEquals(0, value >> size);
//        } else if (size < Integer.SIZE) {
//            if (value >= 0) {
//                assertEquals(0, value >> size);
//            } else {
//                assertEquals(-1, value >> (size - 1));
//            }
//        }
//        return value;
//    }
//
//    static <R> R applyRandomSizeValueInt(final BiFunction<Pair<Boolean, Integer>, Integer, R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        return applyRandomSizeInt((unsigned, size) -> {
//            final int value;
//            if (unsigned) {
//                value = (current().nextInt() >>> (Integer.SIZE - size));
//                assertTrue(value >= 0);
//            } else {
//                value = (current().nextInt() >> (Integer.SIZE - size));
//                if (size < Integer.SIZE) {
//                    assertEquals(value >> size, value >= 0 ? 0 : -1);
//                }
//            }
//            assertValidValueInt(unsigned, size, value);
//            return function.apply(Pair.of(unsigned, size), value);
//        });
//    }
//
//    static void acceptRandomSizeValueInt(final BiConsumer<Pair<Boolean, Integer>, Integer> consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeValueInt((pair, value) -> {
//            consumer.accept(pair, value);
//            return null;
//        }));
//    }
//
//    // ------------------------------------------------------------------------------------------------------------ long
//    static <R> R applyRandomSizeLong(final boolean unsigned, final IntFunction<R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        final int size = current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1));
//        return function.apply(requireValidSizeLong(unsigned, size));
//    }
//
//    static void acceptRandomSizeLong(final boolean unsigned, final IntConsumer consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeLong(unsigned, size -> {
//            consumer.accept(size);
//            return null;
//        }));
//    }
//
//    static <R> R applyRandomSizeLong(final BiFunction<Boolean, Integer, R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        final boolean unsigned = current().nextBoolean();
//        return applyRandomSizeLong(unsigned, size -> function.apply(unsigned, size));
//    }
//
//    static void acceptRandomSizeLong(final BiConsumer<Boolean, Integer> consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeLong((unsigned, size) -> {
//            consumer.accept(unsigned, size);
//            return null;
//        }));
//    }
//
//    static long assertValidValueLong(final boolean unsigned, final int size, final long value) {
//        if (unsigned) {
//            assertEquals(0L, value >> size);
//        } else if (size < Long.SIZE) {
//            if (value >= 0) {
//                assertEquals(0L, value >> size);
//            } else {
//                assertEquals(-1L, value >> (size - 1));
//            }
//        }
//        return value;
//    }
//
//    static <R> R applyRandomSizeValueLong(final BiFunction<Pair<Boolean, Integer>, Long, R> function) {
//        if (function == null) {
//            throw new NullPointerException("function is null");
//        }
//        return applyRandomSizeLong((unsigned, size) -> {
//            final long value;
//            if (unsigned) {
//                value = (current().nextLong() >>> (Long.SIZE - size));
//                assertTrue(value >= 0);
//            } else {
//                value = (current().nextLong() >> (Long.SIZE - size));
//                if (size < Long.SIZE) {
//                    assertEquals(value >> size, value >= 0L ? 0L : -1L);
//                }
//            }
//            //log.debug("long; unsigned: {}, size: {}, value: {}", unsigned, size, value);
//            return function.apply(Pair.of(unsigned, size), value);
//        });
//    }
//
//    static void acceptRandomSizeValueLong(final BiConsumer<Pair<Boolean, Integer>, Long> consumer) {
//        if (consumer == null) {
//            throw new NullPointerException("consumer is null");
//        }
//        assertNull(applyRandomSizeValueLong((pair, value) -> {
//            consumer.accept(pair, value);
//            return null;
//        }));
//    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoTests() {
        super();
    }
}
