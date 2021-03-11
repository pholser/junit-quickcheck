/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.examples.convention;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitQuickcheck.class)
public class ConventionGenTest {
    @Property public void generatesRandomConventionObjects(
        Convention first,
        Convention second) {

        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }

    @Test
    public void generatorClassIsNotDefinedInServicesFile() throws Exception {
        String servicesFileName =
            "META-INF/services/com.pholser.junit.quickcheck.generator.Generator";
        Enumeration<URL> serviceFiles =
            Thread.currentThread().getContextClassLoader()
                .getResources(servicesFileName);
        if (serviceFiles == null || !serviceFiles.hasMoreElements())
            return;

        while (serviceFiles.hasMoreElements()) {
            URL serviceFile = serviceFiles.nextElement();

            try (BufferedReader reader =
                new BufferedReader(
                    new InputStreamReader(serviceFile.openStream()))) {

                assertTrue(
                    reader.lines().noneMatch(
                        ConventionGen.class.getName()::equals));
            }
        }
    }
}
