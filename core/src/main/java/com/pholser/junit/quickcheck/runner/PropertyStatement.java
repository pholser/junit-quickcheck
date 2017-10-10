/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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
import java.util.stream.Stream;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterSampler;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.PropertyParameterContext;
import com.pholser.junit.quickcheck.internal.SeededValue;
import com.pholser.junit.quickcheck.internal.ShrinkControl;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.PropertyParameterGenerationContext;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.sampling.ExhaustiveParameterSampler;
import com.pholser.junit.quickcheck.runner.sampling.TupleParameterSampler;
import org.junit.AssumptionViolatedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import ru.vyarus.java.generics.resolver.GenericsResolver;

import static com.pholser.junit.quickcheck.runner.PropertyFalsified.*;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.*;

class PropertyStatement extends Statement {
    private final FrameworkMethod method;
    private final TestClass testClass;
    private final GeneratorRepository repo;
    private final GeometricDistribution distro;
    private final List<AssumptionViolatedException> assumptionViolations = new ArrayList<>();
    private final Logger logger;

    private int successes;

    PropertyStatement(
        FrameworkMethod method,
        TestClass testClass,
        GeneratorRepository repo,
        GeometricDistribution distro,
        Logger logger) {

        this.method = method;
        this.testClass = testClass;
        this.repo = repo;
        this.distro = distro;
        this.logger = logger;
    }

    @Override public void evaluate() throws Throwable {
        Map<String, Type> typeVariables =
            GenericsResolver.resolve(testClass.getJavaClass())
                .method(method.getMethod())
                .genericsMap();

        Property marker = method.getAnnotation(Property.class);
        ParameterSampler sampler = sampler(marker);
        ShrinkControl shrinkControl = new ShrinkControl(marker);

        List<PropertyParameterGenerationContext> parameters =
            Arrays.stream(method.getMethod().getParameters())
                .map(p -> parameterContextFor(p, typeVariables))
                .map(p -> new PropertyParameterGenerationContext(
                    p,
                    repo,
                    distro,
                    new SourceOfRandomness(new Random()),
                    sampler
                ))
                .collect(toList());

        Stream<List<SeededValue>> sample = sampler.sample(parameters);
        for (List<SeededValue> args : (Iterable<List<SeededValue>>) sample::iterator)
            property(args, shrinkControl).verify();

        if (successes == 0 && !assumptionViolations.isEmpty()) {
            fail("No values satisfied property assumptions. Violated assumptions: "
                + assumptionViolations);
        }
    }

    private PropertyVerifier property(
        List<SeededValue> arguments,
        ShrinkControl shrinkControl)
        throws InitializationError {

        if (logger.isDebugEnabled()) {
            logger.debug(
                "Verifying property {} from {} with these values:",
                method.getName(),
                testClass.getName());
            logger.debug("{}", Arrays.deepToString(arguments.toArray()));
        }

        List<PropertyParameterGenerationContext> params =
            arguments.stream().map(SeededValue::parameter).collect(toList());
        Object[] args = arguments.stream().map(SeededValue::value).toArray();
        long[] seeds = arguments.stream().mapToLong(SeededValue::seed).toArray();

        return new PropertyVerifier(
            testClass,
            method,
            args,
            seeds,
            s -> ++successes,
            assumptionViolations::add,
            (e, action) -> {
                if (!shrinkControl.shouldShrink()) {
                    shrinkControl.onMinimalCounterexample().handle(args, action);
                    throw counterexampleFound(method.getName(), args, seeds, e);
                }

                try {
                    shrink(params, args, seeds, shrinkControl, e);
                } catch (AssertionError ex) {
                    throw ex;
                } catch (Throwable ex) {
                    throw new AssertionError(ex);
                }
            }
        );
    }

    private void shrink(
        List<PropertyParameterGenerationContext> params,
        Object[] args,
        long[] seeds,
        ShrinkControl shrinkControl,
        AssertionError failure)
        throws Throwable {

        new Shrinker(
            method,
            testClass,
            failure,
            shrinkControl,
            logger)
            .shrink(params, args, seeds);
    }

    private PropertyParameterContext parameterContextFor(
        Parameter parameter,
        Map<String, Type> typeVariables) {

        return new PropertyParameterContext(
            new ParameterTypeContext(
                parameter.getName(),
                parameter.getAnnotatedType(),
                declarerName(parameter),
                typeVariables)
                .allowMixedTypes(true)
        ).annotate(parameter);
    }

    private ParameterSampler sampler(Property marker) {
        switch (marker.mode()) {
            case SAMPLING:
                return new TupleParameterSampler(marker.trials());
            case EXHAUSTIVE:
                return new ExhaustiveParameterSampler(marker.trials());
        }

        throw new AssertionError("Don't recognize mode " + marker.mode());
    }

    private static String declarerName(Parameter p) {
        Executable exec = p.getDeclaringExecutable();
        return exec.getDeclaringClass().getName() + '.' + exec.getName();
    }
}
