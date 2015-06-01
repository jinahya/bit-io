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


package com.github.jinahya.bio;


/**
 * A base class.
 *
 * @author <a href="mailto:onacit@gmail.com">Jin Kwon</a>
 */
public final class BioConstants {


    protected static final int ALIGN_MIN = 0x0000;


    protected static final int ALIGH_MAX = 0x0100;


    /**
     * The minimum value for {@code scale} of bytes.
     */
    protected static final int SCALE_MIN = 0x01;


    /**
     * The maximum value for {@code scale} of bytes.
     */
    protected static final int SCALE_MAX = 0x10;


    /**
     * The minimum value for {@code range} of bytes.
     */
    protected static final int RANGE_MIN = 0x01;


    /**
     * The maximum value for {@code range} of bytes.
     */
    protected static final int RANGE_MAX = 0x08;


    private BioConstants() {

        super();
    }


}

