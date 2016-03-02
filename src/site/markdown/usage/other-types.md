# Generating values of other types


## Explicit generators

To generate random values for property parameters of other types, or to
override the default means of generation for a supported type, mark the
property parameter with `@From` and supply the class of the `Generator` to be
used. If you give multiple `@From` annotations, junit-quickcheck will choose
one on every generation with probability in proportion to its `frequency`
attribute (default is 1).

```java
    public class Version5 extends Generator<UUID> {
        @Override public UUID generate(
            SourceOfRandomness r,
            GenerationStatus status) {

            // ...
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public class IdentificationProperties {
        @Property public void shouldHold(@From(Version5.class) UUID u) {
            // ...
        }
    }
```

junit-quickcheck has built-in facilities for generating values for types that
are functional interfaces (whether or not they are marked with
[FunctionalInterface]
(http://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html),
arrays, or `enum`s. Explicitly naming generators for parameters of these types
overrides the built-in means of generation. This is usually necessary for
functional interfaces that involve generics.


## Implied generators via `ServiceLoader`

To use a generator for your own type without having to use `@From`, you can
package it in a [ServiceLoader]
(http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
JAR file and place the JAR on the class path. junit-quickcheck will make
generators packaged in this way available for use. The generators in the module
`junit-quickcheck-generators` are loaded via this mechanism also; any
generators you supply and make available to the `ServiceLoader` complement
these generators rather than override them.

## `Ctor`

You can generate values for a type that has a single accessible constructor
by using the `Ctor` generator with `@From`.

```java
    public class DollarsAndCents {
        private final BigDecimal amount;

        public Money(BigDecimal amount) {
           this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }

        // ...
    }

    @RunWith(JUnitQuickcheck.class)
    public class DollarsAndCentsProperties {
        @Property public void rounding(@From(Ctor.class) DollarsAndCents d) {
            // ...
        }
    }
```

junit-quickcheck will find generators appropriate for the types of the
constructor parameters, and call upon them to supply values to the constructor
invocation. Any [configuration annotations](configuring.html) on the
constructor parameters will be honored.


## `Fields`

You can generate values for a type that has an accessible zero-arg constructor
by using the `Fields` generator with `@From`.

```java
    public class Counter {
        private int count;

        public Counter increment() {
            ++count;
            return this;
        }

        public Counter decrement() {
            --count;
            return this;
        }

        public int count() {
            return count;
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public class CounterProperties {
        @Property public void incrementing(@From(Fields.class) Counter c) {
            int count = c.count();
            assertEquals(count + 1, c.increment().count());
        }

        @Property public void decrementing(@From(Fields.class) Counter c) {
            int count = c.count();
            assertEquals(count - 1, c.decrement().count());
        }
    }
```

junit-quickcheck will find generators appropriate for the types of the fields
of the class, and call upon them to supply values to the fields of a new
instance of the class (subverting access protection). Any
[configuration annotations](configuring.html) on the fields will be honored.
