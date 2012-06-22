/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Classes.*;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class ForAllMapTheoryParameterTypesTest {
    @Test
    public void huhToHuh() {
        assertThat(testResult(MapOfHuhToHuh.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class MapOfHuhToHuh {
        @Theory
        public void shouldHold(@ForAll Map<?, ?> items) {
        }
    }

    @Test
    public void huhToUpperBound() {
        assertThat(testResult(MapOfHuhToUpperBound.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class MapOfHuhToUpperBound {
        @Theory
        public void shouldHold(@ForAll Map<?, ? extends Short> items) {
            if (!items.isEmpty()) {
                Class<?> superclass = nearestCommonSuperclassOf(classesOf(items.values()));
                assertThat(Short.class, isAssignableFrom(superclass));
            }
        }
    }

    @Test
    public void huhToLowerBound() {
        assertThat(testResult(MapOfHuhToLowerBound.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class MapOfHuhToLowerBound {
        @Theory
        public void shouldHold(@ForAll Map<?, ? super Date> items) {
        }
    }

    @Test
    public void huhToArrayOfSerializable() {
        assertThat(testResult(MapOfHuhToArrayOfSerializable.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class MapOfHuhToArrayOfSerializable {
        @Theory
        public void shouldHold(@ForAll Map<?, Serializable[]> items) {
            for (Serializable[] each : items.values()) {
                for (Serializable s : each) {
                    // ensuring the cast works
                }
            }
        }
    }

    @Test
    public void huhToListOfHuh() {
        assertThat(testResult(MapOfHuhToListOfHuh.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class MapOfHuhToListOfHuh {
        @Theory
        public void shouldHold(@ForAll Map<?, List<?>> items) {
            for (List<?> each : items.values()) {
                // ensuring the cast works
            }
        }
    }

    @Test
    public void huhToMapOfIntegerToString() {
        assertThat(testResult(MapOfHuhToMapOfIntegerToString.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class MapOfHuhToMapOfIntegerToString {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 20) Map<?, Map<Integer, String>> items) {
            for (Map<Integer, String> each : items.values()) {
                for (Map.Entry<Integer, String> eachEntry : each.entrySet()) {
                    // ensuring the cast works
                    Integer key = eachEntry.getKey();
                    String value = eachEntry.getValue();
                }
            }
        }
    }

    @Test
    public void huhToListOfHuhExtendsSerializable() {
        assertThat(testResult(MapOfHuhToListOfHuhExtendsSerializable.class), isSuccessful());
    }

    @RunWith(Theories.class)
    public static class MapOfHuhToListOfHuhExtendsSerializable {
        @Theory
        public void shouldHold(@ForAll(sampleSize = 20) Map<?, List<? extends Serializable>> items) {
            for (List<? extends Serializable> each : items.values()) {
                if (!each.isEmpty()) {
                    Class<?> superclass = nearestCommonSuperclassOf(classesOf(each));
                    assertThat(Serializable.class, isAssignableFrom(superclass));
                }
            }
        }
    }
}
