[![Build Status](https://travis-ci.org/pholser/junit-quickcheck.svg?branch=master)](https://travis-ci.org/pholser/junit-quickcheck)

<a href="http://www.yegor256.com/2015/10/17/award-2016.html">
  <img src="http://www.yegor256.com/images/award/2016/winner-pholser.png" width="203" height="45"/>
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
some reasonable assurance over time that the properties hold true for any
valid inputs.

## Getting junit-quickcheck
 
[Version 0.7](https://pholser.github.io/junit-quickcheck/index.html) is the
current stable version of junit-quickcheck.

[Version 0.8-alpha-7](http://pholser.github.io/junit-quickcheck/site/0.8-alpha-7/)
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
