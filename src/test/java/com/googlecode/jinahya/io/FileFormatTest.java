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


package com.googlecode.jinahya.io;


import java.io.IOException;
import java.io.InputStream;
import org.testng.Assert;


/**
 *
 * @author Jin Kwon <jinahya at gmail.com>
 */
public abstract class FileFormatTest<S extends FileFormat> {


    public FileFormatTest(final Class<S> formatClass) {
        super();

        this.formatClass = formatClass;
    }


    protected S newInstance() {
        try {
            return formatClass.newInstance();
        } catch (InstantiationException ie) {
            Assert.fail("failed to create a new instance of " + formatClass,
                        ie);
            throw new RuntimeException(ie);
        } catch (IllegalAccessException iae) {
            Assert.fail("failed to create a new instance of " + formatClass,
                        iae);
            throw new RuntimeException(iae);
        }
    }


    protected S readInstance(final String name)
        throws InstantiationException, IllegalAccessException, IOException {

        final InputStream input = getClass().getResourceAsStream(name);
        try {
            return FileFormat.readInstance(formatClass, input);
        } finally {
            input.close();
        }
    }


    protected final Class<S> formatClass;


}

