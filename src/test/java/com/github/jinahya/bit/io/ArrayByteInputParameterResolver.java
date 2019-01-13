package com.github.jinahya.bit.io;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.io.IOException;

class ArrayByteInputParameterResolver extends AbstractByteInputParameterResolver<ArrayByteInput, byte[]> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final int LENGTH = 1024;

    // -----------------------------------------------------------------------------------------------------------------
    ArrayByteInputParameterResolver() {
        super(ArrayByteInput.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        //return new ArrayByteInput(new byte[LENGTH], 0, LENGTH);
        return new ArrayByteInput(null, -1, -1) {
            @Override
            public int read() throws IOException {
                if (source == null) {
                    source = new byte[LENGTH];
                    limit = source.length;
                    index = 0;
                }
                if (index == limit) {
                    index = 0;
                }
                return super.read();
            }
        };
    }
}
