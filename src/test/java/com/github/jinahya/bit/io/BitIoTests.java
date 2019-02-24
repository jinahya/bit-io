package com.github.jinahya.bit.io;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
final class BitIoTests {

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R randomSizeByte(@NonNull final BiFunction<Boolean, Integer, R> function) {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        return function.apply(unsigned, size);
    }

    static <R> R randomSizeValueByte(@NonNull final BiFunction<Pair<Boolean, Integer>, Byte, R> function) {
        return randomSizeByte((unsigned, size) -> {
            final byte value;
            if (unsigned) {
                value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
                assertTrue(value >= 0);
            } else {
                value = (byte) (current().nextInt() >> (Integer.SIZE - size));
                assertEquals(value >> size, value >= 0 ? 0 : -1);
            }
            log.debug("byte; unsigned: {}, size: {}, value: {}", unsigned, size, value);
            return function.apply(Pair.of(unsigned, size), value);
        });
    }

    static void randomSizeValueByte(@NonNull final BiConsumer<Pair<Boolean, Integer>, Byte> consumer) {
        randomSizeValueByte((pair, value) -> {
            consumer.accept(pair, value);
            return null;
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R randomSizeShort(@NonNull final BiFunction<Boolean, Integer, R> function) {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
        return function.apply(unsigned, size);
    }

    static <R> R randomSizeValueShort(@NonNull final BiFunction<Pair<Boolean, Integer>, Short, R> function) {
        return randomSizeShort((unsigned, size) -> {
            final short value;
            if (unsigned) {
                value = (short) (current().nextInt() >>> (Integer.SIZE - size));
                assertTrue(value >= 0);
            } else {
                value = (short) (current().nextInt() >> (Integer.SIZE - size));
                assertEquals(value >> size, value >= 0 ? 0 : -1);
            }
            log.debug("short; unsigned: {}, size: {}, value: {}", unsigned, size, value);
            return function.apply(Pair.of(unsigned, size), value);
        });
    }

    static void randomSizeValueShort(@NonNull final BiConsumer<Pair<Boolean, Integer>, Short> consumer) {
        randomSizeValueShort((pair, value) -> {
            consumer.accept(pair, value);
            return null;
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R randomSizeInt(@NonNull final BiFunction<Boolean, Integer, R> function) {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Integer.SIZE + (unsigned ? 0 : 1));
        return function.apply(unsigned, size);
    }

    static <R> R randomSizeValueInt(@NonNull final BiFunction<Pair<Boolean, Integer>, Integer, R> function) {
        return randomSizeInt((unsigned, size) -> {
            final int value;
            if (unsigned) {
                value = (current().nextInt() >>> (Integer.SIZE - size));
                assertTrue(value >= 0);
            } else {
                value = (current().nextInt() >> (Integer.SIZE - size));
                if (size < Integer.SIZE) {
                    assertEquals(value >> size, value >= 0 ? 0 : -1);
                }
            }
            log.debug("int; unsigned: {}, size: {}, value: {}", unsigned, size, value);
            return function.apply(Pair.of(unsigned, size), value);
        });
    }

    static void randomSizeValueInt(@NonNull final BiConsumer<Pair<Boolean, Integer>, Integer> consumer) {
        randomSizeValueInt((pair, value) -> {
            consumer.accept(pair, value);
            return null;
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R randomSizeLong(@NonNull final BiFunction<Boolean, Integer, R> function) {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Long.SIZE + (unsigned ? 0 : 1));
        return function.apply(unsigned, size);
    }

    static <R> R randomSizeValueLong(@NonNull final BiFunction<Pair<Boolean, Integer>, Long, R> function) {
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
            log.debug("long; unsigned: {}, size: {}, value: {}", unsigned, size, value);
            return function.apply(Pair.of(unsigned, size), value);
        });
    }

    static void randomSizeValueLong(@NonNull final BiConsumer<Pair<Boolean, Integer>, Long> consumer) {
        randomSizeValueLong((pair, value) -> {
            consumer.accept(pair, value);
            return null;
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoTests() {
        super();
    }
}
