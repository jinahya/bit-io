package com.github.jinahya.bit.io;

import java.io.IOException;

/**
 * An interface for object whose values are read from a bit input.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public interface BitReadable {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads values from specified bit input.
     *
     * @param input the bit input from which values are read.
     * @throws IOException if an I/O error occurs.
     */
    void read(BitInput input) throws IOException;
}
