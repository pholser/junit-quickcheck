# junit-quickcheck: Property-based testing, JUnit-style

junit-quickcheck is a library that supports writing and running property-based
tests in [JUnit](http://junit.org), inspired by
[QuickCheck](http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html) for
[Haskell](http://haskell.org).

junit-quickcheck is source/target-compatible with JDK 8.


## Basic example

```java
    import com.pholser.junit.quickcheck.Property;
    import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
    import org.junit.runner.RunWith;

    import static org.junit.Assert.*;

    @RunWith(JUnitQuickcheck.class)
    public class StringProperties {
        @Property public void concatenationLength(String s1, String s2) {
            assertEquals(s1.length() + s2.length(), (s1 + s2).length());
        }
    }
```