/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.ABox;
import com.pholser.junit.quickcheck.test.generator.AFoo.Same;
import com.pholser.junit.quickcheck.test.generator.AnotherBox;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.X;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ComposedObjectsTest {
    @Test public void askingForGeneratorsByType() {
        assertThat(testResult(GeneratorsByType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class GeneratorsByType {
        static class A {
            Foo foo;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                a.foo = gen().type(Foo.class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test public void askingForGeneratorsForIndividualFields() {
        assertThat(testResult(IndividualFields.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class IndividualFields {
        static class A {
            @X Foo foo;
            @X Box<@Same(3) Foo> boxOfFoo;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @SuppressWarnings("unchecked")
            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                a.foo = (Foo) gen().field(A.class, "foo")
                    .generate(random, status);
                a.boxOfFoo = (Box<Foo>) gen().field(A.class, "boxOfFoo")
                    .generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
            assertTrue(a.foo.marked());
            assertTrue(a.boxOfFoo.marked());
            assertFalse(a.boxOfFoo.contents().marked());
            assertEquals(3, a.boxOfFoo.contents().i());
        }
    }

    @Test public void askingForGeneratorsForAllFieldsOfClassAtOnce() {
        assertThat(testResult(AllFieldsAtOnce.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AllFieldsAtOnce {
        public static class B {
            Foo foo;
            @From(ABox.class) Box<@X @From(AnotherBox.class) Box<@X Foo>> boxOfBoxOfFoo;
        }

        public static class A {
            B b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                a.b = gen().fieldsOf(B.class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
            assertFalse(a.b.foo.marked());
            assertFalse(a.b.boxOfBoxOfFoo.marked());
            assertTrue(a.b.boxOfBoxOfFoo.contents().marked());
            assertTrue(a.b.boxOfBoxOfFoo.contents().contents().marked());
        }
    }

    @Test public void askingForGeneratorsByConstructor() {
        assertThat(testResult(ByConstructor.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ByConstructor {
        public static class B {
            final Foo foo;
            final Box<@X Box<@X Foo>> boxOfBoxOfFoo;

            public B(
                @Same(6) Foo foo,
                Box<@X Box<@X @Same(7) Foo>> boxOfBoxOfFoo) {

                this.foo = foo;
                this.boxOfBoxOfFoo = boxOfBoxOfFoo;
            }
        }

        public static class A {
            B b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                a.b = gen().constructor(B.class, Foo.class, Box.class)
                    .generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
            assertFalse(a.b.foo.marked());
            assertFalse(a.b.boxOfBoxOfFoo.marked());
            assertTrue(a.b.boxOfBoxOfFoo.contents().marked());
            assertTrue(a.b.boxOfBoxOfFoo.contents().contents().marked());
        }
    }

    @Test public void askingForGeneratorsByUnrecognizedConstructor() {
        assertThat(
            testResult(UnrecognizedConstructor.class),
            hasSingleFailureContaining(ReflectionException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class UnrecognizedConstructor {
        public static class B {
            final Foo foo;
            final Box<@X Box<@X Foo>> boxOfBoxOfFoo;

            public B(
                @Same(6) Foo foo,
                Box<@X Box<@X @Same(7) Foo>> boxOfBoxOfFoo) {

                this.foo = foo;
                this.boxOfBoxOfFoo = boxOfBoxOfFoo;
            }
        }

        public static class A {
            B b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                a.b = gen().constructor(B.class, int.class)
                    .generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test
    public void askingForArrayGenerator() {
        assertThat(testResult(AskingForArrayGenerator.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AskingForArrayGenerator {
        static class A {
            Foo[][] foos;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                a.foos = gen().type(Foo[][].class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test
    public void askingForRawComponentizedType() {
        assertThat(testResult(RawComponentizedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class RawComponentizedType {
        static class A {
            Box b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                a.b = gen().type(Box.class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test
    public void askingForArrayOfRawComponentizedType() {
        assertThat(
            testResult(ArrayOfRawComponentizedType.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ArrayOfRawComponentizedType {
        static class A {
            Box[] b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                a.b = gen().type(Box[].class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test
    public void askingForParameterizedComponentizedType() {
        assertThat(testResult(ParameterizedComponentizedType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ParameterizedComponentizedType {
        static class A {
            Box<Foo> b;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @SuppressWarnings("unchecked")
            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                a.b = gen().type(Box.class, Foo.class).generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
        }
    }

    @Test public void askingToMakeASpecificKindOfGenerator() {
        assertThat(testResult(SpecificGenerator.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SpecificGenerator {
        @X public static class AnXBox extends ABox {
        }

        static class A {
            Box<Foo> boxOfFoos;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @SuppressWarnings("unchecked")
            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                AnXBox make = gen().make(AnXBox.class, gen().type(Foo.class));
                a.boxOfFoos = (Box<Foo>) make.generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
            assertTrue(a.boxOfFoos.marked());
        }
    }

    @Test public void specificKindOfGeneratorWithMissingComponents() {
        assertThat(
            testResult(SpecificGeneratorWithMissingComponents.class),
            hasSingleFailureContaining(
                "IllegalArgumentException: Needed 1 components"));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SpecificGeneratorWithMissingComponents {
        @X public static class AnXBox extends ABox {
        }

        static class A {
            Box<Foo> boxOfFoos;
        }

        public static class MakeA extends Generator<A> {
            public MakeA() {
                super(A.class);
            }

            @SuppressWarnings("unchecked")
            @Override public A generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                A a = new A();
                AnXBox make = gen().make(AnXBox.class);
                a.boxOfFoos = (Box<Foo>) make.generate(random, status);
                return a;
            }
        }

        @Property public void holds(@From(MakeA.class) A a) {
            assertTrue(a.boxOfFoos.marked());
        }
    }
}
