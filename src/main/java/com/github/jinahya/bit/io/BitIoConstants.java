package com.github.jinahya.bit.io;

final class BitIoConstants {

    // -----------------------------------------------------------------------------------------------------------------
    @Deprecated
    static final int SIZE_UNSIGNED_INTEGER = Integer.SIZE - 1;

    @Deprecated
    static final int BYTES_INTEGER = Integer.SIZE / Byte.SIZE;

    @Deprecated
    static final int BYTES_LONG = Long.SIZE / Byte.SIZE;

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoConstants() {
        super();
    }
}
