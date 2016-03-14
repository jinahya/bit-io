/*
 *  Copyright 2010 Jin Kwon.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.jinahya.bit.io;

import java.io.IOException;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

/**
 * A class for constraints.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@IgnoreJRERequirement
final class BitIoUtilities {

    public static <T, U extends Throwable> T get(
            final Supplier<? extends T> supplier)
            throws IOException {

        try {
            return supplier.get();
//        } catch (final UncheckedIOException uioe) {
//            throw uioe.getCause();
        } catch (final RuntimeException re) {
            final Throwable cause = re.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw re;
        }
    }

    public static <T, U extends Throwable> T apply(
            final UnaryOperator<T> operator, final T t)
            throws IOException {

        try {
            return operator.apply(t);
//        } catch (final UncheckedIOException uioe) {
//            throw uioe.getCause();
        } catch (final RuntimeException re) {
            final Throwable cause = re.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw re;
        }
    }

    public static <T> void accept(
            final ObjIntConsumer<? super T> consumer, final T t, final int i)
            throws IOException {

        if (consumer == null) {
            throw new NullPointerException("null consumer");
        }

        try {
            consumer.accept(t, i);
//        } catch (final UncheckedIOException uioe) {
//            throw uioe.getCause();
        } catch (final RuntimeException re) {
            final Throwable cause = re.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw re;
        }
    }

    public static <T> int apply(
            final ToIntFunction<? super T> function, final T t)
            throws IOException {

        if (function == null) {
            throw new NullPointerException("null consumer");
        }

        try {
            return function.applyAsInt(t);
//        } catch (final UncheckedIOException uioe) {
//            throw uioe.getCause();
        } catch (final RuntimeException re) {
            final Throwable cause = re.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw re;
        }
    }

    private BitIoUtilities() {

        super();
    }

}
