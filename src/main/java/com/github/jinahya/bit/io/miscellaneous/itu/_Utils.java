package com.github.jinahya.bit.io.miscellaneous.itu;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2026 Jinahya, Inc.
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

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;

import java.util.function.Supplier;

final class _Utils {

    static <T extends BitInput> T requireNonNullInput(final T input) {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        return input;
    }

    static <T extends BitOutput> T requireNonNullOutput(final T output) {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        return output;
    }

    static <T> T requireNonNullValue(final T value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return value;
    }

    static <X extends Exception> int requireGreaterThanOrEqualsTo(final int value, final int minimum,
                                                                  final Supplier<? extends X> exceptionSupplier)
            throws X {
        if (value >= minimum) {
            return value;
        }
        throw exceptionSupplier.get();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private _Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
