package com.github.jinahya.bit.io;

import java.io.IOException;
import java.io.RandomAccessFile;

class RandomAccessFileByteOutput extends AbstractByteOutput<RandomAccessFile> {

    // -----------------------------------------------------------------------------------------------------------------
    public RandomAccessFileByteOutput(final RandomAccessFile target) {
        super(target);
    }

    // -----------------------------------------------------------------------------------------------------------------
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
