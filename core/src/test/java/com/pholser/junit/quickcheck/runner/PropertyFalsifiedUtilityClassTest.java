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

package com.pholser.junit.quickcheck.runner;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.UtilityClassesUninstantiabilityHarness;

import com.pholser.junit.quickcheck.test.generator.AnInt;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class PropertyFalsifiedUtilityClassTest
    extends UtilityClassesUninstantiabilityHarness {

    public PropertyFalsifiedUtilityClassTest() {
        super(PropertyFalsified.class);
    }

    @Test
    public void counterexampleFoundWithAllParametersIsCorrect() {
        String propertyName = "mySuperProperty";
        String[] arguments = {"first", "second", "third"};
        long[] seeds = {12345, 8842};
        String assertionName = "assertion name";
        AssertionError error = new AssertionError(assertionName);

        AssertionError actual =
            PropertyFalsified.counterexampleFound(
                propertyName,
                arguments,
                seeds,
                error);

        String expected =
            format(
                "Property named 'mySuperProperty' failed (assertion name)%n"
                    + "With arguments: [first, second, third]%n"
                    + "Seeds for reproduction: [12345, 8842]");
        assertThat(actual.getMessage(), equalTo(expected));
    }

    @Test
    public void
    counterexampleFoundWhenAssertionErrorPassedHasNoMessageIsCorrect() {
        String propertyName = "mySuperProperty";
        String[] arguments = {"first", "second", "third"};
        long[] seeds = {12345, 8842};
        AssertionError error = new AssertionError();

        AssertionError actual =
            PropertyFalsified.counterexampleFound(
                propertyName,
                arguments,
                seeds,
                error);

        String expected =
            format(
                "Property named 'mySuperProperty' failed:%n"
                    + "With arguments: [first, second, third]%n"
                    + "Seeds for reproduction: [12345, 8842]");
        assertThat(actual.getMessage(), equalTo(expected));
    }

    @Test
    public void smallerCounterexampleFoundWithAllParametersIsCorrect() {
        String propertyName = "mySuperProperty";
        String[] originalArguments = {"first", "second", "third"};
        String[] arguments = {"first"};
        long[] seeds = {12345, 8842};
        String smallerFailureName = "smaller name";
        AssertionError smallerFailure = new AssertionError(smallerFailureName);
        String assertionName = "assertion name";
        AssertionError originalFailure = new AssertionError(assertionName);

        AssertionError actual =
            PropertyFalsified.smallerCounterexampleFound(
                propertyName,
                originalArguments,
                arguments,
                seeds,
                smallerFailure,
                originalFailure);

        String expected =
            format(
                "Property named 'mySuperProperty' failed (smaller name):%n"
                    + "With arguments: [first]%n"
                    + "Original failure message: assertion name%n"
                    + "First arguments found to also provoke a failure: "
                    + "[first, second, third]%n"
                    + "Seeds for reproduction: [12345, 8842]");
        assertThat(actual.getMessage(), equalTo(expected));
    }

    @Test
    public void
    smallerCounterexampleFoundIsCorrectEvenIfSmallerFailureIsNotNamed() {
        String propertyName = "mySuperProperty";
        String[] originalArguments = {"first", "second", "third"};
        String[] arguments = {"first"};
        long[] seeds = {12345, 8842};
        AssertionError smallerFailure = new AssertionError();
        String assertionName = "assertion name";
        AssertionError originalFailure = new AssertionError(assertionName);

        AssertionError actual =
            PropertyFalsified.smallerCounterexampleFound(
                propertyName,
                originalArguments,
                arguments,
                seeds,
                smallerFailure,
                originalFailure);

        String expected =
            format(
                "Property named 'mySuperProperty' failed:%n"
                    + "With arguments: [first]%n"
                    + "Original failure message: assertion name%n"
                    + "First arguments found to also provoke a failure: "
                    + "[first, second, third]%n"
                    + "Seeds for reproduction: [12345, 8842]");
        assertThat(actual.getMessage(), equalTo(expected));
    }

    @Test
    public void
    smallerCounterexampleFoundIsCorrectEvenIfOriginalFailureIsNotNamed() {
        String propertyName = "mySuperProperty";
        String[] originalArguments = {"first", "second", "third"};
        String[] arguments = {"first"};
        long[] seeds = {12345, 8842};
        AssertionError smallerFailure = new AssertionError();
        AssertionError originalFailure = new AssertionError();

        AssertionError actual =
            PropertyFalsified.smallerCounterexampleFound(
                propertyName,
                originalArguments,
                arguments,
                seeds,
                smallerFailure,
                originalFailure);

        String expected =
            format(
                "Property named 'mySuperProperty' failed:%n"
                    + "With arguments: [first]%n"
                    + "First arguments found to also provoke a failure: "
                    + "[first, second, third]%n"
                    + "Seeds for reproduction: [12345, 8842]");
        assertThat(actual.getMessage(), equalTo(expected));
    }

    @Test
    public void github_212_failWithIllegalFormatSpecifierInMessage() {
        assertThat(
            testResult(Failing.class),
            hasFailureContaining("Failure with a %D in the text"));
    }

    @RunWith(JUnitQuickcheck.class)
    public static class Failing {
        @Property public void prop(@From(AnInt.class) int n) {
            fail("Failure with a %D in the text");
        }
    }
}
