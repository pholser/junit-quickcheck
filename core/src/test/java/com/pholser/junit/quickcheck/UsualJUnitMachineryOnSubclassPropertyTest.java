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

import static com.pholser.junit.quickcheck.Classes.currentMethodName;
import static com.pholser.junit.quickcheck.Classes.resourceAsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.Foo;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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

public class UsualJUnitMachineryOnSubclassPropertyTest {
    private static final ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
    private static final PrintStream fakeOut =
        new PrintStream(bytesOut, true);

    @ClassRule public static final ExternalResource sysoutRedirection =
        new ExternalResource() {
            @Override protected void before() {
                System.setOut(fakeOut);
            }

            @Override protected void after() {
                System.setOut(System.out);
            }
        };

    @After public void clearBytesOut() throws Exception {
        bytesOut.reset();
    }

    @Test public void expectedOrderingOfMethods() throws Exception {
        assertThat(testResult(Leaf.class), isSuccessful());
        assertEquals(
            resourceAsString("subclass-property-test-expected.txt"),
            bytesOut.toString().replaceAll(System.lineSeparator(), "\r\n"));
    }

    public static abstract class Root {
        @ClassRule
        public static final ExternalResource firstRootClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Root::firstRootClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Root::firstRootClassRuleField::after");
                }
            };

        @ClassRule
        public static final ExternalResource secondRootClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Root::secondRootClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println("Root::secondRootClassRuleField::after");
                }
            };

        @ClassRule public static ExternalResource firstRootClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Root::firstRootClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Root::firstRootClassRuleMethod::after");
                }
            };
        }

        @ClassRule public static ExternalResource secondRootClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Root::secondRootClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Root::secondRootClassRuleMethod::after");
                }
            };
        }

        @Rule public final ExternalResource firstRootRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println("Root::firstRootRuleField::before");
                }

                @Override protected void after() {
                    System.out.println("Root::firstRootRuleField::after");
                }
            };

        @Rule public final ExternalResource secondRootRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println("Root::secondRootRuleField::before");
                }

                @Override protected void after() {
                    System.out.println("Root::secondRootRuleField::after");
                }
            };

        @Rule public final ExternalResource firstRootRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("Root::firstRootRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println("Root::firstRootRuleMethod::after");
                }
            };
        }

        @Rule public final ExternalResource secondRootRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("Root::secondRootRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println("Root::secondRootRuleMethod::after");
                }
            };
        }

        @BeforeClass public static void rootClassInitialize() {
            System.out.println(currentMethodName());
        }

        @BeforeClass public static void rootClassSetUp() {
            System.out.println(currentMethodName());
        }

        @Before public void rootInitialize() {
            System.out.println(currentMethodName());
        }

        @Before public void rootSetUp() {
            System.out.println(currentMethodName());
        }

        @After public void rootTearDown() {
            System.out.println(currentMethodName());
        }

        @After public void rootReset() {
            System.out.println(currentMethodName());
        }

        @AfterClass public static void rootClassTearDown() {
            System.out.println(currentMethodName());
        }

        @AfterClass public static void rootClassReset() {
            System.out.println(currentMethodName());
        }

        @Property(trials = 2) public void rootProperty(Foo f) {
            System.out.println(currentMethodName());
        }

        @Test public void rootTest() {
            System.out.println(currentMethodName());
        }
    }

    public static class Internal extends Root {
        @ClassRule
        public static final ExternalResource firstInternalClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Internal::firstInternalClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Internal::firstInternalClassRuleField::after");
                }
            };

        @ClassRule
        public static final ExternalResource secondInternalClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Internal::secondInternalClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Internal::secondInternalClassRuleField::after");
                }
            };

        @ClassRule
        public static ExternalResource firstInternalClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Internal::firstInternalClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Internal::firstInternalClassRuleMethod::after");
                }
            };
        }

        @ClassRule
        public static ExternalResource secondInternalClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Internal::secondInternalClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Internal::secondInternalClassRuleMethod::after");
                }
            };
        }

        @Rule public final ExternalResource firstInternalRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Internal::firstInternalRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Internal::firstInternalRuleField::after");
                }
            };

        @Rule public final ExternalResource secondInternalRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Internal::secondInternalRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Internal::secondInternalRuleField::after");
                }
            };

        @Rule public final ExternalResource firstInternalRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Internal::firstInternalRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Internal::firstInternalRuleMethod::after");
                }
            };
        }

        @Rule public final ExternalResource secondInternalRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Internal::secondInternalRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Internal::secondInternalRuleMethod::after");
                }
            };
        }

        @BeforeClass public static void internalClassInitialize() {
            System.out.println(currentMethodName());
        }

        @BeforeClass public static void internalClassSetUp() {
            System.out.println(currentMethodName());
        }

        @Before public void internalInitialize() {
            System.out.println(currentMethodName());
        }

        @Before public void internalSetUp() {
            System.out.println(currentMethodName());
        }

        @After public void internalTearDown() {
            System.out.println(currentMethodName());
        }

        @After public void internalReset() {
            System.out.println(currentMethodName());
        }

        @AfterClass public static void internalClassTearDown() {
            System.out.println(currentMethodName());
        }

        @AfterClass public static void internalClassReset() {
            System.out.println(currentMethodName());
        }

        @Property(trials = 2) public void internalProperty(Foo f) {
            System.out.println(currentMethodName());
        }

        @Test public void internalTest() {
            System.out.println(currentMethodName());
        }
    }

    @RunWith(JUnitQuickcheck.class)
    @FixMethodOrder(NAME_ASCENDING)
    public static class Leaf extends Internal {
        @ClassRule
        public static final ExternalResource firstLeafClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Leaf::firstLeafClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println("Leaf::firstLeafClassRuleField::after");
                }
            };

        @ClassRule
        public static final ExternalResource secondLeafClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Leaf::secondLeafClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Leaf::secondLeafClassRuleField::after");
                }
            };

        @ClassRule public static ExternalResource firstLeafClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Leaf::firstLeafClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Leaf::firstLeafClassRuleMethod::after");
                }
            };
        }

        @ClassRule public static ExternalResource secondLeafClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Leaf::secondLeafClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Leaf::secondLeafClassRuleMethod::after");
                }
            };
        }

        @Rule public final ExternalResource firstLeafRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println("Leaf::firstLeafRuleField::before");
                }

                @Override protected void after() {
                    System.out.println("Leaf::firstLeafRuleField::after");
                }
            };

        @Rule public final ExternalResource secondLeafRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println("Leaf::secondLeafRuleField::before");
                }

                @Override protected void after() {
                    System.out.println("Leaf::secondLeafRuleField::after");
                }
            };

        @Rule public final ExternalResource firstLeafRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("Leaf::firstLeafRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println("Leaf::firstLeafRuleMethod::after");
                }
            };
        }

        @Rule public final ExternalResource secondLeafRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("Leaf::secondLeafRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println("Leaf::secondLeafRuleMethod::after");
                }
            };
        }

        @BeforeClass public static void leafClassInitialize() {
            System.out.println(currentMethodName());
        }

        @BeforeClass public static void leafClassSetUp() {
            System.out.println(currentMethodName());
        }

        @Before public void leafInitialize() {
            System.out.println(currentMethodName());
        }

        @Before public void leafSetUp() {
            System.out.println(currentMethodName());
        }

        @After public void leafTearDown() {
            System.out.println(currentMethodName());
        }

        @After public void leafReset() {
            System.out.println(currentMethodName());
        }

        @AfterClass public static void leafClassTearDown() {
            System.out.println(currentMethodName());
        }

        @AfterClass public static void leafClassReset() {
            System.out.println(currentMethodName());
        }

        @Property(trials = 2) public void leafProperty(Foo f) {
            System.out.println(currentMethodName());
        }

        @Test public void leafTest() {
            System.out.println(currentMethodName());
        }
    }
}
