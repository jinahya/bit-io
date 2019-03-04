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

import java.io.OutputStream;

/**
 * An abstract class for testing subclasses of {@link StreamByteOutput}.
 *
 * @param <T> byte output type parameter
 * @param <U> output stream type parameter
 * @see StreamByteInputTest
 */
abstract class StreamByteOutputTest<T extends StreamByteOutput<U>, U extends OutputStream>
        extends AbstractByteOutputTest<T, U> {

    // -----------------------------------------------------------------------------------------------------------------
    StreamByteOutputTest(final Class<T> byteOutputClass, final Class<U> byteTargetClass) {
        super(byteOutputClass, byteTargetClass);
    }
}
