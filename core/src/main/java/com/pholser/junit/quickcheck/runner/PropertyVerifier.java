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

package com.pholser.junit.quickcheck.runner;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.junit.AssumptionViolatedException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import static java.util.Arrays.*;

class PropertyVerifier extends BlockJUnit4ClassRunner {
    private final FrameworkMethod method;
    private final Object[] args;
    private final long[] initialSeeds;
    private final Consumer<Void> onSuccess;
    private final Consumer<AssumptionViolatedException> onAssumptionViolated;
    private final BiConsumer<AssertionError, Runnable> onFailure;

    PropertyVerifier(
        TestClass clazz,
        FrameworkMethod method,
        Object[] args,
        long[] initialSeeds,
        Consumer<Void> onSuccess,
        Consumer<AssumptionViolatedException> onAssumptionViolated,
        BiConsumer<AssertionError, Runnable> onFailure)
        throws InitializationError {

        super(clazz.getJavaClass());
        this.method = method;
        this.args = args;
        this.initialSeeds = initialSeeds;
        this.onSuccess = onSuccess;
        this.onAssumptionViolated = onAssumptionViolated;
        this.onFailure = onFailure;
    }

    void verify() throws Throwable {
        methodBlock().evaluate();
    }

    private Statement methodBlock() {
        Statement statement = super.methodBlock(method);
        return new Statement() {
            @Override public void evaluate() throws Throwable {
                try {
                    statement.evaluate();
                    onSuccess.accept(null);
                } catch (AssumptionViolatedException e) {
                    onAssumptionViolated.accept(e);
                } catch (AssertionError e) {
                    Runnable repeat = () -> {
                        try {
                            statement.evaluate();
                        } catch (Throwable throwable) {}
                    };
                    onFailure.accept(e, repeat);
                } catch (Throwable e) {
                    reportErrorWithArguments(e);
                }
            }
        };
    }

    @Override protected void collectInitializationErrors(List<Throwable> errors) {
        // do nothing
    }

    @Override protected Statement methodInvoker(
        FrameworkMethod frameworkMethod,
        Object test) {

        return new Statement() {
            @Override public void evaluate() throws Throwable {
                frameworkMethod.invokeExplosively(test, args);
            }
        };
    }

    private void reportErrorWithArguments(Throwable e) {
        throw new AssertionError(
            String.format(
                "Unexpected error in property %s with args %s and initial seeds %s",
                method.getName(),
                asList(args),
                asList(initialSeeds)),
            e);
    }
}
