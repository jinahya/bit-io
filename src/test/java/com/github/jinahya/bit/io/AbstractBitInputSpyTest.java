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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@Slf4j
public class AbstractBitInputSpyTest {

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    void stubRead() throws IOException {
        when(bitInput.read()).thenReturn(current().nextInt(0, 256));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link BitInput#readByte(boolean, int)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(8)
    public void testReadByte() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Byte.SIZE + (unsigned ? 0 : 1));
        final byte value = bitInput.readByte(unsigned, size);
        if (unsigned) {
            assertTrue(value >= 0);
        }
    }

    /**
     * Tests {@link BitInput#readShort(boolean, int)} method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @RepeatedTest(8)
    public void testReadShort() throws IOException {
        final boolean unsigned = current().nextBoolean();
        final int size = current().nextInt(1, Short.SIZE + (unsigned ? 0 : 1));
        final short value = bitInput.readShort(unsigned, size);
        if (unsigned) {
            assertTrue(value >= 0);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Spy
    private AbstractBitInput bitInput;
}
