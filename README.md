# junit-quickcheck: Property-based testing, JUnit-style

junit-quickcheck is a library that supports writing and running property-based
tests in [JUnit](http://junit.org), inspired by
[QuickCheck](http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html) for
[Haskell](http://haskell.org).

junit-quickcheck is source/target-compatible with JDK 8.


## Basic example

```java
    import com.pholser.junit.quickcheck.Property;
    import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
    import org.junit.runner.RunWith;

    import static org.junit.Assert.*;

    @RunWith(JUnitQuickcheck.class)
    public class StringProperties {
        @Property public void concatenationLength(String s1, String s2) {
            assertEquals(s1.length() + s2.length(), (s1 + s2).length());
        }
    }
```


## Downloading

junit-quickcheck's machinery is contained in the JAR file for the module
`junit-quickcheck-core`. You will want to start out also with the JAR file for
the module `junit-quickcheck-generators`, which consists of generators for
basic Java types, such as primitives, arrays, and collections.

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
        <version>0.6-alpha-2</version>
      </dependency>
      <dependency>
        <groupId>com.pholser</groupId>
        <artifactId>junit-quickcheck-generators</artifactId>
        <version>0.6-alpha-2</version>
      </dependency>
      ...
    </dependencies>
    ...
```


## Discussing

There is [a Google group for junit-quickcheck]
(https://groups.google.com/d/forum/junit-quickcheck).


## Using junit-quickcheck

- Create a class to host the properties you want to verify about a part of
your system. Mark it with the annotation `@RunWith(JUnitQuickcheck.class)`.
- Add `public` methods with a return type of `void` on your class, to
represent the individual properties. Mark each of them with the annotation
`@Property`.
- Run your class using JUnit. Each of your properties will be verified against
several randomly generated values for each of the parameters on the properties'
methods.

```java
    import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
    import com.pholser.junit.quickcheck.Property;

    // Imagining the existence of class Crypto...
    @RunWith(JUnitQuickcheck.class)
    public class SymmetricKeyCryptographyProperties {
        @Property public void decryptReversesEncrypt(String plaintext, Key key)
            throws Exception {

            Crypto crypto = new Crypto();

            byte[] ciphertext =
                crypto.encrypt(plaintext.getBytes("US-ASCII"), key);

            assertEquals(
                plaintext,
                new String(crypto.decrypt(ciphertext, key)));
        }
    }
```


### Supported types

Out of the box (core + generators), junit-quickcheck recognizes property
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

When many generators can satisfy a given property parameter based on its type
(for example, `java.io.Serializable`), on a given generation junit-quickcheck
will choose one of the multiple generators at random with (roughly) equal
probability.


### Generating values of other types

To generate random values for property parameters of other types, or to
override the default means of generation for a supported type, mark the
property parameter with `@From` and supply the class of the `Generator` to be
used. If you give multiple `@From` annotations, junit-quickcheck will choose
one on every generation with probability in proportion to its `frequency`
attribute (default is 1).

```java
    @RunWith(JUnitQuickcheck.class)
    public class IdentificationProperties {
        @Property public void shouldHold(@From(Version5.class) UUID u) {
            // ...
        }
    }
```

To add a generator for a type without having to use `@From`, you can package it
in a [ServiceLoader]
(http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
JAR file and place the JAR on the class path. junit-quickcheck will make
generators packaged in this way available for use. The generators in the module
`junit-quickcheck-generators` are loaded via this mechanism also; any
generators you supply and make available to the `ServiceLoader` complement
these generators rather than override them.


#### Functional interfaces

Custom generators for types that are functional interfaces override the
built-in means of generation for such types. This is usually necessary for
functional interfaces that involve generics.


### Configuring generators

If you mark a property parameter with an annotation that is itself marked as
`@GeneratorConfiguration`, then if the `Generator` for that parameter's type
has a public method named `configure` that accepts a single argument of the
annotation type, junit-quickcheck will call the `configure` method
reflectively, passing it the annotation:

```java
    @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
    @Retention(RUNTIME)
    @GeneratorConfiguration
    public @interface Positive {
        // ...
    }

    public class IntegralGenerator extends Generator<Integer> {
        private Positive positive;

        @Override public Integer generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            int value = random.nextInt();
            return positive != null ? Math.abs(value) : value;
        }

        public void configure(Positive positive) {
            this.positive = positive;
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public class Numbers {
        @Property public void holds(@Positive int i) {
        }
    }
```

A `Generator` can have many such `configure` methods.


### Constraining generated values

#### Assumptions

Properties often use _assumptions_ to declare conditions under which they
hold:

```java
    @RunWith(JUnitQuickcheck.class)
    public class PrimeFactorsProperties {
        @Property public void factorsPassPrimalityTest(BigInteger n) {
            assumeThat(n, greaterThan(ZERO));

            for (BigInteger each : PrimeFactors.of(n))
                assertTrue(each.isProbablePrime(1000));
        }

        @Property public void factorsMultiplyToOriginal(BigInteger n) {
            assumeThat(n, greaterThan(ZERO));

            BigInteger product = ONE;
            for (BigInteger each : PrimeFactors.of(n))
                product = product.multiply(each);

            assertEquals(n, product);
        }

        @Property public void factorizationsAreUnique(
            BigInteger m,
            BigInteger n) {

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
    @RunWith(JUnitQuickcheck.class)
    public class SingleDigitProperties {
        @Property public void hold(int digit) {
            // hope we get enough single digits
            assumeThat(digit, greaterThanOrEqualTo(0));
            assumeThat(digit, lessThanOrEqualTo(9));

            // ...
        }
    }
```


#### Generator configuration methods

Generator configuration methods and annotations can constrain the values that
a generator emits. For example, the `@InRange` annotation on property
parameters of integral, floating-point, and `Date` types causes the generators
for those types to emit values that fall within a configured minimum/maximum:

```java
    @RunWith(JUnitQuickcheck.class)
    public class SingleDigitProperties {
        @Property public void hold(@InRange(min = "0", max = "9") int digit) {
            // ...
        }
    }
```

Now, the generator will be configured to ensure that every value generated
meets the desired criteria -- no need to express the desired range of values
as an assumption.


#### Configuration on type uses

Configuration annotations that can target type uses will be honored:

```java
    @RunWith(JUnitQuickcheck.class)
    public class PropertiesOfListsOfSingleDigits {
        @Property public void hold(
            List<@InRange(min = "0", max = "9") Integer> digits) {
                // ...
        }
    }
```


#### Configuration on types in a hierarchy

Recall that potentially many generators can satisfy a given property parameter
based on its type:

```java
    @RunWith(JUnitQuickcheck.class)
    public class SerializationProperties {
        @Property public void hold(
            @InRange(min = "0", max = "10") Serializable s) {
                // ...
        }
    }
```

Only the available generators that can produce something that is
`java.io.Serializable` *and* that support all the configuration annotations on
a property parameter will be called on to generate a value for that parameter.
So, for example, for parameter `s` above, generators for integral values might
be called upon, whereas generators for `ArrayList`s would not. junit-quickcheck
will complain loudly if there are no such generators available.

If you have a family of generators that can produce members of a hierarchy,
you may want to ensure that all the generators respect the same attributes
of a given configuration annotation. Not doing so could lead to surprising
results.


#### Aggregating configuration

Configuration annotations that are directly on a property parameter, and any
configuration annotations on annotations that are directly on a property
parameter (and so on...) are collected to configure the generator(s) for the
parameter:

```java
    @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
    @Retention(RUNTIME)
    @From(MoneyGenerator.class)
    @InRange(min = "0", max = "20")
    @Precision(scale = 2)
    public @interface SmallChange {
    }

    @RunWith(JUnitQuickcheck.class)
    public class Monies {
        @Property public void hold(@SmallChange BigDecimal d) {
            assertEquals(2, d.scale());
            assertThat(
                d,
                allOf(
                    greaterThanOrEqualTo(BigDecimal.ZERO),
                    lessThanOrEqualTo(new BigDecimal("20"))));
        }
    }
```


#### Configuration methods vs. assumptions

When using assumptions with junit-quickcheck, every value fed to a property
parameter counts against the sample size, even if it doesn't pass any
assumptions made against it in the property. You could end up with no values
passing the assumption.

Using generator configurations, assumptions aren't very important, if needed
at all -- every value fed to a property parameter counts against the sample
size, but will meet some conditions that assumptions would otherwise have
tested.


#### `ValuesOf`

You can mark `boolean` and `enum` property parameters with `@ValuesOf` to
force the generation to run through every value in the type's domain, instead
of choosing an element from the domain at random every time.

```java
    enum Ternary { YES, NO, MAYBE }

    @RunWith(JUnitQuickcheck.class)
    public class SmallDomainsProperties {
        @Property public void hold(@ValuesOf boolean b, @ValuesOf Ternary t) {
            // Each verification will be with a different value for b and t.
        }
    }
```

#### Constraint expressions

Constraint expressions allow you to filter the values that reach a property
parameter. Mark the parameter with `@When` and give that annotation's
`satisfies` attribute an [OGNL](http://commons.apache.org/ognl) expression
that will be used to decide whether a generated value will be given to that
parameter.

```java
    @RunWith(JUnitQuickcheck.class)
    public class SingleDigitProperties {
        @Property public void hold(
            @When(satisfies = "#_ >= 0 && #_ <= 9") int digit) {

            // ...
        }
    }
```

A property parameter is referred to as "_" in the constraint expression.
Constraint expressions cannot refer to other property parameters.

junit-quickcheck generates values for a property parameter with a constraint
expression until `sampleSize` values pass the constraint, or until the ratio
of constraint passes to constraint failures is greater than the `discardRatio`
specified by `@When`, if any. Exceeding the discard ratio raises an
exception and thus fails the property.


### Sample size

By default, junit-quickcheck generates 100 sets of random values for the
parameter list of a property. To change this value, use the `trials` attribute
of the `@Property` annotation:

```java
    @RunWith(JUnitQuickcheck.class)
    public class Geography {
        @Property(trials = 250) public void northernHemisphere(
            @From(Coordinates.class) Coordinate c) {

            assumeThat(c.latitude(), greaterThan(BigDecimal.ZERO));

            assertTrue(c.inNorthernHemisphere());
        }
    }

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
```


### Seed

For each property parameter, junit-quickcheck uses a unique value as a seed
for the source of randomness used to generate the parameter's values. To fix
the seed value for a property parameter, use the `seed` attribute of the
`@When` annotation:

```java
    @RunWith(JUnitQuickcheck.class)
    public class SameValues {
        @Property public void holds(@When(seed = -1L) int i) {
            // ...
        }
    }
```

You may want to fix the seed when a property fails, so that you can test the
property over and over again with the same set of generated values that caused
the failure.

junit-quickcheck reports the seed used for a given property parameter by
logging it to a [SLF4J](http://www.slf4j.org/) logger named
`junit-quickcheck.seed-reporting`, at `DEBUG` level:

    Seed for parameter com.your.PropertiesClass.propertyMethod:parameterName is 8007238959251963394

Add an SLF4J binding JAR file to your test class path and logging configuration
for your chosen bound library to see the seed log messages.


### Shrinking

Version 0.6 of junit-quickcheck introduces one of the most compelling features
of a proper QuickCheck: _shrinking_. When a property is disproved for a given
set of values, junit-quickcheck will attempt to find "smaller" sets of values
that also disprove the property, and will report the smallest such set.

For example, imagine the following disprovable property, without shrinking:

```java
    public class Integers {
        public static boolean isPrime(int i) {
            // ...
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public static class IntegerProperties {
        @Property(shrink = false)
        public void primality(@InRange(minInt = 2) int i) {
            assertTrue(String.valueOf(i), Integers.isPrime(i));
        }
    }
```

Verifying the property might yield a failure like:

    java.lang.AssertionError: 422886279

It might not be obvious from looking at this particular failing value why
the implementation of `Integers.isPrime()` is incorrect. It would be nice
to find smaller values, if possible, for which the property is also shown
not to hold. When shrinking is enabled:

```java
    @RunWith(JUnitQuickcheck.class)
    public static class IntegerProperties {
        @Property(shrink = true)
        public void primality(@InRange(minInt = 2) int i) {
            assertTrue(String.valueOf(i), Integers.isPrime(i));
        }
    }
```

the shrinking process might find a smaller value such as:

    java.lang.AssertionError: Property primality falsified for args shrunken to [4]


#### Producing "smaller" values

When a property fails for a set of parameter values, generators of those values
can be called upon to offer "candidates" for smaller values that might also
cause failures. By default, a generator "can" shrink a value (as reported by
its `canShrink()` method) if the value is assignment-compatible with the kind
of values that generator produces, and offers up an empty list of candidates
to the shrinking process (via its `doShrink()` method).

Most of the generators in module `junit-quickcheck-generators`, including
those for primitives/their wrappers, collections, maps, and arrays,
override `doShrink()` to offer "smaller" values to the shrinking process.
Your custom generators can, of course, do the same:


```java
    import java.awt.Point;

    public class Points extends Generator<Point> {
        private static final Point ORIGIN = new Point();

        public Points() {
            super(Point.class);
        }

        @Override public Point generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new Point(random.nextInt(), random.nextInt());
        }

        @Override public List<Point> doShrink(
            SourceOfRandomness random,
            Point larger) {

            if (ORIGIN.equals(larger))
                return Collections.emptyList();

            List<Point> shrinks = new ArrayList<>();
            shrinks.add(new Point(0, larger.y));
            shrinks.add(new Point(larger.x, 0));
            shrinks.add(new Point(larger.x / 2, larger.y));
            shrinks.add(new Point(larger.x, larger.y / 2));
            return shrinks;
        }
    }
```


#### Influencing the shrinking process

- By default, shrinking is enabled. To disable it, set the `shrink` attribute
of a `@Property` annotation to `false`.
- To reduce or increase the maximum number of shrink attempts made for a given
property, set the `maxShrinks` attribute of that `@Property`.
- To reduce or increase the maximum "depth" of the shrink search "tree" for a
given property, set the `maxShrinkDepth` attribute of that `@Property`.


## Background

The [Haskell](http://haskell.org) library [QuickCheck]
(http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html) allows programmers
to specify properties of a function that should hold true for some large
(potentially infinite) set of possible arguments to the function, then
executes the function using lots of random arguments to see whether the
property holds up against them.


### Theories

One JUnit answer to function properties is the notion of
[theories](http://junit.org/apidocs/org/junit/experimental/theories/Theories.html).
Programmers write parameterized tests marked as theories, run using a special
test runner.

```java
    import org.junit.experimental.theories.*;
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


### So?

TDD/BDD builds up designs example by example. The resulting test suites
give programmers confidence that their code works for the examples they
thought of. Theories offer a means to express statements about code that
should hold for an entire domain of inputs, not just a handful of examples,
and to validate those statements against lots of randomly generated inputs.


### Where junit-quickcheck came in

junit-quickcheck began its life as a library that supplies JUnit
theories with random values with which to test the validity of the theories.

In this original incarnation, junit-quickcheck leveraged the
`ParameterSupplier` feature of the JUnit theories machinery, via the
annotation `@ForAll` marking theory parameters.

By default, when the `Theories` runner executes a theory, it attempts to
scrape _data points_ off the theory class to feed to the theories. Data points
come from static fields or methods annotated with `@DataPoint` (single value)
or `@DataPoints` (array/iterable of values). The `Theories` runner feeds all
combinations of data points of types matching a theory's parameters to the
theory for execution.

Marking a theory parameter with an annotation that is itself annotated with
`@ParametersSuppliedBy` tells the `Theories` runner to ask a
`ParameterSupplier` for values for the theory parameter instead. This is how
junit-quickcheck interacted with the `Theories` runner -- `@ForAll` told the
runner to use junit-quickcheck's `ParameterSupplier` rather than the
`DataPoint`-oriented one.


### Drawbacks to `ParameterSupplier` approach to junit-quickcheck

The `Theories` runner executes a theory method once for every combination of
values for theory parameters. This means that for a two-parameter theory
method, the `Theories` runner instantiates the theory class and executes
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

This led to mitigation strategies such as:
- Using the `sampleSize` attribute of `@ForAll` to change the number of
generated values for a given theory parameter

- Collapsing the theory parameters into a class, and using a generator for the
class. This approach can exert positive design pressure:

```java
    public class Coordinate {
        // ...
    }

    public class Coordinates extends Generator<Coordinate> {
        // ...
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

- Sometimes, there is no domain concept underlying the theory's parameters.
In this case, you could create an artificial "tuple" class and use the `Ctor`
or `Fields` generator to produce values:

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

            // ...
        }

        @Theory public void northernHemisphere(
            @ForAll @From(Ctor.class) Coordinate c) {

            // ...
        }
    }
```


### Now...

- Instead of the `Theories` runner, with `@Theory` methods with parameters
marked `@ForAll`, we use the `JUnitQuickcheck` runner, with `@Property`
methods. `@ForAll` is marked as deprecated; support for the
`Theories`-based junit-quickcheck may be removed soon.
- There are `Property.trials()` executions of the property, instead of a
combinatorial number of executions of a `Theory`
- Use the `@When` annotation to control seed and parameter value generation
if desired
- You can still mark property parameters with configuration annotations,
just like you did with `@ForAll` theory parameters
- Your generators should work the same as before


## Similar projects

* [JCheck](http://www.jcheck.org/)
* [QuickCheck](http://java.net/projects/quickcheck/pages/Home). This appears
to be test framework-agnostic, focusing instead on generators of random
values.
* [fj.test package of FunctionalJava (formerly Reductio)]
(http://functionaljava.org/)
* [ScalaCheck](http://code.google.com/p/scalacheck/), if you wish to test
Java or Scala code using Scala.


## About junit-quickcheck

junit-quickcheck is written by Paul Holser, and is distributed under the MIT
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
