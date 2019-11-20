package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A byte input which reads bytes from a random access file.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see RandomAccessFileByteOutput
 */
class RandomAccessFileByteInput extends AbstractByteInput<RandomAccessFile> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified random access file.
     *
     * @param source the random access file from which bytes are read; {@code null} if it's supposed to be lazily
     *               initialized and set.
     * @see #getSource()
     * @see #setSource(RandomAccessFile)
     */
    public RandomAccessFileByteInput(final RandomAccessFile source) {
        super(source);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code read()} method of {@link RandomAccessFileByteInput} class invokes {@link
     * RandomAccessFile#readUnsignedByte()}, on what {@link #getSource()} method returns, and returns the result.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        return getSource().readUnsignedByte();
    }

    // ---------------------------------------------------------------------------------------------------------- source
    @Override
    protected RandomAccessFile getSource() {
        return super.getSource();
    }

    @Override
    protected void setSource(final RandomAccessFile source) {
        super.setSource(source);
    }
}
