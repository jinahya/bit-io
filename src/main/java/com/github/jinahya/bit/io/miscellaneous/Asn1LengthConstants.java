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

final class Asn1LengthConstants {

    static final int MASK_LONG_FORM = 0x80;

    static final int MASK_LENGTH_OCTETS = 0x7F;

    static final int VALUE_INDEFINITE = 0x80;

    static final int VALUE_RESERVED = 0xFF;

    static final int MAX_LENGTH_OCTETS_FOR_LONG = Long.SIZE / Byte.SIZE;

    private Asn1LengthConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
