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

final class CborIntConstants {

    static final int ADDITIONAL_1_BYTE = 24;

    static final int ADDITIONAL_2_BYTES = 25;

    static final int ADDITIONAL_4_BYTES = 26;

    static final int ADDITIONAL_8_BYTES = 27;

    static final int ADDITIONAL_MASK = 0x1F;

    static final int ADDITIONAL_DIRECT_MAX = 23;

    static final int MAJOR_NEGATIVE_INTEGER = 1;

    static final int MAJOR_POSITIVE_INTEGER = 0;

    static final int MAJOR_TYPE_SHIFT = 5;

    private CborIntConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
