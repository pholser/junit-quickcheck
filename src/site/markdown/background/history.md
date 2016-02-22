# History/Background

The [Haskell](http://haskell.org) library [QuickCheck]
(http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html) allows programmers
to specify properties of a function that should hold true for some large
(potentially infinite) set of possible arguments to the function, then
executes the function using lots of random arguments to see whether the
property holds up against them.


## Theories

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


## So?

TDD/BDD builds up designs example by example. The resulting test suites
give programmers confidence that their code works for the examples they
thought of. Theories offer a means to express statements about code that
should hold for an entire domain of inputs, not just a handful of examples,
and to validate those statements against lots of randomly generated inputs.


## Where junit-quickcheck came in

junit-quickcheck began its life as a library that supplies JUnit
theories with random values with which to test the validity of the theories.

Prior to version 0.6, junit-quickcheck leveraged the `ParameterSupplier`
feature of the JUnit theories machinery, via the annotation `@ForAll` marking
theory parameters.

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


## Drawbacks to `ParameterSupplier` approach to junit-quickcheck

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
generated values for a given theory parameter.
- Collapsing the theory parameters into a class, and using a generator for the
class. This approach can exert positive design pressure.

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
or `Fields` generator to produce values.

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
