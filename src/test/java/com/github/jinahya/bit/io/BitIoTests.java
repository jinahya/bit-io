package com.github.jinahya.bit.io;

import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class BitIoTests {

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R applyRandomUnsignedSizeByte(final BiFunction<Boolean, Integer, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        return function.apply(unsigned, size);
    }

    static void acceptRandomUnsignedSizeByte(final BiConsumer<Boolean, Integer> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        applyRandomUnsignedSizeByte((unsigned, size) -> {
            consumer.accept(unsigned, size);
            return null;
        });
    }

    static <R> R applyRandomUnsignedSizeValueByte(final BiFunction<Pair<Boolean, Integer>, Byte, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        return applyRandomUnsignedSizeByte((unsigned, size) -> {
            final byte value = (byte) (current().nextInt() >>> (Integer.SIZE - size));
            if (unsigned) {
                assertTrue(value >= 0);
            } else {
                assertEquals(value >> size, value < 0 ? -1 : 0);
            }
            return function.apply(Pair.of(unsigned, size), value);
        });
    }

    static void acceptRandomUnsignedSizeValueByte(final BiConsumer<Pair<Boolean, Integer>, Byte> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        applyRandomUnsignedSizeValueByte((pair, value) -> {
            consumer.accept(pair, value);
            return null;
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoTests() {
        super();
    }
}
