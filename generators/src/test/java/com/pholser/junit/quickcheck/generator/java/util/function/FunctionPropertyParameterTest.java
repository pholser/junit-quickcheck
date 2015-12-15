package com.pholser.junit.quickcheck.generator.java.util.function;

import java.util.Date;
import java.util.function.Function;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.java.util.DateGenerator;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class FunctionPropertyParameterTest {
    @Test public void unresolvedTypes() {
        assertThat(testResult(Unresolved.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Unresolved<A, R> {
        @Property public void typesAreOk(Function<? super A, ? extends R> f, A arg) {
            R result = f.apply(arg);
        }
    }

    @Test public void unresolvedArgType() {
        assertThat(testResult(UnresolvedArgType.class), isSuccessful());
    }

    public static class UnresolvedArgType<A> extends Unresolved<A, Integer> {
        @Property public void consistent(Function<? super A, Integer> f, A arg) {
            Integer result = f.apply(arg);

            for (int i = 0; i < 10000; ++i)
                assertEquals(result, f.apply(arg));
        }
    }

    @Test public void resolvedTypes() {
        assertThat(testResult(ResolvedTypes.class), isSuccessful());
    }

    public static class ResolvedTypes extends UnresolvedArgType<Date> {
    }
}
