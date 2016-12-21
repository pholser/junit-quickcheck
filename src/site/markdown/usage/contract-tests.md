# Contract Tests

Beginning with version 0.8, junit-quickcheck supports the usual JUnit
machinery on interface default methods (`@Property`, `@Before`, `@After`,
`@Rule`), interface static methods (`@BeforeClass`, `@AfterClass`,
`@ClassRule`), and interface fields (`@ClassRule`).

As with JUnit 5, this allows for tests for interface contracts:

```java
    public interface ComparatorContract<T> {
        Comparator<T> subject();

        @Property default void symmetry(T x, T y) {
            Comparator<T> subject = subject();

            assertEquals(
                signum(subject.compare(x, y)),
                -signum(subject.compare(y, x)),
                0F);
        }
    }

    @RunWith(JUnitQuickcheck.class)
    public class StringCaseInsensitiveProperties
        implements ComparatorContract<String> {
    
        @Override public Comparator<String> subject() {
            return String::compareToIgnoreCase;
        }
    }

    public interface ComparableVersusEqualsContract<T extends Comparable<T>> {
        T thingComparableTo(T thing);

        @Property default void equalsConsistency(T thing) {
            T other = thingComparableTo(thing);
            assumeThat(thing.compareTo(other), equalTo(0));

            assertEquals(thing, other);
        }
    }
    
    @RunWith(JUnitQuickcheck.class)
    public class StringProperties
        implements ComparableVersusEqualsContract<String> {

        @Override public String thingComparableTo(String s) {
            return new String(s);
        }
    }
```
