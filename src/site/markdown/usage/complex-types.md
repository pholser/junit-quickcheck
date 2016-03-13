# Generating values of more complex types

Generators for complex types have many options for generating constituent
values.


## Using the `SourceOfRandomness` handed to the generator directly

```java
    public class PolygonGenerator extends Generator<Polygon> {
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


## Embedding other generators as fields of the generator, calling upon them as
necessary

```java
    public class NonNegativeInts extends Generator<Integer> {
        // ...

        @Override
        public Integer generate(
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
        private final Generator<Integer> nonNegatives =
            new NonNegativeInts();

        // ...

        @Override
        public Counter generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new Counter(nonNegatives.generate(random, status));
        }
    }
```


## Asking for a generator for values of a specific type via `gen()`

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
        // ...
        @Override
        public Counter generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new Counter(
                Math.abs(gen().type(int.class).generate(random, status))
            );
        }
    }
```

- `gen.constructor(...)`

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
        // ...
        @Override
        public Counter generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new TrafficTracker(
                gen().constructor(Counter.class, int.class)
                    .generate(random, status));
        }
    }
```

- `gen.fieldsOf(...)`

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
        // ...
        @Override
        public Counter generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            return new TrafficTracker(
                gen().fieldsOf(Counter.class).generate(random, status));
        }
    }
```

- `gen.parameter(...)`

This method gives a generator that can produce instances of the type of the
given reflected method parameter, and that honors all of the configuration
annotations on the parameter.

- `gen.field(Class<?>, String)`

This method gives a generator that can produce instances of the type of the
field of a given name on the given type, and that honors all of the
configuration annotations on the field.

- `gen.field(Field)`

This method gives a generator that can produce instances of the type of the
given reflected field, and that honors all of the configuration annotations
on the field.


# Generators for types with component types: `ComponentizedGenerator`

Extend `ComponentizedGenerator` instead of `Generator` when the type of values
you need to generate have component types; for example, collections, maps.
This is usually necessary for generating values for types that involve
generics.
