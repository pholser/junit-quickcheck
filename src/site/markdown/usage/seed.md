# Seed

For each property parameter, junit-quickcheck uses a unique value as a seed
for the source of randomness used to generate the parameter's values. To fix
the seed value for a property parameter, use the `seed` attribute of the
`@When` annotation:

```java
    @RunWith(JUnitQuickcheck.class)
    public class SameValues {
        @Property public void holds(@When(seed = -1L) int i) {
            // ...
        }
    }
```

You may want to fix the seed when a property fails, so that you can test the
property over and over again with the same set of generated values that caused
the failure.

junit-quickcheck reports the seed used for a given property parameter by
logging it to a [SLF4J](http://www.slf4j.org/) logger named
`junit-quickcheck.seed-reporting`, at `DEBUG` level:

    Seed for parameter com.your.PropertiesClass.propertyMethod:parameterName is 8007238959251963394

Add an SLF4J binding JAR file to your test class path and logging configuration
for your chosen bound library to see the seed log messages.
