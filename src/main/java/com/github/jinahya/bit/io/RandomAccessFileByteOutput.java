package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A byte input which writes bytes to a random access file.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see RandomAccessFileByteInput
 */
class RandomAccessFileByteOutput extends AbstractByteOutput<RandomAccessFile> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified random access file.
     *
     * @param target the random access to which bytes are written; {@code null} if it's supposed to be lazily
     *               initialized and set.
     * @see #getTarget()
     * @see #setTarget(RandomAccessFile)
     */
    public RandomAccessFileByteOutput(final RandomAccessFile target) {
        super(target);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc} The {@code write(int)} method of {@code RandomAccessFileByteOutput} class invokes {@link
     * RandomAccessFile#writeByte(int)}, on what {@link #getTarget()} method returns, with specified value.
     *
     * @param value {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void write(final int value) throws IOException {
        getTarget().writeByte(value);
    }

    // ---------------------------------------------------------------------------------------------------------- target
    @Override
    protected RandomAccessFile getTarget() {
        return super.getTarget();
    }

    @Override
    protected void setTarget(final RandomAccessFile target) {
        super.setTarget(target);
    }
}
