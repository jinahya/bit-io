package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitStackTest {

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testPushAssertStackOverflow() {
        final BitStack stack = new BitStack();
        stack.push(Long.SIZE - 1, current().nextLong() >>> 1);
        assertThrows(RuntimeException.class,
                     () -> stack.push(current().nextInt(1, Long.SIZE), current().nextLong() >>> 1));
    }

    @Test
    void testPush() {
        final BitStack stack = new BitStack();
        stack.push(Long.SIZE - 1, current().nextLong() >>> 1);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testPopAssertStackUnderflow() {
        final BitStack stack = new BitStack();
        assertThrows(RuntimeException.class, () -> stack.pop(current().nextInt(1, Long.SIZE)));
    }

    @Test
    void testPop() {
        final BitStack stack = new BitStack();
        final int size = current().nextInt(Long.SIZE);
        final long value = current().nextLong() >>> 1;
        stack.push(size, value);
        assertTrue(stack.pop(current().nextInt(1, size + 1)) <= value);
    }
}
