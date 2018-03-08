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

package com.pholser.junit.quickcheck.runner;

import java.util.Arrays;

final class PropertyFalsified {
    private PropertyFalsified() {
        throw new UnsupportedOperationException();
    }

    static AssertionError counterexampleFound(
        String propertyName,
        Object[] args,
        long[] seeds,
        AssertionError e) {

        String message = "Property named '%s' failed%s%n"
            + "With arguments: %s%n"
            + "Seeds for reproduction: %s";

        return new AssertionError(
            String.format(
                message,
                propertyName,
                e.getMessage() == null
                    ? ":"
                    : " (" + e.getMessage() + "):",
                Arrays.deepToString(args),
                Arrays.toString(seeds)),
            e);
    }

    static AssertionError smallerCounterexampleFound(
        String propertyName,
        Object[] originalArgs,
        Object[] args,
        long[] seeds,
        AssertionError smallerFailure,
        AssertionError originalFailure) {

        String message = "Property named '%s' failed%s%n"
            + "With arguments: %s%n"
            + (originalFailure.getMessage() == null
                ? ""
                : "Original failure message: "
                    + originalFailure.getMessage()
                    + "%n"
            )
            + "First arguments found to also provoke a failure: %s%n"
            + "Seeds for reproduction: %s";

        AssertionError e = new AssertionError(
            String.format(
                message,
                propertyName,
                smallerFailure.getMessage() == null
                    ? ":"
                    : " (" + smallerFailure.getMessage() + "):",
                Arrays.deepToString(args),
                Arrays.deepToString(originalArgs),
                Arrays.toString(seeds)),
            originalFailure);

        e.setStackTrace(smallerFailure.getStackTrace());
        return e;
    }
}
