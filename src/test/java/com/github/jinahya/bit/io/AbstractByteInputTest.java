package com.github.jinahya.bit.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.Mockito.mock;

@ExtendWith({MockitoExtension.class})
abstract class AbstractByteInputTest<T extends AbstractByteInput<U>, U> extends ByteInputTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    AbstractByteInputTest(final Class<T> byteInputClass, final Class<U> sourceClass) {
        super(byteInputClass);
        this.sourceClass = Objects.requireNonNull(sourceClass, "sourceClass is null");
    }

    // ---------------------------------------------------------------------------------------------------------- source
    @Test
    public void testGetSource() {
        final U source = byteInput.getSource();
    }

    @Test
    public void testSetSource() {
        byteInput.setSource(null);
        byteInput.setSource(sourceMock);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The type of byte source.
     */
    protected final Class<U> sourceClass;

    @Mock
    protected U sourceMock;
}
