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
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * A readable byte channel whose {@link ReadableByteChannel#read(ByteBuffer)} method just fully fills up given buffer.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BlackByteChannel
 */
class WhiteByteChannel implements ReadableByteChannel {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read(final ByteBuffer dst) throws IOException {
        int r;
        for (r = 0; dst.hasRemaining(); r++) {
            dst.put((byte) current().nextInt());
        }
        return r;
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void close() throws IOException {
        // empty
    }
}
