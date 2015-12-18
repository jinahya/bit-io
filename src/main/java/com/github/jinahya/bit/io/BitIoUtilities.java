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


import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


/**
 * A class for constraints.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoUtilities {


    public static <T, U extends Throwable> T get(
        final Supplier<? extends T> supplier, final Class<U> throwable)
        throws U {

        try {
            return supplier.get();
        } catch (final RuntimeException re) {
            final Throwable cause = re.getCause();
            if (throwable.isInstance(cause)) {
                throw throwable.cast(cause);
            }
            throw re;
        }
    }


    public static <T, R, U extends Throwable> R apply(
        final Function<? super T, ? extends R> function, final T t,
        final Class<U> throwable)
        throws U {

        try {
            return function.apply(t);
        } catch (final RuntimeException re) {
            final Throwable cause = re.getCause();
            if (throwable.isInstance(cause)) {
                throw throwable.cast(cause);
            }
            throw re;
        }
    }


    public static <T, U extends Throwable> T apply(
        final UnaryOperator<T> operator, final T t, final Class<U> throwable)
        throws U {

        try {
            return operator.apply(t);
        } catch (final RuntimeException re) {
            final Throwable cause = re.getCause();
            if (throwable.isInstance(cause)) {
                throw throwable.cast(cause);
            }
            throw re;
        }
    }


    private BitIoUtilities() {

        super();
    }

}

