package com.github.jinahya.bit.io;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

/**
 * A class for converting an instance of {@link ByteOutput} to an instance of {@link BitOutput}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see BitInputConverter
 */
class BitOutputConverter implements ArgumentConverter {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public Object convert(final Object source, final ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof ByteOutput)) {
            throw new ArgumentConversionException("can't convert " + source);
        }
        return new DefaultBitOutput((ByteOutput) source);
    }
}
