package com.github.jinahya.bit.io;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

final class BitIoUtils {

    // -----------------------------------------------------------------------------------------------------------------
    public static long reverse(final int size, long value) {
        requireValidSizeLong(true, size);
        long result = 0L;
        for (int i = 0; i < size; i++) {
            result <<= 1;
            result |= value & 0x01;
            value >>= 1;
        }
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoUtils() {
        super();
    }
}
