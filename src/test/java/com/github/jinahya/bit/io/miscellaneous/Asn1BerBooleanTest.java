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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.bit.io.miscellaneous._TestUtils.read;
import static com.github.jinahya.bit.io.miscellaneous._TestUtils.write;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class Asn1BerBooleanTest {

    @Test
    void readsAnyNonZeroAsTrue() throws IOException {
        assertEquals(Boolean.TRUE, read(Asn1BerBoolean.INSTANCE, new byte[]{0x01}));
    }

    @Test
    void writesTrueAsDerCanonicalTrue() throws IOException {
        assertArrayEquals(new byte[]{(byte) 0xFF}, write(Asn1BerBoolean.INSTANCE, Boolean.TRUE));
    }
}
