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
 * Constants for LEB128 byte layout.
 */
final class Leb128Constants {

    /**
     * The number of payload bits in each LEB128 byte; {@value}.
     */
    static final int SIZE_PAYLOAD = 7;

    /**
     * The mask for payload bits; {@value}.
     */
    static final int MASK_PAYLOAD = 0x7F;

    /**
     * The continuation bit mask; {@value}.
     */
    static final int MASK_CONTINUATION = 0x80;

    /**
     * The sign bit mask inside a payload group; {@value}.
     */
    static final int MASK_SIGN = 0x40;

    // -----------------------------------------------------------------------------------------------------------------

    private Leb128Constants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
