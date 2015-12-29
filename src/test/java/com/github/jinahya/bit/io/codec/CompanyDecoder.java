/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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

package com.github.jinahya.bit.io.codec;


import com.github.jinahya.bit.io.BitInput;
import com.github.jinahya.bit.io.Company;
import com.github.jinahya.bit.io.Employee;
import java.io.IOException;
import java.io.UncheckedIOException;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class CompanyDecoder extends NullableDecoder<Company> {


    public CompanyDecoder(final boolean nullable) {

        super(nullable);

        employeeDecoder = NullableDecoder.newInstance(
            Company.EMPLOYEE_NULLABLE,
            i -> {
                try {
                    return i.readObject(new Employee());
                } catch (final IOException ioe) {
                    throw new UncheckedIOException(ioe);
                }
            });
    }


    @Override
    protected Company decodeValue(final BitInput input) throws IOException {

        final Company value = new Company();

        final int size = input.readInt(true, Company.EMPLOYEES_SIZE);

        for (int i = 0; i < size; i++) {
            value.getEmployees().add(employeeDecoder.decode(input));
        }

        return value;
    }


    private final BitDecoder<Employee> employeeDecoder;

}

