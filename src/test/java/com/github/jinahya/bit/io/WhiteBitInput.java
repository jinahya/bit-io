package com.github.jinahya.bit.io;

/**
 * A class of {@link DefaultBitInput} whose {@link DefaultBitInput#delegate} is an instance of {@link WhiteByteInput}.
 *
 * @see BlackBitOutput
 */
class WhiteBitInput extends DefaultBitInput<WhiteByteInput> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    WhiteBitInput() {
        super(new WhiteByteInput());
    }
}
