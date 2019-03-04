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

import java.io.IOException;

/**
 * A class for utilities and constants related to {@link ByteOutput}.
 */
public final class ByteOutputs {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An implementation of {@link ByteOutput} whose {@link ByteOutput#write(int)} simply discard given value.
     */
    private static final class NullByteOutput implements ByteOutput {

        /**
         * {@inheritDoc} The {@code write} method of {@code NullByteOutput} class simply discards given value.
         *
         * @throws IOException {@inheritDoc}
         */
        @Override
        public void write(final int octet) throws IOException {
            // does nothing.
        }
    }

    /**
     * Returns a new {@link ByteOutput} whose {@link ByteOutput#write(int)} method simply discards given value.
     *
     * @return a {@link ByteOutput} does nothing.
     */
    public static ByteOutput nullByteOutput() {
        return new NullByteOutput();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private ByteOutputs() {
        super();
    }
}
