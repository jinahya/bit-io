package com.github.jinahya.bit.io.miscellaneous;

import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.BitOutput;

final class _Utils {

    static <T extends BitInput> T requireNonNullInput(final T input) {
        if (input == null) {
            throw new NullPointerException("input is null");
        }
        return input;
    }

    static <T extends BitOutput> T requireNonNullOutput(final T output) {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        return output;
    }

    static <T> T requireNonNullValue(final T value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return value;
    }

    private _Utils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
