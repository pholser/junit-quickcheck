package com.pholser.junit.quickcheck;

import java.util.Collection;
import java.util.Iterator;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/** Inspired by <a href="http://aberrantcode.blogspot.com/2007/07/collections-deepequals.html">this blog entry</a>. */
public class Objects {
    private Objects() {
        throw new UnsupportedOperationException();
    }

    public static Matcher<Object> deepEquals(final Object comparand) {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object target) {
                return deepEquals(comparand, target);
            }

            @Override
            public void describeTo(Description description) {
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

        if (first instanceof Collection<?> && second instanceof Collection<?>)
            return linearDeepEquals((Collection<?>) first, (Collection<?>) second);

        if (first instanceof Object[] && second instanceof Object[])
            return linearDeepEquals((Object[]) first, (Object[]) second);

        if (first.getClass().isArray() && second.getClass().isArray())
            return Arrays.toList(first).equals(Arrays.toList(second));

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
}
