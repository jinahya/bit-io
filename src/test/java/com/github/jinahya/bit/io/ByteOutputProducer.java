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

class ByteOutputProducer {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
        return new BlackArrayByteOutput();
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
    BlackBufferByteOutput produceBufferByteOutput(final InjectionPoint injectionPoint) {
        return new BlackBufferByteOutput();
    }

    void disposeBufferByteOutput(@Typed @Disposes final BlackBufferByteOutput byteOutput) {
    }

    // ------------------------------------------------------------------------------------------------------------ data
    @Typed
    @Produces
    BlackDataByteOutput produceDataByteOutput(final InjectionPoint injectionPoint) {
        return new BlackDataByteOutput();
    }

    void disposeDataByteOutput(@Typed @Disposes final BlackDataByteOutput byteOutput) {
    }

    // ---------------------------------------------------------------------------------------------------------- stream
    @Typed
    @Produces
    BlackStreamByteOutput produceStreamByteOutput(final InjectionPoint injectionPoint) {
        return new BlackStreamByteOutput();
    }

    void disposeStreamByteOutput(@Typed @Disposes final BlackStreamByteOutput byteOutput) {
    }
}
