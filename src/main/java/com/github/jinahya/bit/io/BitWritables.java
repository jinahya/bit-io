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
import java.util.Map;
import java.util.WeakHashMap;

import static java.util.Collections.synchronizedMap;

final class BitWritables {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Map<Class<?>, BitWriter<?>> BIT_WRITERS;

    static {
        BIT_WRITERS = synchronizedMap(new WeakHashMap<Class<?>, BitWriter<?>>());
    }

    public static <T extends BitWritable> BitWriter<T> cachedBitWriterFor(final Class<? extends T> type) {
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        synchronized (BIT_WRITERS) {
            @SuppressWarnings({"unchecked"})
            BitWriter<T> value = (BitWriter<T>) BIT_WRITERS.get(type);
            if (value == null) {
                value = newBitWriterFor(type);
                BIT_WRITERS.put(type, value);
            }
            return value;
        }
    }

    /**
     * Creates a new bit writer for instances of specified type.
     *
     * @param type the type of bit writable.
     * @param <T>  bit writable type parameter
     * @return a new bit writer.
     */
    public static <T extends BitWritable> BitWriter<T> newBitWriterFor(final Class<? extends T> type) {
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        return new BitWriter<T>() {
            @Override
            public void write(final BitOutput output, final T value) throws IOException {
                value.write(output);
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private BitWritables() {
        super();
    }
}
