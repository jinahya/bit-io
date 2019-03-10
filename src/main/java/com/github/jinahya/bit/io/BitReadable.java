package com.github.jinahya.bit.io;

import java.io.IOException;

/**
 * An interface for objects which each can reads values from a bit input.
 */
public interface BitReadable {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads values from given bit input.
     *
     * @param bitInput the bit input from which values are read.
     * @throws IOException if an I/O error occurs.
     */
    void read(BitInput bitInput) throws IOException;
}
