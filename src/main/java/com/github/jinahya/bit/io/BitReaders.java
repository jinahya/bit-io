package com.github.jinahya.bit.io;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

final class BitReaders {

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends BitReadable> BitReader<T> bitReaderFor(final Class<? extends T> type) {
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        return new BitReader<T>() {
            @Override
            public T read(final BitInput input) throws IOException {
                try {
                    final Constructor<? extends T> constructor = type.getDeclaredConstructor();
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
        };
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitReaders() {
    }
}
