package com.github.jinahya.bit.io.miscellaneous;

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

/**
 * Constants for Exp-Golomb code values representable through {@code long}.
 */
final class ExpGolombConstants {

    /**
     * The minimum signed Exp-Golomb value representable when {@code codeNum <= Long.MAX_VALUE}.
     */
    static final long MIN_SE = -(Long.MAX_VALUE >> 1);

    /**
     * The maximum signed Exp-Golomb value representable when {@code codeNum <= Long.MAX_VALUE}.
     */
    static final long MAX_SE = (Long.MAX_VALUE >>> 1) + 1L;

    // -----------------------------------------------------------------------------------------------------------------

    private ExpGolombConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
