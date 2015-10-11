# junit-quickcheck: Property-based testing, JUnit-style

junit-quickcheck is a library that supplies [JUnit](http://junit.org)
[theories](http://groups.csail.mit.edu/pag/pubs/test-theory-demo-oopsla2007.pdf)
with random values with which to test the validity of the theories.

```java
    import com.pholser.junit.quickcheck.ForAll;
    import org.junit.contrib.theories.Theories;
    import org.junit.contrib.theories.Theory;
    import org.junit.runner.RunWith;

    import static org.junit.Assert.*;

    @RunWith(Theories.class)
    public class StringTheories {
        @Theory public void concatenationLength(
            @ForAll String s1,
            @ForAll String s2) {

            assertEquals(s1.length() + s2.length(), (s1 + s2).length());
        }
    }
```

As of version 0.5, junit-quickcheck is built with JDK 8, and
source/target-compatible with 1.8 and beyond.

**PLEASE NOTE**: junit-quickcheck uses a
[version of the JUnit theories runner]
(https://github.com/junit-team/junit.contrib/tree/master/theories) 
that has been modified to respect generics on theory parameter types, as
described [here](http://github.com/junit-team/junit/issues/64). The classes
that comprise this rendition of the JUnit theories runner are packaged as
`org.junit.contrib.theories.*`, rather than
`org.junit.experimental.theories.*`. Be sure to use the `contrib` version of
the runner, annotations, etc. with junit-quickcheck.


### Downloading

junit-quickcheck's basic machinery is contained in the JAR file for the
module `junit-quickcheck-core`. You will want to start out also with the
JAR file for the module `junit-quickcheck-generators`, which consists of
generators for theory parameters of basic Java types, such as
primitives, arrays, and collections.

There is also a module `junit-quickcheck-guava`, containing generators for
[Guava](https://code.google.com/p/guava-libraries/) types.

Releases are synced to the central Maven repository. Declare `<dependency>`
elements in your POM like so:

```xml
    ...
    <dependencies>
      ...
      <dependency>
        <groupId>com.pholser</groupId>
        <artifactId>junit-quickcheck-core</artifactId>
        <version>0.5</version>
      </dependency>
      <dependency>
        <groupId>com.pholser</groupId>
        <artifactId>junit-quickcheck-generators</artifactId>
        <version>0.5</version>
      </dependency>
      ...
    </dependencies>
    ...
```    

### Discussing

There is [a Google group for junit-quickcheck]
(https://groups.google.com/d/forum/junit-quickcheck).


### Background

The [Haskell](http://haskell.org) library [QuickCheck]
(http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html) allows programmers
to specify properties of a function that should hold true for some large
(potentially infinite) set of possible arguments to the function, then
executes the function using lots of random arguments to see whether the
property holds up against them.


#### Theories

JUnit's answer to function properties is the notion of _theories_.
Programmers write parameterized tests marked as theories, run using a
special test runner. 

```java
    import org.junit.contrib.theories.*;
    import static org.hamcrest.Matchers.*;
    import static org.junit.Assert.*;
    import static org.junit.Assume.*;

    // Imagining the existence of classes Money and Account...
    @RunWith(Theories.class)
    public class Accounts {
        @Theory public void withdrawingReducesBalance(
            Money originalBalance,
            Money withdrawalAmount) {

            assumeThat(originalBalance, greaterThan(Money.NONE));
            assumeThat(
                withdrawalAmount,
                allOf(greaterThan(Money.NONE), lessThan(originalBalance)));

            Account account = new Account(originalBalance);

            account.withdraw(withdrawalAmount);

            assertEquals(
                originalBalance.minus(withdrawalAmount),
                account.balance());
        }
    }
```


#### So?

TDD/BDD builds up designs example by example. The resulting test suites
give programmers confidence that their code works for the examples they
thought of. Theories offer a means to express statements about code that
should hold for an entire domain of inputs, not just a handful of examples,
and to validate those statements against lots of randomly generated inputs.


### Using junit-quickcheck

Create theories as you normally would with JUnit. To exercise the theory
with lots of randomly generated values for a theory parameter, mark the theory
parameter with `@ForAll`:

```java
    import com.pholser.junit.quickcheck.ForAll;

    // Imagining the existence of class Crypto...
    @RunWith(Theories.class)
    public class SymmetricKeyCryptography {
        @Theory public void decryptReversesEncrypt(
            @ForAll String plaintext,
            @ForAll Key key) throws Exception {

            Crypto crypto = new Crypto();

            byte[] ciphertext =
                crypto.encrypt(plaintext.getBytes("US-ASCII"), key);

            assertEquals(
                plaintext,
                new String(crypto.decrypt(ciphertext, key)));
        }
    }
```


#### Supported types

Out of the box (core + generators), junit-quickcheck recognizes theory
parameters of the following types:

* all Java primitives and primitive wrappers
* `java.math.Big(Decimal|Integer)`
* `java.util.Date`
* any `enum`
* `String`
* "functional interfaces" (interfaces with a single method that does not
  override a method from `java.lang.Object`)
* `java.util.ArrayList` and `java.util.LinkedList` of supported types
* `java.util.HashSet` and `java.util.LinkedHashSet` of supported types
* `java.util.HashMap` and `java.util.LinkedHashMap` of supported types
* arrays of supported types
* others...

When many generators can satisfy a given theory parameter based on its type
(for example, `java.io.Serializable`), on a given generation junit-quickcheck
will choose one of the multiple generators at random with (roughly) equal
probability.


#### Generating values of other types

To generate random values for theory parameters of other types, or to override
the default means of generation for a supported type, mark the theory
parameter already marked as `@ForAll` with `@From` and supply the class(es) of
the `Generator` to be used. If you give multiple `@From` annotations,
junit-quickcheck will choose one on every generation with probability in
proportion to its `frequency` attribute (default is 1).

```java
    @RunWith(Theories.class)
    public class IdentificationTheories {
        @Theory public void shouldHold(@ForAll @From(Version5.class) UUID u) {
            // ...
        }
    }
```

To add a generator for a type without having to use `@From`, you can package
your `Generator` in a [ServiceLoader]
(http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html)
JAR file and place the JAR on the class path. junit-quickcheck will make those
generators available for use. The generators in the module
`junit-quickcheck-generators` are loaded via this mechanism also; any
generators you supply and make available to the `ServiceLoader` complement
these generators rather than override them.

##### Functional interfaces

Custom generators for types that are functional interfaces override the
built-in means of generation for such types. This is usually necessary for
functional interfaces that involve generics.


#### Configuring generators

Over the period of generating values for a single theory parameter, you can
feed specific configurations to the generator(s) for that parameter.  If you
mark a theory parameter already marked as `@ForAll` with an annotation that is
itself marked as `@GeneratorConfiguration`, then if the `Generator` for that
parameter's type has a public method named `configure` that accepts a single
parameter of the annotation type, junit-quickcheck will call the `configure`
method reflectively, passing it the annotation:

```java
    @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
    @Retention(RUNTIME)
    @GeneratorConfiguration
    public @interface Stuff {
        // ...
    }

    public class FooGenerator extends Generator<Foo> {
        // ...

        public void configure(Stuff stuff) {
            // ...
        }
    }

    @RunWith(Theories.class)
    public class FooTheories {
        @Theory public void holds(@ForAll @Stuff Foo f) {
            // ...
        }
    }
```

A `Generator` can have many such `configure` methods.


#### Constraining generated values

##### Assumptions

Theories often use _assumptions_ to declare conditions under which they hold:

```java
    @RunWith(Theories.class)
    public class PrimeFactorsTheories {
        @Theory public void factorsPassPrimalityTest(@ForAll BigInteger n) {
            assumeThat(n, greaterThan(ZERO));

            for (BigInteger each : PrimeFactors.of(n))
                assertTrue(each.isProbablePrime(1000));
        }

        @Theory public void factorsMultiplyToOriginal(@ForAll BigInteger n) {
            assumeThat(n, greaterThan(ZERO));

            BigInteger product = ONE;
            for (BigInteger each : PrimeFactors.of(n))
                product = product.multiply(each);

            assertEquals(n, product);
        }

        @Theory public void factorizationsAreUnique(
            @ForAll BigInteger m,
            @ForAll BigInteger n) {

            assumeThat(m, greaterThan(ZERO));
            assumeThat(n, greaterThan(ZERO));
            assumeThat(m, not(equalTo(n)));

            assertThat(PrimeFactors.of(m), not(equalTo(PrimeFactors.of(n))));
        }
    }
```

Sometimes, using assumptions with junit-quickcheck can yield too few values
that meet the desired criteria:

```java
    @RunWith(Theories.class)
    public class SingleDigitTheories {
        @Theory public void hold(@ForAll int digit) {
            // hope we get enough single digits
            assumeThat(digit, greaterThanOrEqualTo(0));
            assumeThat(digit, lessThanOrEqualTo(9));

            // ...
        }
    }
```

##### Generator configuration methods

Generator configuration methods and annotations can constrain the values that
a generator emits. For example, the `@InRange` annotation on theory parameters
of integral, floating-point, and `Date` types causes the generators for those
types to emit values that fall within a configured minimum/maximum:

```java
    @RunWith(Theories.class)
    public class SingleDigitTheories {
        @Theory public void hold(
            @ForAll @InRange(min = "0", max = "9") int digit) {

            // ...
        }
    }
```

Now, the generator will be configured to ensure that every value generated
meets the desired criteria -- no need to express the desired range of values
as an assumption.

###### Configuration on type uses

Configuration annotations that can target type uses will be honored:

```java
    @RunWith(Theories.class)
    public class ListsOfSingleDigitTheories {
        @Theory public void hold(
            @ForAll List<@InRange(min = "0", max = "9") Integer> digits) {
                // ...
        }
    }
```

###### Configuration on types in a hierarchy

Recall that for a given theory parameter, potentially many generators can
satisfy a given theory parameter based on its type:

```java
    @RunWith(Theories.class)
    public class Serialization {
        @Theory public void hold(
            @ForAll @InRange(min = "0", max = "10") Serializable s) {
        }
    }
```

Any available generators that can produce something that is
`java.io.Serializable` might be called on to generate a value for parameter
`s` above. Because of this, any configuration annotations on a parameter or
type use are ignored by a generator that cannot support the annotation. This
may or may not matter depending on the nature of the theory you're writing.

Also, if you have a family of generators that can produce members of a
hierarchy, you may want to ensure that all the generators respect the same
attributes of a given configuration annotation. Not doing so could lead to
surprising results.

###### Aggregating configuration

Configuration annotations that are directly on a parameter, and any
configuration annotations on annotations that are directly on a parameter
(and so on...) are collected to configure the generator(s) for the parameter:

```java
    @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
    @Retention(RUNTIME)
    @From(MoneyGenerator.class)
    @InRange(min = "0", max = "20")
    @Precision(scale = 2)
    public @interface SmallChange {
    }

    @RunWith(Theories.class)
    public class Monies {
        @Theory public void hold(@ForAll @SmallChange BigDecimal m) {
            assertEquals(2, m.scale());
            assertThat(
                m,
                allOf(greaterThanOrEqualTo(BigDecimal.ZERO),
                    lessThanOrEqualTo(new BigDecimal("20"))));
        }
    }
```

###### Configuration methods vs. assumptions

When using assumptions with junit-quickcheck, every value fed to a `@ForAll`
theory parameter counts against the sample size, even if it doesn't pass any
assumptions made against it in the theory. You could end up with no values
passing the assumption.

Using generator configurations, assumptions aren't very important, if needed
at all -- every value fed to a `@ForAll` theory parameter counts against the
sample size, but will meet some conditions that assumptions would otherwise
have tested.

###### `ValuesOf`

You can mark `boolean` and `enum` theory parameters with `@ValuesOf` to
force the generation to run through every value in the type's domain,
instead of choosing an element from the domain at random every time.
This also effectively dictates the sample size for the parameter.

```java
    enum Ternary { YES, NO, MAYBE }

    @RunWith(Theories.class)
    public class TheoriesOfSmallDomains {
        @Theory public void hold(
            @ForAll @ValuesOf boolean b,
            @ForAll @ValuesOf Ternary t) {

            // Sample sizes of 2 and 3, respectively.
            // Each combination of potential values will be generated.
        }
    }
```

##### Constraint expressions

Constraint expressions allow you to filter the values that reach a theory
parameter. Supply the `suchThat` attribute of `@ForAll` an
[OGNL](http://commons.apache.org/ognl) expression that will be used to decide
whether a generated value will be given to the theory method.

```java
    @RunWith(Theories.class)
    public class SingleDigitTheories {
        @Theory public void hold(@ForAll(suchThat = "#_ >= 0 && #_ <= 9") int digit) {
            // ...
        }
    }
```

A theory parameter is referred to as "_" in the constraint expression.
Constraint expressions cannot refer to other theory parameters.

junit-quickcheck generates values for a theory parameter with a constraint
expression until `sampleSize` values pass the constraint, or until the ratio
of constraint passes to constraint failures is greater than the `discardRatio`
specified by `@ForAll`, if any. Exceeding the discard ratio raises an
exception and thus fails the theory.


#### Sample size

By default, junit-quickcheck generates 100 random values for a parameter
marked `@ForAll`.

**NOTE**: junit-quickcheck uses the `Theories` runner, which executes a theory
method for every combination of values for theory parameters. This means that
for a two-parameter theory method, where each parameter is marked with
`@ForAll`, the `Theories` runner instantiates the theory class and executes
the theory method 10,000 times (100 * 100).

```java
    @RunWith(Theories.class)
    public class GeographyTheories {
        @Theory public void northernHemisphere(
            @ForAll @InRange(min = "-90", max = "90") BigDecimal latitude,
            @ForAll @InRange(min = "-180", max = "180") BigDecimal longitude) {

            assumeThat(latitude, greaterThan(BigDecimal.ZERO));

            assertTrue(Earth.isInNorthernHemisphere(latitude, longitude));
        }
    }
```

If you don't want to take on that many invocations, here are some mitigation
strategies you can use:

- Use the `sampleSize` attribute of `@ForAll` to change the number of
generated values for a given theory parameter:

```java
    @RunWith(Theories.class)
    public class GeographyTheories {
        @Theory public void northernHemisphere(
            @ForAll(sampleSize = 20) @InRange(min = "-90", max = "90")
                BigDecimal latitude,
            @ForAll(sampleSize = 20) @InRange(min = "-180", max = "180")
                BigDecimal longitude) {

            assumeThat(latitude, greaterThan(BigDecimal.ZERO));

            assertTrue(Earth.isInNorthernHemisphere(latitude, longitude));
        }
    }
```

- Collapse the theory parameters into a class, and use a generator for the
class. This approach can exert positive pressure on your designs:

```java
    public class Coordinate {
        private final BigDecimal latitude, longitude;

        public Coordinate(BigDecimal latitude, BigDecimal longitude) {
            // argument checks here...

            this.latitude = latitude;
            this.longitude = longitude;
        }

        public BigDecimal latitude() { return latitude; }
        public BigDecimal longitude() { return longitude; }
        public boolean inNorthernHemisphere() {
            return latitude.compareTo(BigDecimal.ZERO) > 0;
        }
    }

    public class Coordinates extends Generator<Coordinate> {
        @Override public Coordinate generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new Coordinate(
                BigDecimal.valueOf(random.nextDouble(-90, 90))
                    .setScale(6, RoundingMode.CEILING),
                BigDecimal.valueOf(random.nextDouble(-180, 180))
                    .setScale(6, RoundingMode.CEILING));
        }
    }

    @RunWith(Theories.class)
    public class GeographyTheories {
        @Theory public void northernHemisphere(
            @ForAll @From(Coordinates.class) Coordinate c) {

            assumeThat(c.latitude(), greaterThan(BigDecimal.ZERO));

            assertTrue(c.inNorthernHemisphere());
        }
    }
```

- If you opt for artificially collapsing theory parameters into a class
(that is, not introducing a new concept into your domain), you can use either
the `Fields` or the `Ctor` generator to avoid writing a custom generator:

```java
    @RunWith(Theories.class)
    public class ThreeDimensionalSpaceTheories {
        public static class Point {
            public double x, y, z;
        }

        @Theory public void originDistance(
            @ForAll @From(Fields.class) Point p) {

            assertEquals(
                Math.sqrt(p.x * p.x + p.y * p.y + p.z * p.z),
                Space.distanceFromOrigin(p.x, p.y, p.z));
        }
    }
   
```

```java
    @RunWith(Theories.class)
    public class GeographyTheories {
        public static class Coordinate {
            private final BigDecimal latitude, longitude;

            public Coordinate(
                @InRange(min = "-90", max = "90") BigDecimal latitude,
                @InRange(min = "-180", max = "180") BigDecimal longitude) {

                this.latitude = latitude;
                this.longitude = longitude;
            }

            public BigDecimal latitude() { return latitude; }
            public BigDecimal longitude() { return longitude; }
            public boolean inNorthernHemisphere() {
                return latitude.compareTo(BigDecimal.ZERO) > 0;
            }
        }

        @Theory public void northernHemisphere(
            @ForAll @From(Ctor.class) Coordinate c) {

            assumeThat(c.latitude(), greaterThan(BigDecimal.ZERO));

            assertTrue(c.inNorthernHemisphere());
        }
    }
```

junit-quickcheck will honor any generation-influencing annotations applied to
either fields (when using the `Fields` generator) or constructor parameters
(when using the `Ctor` generator) when the respective generators create values
for the fields or constructor parameters.


#### Seed

For each theory parameter, junit-quickcheck uses a unique value as a seed for
the source of randomness used to generate the parameter's values. To fix the
seed value for a theory parameter, use the `seed` attribute of the `@ForAll`
annotation:

```java
    @RunWith(Theories.class)
    public class SameValues {
        @Theory public void holds(@ForAll(seed = -1L) int i) {
            // ...
        }
    }
```

You may want to fix the seed when a theory fails, so that you can execute the
theory over and over again with the same set of generated values that caused
the failure.

junit-quickcheck reports the seed used for a given theory parameter by logging
it to a [SLF4J](http://www.slf4j.org/) logger named
`junit-quickcheck.seed-reporting`, at `DEBUG` level:

    Seed for parameter com.your.TheoryClass.theoryMethod:parameterName is 8007238959251963394

Add an SLF4J binding JAR file to your test class path and logging configuration
for your chosen bound library to see the seed log messages.


### How it works

junit-quickcheck leverages the `ParameterSupplier` feature of the JUnit
theories machinery.

By default, when the `Theories` runner executes a theory, it attempts to
scrape _data points_ off the theory class to feed to the theories. Data points
come from static fields or methods annotated with `@DataPoint` (single value)
or `@DataPoints` (array/iterable of values). The `Theories` runner feeds all
combinations of data points of types matching a theory's parameters to the
theory for execution.

Marking a theory parameter with an annotation that is itself annotated with
`@ParametersSuppliedBy` tells the `Theories` runner to ask a
`ParameterSupplier` for values for the theory parameter instead. This is how
junit-quickcheck interacts with the `Theories` runner -- `@ForAll` tells the
runner to use junit-quickcheck's `ParameterSupplier` rather than the
`DataPoint`-oriented one.


### Similar projects

* [JCheck](http://www.jcheck.org/). This uses its own test runner, whereas
junit-quickcheck leverages the existing `Theories` runner and
`ParameterSupplier`s.
* [QuickCheck](http://java.net/projects/quickcheck/pages/Home). This appears
to be test framework-agnostic, focusing instead on generators of random
values.
* [fj.test package of FunctionalJava (formerly Reductio)]
(http://functionaljava.org/)
* [ScalaCheck](http://code.google.com/p/scalacheck/), if you wish to test
Java or Scala code using Scala.


### About junit-quickcheck

junit-quickcheck was written by Paul Holser, and is distributed under the MIT
License.

    The MIT License

    Copyright (c) 2010-2015 Paul R. Holser, Jr.

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
