package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream whose {@link OutputStream#write(int)} does nothing.
 *
 * @see WhiteInputStream
 * @deprecated Use {@link OutputStream#nullOutputStream()}
 */
@Deprecated
final class BlackOutputStream extends OutputStream {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void write(final int b) throws IOException {
        // does nothing.
    }
}
