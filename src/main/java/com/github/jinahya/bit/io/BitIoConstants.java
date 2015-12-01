/*
 *  Copyright 2010 Jin Kwon.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package com.github.jinahya.bit.io;


/**
 * A class for constants.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class BitIoConstants {


    protected static final int UBYTE_SIZE_MIN = 0x01;


    protected static final int UBYTE_SIZE_MAX = 0x08;


    protected static final int USHORT_SIZE_MIN = 0x01;


    protected static final int USHORT_SIZE_MAX = 0x10;


    protected static final int UINT_SIZE_MIN = 0x01;


    protected static final int UINT_SIZE_MAX = 0x1F;


    protected static final int INT_SIZE_MIN = 0x02;


    protected static final int INT_SIZE_MAX = 0x20;


    protected static final int ULONG_SIZE_MIN = 0x01;


    protected static final int ULONG_SIZE_MAX = 0x3F;


    protected static final int LONG_SIZE_MIN = 0x02;


    protected static final int LONG_SIZE_MAX = 0x40;


    /**
     * The minimum value for {@code scale} of bytes.
     */
    protected static final int SCALE_SIZE_MIN = 0x01;


    /**
     * The maximum value for {@code scale} of bytes.
     */
    protected static final int SCALE_SIZE_MAX = 0x10;


    /**
     * The minimum value for {@code range} of bytes.
     */
    protected static final int RANGE_SIZE_MIN = 0x01;


    /**
     * The maximum value for {@code range} of bytes.
     */
    protected static final int RANGE_SIZE_MAX = 0x08;


    /**
     * The minimum value for {@code length} in {@code align}.
     */
    protected static final int ALIGN_BYTES_MIN = 0x00001;


    /**
     * The maximum value for {@code length} in {@code align}.
     */
    protected static final int ALIGN_BYTES_MAX = 0x10000;


    private BitIoConstants() {

        super();
    }

}

