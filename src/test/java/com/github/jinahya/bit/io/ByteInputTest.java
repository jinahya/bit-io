package com.github.jinahya.bit.io;

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

@ExtendWith({WeldJunit5Extension.class})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ByteInputTest<T extends ByteInput> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    ByteInputTest(final Class<T> byteInputClass) {
        super();
        this.byteInputClass = Objects.requireNonNull(byteInputClass, "byteInputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @BeforeEach
    //@BeforeAll // seems not work with PER_CLASS
    void selectByteInput() {
        byteInput = byteInputInstance.select(byteInputClass).get();
        logger.debug("byteInput: {}", byteInput);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testRead() throws IOException {
        final int octet = byteInput.read();
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<T> byteInputClass;

    @Typed
    @Inject
    private Instance<ByteInput> byteInputInstance;

    T byteInput;
}
