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

public class UsualJUnitMachineryOnTraitBasedPropertyTest {
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
            resourceAsString("trait-property-test-expected.txt"),
            bytesOut.toString().replaceAll(System.lineSeparator(), "\r\n"));
    }

    public interface TraitA {
        @ClassRule ExternalResource firstTraitAClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitA::firstTraitAClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitA::firstTraitAClassRuleField::after");
                }
            };

        @ClassRule ExternalResource secondTraitAClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitA::secondTraitAClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitA::secondTraitAClassRuleField::after");
                }
            };

        @ClassRule static ExternalResource firstTraitAClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitA::firstTraitAClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitA::firstTraitAClassRuleMethod::after");
                }
            };
        }

        @ClassRule static ExternalResource secondTraitAClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitA::secondTraitAClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitA::secondTraitAClassRuleMethod::after");
                }
            };
        }

        @Rule default ExternalResource firstTraitARule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitA::firstTraitARule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitA::firstTraitARule::after");
                }
            };
        }

        @Rule default ExternalResource secondTraitARule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitA::secondTraitARule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitA::secondTraitARule::after");
                }
            };
        }

        @BeforeClass static void traitAClassInitialize() {
            System.out.println(currentMethodName());
        }

        @BeforeClass static void traitAClassSetUp() {
            System.out.println(currentMethodName());
        }

        @Before default void traitAInitialize() {
            System.out.println(currentMethodName());
        }

        @Before default void traitASetUp() {
            System.out.println(currentMethodName());
        }

        @After default void traitATearDown() {
            System.out.println(currentMethodName());
        }

        @After default void traitAReset() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitAClassTearDown() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitAClassReset() {
            System.out.println(currentMethodName());
        }

        @Property(trials = 2) default void traitAProperty(Foo f) {
            System.out.println(currentMethodName());
        }

        @Test default void traitATest() {
            System.out.println(currentMethodName());
        }
    }

    public interface TraitB extends TraitA {
        @ClassRule ExternalResource firstTraitBClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitB::firstTraitBClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitB::firstTraitBClassRuleField::after");
                }
            };

        @ClassRule ExternalResource secondTraitBClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitB::secondTraitBClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitB::secondTraitBClassRuleField::after");
                }
            };

        @ClassRule static ExternalResource firstTraitBClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitB::firstTraitBClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitB::firstTraitBClassRuleMethod::after");
                }
            };
        }

        @ClassRule static ExternalResource secondTraitBClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitB::secondTraitBClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitB::secondTraitBClassRuleMethod::after");
                }
            };
        }

        @Rule default ExternalResource firstTraitBRule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitB::firstTraitBRule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitB::firstTraitBRule::after");
                }
            };
        }

        @Rule default ExternalResource secondTraitBRule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitB::secondTraitBRule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitB::secondTraitBRule::after");
                }
            };
        }

        @BeforeClass static void traitBClassInitialize() {
            System.out.println(currentMethodName());
        }

        @BeforeClass static void traitBClassSetUp() {
            System.out.println(currentMethodName());
        }

        @Before default void traitBInitialize() {
            System.out.println(currentMethodName());
        }

        @Before default void traitBSetUp() {
            System.out.println(currentMethodName());
        }

        @After default void traitBTearDown() {
            System.out.println(currentMethodName());
        }

        @After default void traitBReset() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitBClassTearDown() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitBClassReset() {
            System.out.println(currentMethodName());
        }

        @Property(trials = 2) default void traitBProperty(Foo f) {
            System.out.println(currentMethodName());
        }

        @Test default void traitBTest() {
            System.out.println(currentMethodName());
        }
    }

    public interface TraitC {
        @ClassRule ExternalResource firstTraitCClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitC::firstTraitCClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitC::firstTraitCClassRuleField::after");
                }
            };

        @ClassRule ExternalResource secondTraitCClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitC::secondTraitCClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitC::secondTraitCClassRuleField::after");
                }
            };

        @ClassRule static ExternalResource firstTraitCClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitC::firstTraitCClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitC::firstTraitCClassRuleMethod::after");
                }
            };
        }

        @ClassRule static ExternalResource secondTraitCClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitC::secondTraitCClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitC::secondTraitCClassRuleMethod::after");
                }
            };
        }

        @Rule default ExternalResource firstTraitCRule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitC::firstTraitCRule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitC::firstTraitCRule::after");
                }
            };
        }

        @Rule default ExternalResource secondTraitCRule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitC::secondTraitCRule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitC::secondTraitCRule::after");
                }
            };
        }

        @BeforeClass static void traitCClassInitialize() {
            System.out.println(currentMethodName());
        }

        @BeforeClass static void traitCClassSetUp() {
            System.out.println(currentMethodName());
        }

        @Before default void traitCInitialize() {
            System.out.println(currentMethodName());
        }

        @Before default void traitCSetUp() {
            System.out.println(currentMethodName());
        }

        @After default void traitCTearDown() {
            System.out.println(currentMethodName());
        }

        @After default void traitCReset() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitCClassTearDown() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitCClassReset() {
            System.out.println(currentMethodName());
        }

        @Property(trials = 2) default void traitCProperty(Foo f) {
            System.out.println(currentMethodName());
        }

        @Test default void traitCTest() {
            System.out.println(currentMethodName());
        }
    }

    public interface TraitD {
        @ClassRule ExternalResource traitDClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitD::traitDClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println("TraitD::traitDClassRuleField::after");
                }
            };

        @ClassRule static ExternalResource traitDClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitD::traitDClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitD::traitDClassRuleMethod::after");
                }
            };
        }

        @Rule default ExternalResource firstTraitDRule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitD::firstTraitDRule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitD::firstTraitDRule::after");
                }
            };
        }

        @Rule default ExternalResource secondTraitDRule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitD::secondTraitDRule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitD::secondTraitDRule::after");
                }
            };
        }

        @BeforeClass static void traitDClassInitialize() {
            System.out.println(currentMethodName());
        }

        @BeforeClass static void traitDClassSetUp() {
            System.out.println(currentMethodName());
        }

        @Before default void traitDInitialize() {
            System.out.println(currentMethodName());
        }

        @Before default void traitDSetUp() {
            System.out.println(currentMethodName());
        }

        @After default void traitDTearDown() {
            System.out.println(currentMethodName());
        }

        @After default void traitDReset() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitDClassTearDown() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitDClassReset() {
            System.out.println(currentMethodName());
        }

        @Property(trials = 2) default void traitDProperty(Foo f) {
            System.out.println(currentMethodName());
        }

        @Test default void traitDTest() {
            System.out.println(currentMethodName());
        }
    }

    public interface TraitE {
        @ClassRule ExternalResource traitEClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitE::traitEClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println("TraitE::traitEClassRuleField::after");
                }
            };

        @ClassRule static ExternalResource traitEClassRuleMethod() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "TraitE::traitEClassRuleMethod::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "TraitE::traitEClassRuleMethod::after");
                }
            };
        }

        @Rule default ExternalResource firstTraitERule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitE::firstTraitERule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitE::firstTraitERule::after");
                }
            };
        }

        @Rule default ExternalResource secondTraitERule() {
            return new ExternalResource() {
                @Override protected void before() {
                    System.out.println("TraitE::secondTraitERule::before");
                }

                @Override protected void after() {
                    System.out.println("TraitE::secondTraitERule::after");
                }
            };
        }

        @BeforeClass static void traitEClassInitialize() {
            System.out.println(currentMethodName());
        }

        @BeforeClass static void traitEClassSetUp() {
            System.out.println(currentMethodName());
        }

        @Before default void traitEInitialize() {
            System.out.println(currentMethodName());
        }

        @Before default void traitESetUp() {
            System.out.println(currentMethodName());
        }

        @After default void traitETearDown() {
            System.out.println(currentMethodName());
        }

        @After default void traitEReset() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitEClassTearDown() {
            System.out.println(currentMethodName());
        }

        @AfterClass static void traitEClassReset() {
            System.out.println(currentMethodName());
        }

        @Property(trials = 2) default void traitEProperty(Foo f) {
            System.out.println(currentMethodName());
        }

        @Test default void traitETest() {
            System.out.println(currentMethodName());
        }
    }

    public static abstract class Root implements TraitB {
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
                    System.out.println(
                        "Root::secondRootClassRuleField::after");
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

        @ClassRule
        public static ExternalResource secondRootClassRuleMethod() {
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

    public static class Internal extends Root implements TraitD, TraitE {
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
    public static class Leaf extends Internal implements TraitC {
        @ClassRule
        public static final ExternalResource firstLeafClassRuleField =
            new ExternalResource() {
                @Override protected void before() {
                    System.out.println(
                        "Leaf::firstLeafClassRuleField::before");
                }

                @Override protected void after() {
                    System.out.println(
                        "Leaf::firstLeafClassRuleField::after");
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

        @ClassRule
        public static ExternalResource secondLeafClassRuleMethod() {
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
