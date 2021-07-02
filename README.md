[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pholser/property-binder/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.pholser/junit-quickcheck)
[![Build Status](https://travis-ci.org/pholser/junit-quickcheck.svg?branch=master)](https://travis-ci.org/pholser/junit-quickcheck)
[![Code Quality: Java](https://img.shields.io/lgtm/grade/java/g/pholser/junit-quickcheck.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/pholser/junit-quickcheck/context:java)
[![Total Alerts](https://img.shields.io/lgtm/alerts/g/pholser/junit-quickcheck.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/pholser/junit-quickcheck/alerts)

<a href="http://www.yegor256.com/2015/10/17/award-2016.html">
  <img src="http://www.yegor256.com/images/award/2016/winner-pholser.png" width="203" height="45" alt="Software Quality Award 2016"/>
</a>

# junit-quickcheck: Property-based testing, JUnit-style

junit-quickcheck is a library that supports writing and running property-based
tests in JUnit, inspired by QuickCheck for Haskell.

Property-based tests capture characteristics, or "properties", of the output
of code that should be true given arbitrary inputs that meet certain criteria.
For example, imagine a function that produces a list of the prime factors of
a positive integer `n` greater than 1. Regardless of the specific value of
`n`, the function must give a list whose members are all primes, must
equal `n` when all multiplied together, and must be different from the
factorization of a positive integer `m` greater than 1 and not equal to
`n`.

Rather than testing such properties for all possible inputs, junit-quickcheck
and other QuickCheck kin generate some number of random inputs, and verify
that the properties hold at least for the generated inputs. This gives us
some reasonable assurance upon repeated test runs that the properties
hold true for any valid inputs.

## Documentation

[Documentation for the current stable version](https://pholser.github.io/junit-quickcheck/index.html)

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

## Other examples

After browsing the [documentation](#documentation), have a look at some
[examples](examples) in module `junit-quickcheck-examples`. These are built
with junit-quickcheck.
