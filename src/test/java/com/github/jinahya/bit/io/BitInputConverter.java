package com.github.jinahya.bit.io;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

/**
 * A class for converting an instance of {@link ByteInput} to an instance of {@link BitInput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitOutputConverter
 */
class BitInputConverter implements ArgumentConverter {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public Object convert(final Object source, final ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof ByteInput)) {
            throw new ArgumentConversionException("can't convert " + source);
        }
        return new DefaultBitInput((ByteInput) source);
    }
}
