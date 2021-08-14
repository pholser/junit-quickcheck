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

package com.pholser.junit.quickcheck;

import static com.pholser.junit.quickcheck.Annotations.defaultPropertyTrialCount;
import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.hasSingleFailureContaining;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import com.pholser.junit.quickcheck.conversion.StringConversion;
import com.pholser.junit.quickcheck.generator.Also;
import com.pholser.junit.quickcheck.generator.Only;
import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import com.pholser.junit.quickcheck.test.generator.Between;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;

public class SamplingOnlyFromAGivenSetTest {
    @Test public void primitiveBooleans() throws Exception {
        assertThat(testResult(PrimitiveBooleans.class), isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            PrimitiveBooleans.iterations);
        PrimitiveBooleans.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveBooleans {
        static int iterations;

        private static final Set<Boolean> candidates = singleton(true);

        @Property public void shouldHold(@Only("true") boolean b) {
            assertTrue(candidates.contains(b));
            ++iterations;
        }
    }

    @Test public void wrapperBooleans() throws Exception {
        assertThat(testResult(WrapperBooleans.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperBooleans.iterations);
        WrapperBooleans.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperBooleans {
        static int iterations;

        private static final Set<Boolean> candidates = singleton(false);

        @Property public void shouldHold(@Only("false") Boolean b) {
            assertTrue(candidates.contains(b));
            ++iterations;
        }
    }

    @Test public void primitiveBytes() throws Exception {
        assertThat(testResult(PrimitiveBytes.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveBytes.iterations);
        PrimitiveBytes.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveBytes {
        static int iterations;

        private static final Set<Byte> candidates =
            new HashSet<>(asList(Byte.valueOf("12"), Byte.valueOf("-13")));

        @Property public void shouldHold(@Only({"12", "-13"}) byte b) {
            assertTrue(candidates.contains(b));
            ++iterations;
        }
    }

    @Test public void wrapperBytes() throws Exception {
        assertThat(testResult(WrapperBytes.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperBytes.iterations);
        WrapperBytes.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperBytes {
        static int iterations;

        private static final Set<Byte> candidates =
            new HashSet<>(asList(Byte.valueOf("14"), Byte.valueOf("-15")));

        @Property public void shouldHold(@Only({"14", "-15"}) Byte b) {
            assertTrue(candidates.contains(b));
            ++iterations;
        }
    }

    @Test public void primitiveChars() throws Exception {
        assertThat(testResult(PrimitiveChars.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveChars.iterations);
        PrimitiveChars.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveChars {
        static int iterations;

        private static final Set<Character> candidates =
            new HashSet<>(asList('Z', 'z'));

        @Property public void shouldHold(@Only({"Z", "z"}) char ch) {
            assertTrue(candidates.contains(ch));
            ++iterations;
        }
    }

    @Test public void wrapperChars() throws Exception {
        assertThat(testResult(WrapperChars.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperChars.iterations);
        WrapperChars.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperChars {
        static int iterations;

        private static final Set<Character> candidates =
            new HashSet<>(asList('@', '#'));

        @Property public void shouldHold(@Only({"@", "#"}) Character ch) {
            assertTrue(candidates.contains(ch));
            ++iterations;
        }
    }

    @Test public void primitiveDoubles() throws Exception {
        assertThat(testResult(PrimitiveDoubles.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveDoubles.iterations);
        PrimitiveDoubles.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveDoubles {
        static int iterations;

        private static final Set<Double> candidates =
            new HashSet<>(asList(3.2, -4D));

        @Property public void shouldHold(@Only({"3.2", "-4"}) double d) {
            assertTrue(candidates.contains(d));
            ++iterations;
        }
    }

    @Test public void wrapperDoubles() throws Exception {
        assertThat(testResult(WrapperDoubles.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperDoubles.iterations);
        WrapperDoubles.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperDoubles {
        static int iterations;

        private static final Set<Double> candidates =
            new HashSet<>(asList(2.7, -3.14));

        @Property public void shouldHold(@Only({"2.7", "-3.14"}) Double d) {
            assertTrue(candidates.contains(d));
            ++iterations;
        }
    }

    @Test public void primitiveFloats() throws Exception {
        assertThat(testResult(PrimitiveFloats.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveFloats.iterations);
        PrimitiveFloats.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveFloats {
        static int iterations;

        private static final Set<Float> candidates =
            new HashSet<>(asList(3.3F, -5F));

        @Property public void shouldHold(@Only({"3.3", "-5"}) float f) {
            assertTrue(candidates.contains(f));
            ++iterations;
        }
    }

    @Test public void wrapperFloats() throws Exception {
        assertThat(testResult(WrapperFloats.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperFloats.iterations);
        WrapperFloats.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperFloats {
        static int iterations;

        private static final Set<Float> candidates =
            new HashSet<>(asList(1.7F, -4.14F));

        @Property public void shouldHold(@Only({"1.7", "-4.14"}) Float f) {
            assertTrue(candidates.contains(f));
            ++iterations;
        }
    }

    @Test public void primitiveIntegers() throws Exception {
        assertThat(testResult(PrimitiveIntegers.class), isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            PrimitiveIntegers.iterations);
        PrimitiveIntegers.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveIntegers {
        static int iterations;

        private static final Set<Integer> candidates =
            new HashSet<>(asList(1, 2, 3));

        @Property public void shouldHold(@Only({"1", "2", "3"}) int i) {
            assertTrue(candidates.contains(i));
            ++iterations;
        }
    }

    @Test public void wrapperIntegers() throws Exception {
        assertThat(testResult(WrapperIntegers.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperIntegers.iterations);
        WrapperIntegers.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperIntegers {
        static int iterations;

        private static final Set<Integer> candidates =
            new HashSet<>(asList(4, 5));

        @Property public void shouldHold(@Only({"4", "5"}) Integer i) {
            assertTrue(candidates.contains(i));
            ++iterations;
        }
    }

    @Test public void primitiveLongs() throws Exception {
        assertThat(testResult(PrimitiveLongs.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveLongs.iterations);
        PrimitiveLongs.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveLongs {
        static int iterations;

        private static final Set<Long> candidates =
            new HashSet<>(asList(-6L, -7L, -8L));

        @Property public void shouldHold(@Only({"-6", "-7", "-8"}) long ell) {
            assertTrue(candidates.contains(ell));
            ++iterations;
        }
    }

    @Test public void wrapperLongs() throws Exception {
        assertThat(testResult(WrapperLongs.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperLongs.iterations);
        WrapperLongs.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperLongs {
        static int iterations;

        private static final Set<Long> candidates =
            new HashSet<>(asList(10L, 11L, 12L));

        @Property public void shouldHold(@Only({"10", "11", "12"}) Long ell) {
            assertTrue(candidates.contains(ell));
            ++iterations;
        }
    }

    @Test public void primitiveShorts() throws Exception {
        assertThat(testResult(PrimitiveShorts.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveShorts.iterations);
        PrimitiveShorts.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveShorts {
        static int iterations;

        private static final Set<Short> candidates =
            new HashSet<>(asList(Short.valueOf("9"), Short.valueOf("8")));

        @Property public void shouldHold(@Only({"9", "8"}) short sh) {
            assertTrue(candidates.contains(sh));
            ++iterations;
        }
    }

    @Test public void wrapperShorts() throws Exception {
        assertThat(testResult(WrapperShorts.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperShorts.iterations);
        WrapperShorts.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperShorts {
        static int iterations;

        private static final Set<Short> candidates =
            new HashSet<>(asList(Short.valueOf("-13"), Short.valueOf("-14")));

        @Property public void shouldHold(@Only({"-13", "-14"}) Short sh) {
            assertTrue(candidates.contains(sh));
            ++iterations;
        }
    }

    @Test public void strings() throws Exception {
        assertThat(testResult(Strings.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), Strings.iterations);
        Strings.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Strings {
        static int iterations;

        private static final Set<String> candidates =
            new HashSet<>(asList("some", "values"));

        @Property public void shouldHold(@Only({"some", "values"}) String s) {
            assertTrue(candidates.contains(s));
            ++iterations;
        }
    }

    @Test public void enums() throws Exception {
        assertThat(testResult(Enums.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), Enums.iterations);
        Enums.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Enums {
        static int iterations;

        private static final EnumSet<RoundingMode> candidates =
            EnumSet.of(HALF_UP, HALF_EVEN);

        @Property public void shouldHold(
            @Only({"HALF_EVEN", "HALF_UP"}) RoundingMode mode) {

            assertTrue(candidates.contains(mode));
            ++iterations;
        }
    }

    @Test public void ctorOnly() throws Exception {
        assertThat(testResult(CtorOnly.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), CtorOnly.iterations);
        CtorOnly.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class CtorOnly {
        public static final class Target {
            private final String s;

            public Target(String s) {
                this.s = s;
            }

            @Override public int hashCode() {
                return Objects.hashCode(s);
            }

            @Override public boolean equals(Object o) {
                return o instanceof Target
                    && Objects.equals(s, ((Target) o).s);
            }
        }

        static int iterations;

        private static final Set<Target> candidates =
            new HashSet<>(asList(new Target("a"), new Target("b")));

        @Property public void shouldHold(@Only({"a", "b"}) Target t) {
            assertTrue(candidates.contains(t));
            ++iterations;
        }
    }

    @Test public void favorValueOf() throws Exception {
        assertThat(testResult(FavorValueOf.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), FavorValueOf.iterations);
        FavorValueOf.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class FavorValueOf {
        public static final class Target {
            private String s;

            private Target() {
                // empty on purpose
            }

            public Target(String s) {
                throw new UnsupportedOperationException(s);
            }

            @Override public int hashCode() {
                return Objects.hashCode(s);
            }

            @Override public boolean equals(Object o) {
                return o instanceof Target
                    && Objects.equals(s, ((Target) o).s);
            }

            public static Target valueOf(String s) {
                Target t = new Target();
                t.s = s;
                return t;
            }
        }

        static int iterations;

        private static final Set<Target> candidates =
            new HashSet<>(asList(Target.valueOf("a"), Target.valueOf("b")));

        @Property public void shouldHold(@Only({"a", "b"}) Target t) {
            assertTrue(candidates.contains(t));
            ++iterations;
        }
    }

    @Test public void noImplicitConversion() {
        assertThat(
            testResult(NoImplicitConversion.class),
            hasSingleFailureContaining(ReflectionException.class.getName()));
        assertEquals(0, NoImplicitConversion.iterations);
        NoImplicitConversion.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class NoImplicitConversion {
        public static final class Target {
        }

        static int iterations;

        @Property public void shouldHold(@Only({"a", "b"}) Target t) {
            ++iterations;
        }
    }

    @Test public void explicitConversion() throws Exception {
        assertThat(testResult(ExplicitConversion.class), isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            ExplicitConversion.iterations);
        ExplicitConversion.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ExplicitConversion {
        public static final class YYYYMMDD implements StringConversion {
            private final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy/MM/dd");

            @Override public LocalDate convert(String raw) {
                return LocalDate.parse(raw, formatter);
            }
        }

        static int iterations;

        private static final Set<LocalDate> candidates =
            new HashSet<>(asList(
                LocalDate.of(2017, 1, 1),
                LocalDate.of(2001, 12, 25)));

        @Property public void shouldHold(
            @Only(value = {"2017/01/01", "2001/12/25"}, by = YYYYMMDD.class)
                LocalDate d) {

            assertTrue(candidates.contains(d));
            ++iterations;
        }
    }

    @Test public void manyParameters() throws Exception {
        assertThat(testResult(ManyParameters.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), ManyParameters.iterations);
        ManyParameters.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ManyParameters {
        static int iterations;

        private static final Set<Integer> firstCandidates =
            new HashSet<>(asList(-1, -2, -4));
        private static final Set<Character> secondCandidates =
            new HashSet<>(asList('r', 'y'));

        @Property public void shouldHold(
            @Only({"-1", "-2", "-4"}) int i,
            @Only({"r", "y"}) char ch) {

            assertTrue(firstCandidates.contains(i));
            assertTrue(secondCandidates.contains(ch));
            ++iterations;
        }
    }

    @Test public void onlyTrumpsAlso() throws Exception {
        assertThat(testResult(OnlyTrumpsAlso.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), OnlyTrumpsAlso.iterations);
        OnlyTrumpsAlso.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class OnlyTrumpsAlso {
        private static int iterations;

        private static final Set<Integer> candidates =
            new HashSet<>(asList(1, 2, 3));

        @Property public void shouldHold(
            @Only({"1", "2", "3"}) @Also({"4", "5"}) int i) {

            assertTrue(candidates.contains(i));
            ++iterations;
        }
    }

    @Test public void onlyTrumpsGenerators() throws Exception {
        assertThat(testResult(OnlyTrumpsGenerators.class), isSuccessful());
        assertEquals(
            defaultPropertyTrialCount(),
            OnlyTrumpsGenerators.iterations);
        OnlyTrumpsGenerators.iterations = 0;
    }

    @RunWith(JUnitQuickcheck.class)
    public static class OnlyTrumpsGenerators {
        private static int iterations;

        private static final Set<Integer> candidates =
            new HashSet<>(asList(1, 2, 3));

        @Property public void shouldHold(
            @Only({"1", "2", "3"})
            @From(AnInt.class)
            @Between(min = 4, max = 5)
                int i) {

            assertTrue(candidates.contains(i));
            ++iterations;
        }
    }
}
