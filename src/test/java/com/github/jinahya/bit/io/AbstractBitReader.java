package com.github.jinahya.bit.io;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class AbstractBitReader<T extends BitReadable> implements BitReader<T> {

    // -----------------------------------------------------------------------------------------------------------------
    public AbstractBitReader(final Class<T> type) {
        super();
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        this.type = type;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public T read(final BitInput input) throws IOException {
        try {
            final Constructor<T> constructor = type.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            try {
                final T value = constructor.newInstance();
                value.read(input);
                return value;
            } catch (final InstantiationException ie) {
                throw new RuntimeException(ie);
            } catch (final IllegalAccessException iae) {
                throw new RuntimeException(iae);
            } catch (final InvocationTargetException ite) {
                throw new RuntimeException(ite);
            }
        } catch (final NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected final Class<T> type;
}
