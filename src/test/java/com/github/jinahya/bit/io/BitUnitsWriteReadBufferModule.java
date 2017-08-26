/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
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
 */
package com.github.jinahya.bit.io;

import com.google.inject.AbstractModule;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.wrap;

class BitUnitsWriteReadBufferModule extends AbstractModule {

    @Override
    protected void configure() {
        final byte[] array = new byte[1048576 * 8];
        final ByteBuffer wbuffer = wrap(array);
        final ByteBuffer rbuffer = wrap(array);
        bind(BitOutput.class).toProvider(
                () -> new DefaultBitOutput<>(new BufferByteOutput(wbuffer)));
        bind(BitInput.class).toProvider(
                () -> new DefaultBitInput<>(new BufferByteInput(rbuffer)));
    }
}
