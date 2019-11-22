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

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream whose {@link InputStream#read()} always returns zero.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BlackOutputStream
 * @see WhiteByteChannel
 */
@Slf4j
final class WhiteInputStream extends InputStream {

    // -----------------------------------------------------------------------------------------------------------------
    static final InputStream INSTANCE = new WhiteInputStream();

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads the next byte of data from the input stream. The {@code read()} method of {@code WhiteInputStream} class
     * always returns {@code 0}.
     *
     * @return {@code 0}.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        return 0;
    }
}
