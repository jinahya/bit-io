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

import static java.nio.ByteBuffer.allocate;
import static java.util.concurrent.ThreadLocalRandom.current;

class WhiteBufferByteInput extends BufferByteInput<ByteBuffer> {

    // -----------------------------------------------------------------------------------------------------------------
    WhiteBufferByteInput() {
        super(null);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public int read() throws IOException {
        if (source == null) {
            source = allocate(1);
            source.position(source.limit());
        }
        if (!source.hasRemaining()) {
            source.clear(); // position -> zero, limit -> capacity
            current().nextBytes(source.array());
        }
        return super.read();
    }
}
