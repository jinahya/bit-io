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

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class BitWritablesTest {

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testCachedBitWriterFor() throws IOException {
        final BitWriter<User> expected = BitWritables.cachedBitWriterFor(User.class);
        assertNotNull(expected);
        final BitWriter<User> actual = BitWritables.cachedBitWriterFor(User.class);
        assertNotNull(actual);
        assertSame(expected, actual);
        actual.write(new DefaultBitOutput(new StreamByteOutput(new BlackOutputStream())), new User());
    }
}
