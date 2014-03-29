/*
 * Copyright 2013 Jin Kwon <jinahya at gmail.com>.
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


package com.github.jinahya.io.bit;


import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


/**
 *
 * @author <a href="mailto:jinahya@gmail.com">Jin Kwon</a>
 */
public class RandomValuePack {


    public RandomValuePack(final RandomLengthPack lengths) {

        super();

        this.lengths = Objects.requireNonNull(lengths, "null lengths");
    }


    void read(final BitInput input) throws IOException {

        valueBoolean = input.readBoolean();

        valueIntUnsigned = input.readUnsignedInt(lengths.lengthIntUnsigned);

        valueInt = input.readInt(lengths.lengthInt);

        valueLongUnsigned = input.readUnsignedLong(lengths.lengthLongUnsigned);

        valueLong = input.readLong(lengths.lengthLong);

        valueBytes = input.readBytes(lengths.scaleBytes, lengths.rangeBytes);

        valueStringUtf8 = input.readString("UTF-8");

        valueStringUsAscii = input.readUsAsciiString();
    }


    void write(final BitOutput output) throws IOException {

        valueBoolean = BitIoTests.valueBoolean();
        output.writeBoolean(valueBoolean);

        valueIntUnsigned
            = BitIoTests.valueIntUnsigned(lengths.lengthIntUnsigned);
        output.writeUnsignedInt(lengths.lengthIntUnsigned, valueIntUnsigned);

        valueInt = BitIoTests.valueInt(lengths.lengthInt);
        output.writeInt(lengths.lengthInt, valueInt);

        valueLongUnsigned
            = BitIoTests.valueLongUnsigned(lengths.lengthLongUnsigned);
        output.writeUnsignedLong(lengths.lengthLongUnsigned, valueLongUnsigned);

        valueLong = BitIoTests.valueLong(lengths.lengthLong);
        output.writeLong(lengths.lengthLong, valueLong);

        valueBytes
            = BitIoTests.valueBytes(lengths.scaleBytes, lengths.rangeBytes);
        output.writeBytes(lengths.scaleBytes, lengths.rangeBytes, valueBytes);

        valueStringUtf8 = BitIoTests.valueStringUtf8();
        output.writeString(valueStringUtf8, "UTF-8");

        valueStringUsAscii = BitIoTests.valueStringUsAscii();
        output.writeUsAsciiString(valueStringUsAscii);
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(lengths);
        hash = 71 * hash + (valueBoolean ? 1 : 0);
        hash = 71 * hash + valueIntUnsigned;
        hash = 71 * hash + valueInt;
        hash = 71 * hash
               + (int) (valueLongUnsigned ^ (valueLongUnsigned >>> 32));
        hash = 71 * hash + (int) (valueLong ^ (valueLong >>> 32));
        hash = 71 * hash + Arrays.hashCode(valueBytes);
        hash = 71 * hash + Objects.hashCode(valueStringUtf8);
        hash = 71 * hash + Objects.hashCode(valueStringUsAscii);
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RandomValuePack that = (RandomValuePack) obj;
        if (!Objects.equals(lengths, that.lengths)) {
            return false;
        }
        if (valueBoolean != that.valueBoolean) {
            return false;
        }
        if (valueIntUnsigned != that.valueIntUnsigned) {
            return false;
        }
        if (valueInt != that.valueInt) {
            return false;
        }
        if (valueLongUnsigned != that.valueLongUnsigned) {
            return false;
        }
        if (valueLong != that.valueLong) {
            return false;
        }
        if (!Arrays.equals(valueBytes, that.valueBytes)) {
            return false;
        }
        if (!Objects.equals(valueStringUtf8, that.valueStringUtf8)) {
            return false;
        }
        if (!Objects.equals(valueStringUsAscii, that.valueStringUsAscii)) {
            return false;
        }
        return true;
    }


    private final RandomLengthPack lengths;


    private boolean valueBoolean;


    private int valueIntUnsigned;


    private int valueInt;


    private long valueLongUnsigned;


    private long valueLong;


    private byte[] valueBytes;


    private String valueStringUtf8;


    private String valueStringUsAscii;


}

