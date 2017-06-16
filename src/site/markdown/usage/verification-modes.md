# Verification modes

## "Sampling" mode

By default, junit-quickcheck verifies a property in "sampling" mode --
it generates 100 tuples of random values for the parameter list of a property,
and verifies the property against each of the tuples.

To change the number of generated values, use the `trials` attribute of the
`@Property` annotation.

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

## "Exhaustive" mode

junit-quickcheck can also verify a property in "exhaustive" mode.
In "exhaustive" mode, junit-quickcheck generates `trials` random values for
each of the property's parameters, and verifies the property against each
member of the Cartesian product of the sets of random values for each
parameter. This behavior mirrors that of the JUnit `Theories` runner.

For example, a property such as:

```java
    @RunWith(JUnitQuickcheck.class)
    public class SimpleProperties {
        @Property(trials = 3) public void sum(int a, int b) {
            // ...
        }
    }
```

might be verified with these executions:

```
    sum(12987133, 456123400)
    sum(-283945, 75089314)
    sum(9823745, -139845713)
```

In "exhaustive" mode:

```java
    @RunWith(JUnitQuickcheck.class)
    public class SimpleProperties {
        @Property(trials = 3, mode = EXHAUSTIVE)
        public void sum(int a, int b) {
            // ...
        }
    }
```

this might be verified with these executions:

```
    sum(-891273491, 573198457)
    sum(719283474, 573198457)
    sum(-384571913, 573198457)
    sum(-891273491, 6928374)
    sum(719283474, 6928374)
    sum(-384571913, 6928374)
    sum(-891273491, -123420835)
    sum(719283474, -123420835)
    sum(-384571913, -123420835)
```
