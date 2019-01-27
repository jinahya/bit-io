package com.github.jinahya.bit.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.invoke.MethodHandles;

public class ByteInputProducer {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // ----------------------------------------------------------------------------------------------------------- array
    @Typed
    @Produces
    public ArrayByteInput produceArrayByteInput(final InjectionPoint injectionPoint) {
        logger.debug("produceArrayByteInput({})", injectionPoint);
        return new WhiteArrayByteInput();
    }

    public void disposeArrayByteInput(@Typed @Disposes final ArrayByteInput byteInput) {
        logger.debug("disposeArrayByteInput({})", byteInput);
        // empty, so far.
    }

    // ---------------------------------------------------------------------------------------------------------- buffer
    @Typed
    @Produces
    public WhiteBufferByteInput produceBufferByteInput(final InjectionPoint injectionPoint) {
        logger.debug("productBufferByteInput({})", injectionPoint);
        return new WhiteBufferByteInput();
    }

    public void disposeBufferByteInput(@Disposes @Typed final WhiteBufferByteInput byteInput) {
        logger.debug("disposeBufferByteInput({})", byteInput);
    }

    // ------------------------------------------------------------------------------------------------------------ data
    @Typed
    @Produces
    public WhiteDataByteInput produceDataByteInput(final InjectionPoint injectionPoint) {
        logger.debug("productBufferByteInput({})", injectionPoint);
        return new WhiteDataByteInput();
    }

    public void disposeDataByteInput(@Disposes @Typed final WhiteDataByteInput byteInput) {
        logger.debug("disposeBufferByteInput({})", byteInput);
    }

    // ---------------------------------------------------------------------------------------------------------- stream
    @Typed
    @Produces
    public WhiteStreamByteInput produceStreamByteInput(final InjectionPoint injectionPoint) {
        logger.debug("productBufferByteInput({})", injectionPoint);
        return new WhiteStreamByteInput();
    }

    public void disposeStreamByteInput(@Disposes @Typed final WhiteStreamByteInput byteInput) {
        logger.debug("disposeBufferByteInput({})", byteInput);
    }
}
