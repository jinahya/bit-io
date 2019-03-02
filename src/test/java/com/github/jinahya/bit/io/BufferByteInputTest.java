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

import java.nio.ByteBuffer;

/**
 * An abstract class for testing subclasses of {@link BufferByteInput}.
 *
 * @param <T> byte input type parameter
 * @param <U> byte buffer type paramter
 * @see BufferByteOutputTest
 */
abstract class BufferByteInputTest<T extends BufferByteInput<U>, U extends ByteBuffer>
        extends AbstractByteInputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param byteInputClass  a byte input class.
     * @param byteSourceClass a source class.
     */
    BufferByteInputTest(final Class<T> byteInputClass, final Class<U> byteSourceClass) {
        super(byteInputClass, byteSourceClass);
    }
}
