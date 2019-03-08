package com.github.jinahya.bit.io;

import java.io.IOException;

public interface BitReadable<T extends BitReadable<T>> {

    // -----------------------------------------------------------------------------------------------------------------
    T read(BitInput bitInput) throws IOException;
}
