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

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.pholser.junit.quickcheck.MinimalCounterexampleHook;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.PropertyParameterContext;
import com.pholser.junit.quickcheck.internal.ShrinkControl;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.AssumptionViolatedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import ru.vyarus.java.generics.resolver.GenericsResolver;

import static java.util.stream.Collectors.*;

import static org.junit.Assert.*;

class PropertyStatement extends Statement {
    private final FrameworkMethod method;
    private final TestClass testClass;
    private final GeneratorRepository repo;
    private final GeometricDistribution distro;
    private final Logger seedLog;
    private final List<AssumptionViolatedException> assumptionViolations = new ArrayList<>();
    private int successes;

    PropertyStatement(
        FrameworkMethod method,
        TestClass testClass,
        GeneratorRepository repo,
        GeometricDistribution distro,
        Logger seedLog) {

        this.method = method;
        this.testClass = testClass;
        this.repo = repo;
        this.distro = distro;
        this.seedLog = seedLog;
    }

    @Override public void evaluate() throws Throwable {
        Property marker = method.getAnnotation(Property.class);
        int trials = marker.trials();
        MinimalCounterexampleHook hook = marker.onMinimalCounterexample().newInstance();
        ShrinkControl shrinkControl = new ShrinkControl(
            marker.shrink(),
            marker.maxShrinks(),
            marker.maxShrinkDepth(),
            marker.maxShrinkTime(),
            hook::handle);

        List<PropertyParameterGenerationContext> params = parameters(trials);

        for (int i = 0; i < trials; ++i)
            verifyProperty(params, shrinkControl);

        if (successes == 0 && !assumptionViolations.isEmpty()) {
            fail("No values satisfied property assumptions. Violated assumptions: "
                + assumptionViolations);
        }
    }

    private void verifyProperty(
        List<PropertyParameterGenerationContext> params,
        ShrinkControl shrinkControl)
        throws Throwable {

        List<SeededValue> seededValues = argumentsFor(params);
        Object[] args = seededValues.stream().map(v -> v.value).toArray();
        long[] seeds = seededValues.stream().mapToLong(SeededValue::seed).toArray();
        property(params, args, seeds, shrinkControl).verify();
    }

    private PropertyVerifier property(
        List<PropertyParameterGenerationContext> params,
        Object[] args,
        long[] initialSeeds,
        ShrinkControl shrinkControl)
        throws InitializationError {

        return new PropertyVerifier(
            testClass,
            method,
            args,
            initialSeeds,
            s -> ++successes,
            assumptionViolations::add,
            (e, action) -> {
                if (!shrinkControl.shouldShrink()) {
                    shrinkControl.onMinimalCounterexample().handle(args, action);
                    throw e;
                }

                try {
                    shrink(params, args, initialSeeds, shrinkControl, e);
                } catch (AssertionError ex) {
                    throw ex;
                } catch (Throwable ex) {
                    throw new AssertionError(ex.getCause());
                }
            }
        );
    }

    private void shrink(
        List<PropertyParameterGenerationContext> params,
        Object[] args,
        long[] initialSeeds,
        ShrinkControl shrinkControl,
        AssertionError failure)
        throws Throwable {

        new Shrinker(
            method,
            testClass,
            failure,
            shrinkControl.maxShrinks(),
            shrinkControl.maxShrinkDepth(),
            shrinkControl.maxShrinkTime(),
            shrinkControl.onMinimalCounterexample())
            .shrink(params, args, initialSeeds);
    }

    private List<PropertyParameterGenerationContext> parameters(int trials) {
        Map<String, Type> typeVariables = GenericsResolver.resolve(testClass.getJavaClass())
            .method(method.getMethod())
            .genericsMap();

        return Arrays.stream(method.getMethod().getParameters())
            .map(p -> parameterContextFor(p, trials, typeVariables))
            .map(p -> new PropertyParameterGenerationContext(
                p,
                repo,
                distro,
                new SourceOfRandomness(new Random()),
                seedLog))
            .collect(toList());
    }

    private PropertyParameterContext parameterContextFor(
        Parameter parameter,
        int trials,
        Map<String, Type> typeVariables) {

        return new PropertyParameterContext(
            new ParameterTypeContext(
                parameter.getName(),
                parameter.getAnnotatedType(),
                declarerName(parameter),
                typeVariables)
                .allowMixedTypes(true),
            trials
        ).annotate(parameter);
    }

    private static String declarerName(Parameter p) {
        Executable exec = p.getDeclaringExecutable();
        return exec.getDeclaringClass().getName() + '.' + exec.getName();
    }

    private List<SeededValue> argumentsFor(List<PropertyParameterGenerationContext> params) {
        return params.stream()
            .map(p -> new SeededValue(p.generate(), p.effectiveSeed()))
            .collect(toList());
    }

    private static class SeededValue {
        private final Object value;
        private final long seed;

        SeededValue(Object value, long seed) {
            this.value = value;
            this.seed = seed;
        }

        Object value() {
            return value;
        }

        long seed() {
            return seed;
        }
    }
}
