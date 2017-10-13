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

## `@Only` and `@Also`

These annotations can influence how junit-quickcheck chooses the set of values
from which the value of a property parameter is drawn.

In "sampling" mode, there will be `Property.trials()` values for each
parameter.

* If the parameter is marked with `@Only`, then its values are chosen at
random from the specified set.
* If the parameter is marked with `@Also`, then those values are used, and
`trials - |Also.value|` values are chosen from generators.
* Otherwise, the values are chosen exclusively by a generator.
* `@Only` wins over `@Also`.
* There will be trials number of executions of the property, once for each
"tuple" of arguments.

In "exhaustive" mode, there will usually be `trials` values for a parameter.
However:

* If the parameter has type `boolean`, `Boolean`, or an `enum`, there will be
2, 2, or `values().length` values, comprising the type's entire domain.
* If the parameter is marked with `@Only`, then only those values (numbering
`|Only.value|`) are used.
* If the parameter is marked with `@Also`, then those values are used, and
`trials - `|Also.value|` values are chosen from the generators.
* Otherwise, `trials` values are chosen by a generator.
* `@Only` wins over `@Also`.
* There will be `product[ |p| | p in parameters ]` number of executions of
the property, one for each member of the cross-product of values to be used
for each parameter.

For example:

```java
@RunWith(JUnitQuickcheck.class)
public class OnlyAndAlso {
    public enum Response { YES, NO, UNSURE }
    
    @Property(trials = 45)
    public void samplingWithOnly(
        int arg0,
        boolean arg1,
        Response arg2,
        @Only({"1", "2", "0", "-1"}) int arg3) {
    
        /*
        Invoked 45 times.
        45 tuples (arg0, arg1, arg2, arg3) generated.
        Each value of each tuple chosen at random from its domain.
        Domain of arg3 narrowed by @Only.
        */
    }
    
    @Property(trials = 77, mode = EXHAUSTIVE)
    public void exhaustiveWithOnly(
        int arg0,
        boolean arg1,
        Response arg2,
        @Only({"1", "2", "0", "-1"}) int arg3) {
    
        /*
        Invoked 77 * 2 * 3 * 4 times --
        once for each choice of 77 randomly chosen arg0,
        two possible values of arg1, three possible values of arg2,
        and four possible values of arg3.
        */
    }

    @Property(trials = 45)
    public void samplingWithAlso(
        int arg0,
        boolean arg1,
        Response arg2,
        @Also({"1", "2", "0", "-1"}) int arg3) {
    
        /*
        Invoked 45 times.
        45 tuples (arg0, arg1, arg2, arg3) generated.
        Each value of each tuple chosen at random from its domain,
        except arg3 uses 1, 2, 0, -1, and 41 other randomly generated values.
        */
    }
    
    @Property(trials = 77, mode = EXHAUSTIVE)
    public void exhaustiveWithOnly(
        int arg0,
        boolean arg1,
        Response arg2,
        @Also({"1", "2", "0", "-1"}) int arg3) {
    
        /*
        Invoked 77 * 2 * 3 * 77 times --
        once for each choice of 77 randomly chosen arg0,
        two possible values of arg1, three possible values of arg2,
        and the four values named in arg3 plus 73 other randomly generated
        values.
        */
    }
}
```

*Note*: You can use `@Only` on types that do not have a generator.

By default, junit-quickcheck converts the values specified in `@Only` and
`@Also` markers to values of the property parameter type via the following
strategy:

* If present, use a `public static` method on the property parameter type
called `valueOf` which accepts a single `String` argument and whose return
type is the type itself.
* Otherwise, if present, use a `public` constructor on the property parameter
type which accepts a single `String`argument.
* Otherwise, fail.

Supply a custom conversion strategy if you wish via the `by` attribute of
`@Only` or `@Also`.
