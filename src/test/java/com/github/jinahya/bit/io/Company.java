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
package com.github.jinahya.bit.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Company {

    public static final int EMPLOYEES_SIZE = 10;

    public static final boolean EMPLOYEE_NULLABLE = false;

    public static Company newRandomInstance() {

        final Company instance = new Company();

        final int size = current().nextInt(256);
        for (int i = 0; i < size; i++) {
            instance.getEmployees().add(
                    current().nextBoolean() ? Employee.newRandomInstance() : null);
        }

        return instance;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.employees);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Company other = (Company) obj;
        if (!Objects.equals(this.employees, other.employees)) {
            return false;
        }
        return true;
    }

    public List<Employee> getEmployees() {

        if (employees == null) {
            employees = new ArrayList<>();
        }

        return employees;
    }

    private List<Employee> employees;

}
