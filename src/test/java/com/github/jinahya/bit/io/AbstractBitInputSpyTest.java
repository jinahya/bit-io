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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class AbstractBitInputSpyTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void stubRead() throws IOException {
        when(bitInput.read()).thenReturn(current().nextInt(0, 256));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link BitInput#readByte(boolean, int)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testReadByte() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        final byte value = bitInput.readByte(unsigned, size);
        if (unsigned) {
            assertTrue(value >= 0);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Spy
    private AbstractBitInput bitInput;
}
