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
 * An abstract class for testing subclasses of {@link DefaultBitOutput}.
 *
 * @param <T> bit output type parameter.
 * @param <U> byte output type parameter.
 */
@ExtendWith({MockitoExtension.class})
public abstract class DefaultBitOutputTest<T extends DefaultBitOutput<U>, U extends ByteOutput>
        extends AbstractBitOutputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param bitOutputClass  bit output class.
     * @param byteOutputClass byte output class.
     */
    public DefaultBitOutputTest(final Class<T> bitOutputClass, final Class<U> byteOutputClass) {
        super(bitOutputClass);
        this.byteOutputClass = requireNonNull(byteOutputClass, "byteOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link DefaultBitOutput#getDelegate()}.
     */
    @Test
    public void testGetDelegate() {
        final U delegate = bitOutput.getDelegate();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link DefaultBitOutput#setDelegate(ByteOutput)}.
     */
    @Test
    public void testSetDelegate() {
        bitOutput.setDelegate(null);
        bitOutput.setDelegate(byteOutputMock);
    }

    /**
     * Tests {@link DefaultBitOutput#delegate(ByteOutput)}.
     */
    @Test
    public void testDelegate() {
        assertEquals(bitOutput, bitOutput.delegate(null));
        assertEquals(bitOutput, bitOutput.delegate(byteOutputMock));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The byte output class.
     */
    protected final Class<U> byteOutputClass;

    @Mock
    protected U byteOutputMock;
}
