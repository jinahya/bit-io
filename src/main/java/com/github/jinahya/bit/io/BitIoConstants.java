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
 * A base class.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public final class BitIoConstants {


    /**
     * The minimum value for {@code length} in {@code align}.
     */
    protected static final int ALIGN_LENGTH_MIN = 0x00001;


    /**
     * The maximum value for {@code length} in {@code align}.
     */
    protected static final int ALIGN_LENGTH_MAX = 0x10000;


    /**
     * The minimum value for {@code scale} of bytes.
     */
    protected static final int BYTES_SCALE_MIN = 0x01;


    /**
     * The maximum value for {@code scale} of bytes.
     */
    protected static final int BYTES_SCALE_MAX = 0x10;


    public static final int BYTES_LENGTH_MIN = 0x00;


    public static final int BYTES_LENGTH_MAX
        = (int) Math.pow(2, BYTES_SCALE_MAX);


    /**
     * The minimum value for {@code range} of bytes.
     */
    protected static final int BYTES_RANGE_MIN = 0x01;


    /**
     * The maximum value for {@code range} of bytes.
     */
    protected static final int BYTES_RANGE_MAX = 0x08;


    private BitIoConstants() {

        super();
    }


}

