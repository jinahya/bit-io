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

final class RiceGolombCodeConstants {

    static final int MIN_PARAMETER = 0;

    static final int MAX_PARAMETER = Long.SIZE - 2;

    static final long MIN_SIGNED_VALUE = -(1L << (Long.SIZE - 2));

    static final long MAX_SIGNED_VALUE = Long.MAX_VALUE >> 1;

    private RiceGolombCodeConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
