package com.github.jinahya.bit.io;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class AbstractBitWriter<T extends BitWritable> implements BitWriter<T> {

    // -----------------------------------------------------------------------------------------------------------------
    public AbstractBitWriter(final Class<T> type) {
        super();
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        this.type = type;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final BitOutput output, final T value) throws IOException {
        value.write(output);
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected final Class<T> type;
}
