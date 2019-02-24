package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

/**
 * An abstract class for testing subclasses of {@link AbstractByteInput}.
 *
 * @param <T> byte input type parameter
 * @param <U> byte source type parameter
 */
@ExtendWith({MockitoExtension.class})
public abstract class AbstractByteInputTest<T extends AbstractByteInput<U>, U> extends ByteInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     *
     * @param byteInputClass  a byte input class to test.
     * @param byteSourceClass a byte source class of the byte input class.
     */
    public AbstractByteInputTest(final Class<T> byteInputClass, final Class<U> byteSourceClass) {
        super(byteInputClass);
        this.byteSourceClass = Objects.requireNonNull(byteSourceClass, "byteSourceClass is null");
    }

    // ---------------------------------------------------------------------------------------------------------- source

    /**
     * Tests {@link AbstractByteInput#getSource()}.
     */
    @Test
    public void testGetSource() {
        final U source = byteInput.getSource();
    }

    /**
     * Tests {@link AbstractByteInput#setSource(Object)}.
     */
    @Test
    public void testSetSource() {
        byteInput.setSource(null);
        byteInput.setSource(byteSourceMock);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The type of byte source.
     */
    protected final Class<U> byteSourceClass;

    /**
     * A mock of {@link #byteSourceClass}.
     */
    @Mock
    protected U byteSourceMock;
}
