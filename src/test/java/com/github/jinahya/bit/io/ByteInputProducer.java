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

    // -----------------------------------------------------------------------------------------------------------------
//    @Produces
//    @Typed
//    public ByteInput produceByteInput(final InjectionPoint injectionPoint) {
//        logger.debug("produceByteInput({})", injectionPoint);
//        {
//            final Annotated annotated = injectionPoint.getAnnotated();
//            logger.debug("injection.annotated: {}", annotated);
//        }
//        {
//            final Bean bean = injectionPoint.getBean();
//            logger.debug("injectionPoint.bean: {}", bean);
//            ofNullable(bean).ifPresent(v -> {
//                logger.debug("injectionPoint.bean.alternative: {}", v.isAlternative());
//                logger.debug("injectionPoint.bean.beanClass: {}", v.getBeanClass());
//                logger.debug("injectionPoint.bean.injectionPoints: {}", v.getInjectionPoints());
//                logger.debug("injectionPoint.bean.nullable: {}", v.isNullable());
//            });
//        }
//        {
//            final Type type = injectionPoint.getType();
//            logger.debug("injectionPoint.type: {}", type);
//        }
//        {
//            final Member member = injectionPoint.getMember();
//            logger.debug("injectionPoint.member: {}", member);
//        }
//        {
//            final Set<Annotation> qualifiers = injectionPoint.getQualifiers();
//            logger.debug("injectionPoint.qualifiers: {},", qualifiers);
//        }
//        return null;
//    }

//    public void disposeByteInput(@Disposes @Typed final ByteInput byteInput) {
//        // empty, so far.
//    }

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
//    public void disposeBufferByteInput(@Disposes @Typed final BufferByteInput byteInput) {
//        // empty, so far.
//    }
}
