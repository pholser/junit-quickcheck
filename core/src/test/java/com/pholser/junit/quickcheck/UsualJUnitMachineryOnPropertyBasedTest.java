/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Foo;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;

public class UsualJUnitMachineryOnPropertyBasedTest {
    private final List<String> expectedStatements =
        asList(
            "second class rule before", "first class rule before",
            "class set up", "class init",
            "second rule before", "first rule before",
            "set up", "init",
            "test",
            "reset", "tear down",
            "first rule after", "second rule after",
            "second rule before", "first rule before",
            "set up", "init",
            "property",
            "reset", "tear down",
            "first rule after", "second rule after",
            "second rule before", "first rule before",
            "set up", "init",
            "property",
            "reset", "tear down",
            "first rule after", "second rule after",
            "class reset", "class tear down",
            "first class rule after", "second class rule after");

    @Test public void orderingOfStatements() {
        assertThat(testResult(PropertyBasedTests.class), isSuccessful());
        assertEquals(expectedStatements, PropertyBasedTests.LOGS);
        PropertyBasedTests.clearLogs();
    }

    @RunWith(JUnitQuickcheck.class)
    @FixMethodOrder(NAME_ASCENDING)
    public static class PropertyBasedTests {
        static final List<String> LOGS = new ArrayList<>();

        @ClassRule public static final ExternalResource firstClassRule =
            new ExternalResource() {
                @Override protected void before() {
                    LOGS.add("first class rule before");
                }

                @Override protected void after() {
                    LOGS.add("first class rule after");
                }
            };

        @ClassRule public static final ExternalResource secondClassRule =
            new ExternalResource() {
                @Override protected void before() {
                    LOGS.add("second class rule before");
                }

                @Override protected void after() {
                    LOGS.add("second class rule after");
                }
            };

        @Rule public final ExternalResource firstRule =
            new ExternalResource() {
                @Override protected void before() {
                    LOGS.add("first rule before");
                }

                @Override protected void after() {
                    LOGS.add("first rule after");
                }
            };

        @Rule public final ExternalResource secondRule =
            new ExternalResource() {
                @Override protected void before() {
                    LOGS.add("second rule before");
                }

                @Override protected void after() {
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

        @AfterClass public static void classReset() {
            LOGS.add("class reset");
        }

        @Property(trials = 2) public void aProperty(Foo f) {
            LOGS.add("property");
        }

        @Test public void aTest() {
            LOGS.add("test");
        }

        public static void clearLogs() {
            LOGS.clear();
        }
    }
}
