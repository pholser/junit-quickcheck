/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.internal.constraint;

import static com.pholser.junit.quickcheck.internal.constraint.ConstraintEvaluator.EvaluationException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ConstraintEvaluatorTest {
    private ConstraintEvaluator evaluator;

    @Before public void beforeEach() {
        evaluator = new ConstraintEvaluator("#_ > 0");
    }

    @Test public void whenExpressionEvaluatesTrue() {
        evaluator.bind(1);

        assertTrue(evaluator.evaluate());
    }

    @Test public void whenExpressionEvaluatesFalse() {
        evaluator.bind(-2);

        assertFalse(evaluator.evaluate());
    }

    @Test public void whenExpressionIsMalformed() {
        assertThrows(
            EvaluationException.class,
            () -> new ConstraintEvaluator("#_ !*@&#^*"));
    }

    @Test public void whenExpressionCannotBeEvaluatedCorrectly() {
        evaluator = new ConstraintEvaluator("#_.foo == 'bar'");
        evaluator.bind(4);

        assertThrows(
            EvaluationException.class,
            () -> evaluator.evaluate());
    }

    @Test public void whenExpressionContainsAnUndefinedVariable() {
        evaluator = new ConstraintEvaluator("#x == -3");
        evaluator.bind(-3);

        assertThrows(
            EvaluationException.class,
            () -> evaluator.evaluate());
    }
}
