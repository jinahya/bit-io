package com.github.jinahya.bit.io;

import java.io.IOException;

/**
 * An interface for objects which each can write value to a bit output.
 */
public interface BitWritable {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes values to given bit output.
     *
     * @param bitOutput the bit output to which values are written.
     * @throws IOException if an I/O error occurs.
     */
    void write(BitOutput bitOutput) throws IOException;
}
