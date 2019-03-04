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

import java.io.EOFException;
import java.io.IOException;

/**
 * A class for utilities and constants related to {@link ByteInput}.
 */
public final class ByteInputs {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An implementation of {@link ByteInput} whose {@link ByteInput#read()} method always throws an {@link
     * EOFException}.
     */
    private static class NullByteInput implements ByteInput {

        /**
         * {@inheritDoc} The {@code read} method of {@code NullByteInput} class always throws an {@link EOFException}.
         *
         * @return {@inheritDoc}
         * @throws IOException {@inheritDoc}
         */
        @Override
        public int read() throws IOException {
            throw new EOFException("an instance of " + getClass());
        }
    }

    /**
     * Returns a new {@link ByteInput} whose {@link ByteInput#read()} method always throws an {@link EOFException}.
     *
     * @return a {@link ByteInput} reached to the EOF.
     */
    public static ByteInput nullByteInput() {
        return new NullByteInput();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private ByteInputs() {
        super();
    }
}
