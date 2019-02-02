package com.github.jinahya.bit.io;

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
