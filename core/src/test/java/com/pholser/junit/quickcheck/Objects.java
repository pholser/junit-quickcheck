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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Inspired by <a href="http://aberrantcode.blogspot.com/2007/07/collections-deepequals.html">this blog entry</a>.
 */
public final class Objects {
    private Objects() {
        throw new UnsupportedOperationException();
    }

    public static Matcher<Object> deepEquals(final Object comparand) {
        return new BaseMatcher<Object>() {
            @Override public boolean matches(Object target) {
                return deepEquals(comparand, target);
            }

            @Override public void describeTo(Description description) {
                description.appendText("an object that is deep-equals to ");
                description.appendValue(comparand);
            }
        };
    }

    /**
     * Sensitive to arrays and collections. Collections must have predictable iteration order.
     *
     * @param first comparand
     * @param second comparand
     * @return result of the deep equality comparison
     */
    public static boolean deepEquals(Object first, Object second) {
        if (first == second)
            return true;

        if (first == null || second == null)
            return false;

        if (first instanceof Set<?> && second instanceof Set<?>)
            return linearDeepEquals((Set<?>) first, (Set<?>) second);

        if (first instanceof Collection<?> && second instanceof Collection<?>)
            return linearDeepEquals((Collection<?>) first, (Collection<?>) second);

        if (first instanceof Object[] && second instanceof Object[])
            return linearDeepEquals((Object[]) first, (Object[]) second);

        if (first.getClass().isArray() && second.getClass().isArray())
            return toList(first).equals(toList(second));

        return first.equals(second);
    }

    private static boolean linearDeepEquals(Object[] first, Object[] second) {
        if (first.length != second.length)
            return false;

        for (int i = 0; i < first.length; ++i) {
            if (!deepEquals(first[i], second[i]))
                return false;
        }

        return true;
    }

    private static boolean linearDeepEquals(Collection<?> first, Collection<?> second) {
        if (first.size() != second.size())
            return false;

        Iterator<?> firstIter = first.iterator();
        Iterator<?> secondIter = second.iterator();

        while (firstIter.hasNext()) {
            if (!deepEquals(firstIter.next(), secondIter.next()))
                return false;
        }

        return true;
    }

    private static boolean linearDeepEquals(Set<?> first, Set<?> second) {
        if (first.size() != second.size())
            return false;

        for (Object x : first) {
            boolean found = false;

            for (Object y : second) {
                if (deepEquals(x, y)) {
                    found = true;
                    break;
                }
            }

            if (!found)
                return false;
        }

        return true;
    }

    static List<?> toList(Object array) {
        int length = Array.getLength(array);

        List<Object> items = new ArrayList<>();
        for (int i = 0; i < length; ++i) {
            items.add(Array.get(array, i));
        }

        return items;
    }
}
