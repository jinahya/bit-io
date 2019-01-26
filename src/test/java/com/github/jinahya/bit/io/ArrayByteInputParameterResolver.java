package com.github.jinahya.bit.io;

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({WeldJunit5Extension.class})
class ArrayByteInputParameterResolver extends AbstractByteInputParameterResolver<ArrayByteInput, byte[]> {

    // -----------------------------------------------------------------------------------------------------------------
    ArrayByteInputParameterResolver() {
        super(ArrayByteInput.class, byte[].class);
    }
}
