package com.github.jinahya.bit.io;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

abstract class ByteInputParameterResolver<T extends ByteInput> implements ParameterResolver {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    ByteInputParameterResolver(final Class<T> byteInputClass) {
        super();
        this.byteInputClass = Objects.requireNonNull(byteInputClass, "byteInputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        //return parameterContext.getParameter().getType() == byteInputClass;
        //return byteInputClass.isAssignableFrom(parameterContext.getParameter().getType());
        //logger.debug("-> type: {}", parameterContext.getParameter().getType());
        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<T> byteInputClass;
}
