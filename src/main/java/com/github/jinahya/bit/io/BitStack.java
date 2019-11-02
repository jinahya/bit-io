package com.github.jinahya.bit.io;

/*-
 * #%L
 * bit-io
 * %%
 * Copyright (C) 2014 - 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.BitSet;

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

final class BitStack {

    // -----------------------------------------------------------------------------------------------------------------
    public long reverse(final int size, final long value) {
        push(size, value);
        return pop(size);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private boolean pop() {
        if (top == 0) {
            throw new RuntimeException("stack underflow");
        }
        return set().get(--top);
    }

    long pop(final int size) {
        requireValidSizeLong(true, size);
        long value = 0L;
        for (int i = 0; i < size; i++) {
            value |= ((pop() ? 1L : 0L) << i);
        }
        return value;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Pushes given bit flag.
     *
     * @param value the bit flag to push.
     */
    private void push(final boolean value) {
        if (top == Integer.MAX_VALUE) {
            throw new RuntimeException("stack overflow");
        }
        set().set(top++, value);
    }

    void push(final int size, long value) {
        requireValidSizeLong(true, size);
        if (value < 0L) {
            throw new IllegalArgumentException("value(" + value + ") < 0L");
        }
        for (int i = 0; i < size; i++) {
            push((value & 0x01L) == 0x01L);
            value >>= 1;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitSet set() {
        if (set == null) {
            set = new BitSet();
        }
        return set;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BitSet set;

    private int top = 0;
}
