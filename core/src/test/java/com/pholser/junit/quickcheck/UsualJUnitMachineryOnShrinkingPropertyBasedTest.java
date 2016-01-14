/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck;

import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Foo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class UsualJUnitMachineryOnShrinkingPropertyBasedTest {
    private List<String> expectedStatements = asList(
        "class init", "class set up",
        "second rule before", "first rule before",
        "init", "set up",
        "property",
        "tear down", "reset",
        "first rule after", "second rule after",
        "second rule before", "first rule before",
        "init", "set up",
        "property",
        "tear down", "reset",
        "first rule after", "second rule after",
        "second rule before", "first rule before",
        "init", "set up",
        "property",
        "tear down", "reset",
        "first rule after", "second rule after",
        "class reset", "class tear down");

    @Test public void orderingOfStatements() throws Exception {
        assertThat(testResult(PropertyBasedTests.class), failureCountIs(1));
        assertEquals(expectedStatements, PropertyBasedTests.LOGS);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PropertyBasedTests {
        static final List<String> LOGS = new ArrayList<>();

        @Rule public final ExternalResource firstRule = new ExternalResource() {
            @Override protected void before() {
                LOGS.add("first rule before");
            }

            @Override
            protected void after() {
                LOGS.add("first rule after");
            }
        };

        @Rule public final ExternalResource secondRule = new ExternalResource() {
            @Override protected void before() {
                LOGS.add("second rule before");
            }

            @Override
            protected void after() {
                LOGS.add("second rule after");
            }
        };

        @BeforeClass public static void classInitialize() {
            LOGS.add("class init");
        }

        @BeforeClass public static void classSetUp() {
            LOGS.add("class set up");
        }

        @Before public void initialize() {
            LOGS.add("init");
        }

        @Before public void setUp() {
            LOGS.add("set up");
        }

        @After public void tearDown() {
            LOGS.add("tear down");
        }

        @After public void reset() {
            LOGS.add("reset");
        }

        @AfterClass public static void classTearDown() {
            LOGS.add("class tear down");
        }

        @AfterClass public static void classUninitialize() {
            LOGS.add("class reset");
        }

        @Property(trials = 2, maxShrinks = 2) public void aProperty(Foo f) {
            LOGS.add("property");

            fail();
        }
    }
}
