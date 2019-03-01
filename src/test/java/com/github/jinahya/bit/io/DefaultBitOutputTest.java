package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * An abstract class for testing subclasses of {@link DefaultBitOutput}.
 *
 * @param <T> bit output type parameter.
 * @param <U> byte output type parameter.
 */
@ExtendWith({MockitoExtension.class})
public abstract class DefaultBitOutputTest<T extends DefaultBitOutput<U>, U extends ByteOutput>
        extends AbstractBitOutputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param bitOutputClass  bit output class.
     * @param byteOutputClass byte output class.
     */
    public DefaultBitOutputTest(final Class<T> bitOutputClass, final Class<U> byteOutputClass) {
        super(bitOutputClass);
        this.byteOutputClass = requireNonNull(byteOutputClass, "byteOutputClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetDelegate() {
        final U delegate = bitOutput.getDelegate();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testSetDelegate() {
        bitOutput.setDelegate(null);
        bitOutput.setDelegate(byteOutputMock);
    }

    @Test
    public void testDelegate() {
        assertEquals(bitOutput, bitOutput.delegate(null));
        assertEquals(bitOutput, bitOutput.delegate(byteOutputMock));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The byte output class.
     */
    protected final Class<U> byteOutputClass;

    @Mock
    protected U byteOutputMock;
}
