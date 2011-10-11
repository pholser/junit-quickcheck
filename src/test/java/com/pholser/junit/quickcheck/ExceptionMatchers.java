package com.pholser.junit.quickcheck;

import java.lang.reflect.InvocationTargetException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ExceptionMatchers {
    private ExceptionMatchers() {
        throw new UnsupportedOperationException();
    }

    public static Matcher<InvocationTargetException> targetOfType(final Class<? extends Throwable> type) {
        return new TypeSafeMatcher<InvocationTargetException>() {
            @Override
            public boolean matchesSafely(InvocationTargetException item) {
                return type.isInstance(item.getTargetException());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("InvocationTargetException with target whose type is ");
                description.appendValue(type);
            }
        };
    }
}
