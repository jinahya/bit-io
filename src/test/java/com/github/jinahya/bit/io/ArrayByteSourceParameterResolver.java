package com.github.jinahya.bit.io;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import static java.util.concurrent.ThreadLocalRandom.current;

class ArrayByteSourceParameterResolver extends ByteSourceParameterResolver<byte[]> {

    // -----------------------------------------------------------------------------------------------------------------
    ArrayByteSourceParameterResolver() {
        super(byte[].class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return current().nextBoolean() ? null : new byte[0];
    }
}
