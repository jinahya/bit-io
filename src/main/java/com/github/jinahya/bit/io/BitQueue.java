/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
 *
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
 */
package com.github.jinahya.bit.io;

/**
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
final class BitQueue {

    //    void enqueue
    // -------------------------------------------------------------------------
    BitQueue() {
        super();
    }

    // -------------------------------------------------------------------------
    void enqueue(final boolean value) {
        if (false) {
            final int next = (limit + 1) % array.length;
            if (next == index) {
                throw new IllegalStateException("full");
            }
            array[limit] = value;
            limit = next;
            return;
        }
        if (limit == index && !empty) {
            throw new IllegalStateException("full");
        }
        array[limit] = value;
        limit = ++limit % array.length;
        empty = false;
    }

    // -------------------------------------------------------------------------
    boolean dequeue() {
        if (index == limit && empty) {
            throw new IllegalStateException("empty");
        }
        empty = true;
        return array[index++];
    }

    // -------------------------------------------------------------------------
    private final boolean[] array = new boolean[Long.SIZE];

    private int index = 0;

    private int limit = 0;

    private boolean empty = true;
}
