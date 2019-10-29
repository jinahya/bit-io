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
import java.io.DataOutputStream;
import java.io.IOException;

import static java.nio.ByteBuffer.allocate;

@Slf4j
class ByteOutputProducer {

    // ----------------------------------------------------------------------------------------------------------- array

    /**
     * Produces an instance of {@link ArrayByteOutput} for specified injection point.
     *
     * @param injectionPoint the injection point
     * @return an instance of {@link ArrayByteOutput}.
     */
    @Typed
    @Produces
    ArrayByteOutput produceArrayByteOutput(final InjectionPoint injectionPoint) {
        return new ArrayByteOutput(null) {
            @Override
            public void write(final int value) throws IOException {
                if (getTarget() == null) {
                    setTarget(new byte[1]);
                    setIndex(0);
                }
                super.write(value);
                if (getIndex() == getTarget().length) {
                    setIndex(0);
                }
            }
        };
    }

    /**
     * Disposes given instance of {@link ArrayByteOutput} produced via {@link #produceArrayByteOutput(InjectionPoint)}.
     *
     * @param byteOutput the instance of {@link ArrayByteOutput} to dispose.
     */
    void disposeArrayByteOutput(@Typed @Disposes final ArrayByteOutput byteOutput) {
    }

    // -----------------------------------------------------------------------------------------------------------buffer
    @Typed
    @Produces
    BufferByteOutput produceBufferByteOutput(final InjectionPoint injectionPoint) {
        return new BufferByteOutput(null) {
            @Override
            public void write(final int value) throws IOException {
                if (getTarget() == null) {
                    setTarget(allocate(1)); // position: zero, limit: capacity
                }
                super.write(value);
                if (!getTarget().hasRemaining()) {
                    getTarget().clear();
                }
            }
        };
    }

    void disposeBufferByteOutput(@Typed @Disposes final BufferByteOutput byteOutput) {
    }

    // ------------------------------------------------------------------------------------------------------------ data
    @Typed
    @Produces
    DataByteOutput produceDataByteOutput(final InjectionPoint injectionPoint) {
        return new DataByteOutput(new DataOutputStream(new BlackOutputStream()));
    }

    void disposeDataByteOutput(@Typed @Disposes final DataByteOutput byteOutput) {
    }

    // ---------------------------------------------------------------------------------------------------------- stream
    @Typed
    @Produces
    StreamByteOutput produceStreamByteOutput(final InjectionPoint injectionPoint) {
        return new StreamByteOutput(new BlackOutputStream());
    }

    void disposeStreamByteOutput(@Typed @Disposes final StreamByteOutput byteOutput) {
    }
}
