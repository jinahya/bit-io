package com.github.jinahya.bit.io;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

class ExtendedRandomAccessFileByteOutput extends RandomAccessFileByteOutput {

    // -----------------------------------------------------------------------------------------------------------------
    ExtendedRandomAccessFileByteOutput(final RandomAccessFile target) {
        super(target);
    }

    ExtendedRandomAccessFileByteOutput(final Path file) throws FileNotFoundException {
        this(new RandomAccessFile(file.toFile(), "rw"));
        this.file = file;
    }

    // -----------------------------------------------------------------------------------------------------------------
    Path file;
}
