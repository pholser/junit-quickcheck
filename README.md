# junit-quickcheck: QuickCheck-style parameter suppliers for JUnit theories

junit-quickcheck is a library that supplies [JUnit](http://junit.org)
[theories](http://groups.csail.mit.edu/pag/pubs/test-theory-demo-oopsla2007.pdf) with random values with which to test
the validity of the theories.

### Background

The [Haskell](http://haskell.org) library [QuickCheck](http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html)
allows programmers to specify properties of a function that should hold true for some large set of possible arguments
to the function, then executes the function using lots of random arguments to see whether the property holds up.

#### Theories

JUnit's answer to function properties is the notion of _theories_. Programmers write parameterized tests marked as
theories, run using a special test runner:

    @RunWith(Theories.class)
    public class Accounts {
        @Theory
        public void withdrawingReducesBalance(Money originalBalance, Money withdrawalAmount) {
            assumeThat(originalBalance, greaterThan(0));
            assumeThat(withdrawalAmount, allOf(greaterThan(0), lessThan(originalBalance));

            Account account = new Account(originalBalance);

            account.withdraw(withdrawalAmount);

            assertEquals(originalBalance.minus(withdrawalAmount), account.balance());
        }
    }

#### So?

TDD/BDD builds up designs example by example. The resulting test suites give programmers confidence that their code
works for the examples they thought of. Theories offer a means to express statements about code that should hold for
an entire domain of inputs, not just a handful of examples, and to validate those statements against lots of randomly
generated inputs.

### Using junit-quickcheck

Create theories as you normally would with JUnit. If you want JUnit to exercise the theory with lots of randomly
generated values for a theory parameter, mark the theory parameter with `@ForAll`:

    @RunWith(Theories.class)
    public class SymmetricKeyCryptography {
        @Theory
        public void decryptReversesEncrypt(@ForAll String plaintext, @ForAll Key key) throws Exception {
            Crypto crypto = new Crypto();

            byte[] ciphertext = crypto.encrypt(plaintext.getBytes("US-ASCII", key));

            assertEquals(plaintext, new String(crypto.decrypt(ciphertext, key)));
        }
    }

#### Sample size

By default, 100 random values will be generated for a parameter marked `@ForAll`. Use the `sampleSize` attribute of
`@ForAll` to change the number of generated values.

#### Supported types

Out of the box, junit-quickcheck can generate random values for theory parameters of the following types:

* all Java primitives and primitive wrappers
* `java.math.Big(Decimal|Integer)`
* `java.util.Date`
* any `enum`
* String
* `java.util.ArrayList` of those types
* `java.util.HashSet` of those types
* `java.util.HashMap` of those types
* arrays of those types

When multiple generators can satisfy a given theory parameter based on its type (for example, `java.io.Serializable`),
on a given generation one of the multiple generators will be chosen at random with equal probability.

#### Generating values of other types

If you wish to generate random values for theory parameters of other types, or to override the default means of
generation for a supported type, mark the theory parameter already marked as `@ForAll` with `@From` and supply the
class(es) of the `Generator` to be used. If you give multiple classes in `@From`, one will be chosen on every
generation with equal probability.

If you wish to add a generator for a type without having to use `@From`, you can package your `Generator` in a
[ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) JAR file and place the JAR on
the class path. junit-quickcheck will make those generators available for use. Any generators you supply in this manner
for already-supported types complement the built-in generators, they do not override them.

#### Configuring generators

Over the period of generating values for a single theory parameter, you can feed specific configurations to the
generator(s) for values of the parameter's type. If you mark a theory parameter already marked as `@ForAll` with an
annotation that is itself marked as `@GeneratorConfiguration`, then if the `Generator` for that parameter's type has a
public method named `configure` that accepts a single parameter of the annotation type, junit-quickcheck will call the
`configure` method reflectively, passing it the annotation:

    @Target(PARAMETER)
    @Retention(RUNTIME)
    @GeneratorConfiguration
    public @interface Stuff {
        // ...
    }

    public class FooGenerator extends Generator<Foo> {
        // ...

        public void configure(@Stuff stuff) {
            // ...
        }
    }

    @RunWith(Theories.class)
    public class FooTheories {
        @Theory
        public void holds(@ForAll @Stuff Foo f) {
            // ...
        }
    }

A `Generator` can have many such `configure` methods.

#### Constraining generated values

##### Assumptions

Theories often use _assumptions_ to declare conditions under which the theories hold:

    @RunWith(Theories.class)
    public class PrimeFactorsTheories {
        @Theory
        public void factorsPassPrimalityTest(@ForAll int n) {
            assumeThat(n, greaterThan(0));

            for (int each : PrimeFactors.of(n))
                assertTrue(BigInteger.valueOf(each).isProbablePrime(1000));
        }

        @Theory
        public void factorsMultiplyToOriginal(@ForAll int n) {
            assumeThat(n, greaterThan(0));

            int product = 1;
            for (int each : PrimeFactors.of(n))
                product *= each;

            assertEquals(n, product);
        }

        @Theory
        public void factorizationsAreUnique(@ForAll int m, @ForAll int n) {
            assumeThat(m, greaterThan(0));
            assumeThat(n, greaterThan(0));
            assumeThat(m, not(equalTo(n)));

            assertThat(PrimeFactors.of(m), not(equalTo(PrimeFactors.of(n))));
        }
    }

There are times when using assumptions with junit-quickcheck may yield too few values that meet the desired
criteria:

    @RunWith(Theories.class)
    public class SingleDigitTheories {
        @Theory
        public void hold(@ForAll int digit) {
            // hope we get enough single digits
            assumeThat(digit, greaterThanOrEqualTo(0));
            assumeThat(digit, lessThanOrEqualTo(9));

            // ...
        }
    }

Here, junit-quickcheck will generate 100 values, but there's not much guarantee that we'll get very many, if any,
values to test out the theory.

##### Generator configuration methods

Generator configuration methods and annotations can serve to constrain the values that a generator emits. For example,
the `@InRange` annotation on theory parameters of integral, floating-point, and `Date` types causes the generators for
those types to emit values that fall within a configured minimum/maximum:

    @RunWith(Theories.class)
    public class SingleDigitTheories {
        @Theory
        public void hold(@ForAll @InRange(minInt = 0, maxInt = 9) int digit) {
            // ...
        }
    }

Now, the generator will be configured to ensure that every value generated meets the desired criteria -- no need to
couch the desired range of values as an assumption.

When using assumptions with junit-quickcheck, every value fed to a `@ForAll` theory parameter counts against the
sample size, even if it doesn't pass any assumptions made against it in the theory. You could end up with no values
passing the assumption.

Using generator configurations, assumptions aren't very important, if needed at all -- every value fed to a `@ForAll`
theory parameter counts against the sample size, but will meet some expectations that assumptions would otherwise have
tested.

##### Constraint expressions

Constraint expressions enable you to filter the values that reach a theory parameter. Mark a theory parameter already
marked as `@ForAll` with `@SuchThat`, supplying an [OGNL](http://commons.apache.org/ognl) expression that will be used
to decide whether a generated value will be given to the theory method.

    @RunWith(Theories.class)
    public class SingleDigitTheories {
        @Theory
        public void hold(@ForAll @SuchThat("#digit >= 0 && #digit <= 9") int digit) {
            // ...
        }
    }

You can refer to the name of the theory parameter as an OGNL variable reference in the constraint expression.
Constraint expressions cannot refer to other theory parameters.

junit-quickcheck generates values for a theory parameter with a constraint expression until `sampleSize` values pass
the constraint, or until the ratio of constraint passes to constraint failures is greater than the `discardRatio`
specified by `@ForAll`, if any. Exceeding the discard ratio raises an exception and thus fails the theory.

### How it works

junit-quickcheck leverages the
[`ParameterSupplier`](http://kentbeck.github.com/junit/javadoc/latest/org/junit/experimental/theories/ParameterSupplier.html)
feature of JUnit.

By default, when the `Theories` runner executes a theory, it attempts to scrape _data points_ off the theory class to
feed to the theories. Data points come from static fields or methods annotated with `@DataPoint` (single value) or
`@DataPoints` (array of values). The `Theories` runner arranges for all combinations of data points of types matching a
theory parameter to be fed to the theory for execution.

Marking a theory parameter with an annotation that is itself annotated with
[`@ParametersSuppliedBy`](http://kentbeck.github.com/junit/javadoc/latest/org/junit/experimental/theories/ParametersSuppliedBy.html)
tells the `Theories` runner to ask a `ParameterSupplier` for values for the theory parameter instead. This is how
junit-quickcheck interacts with the `Theories` runner -- `@ForAll` tells the runner to use junit-quickcheck's
`ParameterSupplier` rather than the `DataPoint`-oriented one.

In order for junit-quickcheck (or any `ParameterSupplier`, for that matter) to be able to leverage generics info on a
theory parameter (e.g. to distinguish between a parameter of type `List<String>` and a parameter of type
`List<Integer>`), [this issue](https://github.com/KentBeck/junit/issues#issue/64) will need to be resolved.

### Similar projects

* [JCheck](http://www.jcheck.org/). This uses its own test runner, whereas junit-quickcheck leverages the existing
`Theories` runner and `ParameterSupplier`s.
* [QuickCheck](http://java.net/projects/quickcheck/pages/Home). This appears to be test framework-agnostic, focusing
instead on generators of random values.
* [fj.test package of FunctionalJava (formerly Reductio)](http://functionaljava.org/)
* [ScalaCheck](http://code.google.com/p/scalacheck/), if you wish to test Java or Scala code using Scala.

### About junit-quickcheck

junit-quickcheck was written by Paul Holser, and is distributed under the MIT License.

    The MIT License

    Copyright (c) 2010-2012 Paul R. Holser, Jr.

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
