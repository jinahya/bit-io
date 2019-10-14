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

import org.jeasy.random.EasyRandom;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;

class Profile implements BitReadable, BitWritable {

    // -----------------------------------------------------------------------------------------------------------------
    static final int SIZE_MIN_NAME = 1;

    static final int SIZE_MAX_NAME = 255;

    // -----------------------------------------------------------------------------------------------------------------
    static final int MIN_AGE = 0;

    static final int MAX_AGE = 127;

    static final int SIZE_AGE = 7;

    static {
        assert MAX_AGE <= (1 << (SIZE_AGE)) - 1;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static ValidatorFactory VALIDATOR_FACTORY;

    static ValidatorFactory validatorFactory() {
        if (VALIDATOR_FACTORY == null) {
            VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
        }
        return VALIDATOR_FACTORY;
    }

    static transient Validator VALIDATOR;

    static Validator validator() {
        if (VALIDATOR == null) {
            VALIDATOR = validatorFactory().getValidator();
        }
        return VALIDATOR;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Profile newInstance() {
        final Profile instance = new EasyRandom().nextObject(Profile.class);
        if (current().nextBoolean()) {
            instance.name = null;
        }
        final Set<ConstraintViolation<Profile>> violations = validator().validate(instance);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return instance;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Profile that = (Profile) o;
        return age == that.age &&
               married == that.married &&
               Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, married);
    }

    @Override
    public String toString() {
        return super.toString() + "{"
               + "name=" + name
               + ",age=" + age
               + ",married=" + married
               + "}";
    }

    // -----------------------------------------------------------------------------------------------------------------
    void validate() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void read(final BitInput bitInput) throws IOException {
        if (bitInput.readBoolean()) {
            final int length = bitInput.readInt(true, 16);
            final byte[] bytes = new byte[length];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = bitInput.readByte(false, Byte.SIZE);
            }
            name = new String(bytes, UTF_8);
        }
        age = bitInput.readInt(true, SIZE_AGE);
        married = bitInput.readBoolean();
    }

    @Override
    public void write(final BitOutput bitOutput) throws IOException {
        {
            bitOutput.writeBoolean(name != null);
            if (name != null) {
                final byte[] bytes = name.getBytes(UTF_8);
                bitOutput.writeInt(true, 16, bytes.length);
                for (int i = 0; i < bytes.length; i++) {
                    bitOutput.writeByte(false, Byte.SIZE, bytes[i]);
                }
            }
        }
        bitOutput.writeInt(true, SIZE_AGE, age);
        bitOutput.writeBoolean(married);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Size(min = SIZE_MIN_NAME, max = SIZE_MAX_NAME)
    private String name;

    @Min(MIN_AGE)
    @Max(MAX_AGE)
    private int age;

    private boolean married;
}
