/*
 The MIT License

 Copyright (c) 2010-2018 Paul R. Holser, Jr.

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
package com.pholser.junit.quickcheck.examples.nullable;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.examples.number.NonNegative;
import com.pholser.junit.quickcheck.generator.java.lang.IntegerGenerator;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.hamcrest.Matchers;
import org.junit.runner.RunWith;

import javax.annotation.Nullable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

@RunWith(JUnitQuickcheck.class)
public class NullableParameterTest {
    @Property public void shouldNotBeNullByDefault(Integer value) {
        assertNotNull(value);
    }

    @Property public void mayBeNull(@Nullable Integer value) {
        assumeThat("Some of the generated values will be null", value, Matchers.nullValue());
    }

    @Property public void mayNotBeNull(@Nullable Integer value) {
        assumeThat("Some of the generated values will not be null", value, Matchers.notNullValue());
    }

    @Property public void nullableAnnotationAndExplicitGenerator(@Nullable @From(IntegerGenerator.class) Integer value) {
        assumeThat("Some of the generated values will be null", value, Matchers.nullValue());
    }

    @Property public void nullableAnnotationAndConfigurationProperty(@Nullable @NonNegative Integer value) {
        assumeThat("Some of the generated values will not be null", value, Matchers.notNullValue());
        assertThat(value, Matchers.greaterThanOrEqualTo(0));
    }

    @Property public void nullableAnnotationOnEnum(@Nullable SomeEnum value) {
        assumeThat("Some of the generated values will be null", value, Matchers.nullValue());
    }

    @Property public void nullableAnnotationOnArray(@Nullable Integer[] value) {
        assumeThat("Some of the generated values will be null", value, Matchers.nullValue());
    }

    @Property public void nullableAnnotationOnPrimitive(@Nullable int value) { // nonsense, but allowed by the compiler
        assertNotNull(value);
    }

    enum SomeEnum { VALUE_1 }
}
