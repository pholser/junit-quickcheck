# junit-quickcheck: QuickCheck-style parameter suppliers for JUnit theories

junit-quickcheck is a library that supplies [JUnit](http://junit.org)
[theories](http://groups.csail.mit.edu/pag/pubs/test-theory-demo-oopsla2007.pdf)
with random values with which to test the validity of the theories.

### Background

The [Haskell](http://haskell.org) library
[QuickCheck](http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html)
allows programmers to specify properties of a function that should hold true
for some large set of possible arguments to the function, then executes the
function using lots of random arguments to see whether the property holds up.

#### Theories

JUnit's answer to function properties is the notion of theories. Programmers
write parameterized tests marked as theories, run using a special test runner:

    @RunWith(Theories.class)
    public class Accounts {
        @Theory
        public void withdrawingReducesBalance(Money originalBalance, Money withdrawalAmount) {
            Account account = new Account(originalBalance);

            account.withdraw(withdrawalAmount);

            assertEquals(originalBalance.minus(withdrawalAmount), account.balance());
        }
    }

#### So?
TDD/BDD builds up designs example by example. The resulting test suites give
programmers confidence that their code works for the examples they thought of.
Theories offer a means to express statements about code that should hold for
an entire domain of inputs, not just a handful of examples, and validate those
statements against lots of randomly generated inputs.

### Using junit-quickcheck

Create theories as you normally would with JUnit. If you want JUnit to exercise
the theory with lots of randomly generated values for a theory parameter,
annotate the theory parameter with `@ForAll`:

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

By default, 100 random values will be generated for a parameter marked
`@ForAll`. Use the `sampleSize` attribute of `@ForAll` to change the number of
generated values.

#### Supported types

Out of the box, junit-quickcheck can generate random values for theory
parameters of the following types:

* all Java primitives and primitive wrappers
* `java.math.Big(Decimal|Integer)`
* `java.util.Date`
* any `enum`
* `java.util.ArrayList` of those types
* `java.util.HashSet` of those types
* `java.util.HashMap` of those types
* arrays

Sample size is disregarded for booleans and `enum`s. Booleans will effectively have a sample size of 2; `true` and `false` are both considered when running a theory with a boolean parameter. `enum`s will effectively have a sample size equal to the number of values of the `enum`; all these values are considered when running a theory with an `enum` parameter.

When multiple generators can satisfy a given theory parameter based on its type
(for example, `java.io.Serializable`), on a given generation one of the multiple
generators will be chosen at random with equal probability.

#### Using other types

If you wish to generate random values for theory parameters of other types, or
to override the default means of generation for a supported type, annotate the
theory parameter with `@From` and supply the class(es) of the `Generator` to be
used. If you give multiple classes in `@From`, one will be chosen on every
generation with equal probability.

If you wish to add a generator for a type without having to use `@From`, you can
package your `Generator` in a
[ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html)
JAR file and place the JAR on the class path. junit-quickcheck will make those
generators available for use. Any generators you supply in this manner for
already-supported types complement the built-in generators, they do not override
them.

### How it works

junit-quickcheck leverages the little-known
[`ParameterSupplier`](http://kentbeck.github.com/junit/javadoc/latest/org/junit/experimental/theories/ParameterSupplier.html)
feature of JUnit.

By default, when the `Theories` runner executes a theory, it attempts to scrape
_data points_ off the theory class to feed to the theories. Data points come
from static fields or methods annotated with `@DataPoint` (single value) or
`@DataPoints` (array of values). The `Theories` runner arranges for all
combinations of data points of types matching a theory parameter to be fed to
the theory for execution.

Marking a theory parameter with an annotation that is itself annotated with
[`@ParametersSuppliedBy`](http://kentbeck.github.com/junit/javadoc/latest/org/junit/experimental/theories/ParametersSuppliedBy.html)
tells the `Theories` runner to ask a `ParameterSupplier` for values for the
theory parameter instead. This is how junit-quickcheck interacts with the
`Theories` runner -- `@ForAll` tells the runner to use junit-quickcheck's
`ParameterSupplier` rather than the `DataPoint`-oriented one.

In order for junit-quickcheck (or any `ParameterSupplier`, for that matter) to
be able to leverage generics info on a theory parameter (e.g. to distinguish
between a parameter of type `List<String>` and a parameter of type
`List<Integer>`),
[this issue](https://github.com/KentBeck/junit/issues#issue/64) will need to be
resolved.

### Similar projects

* [JCheck](http://www.jcheck.org/). This uses its own test runner, whereas
junit-quickcheck leverages the existing `Theories` runner and
`ParameterSupplier`s.
* [QuickCheck](http://java.net/projects/quickcheck/pages/Home). This appears to
be test framework-agnostic, focusing instead on generators of random values.
* [fj.test package of FunctionalJava (formerly Reductio)](http://functionaljava.org/)
* [ScalaCheck](http://code.google.com/p/scalacheck/), if you wish to test Java
or Scala code using Scala.

### About junit-quickcheck

junit-quickcheck was written by Paul Holser, and is distributed under the MIT
License.

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

