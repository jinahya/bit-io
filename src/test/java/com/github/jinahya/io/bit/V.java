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
 * @author Jin Kwon <jinahya at gmail.com>
 */
public class V {


    public V(final L lengths) {

        super();

        this.l = Objects.requireNonNull(lengths, "null lengths");
    }


    void read(final BitInput<?> input) throws IOException {

        vBoolean = input.readBoolean();

        vIntUnsigned = input.readUnsignedInt(l.lIntUnsigned);

        vInt = input.readInt(l.lInt);

        vLongUnsigned = input.readUnsignedLong(l.lLongUnsigned);

        vLong = input.readLong(l.lLong);

        vBytes = input.readBytes(l.sBytes, l.rByhtes);

        vStringUtf8 = input.readString("UTF-8");

        vStringUsAscii = input.readUsAsciiString();

        input.align((short) 1);
    }


    void write(final BitOutput<?> output) throws IOException {

        vBoolean = BitIoTests.valueBoolean();
        output.writeBoolean(vBoolean);

        vIntUnsigned = BitIoTests.valueIntUnsigned(l.lIntUnsigned);
        output.writeUnsignedInt(l.lIntUnsigned, vIntUnsigned);

        vInt = BitIoTests.valueInt(l.lInt);
        output.writeInt(l.lInt, vInt);

        vLongUnsigned = BitIoTests.valueLongUnsigned(l.lLongUnsigned);
        output.writeUnsignedLong(l.lLongUnsigned, vLongUnsigned);

        vLong = BitIoTests.valueLong(l.lLong);
        output.writeLong(l.lLong, vLong);

        vBytes = BitIoTests.valueBytes(l.sBytes, l.rByhtes);
        output.writeBytes(l.sBytes, l.rByhtes, vBytes);

        vStringUtf8 = BitIoTests.valueStringUtf8();
        output.writeString(vStringUtf8, "UTF-8");

        vStringUsAscii = BitIoTests.valueStringUsAscii();
        output.writeUsAsciiString(vStringUsAscii);

        output.align((short) 1);
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(l);
        hash = 71 * hash + (vBoolean ? 1 : 0);
        hash = 71 * hash + vIntUnsigned;
        hash = 71 * hash + vInt;
        hash = 71 * hash + (int) (vLongUnsigned ^ (vLongUnsigned >>> 32));
        hash = 71 * hash + (int) (vLong ^ (vLong >>> 32));
        hash = 71 * hash + Arrays.hashCode(vBytes);
        hash = 71 * hash + Objects.hashCode(vStringUtf8);
        hash = 71 * hash + Objects.hashCode(vStringUsAscii);
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
        final V that = (V) obj;
        if (!Objects.equals(l, that.l)) {
            return false;
        }
        if (vBoolean != that.vBoolean) {
            return false;
        }
        if (vIntUnsigned != that.vIntUnsigned) {
            return false;
        }
        if (vInt != that.vInt) {
            return false;
        }
        if (vLongUnsigned != that.vLongUnsigned) {
            return false;
        }
        if (vLong != that.vLong) {
            return false;
        }
        if (!Arrays.equals(vBytes, that.vBytes)) {
            return false;
        }
        if (!Objects.equals(vStringUtf8, that.vStringUtf8)) {
            return false;
        }
        if (!Objects.equals(vStringUsAscii, that.vStringUsAscii)) {
            return false;
        }
        return true;
    }


    final L l;


    boolean vBoolean;


    private int vIntUnsigned;


    private int vInt;


    private long vLongUnsigned;


    private long vLong;


    private byte[] vBytes;


    private String vStringUtf8;


    private String vStringUsAscii;


}

