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

import java.util.function.BiFunction;

public interface TriFunction<T, U, V, R> {

    R apply(T t, U u, V v);

    default <I, R> TriFunction<T, U, V, R> a1(final BiFunction<T, U, I> f1, BiFunction<? super I, V, ? extends R> f2) {
        return (t, u, v) -> f2.apply(f1.apply(t, u), v);
    }

    default <I, R> TriFunction<T, U, V, R> a2(final BiFunction<U, V, I> f1, BiFunction<T, ? super I, ? extends R> f2) {
        return (t, u, v) -> f2.apply(t, f1.apply(u, v));
    }
}
