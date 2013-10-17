# junit-quickcheck: QuickCheck-style parameter suppliers for JUnit theories

junit-quickcheck is a library that supplies [JUnit](http://junit.org)
[theories](http://groups.csail.mit.edu/pag/pubs/test-theory-demo-oopsla2007.pdf) with random values with which to test
the validity of the theories.

**PLEASE NOTE**: junit-quickcheck uses a
[version of the JUnit theories runner](https://github.com/junit-team/junit.contrib/tree/master/theories) that has been
modified to respect generics on theory parameter types, as described
[here](http://github.com/junit-team/junit/issues/64). The classes that comprise this rendition of the JUnit theories
runner are packaged as `org.junit.contrib.theories.*`, rather than `org.junit.experimental.theories.*`.
Be sure to use the `contrib` version of the runner, annotations, etc. with junit-quickcheck.

### Downloading

junit-quickcheck's framework is contained in the JAR file for the module `junit-quickcheck-core`. You will
want to start out also with the JAR file for the module `junit-quickcheck-generators`, which consists of generators
for theory parameters of basic Java types, such as primitives, arrays, and collections.

There is also a module `junit-quickcheck-guava`, containing generators for
[Guava](https://code.google.com/p/guava-libraries/) types.

Releases are synced to the central Maven repository. Declare `<dependency>` elements in your POM like so:

    ...
    <dependencies>
      ...
      <dependency>
        <groupId>com.pholser</groupId>
        <artifactId>junit-quickcheck-core</artifactId>
        <version>0.3</version>
      </dependency>
      <dependency>
        <groupId>com.pholser</groupId>
        <artifactId>junit-quickcheck-generators</artifactId>
        <version>0.2</version>
      </dependency>
      ...
    </dependencies>
    ...

### Background

The [Haskell](http://haskell.org) library [QuickCheck](http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html)
allows programmers to specify properties of a function that should hold true for some large set of possible arguments
to the function, then executes the function using lots of random arguments to see whether the property holds up.

#### Theories

JUnit's answer to function properties is the notion of _theories_. Programmers write parameterized tests marked as
theories, run using a special test runner:

    @RunWith(Theories.class)
    public class Accounts {
        @Theory public void withdrawingReducesBalance(Money originalBalance, Money withdrawalAmount) {
            assumeThat(originalBalance, greaterThan(Money.NONE));
            assumeThat(withdrawalAmount, allOf(greaterThan(Money.NONE), lessThan(originalBalance)));

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

    import com.pholser.junit.quickcheck.ForAll;

    @RunWith(Theories.class)
    public class SymmetricKeyCryptography {
        @Theory public void decryptReversesEncrypt(@ForAll String plaintext, @ForAll Key key) throws Exception {
            Crypto crypto = new Crypto();

            byte[] ciphertext = crypto.encrypt(plaintext.getBytes("US-ASCII"), key);

            assertEquals(plaintext, new String(crypto.decrypt(ciphertext, key)));
        }
    }

#### Sample size

By default, 100 random values will be generated for a parameter marked `@ForAll`. Use the `sampleSize` attribute of
`@ForAll` to change the number of generated values.

#### Supported types

Out of the box (core + generators), junit-quickcheck can generate random values for theory parameters of the following
types:

* all Java primitives and primitive wrappers
* `java.math.Big(Decimal|Integer)`
* `java.util.Date`
* any `enum`
* `String`
* "functional interfaces" (interfaces with a single method that does not override a method from `java.lang.Object`)
* `java.util.ArrayList` of supported types
* `java.util.HashSet` of supported types
* `java.util.HashMap` of supported types
* arrays of supported types

When multiple generators can satisfy a given theory parameter based on its type (for example, `java.io.Serializable`),
on a given generation one of the multiple generators will be chosen at random with equal probability.

#### Generating values of other types

If you wish to generate random values for theory parameters of other types, or to override the default means of
generation for a supported type, mark the theory parameter already marked as `@ForAll` with `@From` and supply the
class(es) of the `Generator` to be used. If you give multiple classes in `@From`, one will be chosen on every
generation with equal probability.

If you wish to add a generator for a type without having to use `@From`, you can package your `Generator` in a
[ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) JAR file and place the JAR on
the class path. junit-quickcheck will make those generators available for use. The generators in the module
`junit-quickcheck-generators` are loaded via this mechanism also; any generators you supply and make available to
the `ServiceLoader` complement these generators rather than override them.

Custom generators for types that are functional interfaces override the built-in means of generation for such types.
This is usually necessary for functional interfaces that involve generics.

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
        @Theory public void holds(@ForAll @Stuff Foo f) {
            // ...
        }
    }

A `Generator` can have many such `configure` methods.

Generators of "componentized" types such as arrays and lists pass configurations on parameters of their type to
generators of the component types.

#### Constraining generated values

##### Assumptions

Theories often use _assumptions_ to declare conditions under which the theories hold:

    @RunWith(Theories.class)
    public class PrimeFactorsTheories {
        @Theory public void factorsPassPrimalityTest(@ForAll int n) {
            assumeThat(n, greaterThan(0));

            for (int each : PrimeFactors.of(n))
                assertTrue(BigInteger.valueOf(each).isProbablePrime(1000));
        }

        @Theory public void factorsMultiplyToOriginal(@ForAll int n) {
            assumeThat(n, greaterThan(0));

            int product = 1;
            for (int each : PrimeFactors.of(n))
                product *= each;

            assertEquals(n, product);
        }

        @Theory public void factorizationsAreUnique(@ForAll int m, @ForAll int n) {
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
        @Theory public void hold(@ForAll int digit) {
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
        @Theory public void hold(@ForAll @InRange(minInt = 0, maxInt = 9) int digit) {
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

`boolean` and `enum` theory parameters can be annotated with `@ValuesOf` to force the generation to run through
every value in the type's domain, instead of choosing an element from the domain at random every time. This also
effectively dictates the sample size for the parameter.

    enum Ternary { YES, NO, MAYBE }

    @RunWith(Theories.class)
    public class TheoriesOfSmallDomains {
        @Theory public void hold(@ForAll @ValuesOf boolean b, @ForAll @ValuesOf Ternary t) {
            // sample sizes of 2 and 3, respectively. Each combination of potential values will be generated.
        }
    }


##### Constraint expressions

Constraint expressions enable you to filter the values that reach a theory parameter. Mark a theory parameter already
marked as `@ForAll` with `@SuchThat`, supplying an [OGNL](http://commons.apache.org/ognl) expression that will be used
to decide whether a generated value will be given to the theory method.

    @RunWith(Theories.class)
    public class SingleDigitTheories {
        @Theory public void hold(@ForAll @SuchThat("#_ >= 0 && #_ <= 9") int digit) {
            // ...
        }
    }

A theory parameter is referred to as "_" in the constraint expression. Constraint expressions cannot refer to other
theory parameters.

junit-quickcheck generates values for a theory parameter with a constraint expression until `sampleSize` values pass
the constraint, or until the ratio of constraint passes to constraint failures is greater than the `discardRatio`
specified by `@ForAll`, if any. Exceeding the discard ratio raises an exception and thus fails the theory.

### How it works

junit-quickcheck leverages the `ParameterSupplier` feature of the JUnit theories machinery.

By default, when the `Theories` runner executes a theory, it attempts to scrape _data points_ off the theory class to
feed to the theories. Data points come from static fields or methods annotated with `@DataPoint` (single value) or
`@DataPoints` (array of values). The `Theories` runner arranges for all combinations of data points of types matching a
theory parameter to be fed to the theory for execution.

Marking a theory parameter with an annotation that is itself annotated with `@ParametersSuppliedBy`
tells the `Theories` runner to ask a `ParameterSupplier` for values for the theory parameter instead. This is how
junit-quickcheck interacts with the `Theories` runner -- `@ForAll` tells the runner to use junit-quickcheck's
`ParameterSupplier` rather than the `DataPoint`-oriented one.

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

    Copyright (c) 2010-2013 Paul R. Holser, Jr.

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
