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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * An abstract class for testing subclasses of {@link DefaultBitInput}.
 *
 * @param <T> bit input type parameter
 * @param <U> byte input type parameter.
 */
@ExtendWith({MockitoExtension.class})
public abstract class DefaultBitInputTest<T extends DefaultBitInput<U>, U extends ByteInput>
        extends AbstractBitInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    public DefaultBitInputTest(final Class<T> bitInputClass, final Class<U> byteInputClass) {
        super(bitInputClass);
        this.byteInputClass = requireNonNull(byteInputClass, "byteInputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link DefaultBitInput#getDelegate()}.
     */
    @Test
    public void testGetDelegate() {
        final U delegate = bitInput.getDelegate();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link DefaultBitInput#setDelegate(ByteInput)}.
     */
    @Test
    public void testSetDelegate() {
        bitInput.setDelegate(null);
        bitInput.setDelegate(byteInputMock);
    }

    /**
     * Tests {@link DefaultBitInput#delegate(ByteInput)}.
     */
    @Test
    public void testDelegate() {
        assertEquals(bitInput, bitInput.delegate(null));
        assertEquals(bitInput, bitInput.delegate(byteInputMock));
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected final Class<U> byteInputClass;

    @Mock
    protected U byteInputMock;
}
