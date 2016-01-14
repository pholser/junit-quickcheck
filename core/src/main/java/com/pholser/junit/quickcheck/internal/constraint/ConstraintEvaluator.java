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

package com.pholser.junit.quickcheck.internal.constraint;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class ConstraintEvaluator {
    private final Object constraint;
    private final OgnlContext bindings;

    public ConstraintEvaluator(String expression) {
        try {
            constraint = expression == null ? null : Ognl.parseExpression(expression);
            bindings = new ConstraintOgnlContext();
        } catch (OgnlException ex) {
            throw new EvaluationException(ex);
        }
    }

    public boolean evaluate() {
        try {
            return constraint == null || (Boolean) Ognl.getValue(constraint, bindings, (Object) null);
        } catch (OgnlException ex) {
            throw new EvaluationException(ex);
        }
    }

    public void bind(Object value) {
        bindings.put("_", value);
    }

    public static class EvaluationException extends RuntimeException {
        private static final long serialVersionUID = Integer.MIN_VALUE;

        EvaluationException(String message) {
            super(message);
        }

        EvaluationException(Throwable cause) {
            super(cause);
        }
    }

    private static class ConstraintOgnlContext extends OgnlContext {
        @Override public Object get(Object key) {
            if (!containsKey(key))
                throw new EvaluationException("Referring to undefined variable '" + key + "']");
            return super.get(key);
        }
    }
}
