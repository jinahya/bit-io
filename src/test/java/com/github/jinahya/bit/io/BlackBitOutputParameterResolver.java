package com.github.jinahya.bit.io;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class BlackBitOutputParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return BitOutput.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return new BlackBitOutput();
    }
}
