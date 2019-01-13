package com.github.jinahya.bit.io;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Objects;

abstract class ByteSourceParameterResolver<T> implements ParameterResolver {

    // -----------------------------------------------------------------------------------------------------------------
    ByteSourceParameterResolver(final Class<T> byteSourceClass) {
        super();
        this.byteSourceClass = Objects.requireNonNull(byteSourceClass, "byteSourceClass is null");
    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == byteSourceClass;
        //return byteSourceClass.isAssignableFrom(parameterContext.getParameter().getType());
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<T> byteSourceClass;
}
