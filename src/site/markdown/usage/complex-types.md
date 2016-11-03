# Generating values of more complex types

Generators for complex types have many options for generating constituent
values.


## Using the `SourceOfRandomness` handed to the generator directly

```java
    import java.awt.Polygon;

    public class PolygonGenerator extends Generator<Polygon> {
        public PolygonGenerator() {
           super(Polygon.class);
        }

        @Override public Polygon generate(
            SourceOfRandomness r,
            GenerationStatus status) {

            int numberOfPoints = Math.abs(r.nextInt());

            int xs = new int[numberOfPoints];
            for (int i = 0; i < numberOfPoints; ++i)
                xs[i] = r.nextInt();

            int ys = new int[numberOfPoints];
            for (int i = 0; i < numberOfPoints; ++i)
                ys[i] = r.nextInt();

            return new Polygon(xs, ys, numberOfPoints);
        }
    }
```


## Calling for a generator for values of a specific type via `gen()`

Generators that are made available to the `ServiceLoader` or referenced via
`@From` can access other such generators via the superclass method `gen()`.

- `gen().type(...)`

This method asks for an arbitrary generator that can produce instances of the
given type.

```java
    public class Counter {
        private int count;

        public Counter(int count) {
            this.count = count;
        }

        // ...
    }

    public class Counters extends Generator<Counter> {
        public Counters() {
            super(Counter.class);
        }

        @Override public Counter generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new Counter(
                Math.abs(gen().type(int.class).generate(random, status)));
        }
    }
```

- `gen().constructor(...)`

This method gives a generator that produces instances of the given type by
invoking a constructor reflectively, generating random values for the
constructor's arguments using available generators. This works a lot like
annotating a property parameter with `@From(Ctor.class)`. Any
[configuration annotations](configuring.html) on the constructor parameters
will be honored.

```java
    public class Counter {
        private int count;

        public Counter(int count) {
            this.count = count;
        }

        // ...
    }

    public class TrafficTracker {
        private final Counter counter;

        public TrafficTracker(Counter counter) {
            this.counter = counter;
        }
    }

    public class TrafficTrackers extends Generator<TrafficTracker> {
        public TrafficTrackers() {
            super(TrafficTracker.class);
        }

        @Override public Counter generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new TrafficTracker(
                gen().constructor(Counter.class, int.class)
                    .generate(random, status));
        }
    }
```

- `gen().fieldsOf(...)`

This method gives a generator that produces instances of the given type by
instantiating using an accessible zero-arg constructor, and generating random
values for the instance's fields using available generators. This works a lot
like annotating a property parameter with `@From(Fields.class)`. Any
[configuration annotations](configuring.html) on the fields will be honored.

```java
    public class Counter {
        private int count;

        // ...
    }

    public class TrafficTracker {
        private final Counter counter;

        public TrafficTracker(Counter counter) {
            this.counter = counter;
        }
    }

    public class TrafficTrackers extends Generator<TrafficTracker> {
        public TrafficTrackers() {
            super(TrafficTracker.class);
        }

        @Override public Counter generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new TrafficTracker(
                gen().fieldsOf(Counter.class).generate(random, status));
        }
    }
```

- `gen().parameter(...)`

This method gives a generator that can produce instances of the type of the
given reflected method parameter, and that honors all of the configuration
annotations on the parameter.

- `gen().field(Class<?>, String)`

This method gives a generator that can produce instances of the type of the
field of a given name on the given type, and that honors all of the
configuration annotations on the field.

- `gen().field(Field)`

This method gives a generator that can produce instances of the type of the
given reflected field, and that honors all of the configuration annotations
on the field.

- `gen().make(...)`

This method makes an instance of the given generator class, makes the
available generators available to it, and configures it with whatever
configuration annotations live on the generator class.

```java
    public class NonNegativeInts extends Generator<Integer> {
        public NonNegativeInts() {
            super(Arrays.asList(Integer.class, int.class));
        }

        @Override public Integer generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return Math.abs(random.nextInt());
        }
    }

    public class Counter {
        private int count;

        public Counter(int count) {
            this.count = count;
        }

        // ...
    }

    public class Counters extends Generator<Counter> {
        public Counters() {
            super(Counter.class);
        }

        @Override public Counter generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new Counter(
                gen().make(NonNegativeInts.class)
                    .generate(random, status));
        }
    }
```

# Generators for types with component types: `ComponentizedGenerator`

Extend class `ComponentizedGenerator` instead of `Generator` when the type
of values you need to generate has component types; for example, collections
and maps. This is usually necessary for generating values for types that
involve generics.

```java
    public final class Either<L, R> {
        private final Optional<L> left;
        private final Optional<R> right;

        private Either(Optional<L> left, Optional<R> right) {
            this.left = left;
            this.right = right;
        }

        public static <A, B> Either<A, B> makeLeft(A left) {
            return new Either<>(Optional.of(left), Optional.empty());
        }

        public static <A, B> Either<A, B> makeRight(B right) {
            return new Either<>(Optional.empty(), Optional.of(right));
        }

        public <T> T map(
            Function<? super L, ? extends T> ifLeft,
            Function<? super R, ? extends T> ifRight) {

            return left.map(ifLeft).orElseGet(() -> right.map(ifRight).get());
        }

        @Override public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof Either<?, ?>))
                return false;

            Either<?, ?> other = (Either<?, ?>) o;
            return left.equals(other.left) && right.equals(other.right);
        }

        @Override public int hashCode() {
            return Objects.hash(left, right);
        }

        @Override public String toString() {
            return map(
                left -> "Left[" + left + ']',
                right -> "Right[" + right + ']');
        }
    }

    public class EitherGenerator extends ComponentizedGenerator<Either> {
        public EitherGenerator() {
            super(Either.class);
        }

        @Override public Either<?, ?> generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return random.nextBoolean()
                ? Either.createLeft(componentGenerators().get(0).generate(random, status))
                : Either.createRight(componentGenerators().get(1).generate(random, status));
        }

        @Override public int numberOfNeededComponents() {
            return 2;
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public class Eithers {
        @Property public void constrained(
            Either<
                @InRange(minInt = 0) Integer,
                @InRange(minDouble = -7.0, maxDouble = -4.0) Double> e) {

            e.map(
                left -> {
                    assertThat(left, greaterThanOrEqualTo(0));
                    return 0;
                },
                right -> {
                    assertThat(
                        right,
                        allOf(greaterThanOrEqualTo(-7.0), lessThanOrEqualTo(-4.0)));
                    return 1;
                }
            );
        }
    }
```
