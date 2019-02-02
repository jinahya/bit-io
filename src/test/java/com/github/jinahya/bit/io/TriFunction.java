package com.github.jinahya.bit.io;

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
