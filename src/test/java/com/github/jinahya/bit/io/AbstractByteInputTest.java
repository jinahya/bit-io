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

/**
 * An abstract class for testing subclasses of {@link AbstractByteInput}.
 *
 * @param <T> byte input type parameter
 * @param <U> byte source type parameter
 * @see AbstractByteOutputTest
 */
@ExtendWith({MockitoExtension.class})
public abstract class AbstractByteInputTest<T extends AbstractByteInput<U>, U> extends ByteInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified byte input class and byte source class.
     *
     * @param byteInputClass  the byte input class to test.
     * @param byteSourceClass the byte source class of the byte input class.
     */
    public AbstractByteInputTest(final Class<T> byteInputClass, final Class<U> byteSourceClass) {
        super(byteInputClass);
        this.byteSourceClass = requireNonNull(byteSourceClass, "byteSourceClass is null");
    }

    // ---------------------------------------------------------------------------------------------------------- source

    /**
     * Tests {@link AbstractByteInput#getSource()}.
     */
    @Test
    public void testGetSource() {
        final U source = byteInput.getSource();
    }

    /**
     * Tests {@link AbstractByteInput#setSource(Object)}. The {@code testSetSource} method of {@code
     * AbstractByteInputTest} class invokes {@link AbstractByteInput#setSource(Object)} twice, with {@code null} and a
     * {@code mock}, respectively.
     */
    @Test
    public void testSetSource() {
        byteInput.setSource(null);
        byteInput.setSource(byteSourceMock);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The type of byte source.
     */
    protected final Class<U> byteSourceClass;

    @Mock
    private U byteSourceMock;
}
