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

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.DataInputStream;
import java.io.IOException;

import static java.nio.ByteBuffer.allocate;

@Slf4j
class ByteInputProducer {

    // ----------------------------------------------------------------------------------------------------------- array
    @Typed
    @Produces
    ArrayByteInput produceArrayByteInput(final InjectionPoint injectionPoint) {
        return new ArrayByteInput(null) {
            @Override
            public int read() throws IOException {
                if (getSource() == null) {
                    setSource(new byte[1]);
                    setIndex(getSource().length);
                }
                if (getIndex() == getSource().length) {
                    setIndex(0);
                }
                return super.read();
            }
        };
    }

    void disposeArrayByteInput(@Typed @Disposes final ArrayByteInput byteInput) {
        // does nothing.
    }

    // ---------------------------------------------------------------------------------------------------------- buffer
    @Typed
    @Produces
    BufferByteInput produceBufferByteInput(final InjectionPoint injectionPoint) {
        return new BufferByteInput(null) {
            @Override
            public int read() throws IOException {
                if (getSource() == null) {
                    setSource(allocate(1));
                    getSource().position(getSource().limit());
                }
                if (!getSource().hasRemaining()) {
                    getSource().clear(); // position -> zero, limit -> capacity
                }
                return super.read();
            }
        };
    }

    void disposeBufferByteInput(@Disposes @Typed final BufferByteInput byteInput) {
        // does nothing.
    }

    // ------------------------------------------------------------------------------------------------------------ data
    @Typed
    @Produces
    DataByteInput produceDataByteInput(final InjectionPoint injectionPoint) {
        return new DataByteInput(new DataInputStream(new WhiteInputStream()));
    }

    void disposeDataByteInput(@Disposes @Typed final DataByteInput byteInput) {
        // does nothing.
    }

    // ---------------------------------------------------------------------------------------------------------- stream
    @Typed
    @Produces
    StreamByteInput produceStreamByteInput(final InjectionPoint injectionPoint) {
        return new StreamByteInput(new WhiteInputStream());
    }

    void disposeStreamByteInput(@Disposes @Typed final StreamByteInput byteInput) {
        // does nothing.
    }
}
