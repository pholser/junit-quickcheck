# Configuring generators

If you mark a property parameter with an annotation that is itself marked as
`@GeneratorConfiguration`, then if the `Generator` for that parameter's type
has a public method named `configure` that accepts a single argument of the
annotation type, junit-quickcheck will call the `configure` method
reflectively, passing it the annotation. The generator can then retain the
annotation to influence the results of `generate()`.

*Note*: Remember to mark your configuration annotation as
`@Retention(RetentionPolicy.RUNTIME)`. Otherwise, junit-quickcheck will not
detect it.

```java
    @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
    @Retention(RUNTIME)
    @GeneratorConfiguration
    public @interface NonNegative {
        // ...
    }

    public class IntegralGenerator extends Generator<Integer> {
        private NonNegative nonNegative;

        public IntegralGenerator() {
            super(Arrays.asList(Integer.class, int.class));
        }

        @Override public Integer generate(
            SourceOfRandomness random,
            GenerationStatus status) {

            int value = random.nextInt();
            return nonNegative != null ? abs(value) : value;
        }

        public void configure(NonNegative nonNegative) {
            this.nonNegative = nonNegative;
        }

        // Ever-so-slightly favors +(MIN_VALUE + 1).
        // Math.abs(MIN_VALUE) is negative.
        private static int abs(int i) {
            return i == Integer.MIN_VALUE
                ? Math.abs(i + 1)
                : Math.abs(i);
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public class Numbers {
        @Property public void nonNegativity(@NonNegative int i) {
            assertThat(i, greaterThanOrEqualTo(0));
        }
    }
```

A `Generator` can have many such `configure` methods.


## Configuration on type uses

Configuration annotations that can target type uses will be honored.

*Note for Kotlin users: You may need to tell the Kotlin compiler to
emit annotations on type uses correctly, via the compiler flag
`-Xemit-jvm-type-annotations`.
See [this issue](https://youtrack.jetbrains.com/issue/KT-35843)*.

```java
    @RunWith(JUnitQuickcheck.class)
    public class PropertiesOfListsOfSingleDigits {
        @Property public void hold(
            List<@InRange(min = "0", max = "9") Integer> digits) {
                // ...
        }
    }
```


## Configuration on types in a hierarchy

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
So, for example, for parameter `s` above, junit-quickcheck might choose
generators for integral values, but not generators for `ArrayList`s.
junit-quickcheck will complain loudly if there are no such generators
available.

If you have a family of generators that can produce members of a hierarchy,
you may want to ensure that all the generators respect the same attributes
of a given configuration annotation. Not doing so could lead to surprising
results.


## Aggregating configuration

Configuration annotations that are directly on a property parameter, and any
configuration annotations on annotations that are directly on a property
parameter (and so on...) are collected to configure the generator(s) for the
parameter.

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
