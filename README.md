# junit-quickcheck: QuickCheck-style parameter suppliers for JUnit theories

junit-quickcheck is a library that supplies [JUnit](http://junit.org)
[theories](http://groups.csail.mit.edu/pag/pubs/test-theory-demo-oopsla2007.pdf)
with random values with which to test the validity of the theories.

**PLEASE NOTE**: junit-quickcheck uses a
[version of the JUnit theories runner](https://github.com/junit-team/junit.contrib/tree/master/theories) 
that has been modified to respect generics on theory parameter types, as described
[here](http://github.com/junit-team/junit/issues/64). The classes that comprise
this rendition of the JUnit theories runner are packaged as
`org.junit.contrib.theories.*`, rather than `org.junit.experimental.theories.*`.
Be sure to use the `contrib` version of the runner, annotations, etc. with
junit-quickcheck.


### Downloading

junit-quickcheck's framework is contained in the JAR file for the module
`junit-quickcheck-core`. You will want to start out also with the JAR file for
the module `junit-quickcheck-generators`, which consists of generators for
theory parameters of basic Java types, such as primitives, arrays, and
collections.

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
        <version>0.4-SNAPSHOT</version>
      </dependency>
      <dependency>
        <groupId>com.pholser</groupId>
        <artifactId>junit-quickcheck-generators</artifactId>
        <version>0.4-SNAPSHOT</version>
      </dependency>
      ...
    </dependencies>
    ...
```    


### Background

The [Haskell](http://haskell.org) library
[QuickCheck](http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html)
allows programmers to specify properties of a function that should hold
true for some large (potentially infinite) set of possible arguments to
the function, then executes the function using lots of random arguments
to see whether the property holds up against them.


#### Theories

JUnit's answer to function properties is the notion of _theories_.
Programmers write parameterized tests marked as theories, run using a
special test runner:

```java
    import org.junit.contrib.theories.*;
    import static org.hamcrest.Matchers.*;
    import static org.junit.Assert.*;
    import static org.junit.Assume.*;
    
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

            assertEquals(originalBalance.minus(withdrawalAmount), account.balance());
        }
    }
```


#### So?

TDD/BDD builds up designs example by example. The resulting test suites
give programmers confidence that their code works for the examples they
thought of. Theories offer a means to express statements about code that
should hold for an entire domain of inputs, not just a handful of
examples, and to validate those statements against lots of randomly generated
inputs.


### Using junit-quickcheck

Create theories as you normally would with JUnit. If you want JUnit to
exercise the theory with lots of randomly generated values for a theory
parameter, mark the theory parameter with `@ForAll`:

```java
    import com.pholser.junit.quickcheck.ForAll;

    @RunWith(Theories.class)
    public class SymmetricKeyCryptography {
        @Theory public void decryptReversesEncrypt(
            @ForAll String plaintext,
            @ForAll Key key) throws Exception {

            Crypto crypto = new Crypto();

            byte[] ciphertext = crypto.encrypt(plaintext.getBytes("US-ASCII"), key);

            assertEquals(plaintext, new String(crypto.decrypt(ciphertext, key)));
        }
    }
```


#### Supported types

Out of the box (core + generators), junit-quickcheck can generate
random values for theory parameters of the following types:

* all Java primitives and primitive wrappers
* `java.math.Big(Decimal|Integer)`
* `java.util.Date`
* any `enum`
* `String`
* "functional interfaces" (interfaces with a single method that does not override a method from `java.lang.Object`)
* `java.util.ArrayList` and `java.util.LinkedList` of supported types
* `java.util.HashSet` and `java.util.LinkedHashSet` of supported types
* `java.util.HashMap` and `java.util.LinkedHashMap` of supported types
* arrays of supported types
* others...

When many generators can satisfy a given theory parameter based
on its type (for example, `java.io.Serializable`), on a given
generation one of the multiple generators will be chosen at random
with (roughly) equal probability.


#### Generating values of other types

If you wish to generate random values for theory parameters of other
types, or to override the default means of generation for a supported
type, mark the theory parameter already marked as `@ForAll` with
`@From` and supply the class(es) of the `Generator` to be used.
If you give multiple classes in `@From`, one will be chosen on every
generation with (roughly) equal probability.

```java
    @RunWith(Theories.class)
    public class IdentificationTheories {
        @Theory public void shouldHold(@ForAll @From(Version5.class) UUID u) {
            // ...
        }
    }
```

If you wish to add a generator for a type without having to use
`@From`, you can package your `Generator` in a
[ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html)
JAR file and place the JAR on the class path. junit-quickcheck will
make those generators available for use. The generators in the module
`junit-quickcheck-generators` are loaded via this mechanism also; any
generators you supply and make available to the `ServiceLoader`
complement these generators rather than override them.

Custom generators for types that are functional interfaces override
the built-in means of generation for such types. This is usually
necessary for functional interfaces that involve generics.


#### Configuring generators

Over the period of generating values for a single theory parameter,
you can feed specific configurations to the generator(s) for values
of the parameter's type. If you mark a theory parameter already marked
as `@ForAll` with an annotation that is itself marked as
`@GeneratorConfiguration`, then if the `Generator` for that
parameter's type has a public method named `configure` that accepts
a single parameter of the annotation type, junit-quickcheck will call
the `configure` method reflectively, passing it the annotation:

```java
    @Target({PARAMETER, FIELD})
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
```

A `Generator` can have many such `configure` methods.

Generators of "componentized" types such as arrays and lists pass
configurations on parameters of their type to generators of the
component types. When junit-quickcheck migrates to Java 8, it will
likely lean on [type annotations](https://jcp.org/en/jsr/detail?id=308)
instead.

#### Constraining generated values

##### Assumptions

Theories often use _assumptions_ to declare conditions under which
they hold:

```java
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
```

Sometimes, using assumptions with junit-quickcheck may yield too
few values that meet the desired criteria:

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

Here, there's not much guarantee that we'll get very many, if any,
values to test out the theory.

##### Generator configuration methods

Generator configuration methods and annotations can serve to
constrain the values that a generator emits. For example,
the `@InRange` annotation on theory parameters of integral,
floating-point, and `Date` types causes the generators for those
types to emit values that fall within a configured
minimum/maximum:

```java
    @RunWith(Theories.class)
    public class SingleDigitTheories {
        @Theory public void hold(@ForAll @InRange(minInt = 0, maxInt = 9) int digit) {
            // ...
        }
    }
```

Now, the generator will be configured to ensure that every value
generated meets the desired criteria -- no need to express the
desired range of values as an assumption.

When using assumptions with junit-quickcheck, every value fed
to a `@ForAll` theory parameter counts against the sample size,
even if it doesn't pass any assumptions made against it in the
theory. You could end up with no values passing the assumption.

Using generator configurations, assumptions aren't very important,
if needed at all -- every value fed to a `@ForAll` theory parameter
counts against the sample size, but will meet some conditions that
assumptions would otherwise have tested.

`boolean` and `enum` theory parameters can be annotated with
`@ValuesOf` to force the generation to run through every value in
the type's domain, instead of choosing an element from the domain
at random every time. This also effectively dictates the sample
size for the parameter.

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

Constraint expressions enable you to filter the values that
reach a theory parameter. Mark a theory parameter already marked
as `@ForAll` with `@SuchThat`, supplying an
[OGNL](http://commons.apache.org/ognl) expression that will be
used to decide whether a generated value will be given to the
theory method.

```java
    @RunWith(Theories.class)
    public class SingleDigitTheories {
        @Theory public void hold(@ForAll @SuchThat("#_ >= 0 && #_ <= 9") int digit) {
            // ...
        }
    }
```

A theory parameter is referred to as "_" in the constraint expression.
Constraint expressions cannot refer to other theory parameters.

junit-quickcheck generates values for a theory parameter with a
constraint expression until `sampleSize` values pass the
constraint, or until the ratio of constraint passes to constraint
failures is greater than the `discardRatio` specified by `@ForAll`,
if any. Exceeding the discard ratio raises an exception and thus
fails the theory.


#### Sample size

By default, 100 random values are generated for a parameter
marked `@ForAll`.

**NOTE**: Because junit-quickcheck uses the `Theories` runner,
a given theory method will be executed for every combination of values
for theory parameters. This means that for a two-parameter theory
method, where each parameter is marked with `@ForAll`, the theory
class will be instantiated and the theory executed 10,000 times
(100 * 100).

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

If you don't want to take on that many invocations, here are some
mitigation strategies you can use:

- Use the `sampleSize` attribute of `@ForAll` to change the number
of generated values for a given theory parameter:

```java
    @RunWith(Theories.class)
    public class GeographyTheories {
        @Theory public void northernHemisphere(
            @ForAll(sampleSize = 20) @InRange(min = "-90", max = "90") BigDecimal latitude,
            @ForAll(sampleSize = 20) @InRange(min = "-180", max = "180") BigDecimal longitude) {

            assumeThat(latitude, greaterThan(BigDecimal.ZERO));

            assertTrue(Earth.isInNorthernHemisphere(latitude, longitude));
        }
    }
```

- Collapse the parameters into a class, and use a generator for the
class. Sometimes, this approach can exert positive pressure on your
designs:

```java
    public class Coordinate {
        private final BigDecimal latitude;
        private final BigDecimal longitude;

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

- If you opt for artificially collapsing theory parameters
into a class (that is, not introducing a new concept into your
domain), you can avoid writing a custom generator by using either
the `Fields` or the `Ctor` generator, like so:

```java
    @RunWith(Theories.class)
    public class ThreeDimensionalSpaceTheories {
        public static class Point {
            public double x;
            public double y;
            public double z;
        }

        @Theory public void originDistance(@ForAll @From(Fields.class) Point p) {
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
            private final BigDecimal latitude;
            private final BigDecimal longitude;

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

        @Theory public void northernHemisphere(@ForAll @From(Ctor.class) Coordinate c) {
            assumeThat(c.latitude(), greaterThan(BigDecimal.ZERO));

            assertTrue(c.inNorthernHemisphere());
        }
    }
```

Any generation-influencing annotations applied to either fields
(when using the `Fields` generator) or constructor parameters
(with using the `Ctor` generator) will be honored when the
respective generators create values for the fields or constructor
parameters.


### How it works

junit-quickcheck leverages the `ParameterSupplier` feature of
the JUnit theories machinery.

By default, when the `Theories` runner executes a theory,
it attempts to scrape _data points_ off the theory class to
feed to the theories. Data points come from static fields or
methods annotated with `@DataPoint` (single value) or `@DataPoints`
(array/iterable of values). The `Theories` runner arranges for
all combinations of data points of types matching a theory
parameter to be fed to the theory for execution.

Marking a theory parameter with an annotation that is itself
annotated with `@ParametersSuppliedBy` tells the `Theories` runner
to ask a `ParameterSupplier` for values for the theory parameter
instead. This is how junit-quickcheck interacts with the
`Theories` runner -- `@ForAll` tells the runner to use
junit-quickcheck's `ParameterSupplier` rather than the
`DataPoint`-oriented one.


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

    Copyright (c) 2010-2014 Paul R. Holser, Jr.

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
