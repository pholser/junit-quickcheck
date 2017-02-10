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

package com.pholser.junit.quickcheck.generator;

import java.util.Optional;

import com.google.common.testing.EqualsTester;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus.Key;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;
import static org.junit.rules.ExpectedException.*;

public class GenerationStatusOnPropertyParametersTest {
    @Rule public final ExpectedException thrown = none();

    @Test public void sizeNeverExceedsSampleSize() throws Exception {
        assertThat(testResult(SizeProperties.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SizeProperties {
        public static class AnObject extends Generator<Object> {
            public AnObject() {
                super(Object.class);
            }

            @Override public Object generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                assertThat(status.size(), lessThanOrEqualTo(50));
                return this;
            }
        }

        @Property(trials = 50) public void holds(@From(AnObject.class) Object o) {
            // yay, all good
        }
    }

    @Test public void contextValuesForEachParameter() throws Exception {
        assertThat(testResult(ContextValues.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ContextValues {
        public static final Key<Integer> KEY =
            new Key<>("key", Integer.class);

        public static class ASubObject extends Generator<Object> {
            public ASubObject() {
                super(Object.class);
            }

            @Override public Object generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                return status.valueOf(KEY);
            }
        }

        public static class AnObject extends Generator<Object> {
            private final ASubObject subObject;

            public AnObject() {
                super(Object.class);
                this.subObject = new ASubObject();
            }

            @Override public Object generate(
                SourceOfRandomness random,
                GenerationStatus status) {

                int value = status.size();
                status.setValue(KEY, value);

                return subObject.generate(random, status);
            }
        }

        @Property(trials = 10) public void holds(@From(AnObject.class) Object o) {
            assertThat(o, instanceOf(Optional.class));

            @SuppressWarnings("unchecked")
            Optional<Integer> i = (Optional<Integer>) o;

            assertTrue(i.isPresent());
            assertThat(i.get(), lessThanOrEqualTo(10));
        }
    }

    @Test public void rejectsNullStatusKeyName() {
        thrown.expect(NullPointerException.class);

        new Key<>(null, int.class);
    }

    @Test public void rejectsNullStatusKeyType() {
        thrown.expect(NullPointerException.class);

        new Key<>("name", null);
    }

    @Test public void downcastsValues() {
        Object o = 12;

        new Key<>("name", Integer.class).cast(o);
    }

    @Test public void equalsAndHashCodeOnStatusKeys() {
        new EqualsTester()
            .addEqualityGroup(new Key<>("a", String.class), new Key<>("a", String.class))
            .addEqualityGroup(new Key<>("a", Integer.class), new Key<>("a", Integer.class))
            .addEqualityGroup(new Key<>("b", String.class), new Key<>("b", String.class))
            .addEqualityGroup(true, true)
            .testEquals();
    }
}
