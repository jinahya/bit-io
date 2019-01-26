package com.github.jinahya.bit.io;

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Objects;

@ExtendWith({WeldJunit5Extension.class})
abstract class ByteInputParameterResolver<T extends ByteInput> implements ParameterResolver {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    public ByteInputParameterResolver(final Class<T> byteInputClass) {
        super();
        this.byteInputClass = Objects.requireNonNull(byteInputClass, "byteInputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        logger.debug("supportsParameter({}, {})", parameterContext, extensionContext);
        final Parameter parameter = parameterContext.getParameter();
        logger.debug("parameterContext.parameter: {}", parameter);
        final Class<?> type = parameter.getType();
        logger.debug("parameterContext.parameter.type: {}", type);
        final Executable executable = parameterContext.getDeclaringExecutable();
        logger.debug("parameterContext.declaringExecutable: {}", executable);
        return true;
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        logger.debug("resolveParameter({}, {}", parameterContext, extensionContext);
        logger.debug("byteInputInstance: {}", byteInputInstance);
        return byteInputInstance.select(byteInputClass).get();
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<T> byteInputClass;

    @Typed
    @Inject
    Instance<ByteInput> byteInputInstance;
}
