[![Build Status](https://travis-ci.org/pholser/junit-quickcheck.svg?branch=master)](https://travis-ci.org/pholser/junit-quickcheck)

<a href="http://www.yegor256.com/2015/10/17/award-2016.html">
  <img src="http://www.yegor256.com/images/award/2016/winner-pholser.png" width="203" height="45"/>
</a>

# junit-quickcheck: Property-based testing, JUnit-style

junit-quickcheck is a library that supports writing and running property-based
tests in JUnit, inspired by QuickCheck for Haskell.

[Version 0.7](https://pholser.github.io/junit-quickcheck/index.html) is the
current stable version of junit-quickcheck.

[Version 0.8-alpha-6](http://pholser.github.io/junit-quickcheck/site/0.8-alpha-6/)
is available.

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
