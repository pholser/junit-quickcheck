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

import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.pholser.junit.quickcheck.conversion.StringConversion;
import com.pholser.junit.quickcheck.generator.Also;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.test.generator.ABool;
import com.pholser.junit.quickcheck.test.generator.AByte;
import com.pholser.junit.quickcheck.test.generator.AChar;
import com.pholser.junit.quickcheck.test.generator.ADouble;
import com.pholser.junit.quickcheck.test.generator.AFloat;
import com.pholser.junit.quickcheck.test.generator.ALong;
import com.pholser.junit.quickcheck.test.generator.AShort;
import com.pholser.junit.quickcheck.test.generator.AString;
import com.pholser.junit.quickcheck.test.generator.AnInt;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Annotations.*;
import static com.pholser.junit.quickcheck.Mode.*;
import static java.math.RoundingMode.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ExhaustingAGivenSetButIncludingAnotherTest {
    @Test public void primitiveBooleans() throws Exception {
        assertThat(testResult(PrimitiveBooleans.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveBooleans.iterations);
        assertEquals(
            singletonList(true),
            PrimitiveBooleans.values.subList(0, 1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveBooleans {
        static int iterations;

        private static final List<Boolean> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also("true") @From(ABool.class) boolean b) {
            values.add(b);
            ++iterations;
        }
    }

    @Test public void wrapperBooleans() throws Exception {
        assertThat(testResult(WrapperBooleans.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperBooleans.iterations);
        assertEquals(
            singletonList(false),
            WrapperBooleans.values.subList(0, 1));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperBooleans {
        static int iterations;

        private static final List<Boolean> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also("false") @From(ABool.class) boolean b) {
            values.add(b);
            ++iterations;
        }
    }

    @Test public void primitiveBytes() throws Exception {
        assertThat(testResult(PrimitiveBytes.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveBytes.iterations);
        assertEquals(
            new HashSet<>(asList(Byte.valueOf("12"), Byte.valueOf("-13"))),
            new HashSet<>(PrimitiveBytes.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveBytes {
        static int iterations;

        private static final List<Byte> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"12", "-13"}) @From(AByte.class) byte b) {
            values.add(b);
            ++iterations;
        }
    }

    @Test public void wrapperBytes() throws Exception {
        assertThat(testResult(WrapperBytes.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperBytes.iterations);
        assertEquals(
            new HashSet<>(asList(Byte.valueOf("14"), Byte.valueOf("-15"))),
            new HashSet<>(WrapperBytes.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperBytes {
        static int iterations;

        private static final List<Byte> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"14", "-15"}) @From(AByte.class) Byte b) {
            values.add(b);
            ++iterations;
        }
    }

    @Test public void primitiveChars() throws Exception {
        assertThat(testResult(PrimitiveChars.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveChars.iterations);
        assertEquals(
            new HashSet<>(asList('Z', 'z')),
            new HashSet<>(PrimitiveChars.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveChars {
        static int iterations;

        private static final List<Character> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"Z", "z"}) @From(AChar.class) char ch) {
            values.add(ch);
            ++iterations;
        }
    }

    @Test public void wrapperChars() throws Exception {
        assertThat(testResult(WrapperChars.class), isSuccessful());
        assertEquals(23, WrapperChars.iterations);
        assertEquals(
            new HashSet<>(asList('@', '#')),
            new HashSet<>(WrapperChars.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperChars {
        static int iterations;

        private static final List<Character> values = new ArrayList<>();

        @Property(trials = 23, mode = EXHAUSTIVE)
        public void shouldHold(@Also({"@", "#"}) @From(AChar.class) Character ch) {
            values.add(ch);
            ++iterations;
        }
    }

    @Test public void primitiveDoubles() throws Exception {
        assertThat(testResult(PrimitiveDoubles.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveDoubles.iterations);
        assertEquals(
            new HashSet<>(asList(3.2, -4D)),
            new HashSet<>(PrimitiveDoubles.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveDoubles {
        static int iterations;

        private static final List<Double> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"3.2", "-4"}) @From(ADouble.class) double d) {
            values.add(d);
            ++iterations;
        }
    }

    @Test public void wrapperDoubles() throws Exception {
        assertThat(testResult(WrapperDoubles.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperDoubles.iterations);
        assertEquals(
            new HashSet<>(asList(2.7, -3.14)),
            new HashSet<>(WrapperDoubles.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperDoubles {
        static int iterations;

        private static final List<Double> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"2.7", "-3.14"}) @From(ADouble.class) Double d) {
            values.add(d);
            ++iterations;
        }
    }

    @Test public void primitiveFloats() throws Exception {
        assertThat(testResult(PrimitiveFloats.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveFloats.iterations);
        assertEquals(
            new HashSet<>(asList(3.3F, -5F)),
            new HashSet<>(PrimitiveFloats.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveFloats {
        static int iterations;

        private static final List<Float> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"3.3", "-5"}) @From(AFloat.class) float f) {
            values.add(f);
            ++iterations;
        }
    }

    @Test public void wrapperFloats() throws Exception {
        assertThat(testResult(WrapperFloats.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperFloats.iterations);
        assertEquals(
            new HashSet<>(asList(1.7F, -4.14F)),
            new HashSet<>(WrapperFloats.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperFloats {
        static int iterations;

        private static final List<Float> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"1.7", "-4.14"}) @From(AFloat.class) Float f) {
            values.add(f);
            ++iterations;
        }
    }

    @Test public void primitiveIntegers() throws Exception {
        assertThat(testResult(PrimitiveIntegers.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveIntegers.iterations);
        assertEquals(
            new HashSet<>(asList(1, 2, 3)),
            new HashSet<>(PrimitiveIntegers.values.subList(0, 3)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveIntegers {
        static int iterations;

        private static final List<Integer> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"1", "2", "3"}) @From(AnInt.class) int i) {
            values.add(i);
            ++iterations;
        }
    }

    @Test public void wrapperIntegers() throws Exception {
        assertThat(testResult(WrapperIntegers.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperIntegers.iterations);
        assertEquals(
            new HashSet<>(asList(4, 5)),
            new HashSet<>(WrapperIntegers.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperIntegers {
        static int iterations;

        private static final List<Integer> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"4", "5"}) @From(AnInt.class) Integer i) {
            values.add(i);
            ++iterations;
        }
    }

    @Test public void primitiveLongs() throws Exception {
        assertThat(testResult(PrimitiveLongs.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveLongs.iterations);
        assertEquals(
            new HashSet<>(asList(-6L, -7L, -8L)),
            new HashSet<>(PrimitiveLongs.values.subList(0, 3)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveLongs {
        static int iterations;

        private static final List<Long> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"-6", "-7", "-8"}) @From(ALong.class) long ell) {
            values.add(ell);
            ++iterations;
        }
    }

    @Test public void wrapperLongs() throws Exception {
        assertThat(testResult(WrapperLongs.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperLongs.iterations);
        assertEquals(
            new HashSet<>(asList(10L, 11L, 12L)),
            new HashSet<>(WrapperLongs.values.subList(0, 3)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperLongs {
        static int iterations;

        private static final List<Long> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"10", "11", "12"}) @From(ALong.class) Long ell) {
            values.add(ell);
            ++iterations;
        }
    }

    @Test public void primitiveShorts() throws Exception {
        assertThat(testResult(PrimitiveShorts.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), PrimitiveShorts.iterations);
        assertEquals(
            new HashSet<>(asList(Short.valueOf("9"), Short.valueOf("8"))),
            new HashSet<>(PrimitiveShorts.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class PrimitiveShorts {
        static int iterations;

        private static final List<Short> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"9", "8"}) @From(AShort.class) short sh) {
            values.add(sh);
            ++iterations;
        }
    }

    @Test public void wrapperShorts() throws Exception {
        assertThat(testResult(WrapperShorts.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), WrapperShorts.iterations);
        assertEquals(
            new HashSet<>(asList(Short.valueOf("-13"), Short.valueOf("-14"))),
            new HashSet<>(WrapperShorts.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class WrapperShorts {
        static int iterations;

        private static final List<Short> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"-13", "-14"}) @From(AShort.class) Short sh) {
            values.add(sh);
            ++iterations;
        }
    }

    @Test public void strings() throws Exception {
        assertThat(testResult(Strings.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), Strings.iterations);
        assertEquals(
            new HashSet<>(asList("some", "values")),
            new HashSet<>(Strings.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Strings {
        static int iterations;

        private static final List<String> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"some", "values"}) @From(AString.class) String s) {
            values.add(s);
            ++iterations;
        }
    }

    @Test public void enums() throws Exception {
        assertThat(testResult(Enums.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), Enums.iterations);
        assertEquals(
            EnumSet.of(HALF_UP, HALF_EVEN),
            new HashSet<>(Enums.values.subList(0, 2)));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Enums {
        static int iterations;

        private static final List<RoundingMode> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"HALF_EVEN", "HALF_UP"}) RoundingMode mode) {
            values.add(mode);
            ++iterations;
        }
    }

    @Test public void ctorOnly() throws Exception {
        assertThat(testResult(CtorOnly.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), CtorOnly.iterations);
        assertEquals(
            new HashSet<>(
                asList(new CtorOnly.Target("a"), new CtorOnly.Target("b"))
            ),
            new HashSet<>(CtorOnly.values.subList(0, 2)));
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
                return o instanceof Target && Objects.equals(s, ((Target) o).s);
            }
        }

        public static class Targets extends Generator<Target> {
            public Targets() {
                super(Target.class);
            }

            @Override public Target generate(SourceOfRandomness r, GenerationStatus s) {
                String value = "abcdefghij";
                int index = r.nextInt(value.length());
                return new Target("abcdefghij".substring(index));
            }
        }

        static int iterations;

        private static final List<Target> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"a", "b"}) @From(Targets.class) Target t) {
            values.add(t);
            ++iterations;
        }
    }

    @Test public void favorValueOf() throws Exception {
        assertThat(testResult(FavorValueOf.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), FavorValueOf.iterations);
        assertEquals(
            new HashSet<>(
                asList(
                    FavorValueOf.Target.valueOf("a"),
                    FavorValueOf.Target.valueOf("b"))
            ),
            new HashSet<>(FavorValueOf.values.subList(0, 2)));
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
                return o instanceof Target && Objects.equals(s, ((Target) o).s);
            }

            public static Target valueOf(String s) {
                Target t = new Target();
                t.s = s;
                return t;
            }
        }

        public static class Targets extends Generator<Target> {
            public Targets() {
                super(Target.class);
            }

            @Override public Target generate(SourceOfRandomness r, GenerationStatus s) {
                String value = "abcdefghij";
                int index = r.nextInt(value.length());
                return Target.valueOf("abcdefghij".substring(index));
            }
        }

        static int iterations;

        private static final List<Target> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"a", "b"}) @From(Targets.class) Target t) {
            values.add(t);
            ++iterations;
        }
    }

    @Test public void noImplicitConversion() throws Exception {
        assertThat(
            testResult(NoImplicitConversion.class),
            hasSingleFailureContaining(ReflectionException.class.getName()));
        assertEquals(0, NoImplicitConversion.iterations);
    }

    @RunWith(JUnitQuickcheck.class)
    public static class NoImplicitConversion {
        public static final class Target {
        }

        static int iterations;

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(@Also({"a", "b"}) Target t) {
            ++iterations;
        }
    }

    @Test public void explicitConversion() throws Exception {
        assertThat(testResult(ExplicitConversion.class), isSuccessful());
        assertEquals(defaultPropertyTrialCount(), ExplicitConversion.iterations);
        assertEquals(
            new HashSet<>(asList(
                LocalDate.of(2017, 1, 1),
                LocalDate.of(2001, 12, 25))),
            new HashSet<>(ExplicitConversion.values.subList(0, 2)));
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

        public static final class LocalDates extends Generator<LocalDate> {
            public LocalDates() {
                super(LocalDate.class);
            }

            @Override public LocalDate generate(SourceOfRandomness r, GenerationStatus s) {
                return Instant.ofEpochMilli(r.nextLong())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            }
        }

        static int iterations;

        private static final List<LocalDate> values = new ArrayList<>();

        @Property(mode = EXHAUSTIVE)
        public void shouldHold(
            @Also(value = {"2017/01/01", "2001/12/25"}, by = YYYYMMDD.class)
            @From(LocalDates.class)
            LocalDate d) {

            values.add(d);
            ++iterations;
        }
    }

    @Test public void manyParameters() throws Exception {
        assertThat(testResult(ManyParameters.class), isSuccessful());
        assertEquals(16, ManyParameters.iterations);
        assertEquals(
            asList(-1, -2, -4),
            ManyParameters.firstTestCases.subList(0, 3));
        assertEquals(
            asList(-1, -2, -4),
            ManyParameters.firstTestCases.subList(4, 7));
        assertEquals(
            asList(-1, -2, -4),
            ManyParameters.firstTestCases.subList(8, 11));
        assertEquals(
            asList(-1, -2, -4),
            ManyParameters.firstTestCases.subList(12, 15));
        assertEquals(
            asList('r', 'r', 'r', 'r', 'y', 'y', 'y', 'y'),
            ManyParameters.secondTestCases.subList(0, 8));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ManyParameters {
        static int iterations;

        private static final List<Integer> firstTestCases = new LinkedList<>();
        private static final List<Character> secondTestCases = new LinkedList<>();

        @Property(trials = 4, mode = EXHAUSTIVE) public void shouldHold(
            @Also({"-1", "-2", "-4"}) @From(AnInt.class) int i,
            @Also({"r", "y"}) @From(AChar.class) char ch) {

            firstTestCases.add(i);
            secondTestCases.add(ch);
            ++iterations;
        }
    }

    @Test public void manyParametersWithBooleanAndEnum() throws Exception {
        assertThat(testResult(ManyParametersWithBooleanAndEnum.class), isSuccessful());

        int expectedCount = 4 * 4 * 2 * RoundingMode.values().length;
        assertEquals(
            expectedCount,
            ManyParametersWithBooleanAndEnum.iterations);
        for (int i = 0; i < expectedCount / 4; ++i) {
            assertEquals(
                String.valueOf(i),
                asList(3, 7),
                ManyParametersWithBooleanAndEnum.firstTestCases.subList(i * 4, i * 4 + 2));
        }
        for (int i = 0; i < expectedCount / 16; ++i) {
            assertEquals(
                String.valueOf(i),
                asList('a', 'a', 'a', 'a', 'b', 'b', 'b', 'b', 'c', 'c', 'c', 'c'),
                ManyParametersWithBooleanAndEnum.secondTestCases.subList(i * 16, i * 16 + 12));
        }
        for (int i = 0; i < expectedCount / 32; ++i) {
            assertEquals(
                asList(false, false, false, false, false, false, false, false,
                    false, false, false, false, false, false, false, false),
                ManyParametersWithBooleanAndEnum.thirdTestCases.subList(i * 32, i * 32 + 16));
            assertEquals(
                asList(true, true, true, true, true, true, true, true,
                    true, true, true, true, true, true, true, true),
                ManyParametersWithBooleanAndEnum.thirdTestCases.subList(i * 32 + 16, i * 32 + 32));
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ManyParametersWithBooleanAndEnum {
        static int iterations;

        private static final List<Integer> firstTestCases = new LinkedList<>();
        private static final List<Character> secondTestCases = new LinkedList<>();
        private static final List<Boolean> thirdTestCases = new LinkedList<>();
        private static final List<RoundingMode> fourthTestCases = new LinkedList<>();

        @Property(trials = 4, mode = EXHAUSTIVE)
        public void shouldHold(
            @Also({"3", "7"}) @From(AnInt.class) int i,
            @Also({"a", "b", "c"}) @From(AChar.class) char ch,
            boolean b,
            RoundingMode mode) {

            firstTestCases.add(i);
            secondTestCases.add(ch);
            thirdTestCases.add(b);
            fourthTestCases.add(mode);
            ++iterations;
        }
    }
}
