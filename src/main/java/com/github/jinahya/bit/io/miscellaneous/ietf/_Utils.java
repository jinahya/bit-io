package com.github.jinahya.bit.io.miscellaneous.ietf;

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

import java.util.Objects;

final class _Utils {

    static <T extends BitInput> T requireNonNullInput(final T input) {
        return Objects.requireNonNull(input, "input is null");
    }

    static <T extends BitOutput> T requireNonNullOutput(final T output) {
        return Objects.requireNonNull(output, "output is null");
    }

    static <T> T requireNonNullValue(final T value) {
        return Objects.requireNonNull(value, "value is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    private _Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
