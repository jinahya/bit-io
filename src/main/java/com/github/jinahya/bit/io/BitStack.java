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

import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeInt;
import static com.github.jinahya.bit.io.BitIoConstraints.requireValidSizeLong;

final class BitStack {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reverses the lower specified bits of specified value.
     *
     * @param size  the number of bits to reverse.
     * @param value the value to be reversed.
     * @return the value with reversed bits.
     */
    public long reverse(final int size, final long value) {
        requireValidSizeLong(false, size);
        push(size, value);
        return popLong(size);
    }
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Pushes given bit flag.
     *
     * @param value the bit flag to push.
     */
    private void push(final boolean value) {
        if (top == Integer.MIN_VALUE) {
            throw new RuntimeException("stack overflow");
        }
        set().set(top++, value);
    }

    /**
     * Pushes the lower specified bits of specified value.
     *
     * @param size  the number of lower bits to push.
     * @param value the value whose lower {@code size} bits are pushed.
     */
    public void push(int size, int value) {
        requireValidSizeInt(false, size);
        for (; size > 0; size--) {
            push((value & 0x01L) == 0x01L);
            value >>= 1;
        }
    }

    /**
     * Pushes the lower specified bits of specified value.
     *
     * @param size  the number of lower bits to push.
     * @param value the value whose lower {@code size} bits are pushed.
     */
    public void push(int size, long value) {
        requireValidSizeLong(false, size);
        for (; size > 0; size--) {
            push((value & 0x01L) == 0x01L);
            value >>= 1;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Pops the flag on the {@code top}.
     *
     * @return a flag on the {@code top}.
     */
    private boolean pop() {
        if (top == 0) {
            throw new RuntimeException("stack underflow");
        }
        return set().get(--top);
    }

    /**
     * Pops an {@code int} value of specified bit size.
     *
     * @param size the number of lower bits for the number.
     * @return a value of specified bit size.
     */
    public int popInt(int size) {
        requireValidSizeLong(true, size);
        int value = 0;
        for (; size > 0; size--) {
            value |= (pop() ? 1 : 0);
            value <<= 1;
        }
        return value;
    }

    /**
     * Pops a {@code long} value of specified bit size.
     *
     * @param size the number of lower bits for the number.
     * @return a value of specified bit size.
     */
    public long popLong(final int size) {
        requireValidSizeLong(true, size);
        long value = 0L;
        for (int i = 0; i < size; i++) {
            value |= ((pop() ? 1L : 0L) << i);
        }
        return value;
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
