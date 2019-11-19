package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.RandomAccessFile;

class RandomAccessFileByteInput extends AbstractByteInput<RandomAccessFile> {

    // -----------------------------------------------------------------------------------------------------------------
    public RandomAccessFileByteInput(final RandomAccessFile source) {
        super(source);
    }

    // -----------------------------------------------------------------------------------------------------------------
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
