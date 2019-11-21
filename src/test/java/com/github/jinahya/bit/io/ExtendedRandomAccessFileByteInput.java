package com.github.jinahya.bit.io;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

class ExtendedRandomAccessFileByteInput extends RandomAccessFileByteInput {

    // -----------------------------------------------------------------------------------------------------------------
    ExtendedRandomAccessFileByteInput(final RandomAccessFile source) {
        super(source);
    }

    ExtendedRandomAccessFileByteInput(final Path file) throws FileNotFoundException {
        this(new RandomAccessFile(file.toFile(), "r"));
        this.file = file;
    }

    // -----------------------------------------------------------------------------------------------------------------
    Path file;
}
