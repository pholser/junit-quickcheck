# Shrinking

Version 0.6 of junit-quickcheck introduces one of the most compelling features
of a proper QuickCheck: _shrinking_. When a property is disproved for a given
set of values, junit-quickcheck will attempt to find "smaller" sets of values
that also disprove the property, and will report the smallest such set, leading to an easier bug investigation for the developer.

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

the shrinking process might find a smaller value:

    java.lang.AssertionError: Property named 'primality' failed (4):
    With arguments: [4]
    Original failure message: 422886279
    First arguments found to also provoke a failure: [422886279]
    Seeds for reproduction: [8922134899668966991]


## Producing "smaller" values

When a property fails for a set of parameter values, generators of those values
can be called upon to offer "candidates" for smaller values that might also
cause failures. By default, a generator "can" shrink a value (as reported by
its `canShrink()` method) if the value is assignment-compatible with the kind
of values that generator produces, and offers up an empty list of candidates
to the shrinking process (via its `doShrink()` method).

Most of the generators in module `junit-quickcheck-generators`, including
those for primitives/their wrappers, collections, maps, and arrays,
override `doShrink()` to offer "smaller" values to the shrinking process.
Your custom generators can, of course, do the same. As of version 0.8,
you can also override `magnitude()` to give the shrinking machinery hints
as to the relative "size" of values that your generator produces.
Shrinkers should provide small values before big ones to make the shrinking process more efficient.

```java
    import java.awt.Point;

    public class Points extends Generator<Point> {
        private static final Point ORIGIN = new Point(0, 0);

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

            return Stream.of(
                new Point(0, 0),
                new Point(0, larger.y / 2),
                new Point(larger.x / 2, 0),
                new Point(larger.x / 2, larger.y / 2),
                new Point(0, larger.y),
                new Point(larger.x, 0))
                .distinct()
                .collect(Collectors.toList());
        }
        
        @Override public BigDecimal magnitude(Object value) {
            return BigDecimal.valueOf(x)
                // For business reasons, y contributes twice as much to the total "magnitude" of a Point as x
                .add(BigDecimal.valueOf(y).times(2));
        }
    }
```


## Influencing the shrinking process

- By default, shrinking is enabled. To disable it, set the `shrink` attribute
of a `@Property` annotation to `false`.
- To reduce or increase the maximum number of shrink attempts made for a given
property, set the `maxShrinks` attribute of that `@Property`.
- To reduce or increase the maximum "depth" of the shrink search "tree" for a
given property, set the `maxShrinkDepth` attribute of that `@Property`.
- Generators that offer configuration annotations that influence the
generation process are responsible for ensuring that any shrunken values they
offer conform to the constraints of specific configuration annotations.
