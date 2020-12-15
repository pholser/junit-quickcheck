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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.ABool;
import com.pholser.junit.quickcheck.test.generator.AByte;
import com.pholser.junit.quickcheck.test.generator.AChar;
import com.pholser.junit.quickcheck.test.generator.ADouble;
import com.pholser.junit.quickcheck.test.generator.AFloat;
import com.pholser.junit.quickcheck.test.generator.AList;
import com.pholser.junit.quickcheck.test.generator.ALong;
import com.pholser.junit.quickcheck.test.generator.AShort;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import com.pholser.junit.quickcheck.test.generator.Between;
import com.pholser.junit.quickcheck.test.generator.Box;
import com.pholser.junit.quickcheck.test.generator.Foo;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

public class PropertiesWithExplicitGeneratorsTest {
    @Test public void explicitPrimitiveBoolean() {
        assertThat(
            testResult(ExplicitPrimitiveBoolean.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitPrimitiveBoolean {
        @Property public void shouldHold(@From(ABool.class) boolean b) {
        }
    }

    @Test public void explicitWrapperBoolean() {
        assertThat(testResult(ExplicitWrapperBoolean.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitWrapperBoolean {
        @Property public void shouldHold(@From(ABool.class) boolean b) {
        }
    }

    @Test public void explicitPrimitiveByte() {
        assertThat(testResult(ExplicitPrimitiveByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitPrimitiveByte {
        @Property public void shouldHold(@From(AByte.class) byte b) {
        }
    }

    @Test public void explicitWrapperByte() {
        assertThat(testResult(ExplicitWrapperByte.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitWrapperByte {
        @Property public void shouldHold(@From(AByte.class) byte b) {
        }
    }

    @Test public void explicitPrimitiveChar() {
        assertThat(testResult(ExplicitPrimitiveChar.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitPrimitiveChar {
        @Property public void shouldHold(@From(AChar.class) char ch) {
        }
    }

    @Test public void explicitWrapperChar() {
        assertThat(testResult(ExplicitWrapperChar.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitWrapperChar {
        @Property public void shouldHold(@From(AChar.class) char ch) {
        }
    }

    @Test public void explicitPrimitiveDouble() {
        assertThat(testResult(ExplicitPrimitiveDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitPrimitiveDouble {
        @Property public void shouldHold(@From(ADouble.class) double d) {
        }
    }

    @Test public void explicitWrapperDouble() {
        assertThat(testResult(ExplicitWrapperDouble.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitWrapperDouble {
        @Property public void shouldHold(@From(ADouble.class) Double d) {
        }
    }

    @Test public void explicitPrimitiveFloat() {
        assertThat(testResult(ExplicitPrimitiveFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitPrimitiveFloat {
        @Property public void shouldHold(@From(AFloat.class) float f) {
        }
    }

    @Test public void explicitWrapperFloat() {
        assertThat(testResult(ExplicitWrapperFloat.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitWrapperFloat {
        @Property public void shouldHold(@From(AFloat.class) Float f) {
        }
    }

    @Test public void explicitPrimitiveInt() {
        assertThat(testResult(ExplicitPrimitiveInt.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitPrimitiveInt {
        @Property public void shouldHold(@From(AnInt.class) int i) {
        }
    }

    @Test public void explicitWrapperInt() {
        assertThat(testResult(ExplicitWrapperInt.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitWrapperInt {
        @Property public void shouldHold(@From(AnInt.class) Integer i) {
        }
    }

    @Test public void explicitPrimitiveLong() {
        assertThat(testResult(ExplicitPrimitiveLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitPrimitiveLong {
        @Property public void shouldHold(@From(ALong.class) long ell) {
        }
    }

    @Test public void explicitWrapperLong() {
        assertThat(testResult(ExplicitWrapperLong.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitWrapperLong {
        @Property public void shouldHold(@From(ALong.class) long ell) {
        }
    }

    @Test public void explicitPrimitiveShort() {
        assertThat(testResult(ExplicitPrimitiveShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitPrimitiveShort {
        @Property public void shouldHold(@From(AShort.class) short sh) {
        }
    }

    @Test public void explicitWrapperShort() {
        assertThat(testResult(ExplicitWrapperShort.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitWrapperShort {
        @Property public void shouldHold(@From(AShort.class) short sh) {
        }
    }

    @Test public void explicitWithParameterizedType() {
        assertThat(
            testResult(ExplicitWithParameterizedType.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitWithParameterizedType {
        @Property public void shouldHold(@From(AList.class) List<Foo> list) {
            for (Foo each : list) {
                // ensure the cast works
            }
        }
    }

    @Test public void explicitPrimitiveIntFixedSeed() {
        assertThat(
            testResult(ExplicitPrimitiveIntFixedSeed.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitPrimitiveIntFixedSeed {
        @Property(trials = 1) public void shouldHold(
            @When(seed = -1) @From(AnInt.class) int i) {

            assertEquals(1155099827, i);
        }
    }

    @Test public void indirectPrimitiveInt() {
        assertThat(testResult(IndirectPrimitiveInt.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class IndirectPrimitiveInt {
        @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
        @Retention(RUNTIME)
        @From(AnInt.class)
        @Between(min = 1, max = 5)
        public @interface Small {
        }

        @Property public void shouldHold(@Small int i) {
            assertThat(
                i,
                allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(5)));
        }
    }

    @Test public void explicitGeneratorTakesPrecedence() {
        assertThat(testResult(WithExplicitGenerator.class), isSuccessful());
        assertEquals(asList(0, 1, 2, 3, 4), WithExplicitGenerator.values);
        WithExplicitGenerator.values.clear();
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithExplicitGenerator {
        public static final List<Integer> values = new ArrayList<>();

        @Property(trials = 5) public void shouldHold(
            @From(Sequence.class) int i) {

            values.add(i);
        }
    }

    public static class Sequence extends Generator<Integer> {
        private int next = 0;

        public Sequence() {
            super(int.class);
        }

        @Override public Integer generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return next++;
        }
    }

    @Test public void typeMismatch() {
        assertThat(
            testResult(WithGeneratorTypeThatDoesNotMatchParameterType.class),
            hasSingleFailureContaining(
                IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WithGeneratorTypeThatDoesNotMatchParameterType {
        @Property public void shouldHold(@From(StringEmitter.class) Number n) {
        }
    }

    public static class StringEmitter extends Generator<String> {
        public StringEmitter() {
            super(String.class);
        }

        @Override public String generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return "foo";
        }
    }

    @Test public void alternateGeneratorOnBasicTypeParameter() {
        assertThat(
            testResult(AlternateGeneratorOnBasicTypeParameter.class),
            isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AlternateGeneratorOnBasicTypeParameter {
        @Property public void holds(
            Box<@From(AnInt.class) @Between(min = 3, max = 4) Integer> box) {

            assertThat(
                box.contents(),
                allOf(greaterThanOrEqualTo(3), lessThanOrEqualTo(4)));
        }
    }

    @Test public void alternateGeneratorOnHuhTypeParameter() {
        assertThat(
            testResult(AlternateGeneratorOnHuhTypeParameter.class),
            hasSingleFailureContaining(
                "Wildcards cannot be marked with @From"));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AlternateGeneratorOnHuhTypeParameter {
        @Property public void holds(Box<@From(AnInt.class) ?> box) {
        }
    }

    @Test public void alternateGeneratorOnExtendsTypeParameter() {
        assertThat(
            testResult(AlternateGeneratorOnExtendsTypeParameter.class),
            hasSingleFailureContaining(
                "Wildcards cannot be marked with @From"));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AlternateGeneratorOnExtendsTypeParameter {
        @Property public void holds(
            Box<@From(AnInt.class) ? extends Integer> box) {
        }
    }

    @Test public void alternateGeneratorOnSuperTypeParameter() {
        assertThat(
            testResult(AlternateGeneratorOnSuperTypeParameter.class),
            hasSingleFailureContaining(
                "Wildcards cannot be marked with @From"));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class AlternateGeneratorOnSuperTypeParameter {
        @Property public void holds(
            Box<@From(AnInt.class) ? super Integer> box) {
        }
    }

    @Test public void typeMismatchOnBasicTypeParameter() {
        assertThat(
            testResult(TypeMismatchOnBasicTypeParameter.class),
            hasSingleFailureContaining(
                IllegalArgumentException.class.getName()));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class TypeMismatchOnBasicTypeParameter {
        @Property public void holds(Box<@From(AnInt.class) String> box) {
        }
    }
}
