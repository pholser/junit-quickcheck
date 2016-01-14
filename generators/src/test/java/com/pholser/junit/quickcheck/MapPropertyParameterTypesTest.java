/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Classes.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class MapPropertyParameterTypesTest {
    @Test public void huhToHuh() {
        assertThat(testResult(MapOfHuhToHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfHuhToHuh {
        @Property(trials = 20) public void shouldHold(Map<?, ?> items) {
        }
    }

    @Test public void huhToUpperBound() {
        assertThat(testResult(MapOfHuhToUpperBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfHuhToUpperBound {
        @Property(trials = 20) public void shouldHold(Map<?, ? extends Short> items) {
            if (!items.isEmpty()) {
                assertThat(
                    Short.class,
                    isAssignableFrom(nearestCommonSuperclassOf(classesOf(items.values()))));
            }
        }
    }

    @Test public void huhToLowerBound() {
        assertThat(testResult(MapOfHuhToLowerBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfHuhToLowerBound {
        @Property(trials = 20) public void shouldHold(Map<?, ? super Date> items) {
        }
    }

    @Test public void huhToArrayOfSerializable() {
        assertThat(testResult(MapOfHuhToArrayOfSerializable.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfHuhToArrayOfSerializable {
        @Property(trials = 20) public void shouldHold(Map<?, Serializable[]> items) {
            for (Serializable[] each : items.values()) {
                for (Serializable s : each) {
                    // ensuring the cast works
                }
            }
        }
    }

    @Test public void huhToListOfHuh() {
        assertThat(testResult(MapOfHuhToListOfHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfHuhToListOfHuh {
        @Property(trials = 20) public void shouldHold(Map<?, List<?>> items) {
            for (List<?> each : items.values()) {
                // ensuring the cast works
            }
        }
    }

    @Test public void huhToMapOfIntegerToString() {
        assertThat(testResult(MapOfHuhToMapOfIntegerToString.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfHuhToMapOfIntegerToString {
        @Property(trials = 20) public void shouldHold(Map<?, Map<Integer, String>> items) {
            for (Map<Integer, String> each : items.values()) {
                for (Map.Entry<Integer, String> eachEntry : each.entrySet()) {
                    // ensuring the cast works
                    Integer key = eachEntry.getKey();
                    String value = eachEntry.getValue();
                }
            }
        }
    }

    @Test public void shrinkingMapOfIntegerToMapOfByteToShort() {
        assertThat(testResult(ShrinkingMapOfIntegerToMapOfByteToShort.class), failureCountIs(1));
        assertThat(ShrinkingMapOfIntegerToMapOfByteToShort.failed.size(), greaterThan(3));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class ShrinkingMapOfIntegerToMapOfByteToShort {
        static Map<Integer, Map<Byte, Short>> failed;

        @Property(trials = 20) public void shouldHold(Map<Integer, Map<Byte, Short>> items) {
            assumeThat(items.size(), greaterThan(3));

            // ensuring casts work
            for (Map.Entry<Integer, Map<Byte, Short>> eachEntry : items.entrySet()) {
                Integer key = eachEntry.getKey();
                Map<Byte, Short> value = eachEntry.getValue();
                for (Map.Entry<Byte, Short> eachSubEntry : value.entrySet()) {
                    Byte b = eachSubEntry.getKey();
                    Short sh = eachSubEntry.getValue();
                }
            }

            failed = items;

            fail();
        }
    }

    @Test public void huhToListOfUpperBound() {
        assertThat(testResult(MapOfHuhToListOfUpperBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfHuhToListOfUpperBound {
        @Property(trials = 20) public void shouldHold(Map<?, List<? extends Serializable>> items) {
            for (List<? extends Serializable> each : items.values()) {
                if (!each.isEmpty()) {
                    assertThat(
                        Serializable.class,
                        isAssignableFrom(nearestCommonSuperclassOf(classesOf(each))));
                }
            }
        }
    }

    @Test public void huhToSetOfLowerBound() {
        assertThat(testResult(MapOfHuhToSetOfLowerBound.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfHuhToSetOfLowerBound {
        @Property(trials = 20) public void shouldHold(Map<?, Set<? super HashMap<?, ?>>> items) {
            for (Set<? super HashMap<?, ?>> each : items.values()) {
                for (Object eachItem : each) {
                }
            }
        }
    }

    @Test public void upperBoundToHuh() {
        assertThat(testResult(MapOfUpperBoundToHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfUpperBoundToHuh {
        @Property(trials = 20) public void shouldHold(Map<? extends Number, ?> items) {
            if (!items.isEmpty()) {
                assertThat(
                    Number.class,
                    isAssignableFrom(nearestCommonSuperclassOf(classesOf(items.keySet()))));
            }
        }
    }

    @Test public void lowerBoundToHuh() {
        assertThat(testResult(MapOfLowerBoundToHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfLowerBoundToHuh {
        @Property(trials = 20) public void shouldHold(Map<? super int[], ?> items) {
        }
    }

    @Test public void arrayOfDateToHuh() {
        assertThat(testResult(MapOfArrayOfDateToHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfArrayOfDateToHuh {
        @Property(trials = 20) public void shouldHold(Map<Date[], ?> items) {
            for (Date[] each : items.keySet()) {
                for (Date d : each) {
                    // ensuring the cast works
                }
            }
        }
    }

    @Test public void setOfHuhToHuh() {
        assertThat(testResult(SetOfHuhToHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class SetOfHuhToHuh {
        @Property(trials = 20) public void shouldHold(Map<Set<?>, ?> items) {
            for (Set<?> each : items.keySet()) {
                // ensuring the cast works
            }
        }
    }

    @Test public void mapOfIntegerToStringToMapOfShortToDate() {
        assertThat(testResult(MapOfIntegerToStringToMapOfShortToDate.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapOfIntegerToStringToMapOfShortToDate {
        @Property(trials = 20) public void shouldHold(Map<Map<Integer, String>, Map<Short, Date>> items) {
            for (Map.Entry<Map<Integer, String>, Map<Short, Date>> entry : items.entrySet()) {
                Map<Integer, String> byInteger = entry.getKey();
                Map<Short, Date> byShort = entry.getValue();
                for (Map.Entry<Integer, String> integerEntry : byInteger.entrySet()) {
                    // ensuring the cast works
                    Integer key = integerEntry.getKey();
                    String value = integerEntry.getValue();
                }
                for (Map.Entry<Short, Date> shortEntry : byShort.entrySet()) {
                    // ensuring the cast works
                    Short key = shortEntry.getKey();
                    Date value = shortEntry.getValue();
                }
            }
        }
    }

    @Test public void iterableOfUpperBoundToHuh() {
        assertThat(testResult(IterableOfUpperBoundToHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class IterableOfUpperBoundToHuh {
        @Property(trials = 20) public void shouldHold(Map<Iterable<? extends Number>, ?> items) {
            for (Iterable<? extends Number> each : items.keySet()) {
                if (!Iterables.isEmpty(each)) {
                    assertThat(
                        Number.class,
                        isAssignableFrom(nearestCommonSuperclassOf(classesOf(each))));
                }
            }
        }
    }

    @Test public void collectionOfLowerBoundToHuh() {
        assertThat(testResult(CollectionOfLowerBoundToHuh.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class CollectionOfLowerBoundToHuh {
        @Property(trials = 20) public void shouldHold(Map<Collection<? super List<?>>, ?> items) {
            for (Collection<? super List<?>> each : items.keySet()) {
                for (Object item : each) {
                }
            }
        }
    }

    @Test public void mapMerging() {
        assertThat(testResult(MapMerging.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class MapMerging {
        @Property public void mergeMapSize(Map<Integer, Integer> m1, Map<Integer, Integer> m2) {
            int intersectionSize = intersectByKeys(m1, m2).size();
            int mergeSize = merge(asList(m1, m2)).size();

            assertEquals(
                m1.getClass().getName() + '/' + m2.getClass().getName(),
                m1.size() + m2.size(),
                mergeSize + intersectionSize);
        }

        private static <K, V> Map<K, V> merge(Collection<Map<K, V>> maps) {
            Map<K, V> result = new HashMap<>();
            maps.forEach(result::putAll);
            return result;
        }

        private static <K1, V1, V2> Map<K1, V1> intersectByKeys(Map<K1, V1> m1, Map<K1, V2> m2) {
            Map<K1, V1> result = new HashMap<>();
            m1.forEach((k1, v1) -> {
                if (m2.containsKey(k1))
                    result.put(k1, v1);
            });
            return result;
        }
    }
}
