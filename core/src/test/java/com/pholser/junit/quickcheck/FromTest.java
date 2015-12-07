package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.test.generator.Between;
import com.pholser.junit.quickcheck.test.generator.Foo;
import com.pholser.junit.quickcheck.test.generator.AList;
import com.pholser.junit.quickcheck.test.generator.ABool;
import com.pholser.junit.quickcheck.test.generator.AByte;
import com.pholser.junit.quickcheck.test.generator.AChar;
import com.pholser.junit.quickcheck.test.generator.ADouble;
import com.pholser.junit.quickcheck.test.generator.AFloat;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import com.pholser.junit.quickcheck.test.generator.ALong;
import com.pholser.junit.quickcheck.test.generator.AShort;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

@Deprecated
public class FromTest {
    @Test public void explicitPrimitiveBoolean() {
        assertThat(testResult(ExplicitPrimitiveBoolean.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitPrimitiveBoolean {
        @Theory public void shouldHold(@ForAll @From(ABool.class) boolean b) {
        }
    }

    @Test public void explicitWrapperBoolean() {
        assertThat(testResult(ExplicitWrapperBoolean.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitWrapperBoolean {
        @Theory public void shouldHold(@ForAll @From(ABool.class) boolean b) {
        }
    }

    @Test public void explicitPrimitiveByte() {
        assertThat(testResult(ExplicitPrimitiveByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitPrimitiveByte {
        @Theory public void shouldHold(@ForAll @From(AByte.class) byte b) {
        }
    }

    @Test public void explicitWrapperByte() {
        assertThat(testResult(ExplicitWrapperByte.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitWrapperByte {
        @Theory public void shouldHold(@ForAll @From(AByte.class) byte b) {
        }
    }

    @Test public void explicitPrimitiveChar() {
        assertThat(testResult(ExplicitPrimitiveChar.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitPrimitiveChar {
        @Theory public void shouldHold(@ForAll @From(AChar.class) char ch) {
        }
    }

    @Test public void explicitWrapperChar() {
        assertThat(testResult(ExplicitWrapperChar.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitWrapperChar {
        @Theory public void shouldHold(@ForAll @From(AChar.class) char ch) {
        }
    }

    @Test public void explicitPrimitiveDouble() {
        assertThat(testResult(ExplicitPrimitiveDouble.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitPrimitiveDouble {
        @Theory public void shouldHold(@ForAll @From(ADouble.class) double d) {
        }
    }

    @Test public void explicitWrapperDouble() {
        assertThat(testResult(ExplicitWrapperDouble.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitWrapperDouble {
        @Theory public void shouldHold(@ForAll @From(ADouble.class) Double d) {
        }
    }

    @Test public void explicitPrimitiveFloat() {
        assertThat(testResult(ExplicitPrimitiveFloat.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitPrimitiveFloat {
        @Theory public void shouldHold(@ForAll @From(AFloat.class) float f) {
        }
    }

    @Test public void explicitWrapperFloat() {
        assertThat(testResult(ExplicitWrapperFloat.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitWrapperFloat {
        @Theory public void shouldHold(@ForAll @From(AFloat.class) Float f) {
        }
    }

    @Test public void explicitPrimitiveInt() {
        assertThat(testResult(ExplicitPrimitiveInt.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitPrimitiveInt {
        @Theory public void shouldHold(@ForAll @From(AnInt.class) int i) {
        }
    }

    @Test public void explicitWrapperInt() {
        assertThat(testResult(ExplicitWrapperInt.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitWrapperInt {
        @Theory public void shouldHold(@ForAll @From(AnInt.class) Integer i) {
        }
    }

    @Test public void explicitPrimitiveLong() {
        assertThat(testResult(ExplicitPrimitiveLong.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitPrimitiveLong {
        @Theory public void shouldHold(@ForAll @From(ALong.class) long ell) {
        }
    }

    @Test public void explicitWrapperLong() {
        assertThat(testResult(ExplicitWrapperLong.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitWrapperLong {
        @Theory public void shouldHold(@ForAll @From(ALong.class) long ell) {
        }
    }

    @Test public void explicitPrimitiveShort() {
        assertThat(testResult(ExplicitPrimitiveShort.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitPrimitiveShort {
        @Theory public void shouldHold(@ForAll @From(AShort.class) short sh) {
        }
    }

    @Test public void explicitWrapperShort() {
        assertThat(testResult(ExplicitWrapperShort.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitWrapperShort {
        @Theory public void shouldHold(@ForAll @From(AShort.class) short sh) {
        }
    }

    @Test public void explicitWithParameterizedType() {
        assertThat(testResult(ExplicitWithParameterizedType.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitWithParameterizedType {
        @Theory public void shouldHold(@ForAll @From(AList.class) List<Foo> list) {
            for (Foo each : list) {
                // ensure the cast works
            }
        }
    }

    @Test public void explicitPrimitiveIntFixedSeed() {
        assertThat(testResult(ExplicitPrimitiveIntFixedSeed.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class ExplicitPrimitiveIntFixedSeed {
        @Theory public void shouldHold(@ForAll(sampleSize = 1, seed = -1) @From(AnInt.class) int i) {
            assertEquals(1155099827, i);
        }
    }


    @Test public void indirectPrimitiveInt() {
        assertThat(testResult(IndirectPrimitiveInt.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class IndirectPrimitiveInt {
        @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
        @Retention(RUNTIME)
        @From(AnInt.class)
        @Between(min = 1, max = 5)
        public @interface Small {
        }

        @Theory public void shouldHold(@ForAll @Small int i) {
            assertThat(i, allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(5)));
        }
    }
}
