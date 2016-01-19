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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>JUnit test runner for junit-quickcheck property-based tests.</p>
 *
 * <p>When this runner runs a given test class, it regards only
 * {@code public} instance methods with a return type of {@code void} that are
 * marked with either the {@link com.pholser.junit.quickcheck.Property}
 * annotation or the {@code org.junit.Test} annotation.</p>
 *
 * <p>This runner honors {@link org.junit.Rule}, {@link org.junit.Before},
 * {@link org.junit.After}, {@link org.junit.BeforeClass}, and
 * {@link org.junit.AfterClass}. Their execution is wrapped around the
 * verification of a property or execution of a test in the expected
 * order.</p>
 */
public class JUnitQuickcheck extends BlockJUnit4ClassRunner {
    private final GeneratorRepository repo;
    private final GeometricDistribution distro;
    private final Logger seedLog;

    /**
     * Invoked reflectively by JUnit.
     *
     * @param clazz class containing properties to verify
     * @throws InitializationError if there is a problem with the properties class
     */
    public JUnitQuickcheck(Class<?> clazz) throws InitializationError {
        super(clazz);

        SourceOfRandomness random = new SourceOfRandomness(new Random());
        repo = new GeneratorRepository(random).register(new ServiceLoaderGeneratorSource());
        distro = new GeometricDistribution();
        seedLog = LoggerFactory.getLogger("junit-quickcheck.seed-reporting");
    }

    @Override protected void validateTestMethods(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(Test.class, false, errors);
        validatePropertyMethods(errors);
    }

    private void validatePropertyMethods(List<Throwable> errors) {
        for (FrameworkMethod each : getTestClass().getAnnotatedMethods(Property.class))
            each.validatePublicVoid(false, errors);
    }

    @Override protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> methods = new ArrayList<>();
        methods.addAll(getTestClass().getAnnotatedMethods(Test.class));
        methods.addAll(getTestClass().getAnnotatedMethods(Property.class));
        return methods;
    }

    @Override public Statement methodBlock(FrameworkMethod method) {
        return method.getAnnotation(Test.class) != null
            ? super.methodBlock(method)
            : new PropertyStatement(method, getTestClass(), repo, distro, seedLog);
    }
}
