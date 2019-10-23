package com.github.jinahya.bit.io;

import java.util.BitSet;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

final class BitStack {

    // -----------------------------------------------------------------------------------------------------------------
    private void push(final boolean value) {
        if (top == Long.SIZE) {
            throw new RuntimeException("stack overflow");
        }
        set.set(top++, value);
    }

    void push(final int size, long value) {
        requireValidSizeLong(true, size);
        if (value < 0L) {
            throw new IllegalArgumentException("value(" + value + ") < 0L");
        }
        for (int i = 0; i < size; i++) {
            push((value & 0x01L) == 0x01L);
            value >>= 1;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private boolean pop() {
        if (top == 0) {
            throw new RuntimeException("stack underflow");
        }
        return set.get(--top);
    }

    long pop(final int size) {
        System.out.println(set.size());
        requireValidSizeLong(true, size);
        long value = 0L;
        for (int i = 0; i < size; i++) {
            value <<= 1;
            value |= pop() ? 0L : 1L;
        }
        return value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private final BitSet set = new BitSet(Long.SIZE);

    private int top = 0;
}
