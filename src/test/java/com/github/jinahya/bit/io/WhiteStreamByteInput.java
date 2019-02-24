package com.github.jinahya.bit.io;

/**
 * A class of {@link StreamByteInput} whose {@link AbstractByteInput#source} is an instance of {@link
 * WhiteInputStream}.
 *
 * @see BlackStreamByteOutput
 */
class WhiteStreamByteInput extends StreamByteInput<WhiteInputStream> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    WhiteStreamByteInput() {
        super(new WhiteInputStream());
    }
}
