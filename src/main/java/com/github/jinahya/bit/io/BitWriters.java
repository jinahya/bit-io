package com.github.jinahya.bit.io;

import java.io.IOException;

final class BitWriters {

    // -----------------------------------------------------------------------------------------------------------------
    public static final BitWriter<BitWritable> INSTANCE_FOR_BIT_WRITABLES = new BitWriter<BitWritable>() {
        @Override
        public void write(final BitWritable value, final BitOutput output) throws IOException {
            value.write(output);
        }
    };

    public static <T extends BitWritable> BitWriter<T> bitWriterFor(final Class<? extends T> type) {
        return new BitWriter<T>() {
            @Override
            public void write(final T value, final BitOutput output) throws IOException {
                value.write(output);
            }
        };
    }
}
