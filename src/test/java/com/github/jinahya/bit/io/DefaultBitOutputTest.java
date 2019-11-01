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

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * A class for unit-testing {@link DefaultBitOutput} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see DefaultBitInputTest
 */
@ExtendWith({MockitoExtension.class, WeldJunit5Extension.class})
class DefaultBitOutputTest extends AbstractBitOutputTest<DefaultBitOutput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    DefaultBitOutputTest() {
        super(DefaultBitOutput.class);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link DefaultBitOutput#getDelegate()}.
     */
    @Test
    void testGetDelegate() {
        final ByteOutput delegate = bitOutput.getDelegate();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Tests {@link DefaultBitOutput#setDelegate(ByteOutput)}.
     */
    @Test
    void testSetDelegate() {
        bitOutput.setDelegate(null);
        bitOutput.setDelegate(byteOutputMock);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Mock
    private ByteOutput byteOutputMock;
}
