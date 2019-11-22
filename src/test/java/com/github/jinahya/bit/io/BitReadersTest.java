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

import static com.github.jinahya.bit.io.BitReaders.cachedBitReaderFor;
import static com.github.jinahya.bit.io.BitReaders.newBitReaderFor;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A class for unit-testing {@link BitReaders} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class BitReadersTest {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link BitReaders#cachedBitReaderFor(Class)} method.
     */
    @Test
    public void testCachedBitReaderFor() {
        assertThrows(NullPointerException.class, () -> cachedBitReaderFor(null));
        final BitReader<User> expected = cachedBitReaderFor(User.class);
        assertNotNull(expected);
        final BitReader<User> actual = cachedBitReaderFor(User.class);
        assertNotNull(actual);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link BitReaders#newBitReaderFor(Class)} method.
     */
    @Test
    public void testNewBitReaderFor() {
        assertThrows(NullPointerException.class, () -> newBitReaderFor(null));
        final BitReader<User> reader = cachedBitReaderFor(User.class);
        assertNotNull(reader);
    }
}
