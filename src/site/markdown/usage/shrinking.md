# Shrinking

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
Your custom generators can, of course, do the same.

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


## Influencing the shrinking process

- By default, shrinking is enabled. To disable it, set the `shrink` attribute
of a `@Property` annotation to `false`.
- To reduce or increase the maximum number of shrink attempts made for a given
property, set the `maxShrinks` attribute of that `@Property`.
- To reduce or increase the maximum "depth" of the shrink search "tree" for a
given property, set the `maxShrinkDepth` attribute of that `@Property`.
