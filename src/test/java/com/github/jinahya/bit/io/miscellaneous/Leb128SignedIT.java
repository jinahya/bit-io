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

import static com.github.jinahya.bit.io.miscellaneous.Leb128TestUtils.readSigned;
import static com.github.jinahya.bit.io.miscellaneous.Leb128TestUtils.writeSigned;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class Leb128SignedIT
        extends OfficialDocumentITSupport {

    @Test
    void verifiesSignedLeb128ExampleFromWebAssemblySpec() throws Exception {
        final String document = downloadText("https://webassembly.github.io/spec/core/binary/values.html", 256 * 1024);
        assertTrue(document.contains("LEB128"), "downloaded WebAssembly binary values page");

        assertArrayEquals(hex("c0bb78"), writeSigned(-123456L));
        assertEquals(-123456L, readSigned(hex("c0bb78")));
    }
}
