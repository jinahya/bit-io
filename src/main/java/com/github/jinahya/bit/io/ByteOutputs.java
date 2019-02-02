package com.github.jinahya.bit.io;

import java.io.IOException;

/**
 * A class for utilities and constants related to {@link ByteOutput}.
 */
public final class ByteOutputs {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An implementation of {@link ByteOutput} whose {@link ByteOutput#write(int)} simply discard given value.
     */
    private static final class NullByteOutput implements ByteOutput {

        /**
         * {@inheritDoc} The {@code write} method of {@code NullByteOutput} class simply discards given value.
         *
         * @throws IOException {@inheritDoc}
         */
        @Override
        public void write(final int octet) throws IOException {
            // does nothing.
        }
    }

    /**
     * Returns a new {@link ByteOutput} whose {@link ByteOutput#write(int)} } method simply discards given value.
     *
     * @return a {@link ByteOutput} does nothing.
     */
    public static ByteOutput nullByteOutput() {
        return new NullByteOutput();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private ByteOutputs() {
        super();
    }
}
