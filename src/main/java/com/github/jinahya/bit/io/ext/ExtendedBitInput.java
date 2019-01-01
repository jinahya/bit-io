/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
 *
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
 */
package com.github.jinahya.bit.io.ext;

import com.github.jinahya.bit.io.BitInput;

import java.io.IOException;

/**
 * @param <T> bit input type parameter
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
public class ExtendedBitInput<T extends BitInput> {

    public static interface Supplier1<T> {

        T get();
    }

    public static interface Consumer1<T> {

        void accept(T t);
    }

    public static interface BiConsumer1<T, U> {

        void accept(T t, U u);
    }

    public static interface Function1<T, R> {

        R apply(T t);
    }

    public static interface BiFunction1<T, U, R> {

        R apply(T t, U u);
    }

    public static byte readUnsignedByte(final BitInput input, final int size)
            throws IOException {
        return input.readByte(true, size);
    }

    // --------------------------------------------------------------------------
    public static <R> R readObject(final boolean nullable, final BitInput input,
                                   final Function1<BitInput, R> function)
            throws IOException {
        if (nullable && !input.readBoolean()) {
            return null;
        }
        return function.apply(input);
    }

    public static <R> R readObject(final BitInput input,
                                   final Function1<BitInput, R> function)
            throws IOException {
        return readObject(false, input, function);
    }

    public static <R> R readNullable(final BitInput input,
                                     final Function1<BitInput, R> function)
            throws IOException {
        return readObject(true, input, function);
    }

    // -------------------------------------------------------------------------
    public static <U, R> R readObject(
            final boolean nullable, final BitInput input,
            final BiFunction1<BitInput, U, R> function, final U u)
            throws IOException {
        if (nullable && !input.readBoolean()) {
            return null;
        }
        return function.apply(input, u);
    }

    public static <U, R> R readObject(
            final BitInput input, final BiFunction1<BitInput, U, R> function,
            final U u)
            throws IOException {
        return readObject(false, input, function, u);
    }

    public static <U, R> R readNullable(
            final BitInput input, final BiFunction1<BitInput, U, R> function,
            final U u)
            throws IOException {
        return readObject(true, input, function, u);
    }

    // -------------------------------------------------------------------------
    public ExtendedBitInput(final T delegate) {
        super();
        this.delegate = delegate;
    }

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    protected final T delegate;
}
