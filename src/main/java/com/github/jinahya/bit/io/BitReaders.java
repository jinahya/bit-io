package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
