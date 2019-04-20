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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.invoke.MethodHandles;

class ByteInputProducer {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // ----------------------------------------------------------------------------------------------------------- array
    @Typed
    @Produces
    ArrayByteInput produceArrayByteInput(final InjectionPoint injectionPoint) {
        return new WhiteArrayByteInput();
    }

    void disposeArrayByteInput(@Typed @Disposes final ArrayByteInput byteInput) {
    }

    // ---------------------------------------------------------------------------------------------------------- buffer
    @Typed
    @Produces
    WhiteBufferByteInput produceBufferByteInput(final InjectionPoint injectionPoint) {
        return new WhiteBufferByteInput();
    }

    void disposeBufferByteInput(@Disposes @Typed final WhiteBufferByteInput byteInput) {
    }

    // ------------------------------------------------------------------------------------------------------------ data
    @Typed
    @Produces
    WhiteDataByteInput produceDataByteInput(final InjectionPoint injectionPoint) {
        return new WhiteDataByteInput();
    }

    void disposeDataByteInput(@Disposes @Typed final WhiteDataByteInput byteInput) {
    }

    // ---------------------------------------------------------------------------------------------------------- stream
    @Typed
    @Produces
    WhiteStreamByteInput produceStreamByteInput(final InjectionPoint injectionPoint) {
        return new WhiteStreamByteInput();
    }

    void disposeStreamByteInput(@Disposes @Typed final WhiteStreamByteInput byteInput) {
    }
}
