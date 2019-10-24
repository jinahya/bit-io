package com.github.jinahya.bit.io;

import java.io.IOException;

/**
 * An interface for object whose values are written to a bit output.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public interface BitWritable {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Write values to specified bit output.
     *
     * @param output the bit output to which values are written.
     * @throws IOException if an I/O error occurs.
     */
    void write(BitOutput output) throws IOException;
}
