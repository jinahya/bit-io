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

final class BitWriters {

    // -----------------------------------------------------------------------------------------------------------------
    private static Map<Class<?>, BitWriter<?>> BIT_WRITERS;

    private static Map<Class<?>, BitWriter<?>> bitWriters() {
        if (BIT_WRITERS == null) {
            BIT_WRITERS = new WeakHashMap<Class<?>, BitWriter<?>>();
        }
        return BIT_WRITERS;
    }

    private static <T extends BitWritable> BitWriter<T> bitWRiter(final Class<? extends T> type) {
        @SuppressWarnings({"unchecked"})
        BitWriter<T> value = (BitWriter<T>) bitWriters().get(type);
        if (value == null) {
            value = new BitWriter<T>() {
                @Override
                public void write(final BitOutput output, final T value) throws IOException {
                    value.write(output);
                }
            };
            BIT_WRITERS.put(type, value);
        }
        return value;
    }

    public static <T extends BitWritable> BitWriter<T> bitWriterFor(final Class<? extends T> type) {
        return bitWRiter(type);
    }
}