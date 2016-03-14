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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class EmployeeTest extends BitIoTest {

    private static final Logger logger = getLogger(EmployeeTest.class);

    private static List<Employee> employees(final boolean nullable)
            throws IOException {

        final int count = current().nextInt(1024, 2048);

        final List<Employee> employees = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Employee employee = Employee.newRandomInstance();
            if (nullable && current().nextBoolean()) {
                employee = null;
            }
            employees.add(employee);
        }

        return Collections.unmodifiableList(employees);
    }

    @Test
    public static void nullable() throws IOException {

        final boolean nullable = true;
        final List<Employee> employees = employees(nullable);

        for (final Employee employee : employees) {
            BitIoTests.all(nullable, Employee.class, employee);
        }
    }

    @Test
    public static void nonnull() throws IOException {

        final boolean nullable = false;
        final List<Employee> employees = employees(nullable);

        for (final Employee employee : employees) {
            BitIoTests.all(nullable, Employee.class, employee);
        }
    }

    @Test
    public void readWrite() throws IOException {

        final Employee person = new Employee().age(39).married(false);
        person.read(getInput());
        person.write(getOutput());

        getOutput().writeObject(getInput().readObject(new Employee()));
    }

}
