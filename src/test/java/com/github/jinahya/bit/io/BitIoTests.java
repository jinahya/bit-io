package com.github.jinahya.bit.io;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Utilities for testing classes.
 */
final class BitIoTests {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Applies a unsigned flag and a bit size to given function.
     *
     * @param function the function.
     * @param <R> result type parameter.
     * @return the value the function results.
     */
    static <R> R randomSizeByte(final BiFunction<Boolean, Integer, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        return function.apply(unsigned, size);
    }

    static void randomSizeByte(final BiConsumer<Boolean, Integer> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(randomSizeByte((unsigned, size) -> {
            consumer.accept(unsigned, size);
            return null;
        }));
    }

    static <R> R randomSizeValueByte(final BiFunction<Pair<Boolean, Integer>, Byte, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        return randomSizeByte((unsigned, size) -> {
            final byte value;
            if (unsigned) {
                value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
                assertTrue(value >= 0);
            } else {
                value = (byte) (current().nextInt() >> (Integer.SIZE - size));
                assertEquals(value >> size, value >= 0 ? 0 : -1);
            }
            return function.apply(Pair.of(unsigned, size), value);
        });
    }

    static void randomSizeValueByte(final BiConsumer<Pair<Boolean, Integer>, Byte> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(randomSizeValueByte((pair, value) -> {
            consumer.accept(pair, value);
            return null;
        }));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R randomSizeShort(final BiFunction<Boolean, Integer, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
        return function.apply(unsigned, size);
    }

    static void randomSizeShort(final BiConsumer<Boolean, Integer> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(randomSizeShort((unsigned, size) -> {
            consumer.accept(unsigned, size);
            return null;
        }));
    }

    static <R> R randomSizeValueShort(final BiFunction<Pair<Boolean, Integer>, Short, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        return randomSizeShort((unsigned, size) -> {
            final short value;
            if (unsigned) {
                value = (short) (current().nextInt() >>> (Integer.SIZE - size));
                assertTrue(value >= 0);
            } else {
                value = (short) (current().nextInt() >> (Integer.SIZE - size));
                assertEquals(value >> size, value >= 0 ? 0 : -1);
            }
            return function.apply(Pair.of(unsigned, size), value);
        });
    }

    /**
     * Accepts randomly generated {@code unsigned}, {@code size}, and {@code value} to specified consumer.
     *
     * @param consumer the consumer to which random values are accepted.
     */
    static void randomSizeValueShort(final BiConsumer<Pair<Boolean, Integer>, Short> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(randomSizeValueShort((pair, value) -> {
            consumer.accept(pair, value);
            return null;
        }));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Applies randomly generated {@code unsigned} and {@code size} to specified function and returns the result.
     *
     * @param function the function to which values are applied.
     * @param <R>      result type parameter.
     * @return the value the function results.
     */
    static <R> R randomSizeInt(final BiFunction<Boolean, Integer, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1));
        return function.apply(unsigned, size);
    }

    /**
     * Accepts randomly generated unsigned flag and bit size to given consumer.
     *
     * @param consumer the consumer.
     */
    static void randomSizeInt(final BiConsumer<Boolean, Integer> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(randomSizeInt((unsigned, size) -> {
            consumer.accept(unsigned, size);
            return null;
        }));
    }

    /**
     * Asserts given value is valid against specified unsigned flag and bit size.
     *
     * @param unsigned the unsigned flag.
     * @param size     the bit size.
     * @param value    the value to validate.
     * @return given value.
     */
    static int assertValidValueInt(final boolean unsigned, final int size, final int value) {
        if (unsigned) {
            assertEquals(0, value >> size);
        } else if (size < Integer.SIZE) {
            if (value >= 0) {
                assertEquals(0, value >> size);
            } else {
                assertEquals(-1, value >> (size - 1));
            }
        }
        return value;
    }

    static <R> R randomSizeValueInt(final BiFunction<Pair<Boolean, Integer>, Integer, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        return randomSizeInt((unsigned, size) -> {
            final int value;
            if (unsigned) {
                value = (current().nextInt() >>> (Integer.SIZE - size));
            } else {
                value = (current().nextInt() >> (Integer.SIZE - size));
            }
            assertValidValueInt(unsigned, size, value);
            return function.apply(Pair.of(unsigned, size), value);
        });
    }

    static void randomSizeValueInt(final BiConsumer<Pair<Boolean, Integer>, Integer> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(randomSizeValueInt((pair, value) -> {
            consumer.accept(pair, value);
            return null;
        }));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R randomSizeLong(final BiFunction<Boolean, Integer, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1));
        return function.apply(unsigned, size);
    }

    static void randomSizeLong(final BiConsumer<Boolean, Integer> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(randomSizeLong((unsigned, size) -> {
            consumer.accept(unsigned, size);
            return null;
        }));
    }

    static long assertValidValueLong(final boolean unsigned, final int size, final long value) {
        if (unsigned) {
            assertEquals(0L, value >> size);
        } else if (size < Long.SIZE) {
            if (value >= 0) {
                assertEquals(0L, value >> size);
            } else {
                assertEquals(-1L, value >> (size - 1));
            }
        }
        return value;
    }

    static <R> R randomSizeValueLong(final BiFunction<Pair<Boolean, Integer>, Long, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        return randomSizeLong((unsigned, size) -> {
            final long value;
            if (unsigned) {
                value = (current().nextLong() >>> (Long.SIZE - size));
                assertTrue(value >= 0);
            } else {
                value = (current().nextLong() >> (Long.SIZE - size));
                if (size < Long.SIZE) {
                    assertEquals(value >> size, value >= 0L ? 0L : -1L);
                }
            }
            return function.apply(Pair.of(unsigned, size), value);
        });
    }

    static void randomSizeValueLong(final BiConsumer<Pair<Boolean, Integer>, Long> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        assertNull(randomSizeValueLong((pair, value) -> {
            consumer.accept(pair, value);
            return null;
        }));
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoTests() {
        super();
    }
}
