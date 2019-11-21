package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
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
 * Constants for bit-io.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class BitIoConstants {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The minimum value for {@code size} paramter.
     */
    static final int MIN_SIZE = 1;

    // ------------------------------------------------------------------------------------------------------------ Byte
    static final int SIZE_EXPONENT_BYTE = 3;

    @Deprecated
    static final int SIZE_BYTE = 1 << SIZE_EXPONENT_BYTE;

    @Deprecated
    static final int BYTES_BYTE = 1; // SIZE_BYTE / SIZE_BYTE

    // ----------------------------------------------------------------------------------------------------------- Short
    static final int SIZE_EXPONENT_SHORT = 4;

    @Deprecated
    static final int SIZE_SHORT = 1 << SIZE_EXPONENT_SHORT;

    @Deprecated
    static final int BYTES_SHORT = SIZE_SHORT / SIZE_BYTE;

    // --------------------------------------------------------------------------------------------------------- Integer
    static final int SIZE_EXPONENT_INTEGER = 5;

    @Deprecated
    static final int SIZE_INTEGER = 1 << SIZE_EXPONENT_INTEGER;

    @Deprecated
    static final int BYTES_INTEGER = SIZE_INTEGER / SIZE_BYTE;

    // ------------------------------------------------------------------------------------------------------------ Long
    static final int SIZE_EXPONENT_LONG = 6;

    @Deprecated
    static final int SIZE_LONG = 1 << SIZE_EXPONENT_LONG;

    @Deprecated
    static final int BYTES_LONG = SIZE_LONG / SIZE_BYTE;

    // ------------------------------------------------------------------------------------------------------- Character
    static final int SIZE_EXPONENT_CHAR = SIZE_EXPONENT_SHORT;

    @Deprecated
    static final int SIZE_CHAR = 1 << SIZE_EXPONENT_CHAR;

    @Deprecated
    static final int BYTES_CHAR = SIZE_CHAR / SIZE_BYTE;

    // -----------------------------------------------------------------------------------------------------------------
    private BitIoConstants() {
        super();
    }
}
