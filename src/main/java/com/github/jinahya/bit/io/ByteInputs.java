package com.github.jinahya.bit.io;

import java.io.EOFException;
import java.io.IOException;

/**
 * A class for utilities and constants related to {@link ByteInput}.
 */
public final class ByteInputs {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An implementation of {@link ByteInput} whose {@link ByteInput#read()} method always throws an {@link
     * EOFException}.
     */
    public static class NullByteInput implements ByteInput {

        /**
         * {@inheritDoc} The {@code read} method of {@code NullByteInput} class always throws an {@link EOFException}.
         *
         * @return {@inheritDoc}
         * @throws IOException {@inheritDoc}
         */
        @Override
        public int read() throws IOException {
            throw new EOFException("an instance of " + getClass());
        }
    }

    /**
     * Returns a new {@link ByteInput} whose {@link ByteInput#read()} method always throws an {@link EOFException}.
     *
     * @return a {@link ByteInput} reached to EOF.
     */
    public static ByteInput nullByteInput() {
        return new NullByteInput();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private ByteInputs() {
        super();
    }
}
