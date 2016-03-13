# Constraining generated values

## Assumptions

Properties often use _assumptions_ to declare conditions under which they
hold.

```java
    @RunWith(JUnitQuickcheck.class)
    public class PrimeFactorsProperties {
        @Property public void factorsPassPrimalityTest(BigInteger n) {
            assumeThat(n, greaterThan(ZERO));

            for (BigInteger each : PrimeFactors.of(n))
                assertTrue(each.isProbablePrime(1000));
        }

        @Property public void factorsMultiplyToOriginal(BigInteger n) {
            assumeThat(n, greaterThan(ZERO));

            BigInteger product = ONE;
            for (BigInteger each : PrimeFactors.of(n))
                product = product.multiply(each);

            assertEquals(n, product);
        }

        @Property public void factorizationsAreUnique(
            BigInteger m,
            BigInteger n) {

            assumeThat(m, greaterThan(ZERO));
            assumeThat(n, greaterThan(ZERO));
            assumeThat(m, not(equalTo(n)));

            assertThat(PrimeFactors.of(m), not(equalTo(PrimeFactors.of(n))));
        }
    }
```

Sometimes, using assumptions with junit-quickcheck can yield too few values
that meet the desired criteria.

```java
    @RunWith(JUnitQuickcheck.class)
    public class SingleDigitProperties {
        @Property public void hold(int digit) {
            // hope we get enough single digits
            assumeThat(digit, greaterThanOrEqualTo(0));
            assumeThat(digit, lessThanOrEqualTo(9));

            // ...
        }
    }
```


## Generator configuration methods

Generator configuration methods and annotations can constrain the values that
a generator emits. For example, the `@InRange` annotation on property
parameters of integral, floating-point, and `Date` types causes the generators
for those types to emit values that fall within a configured minimum/maximum.

```java
    @RunWith(JUnitQuickcheck.class)
    public class SingleDigitProperties {
        @Property public void hold(@InRange(min = "0", max = "9") int digit) {
            // ...
        }
    }
```

Now, the generator will be configured to ensure that every value generated
meets the desired criteria -- no need to express the desired range of values
as an assumption.


## Configuration methods vs. assumptions

When using assumptions with junit-quickcheck, every value fed to a property
parameter counts against the sample size, even if it doesn't pass any
assumptions made against it in the property. You could end up with no values
passing the assumption.

Using generator configurations, assumptions aren't very important, if needed
at all -- every value fed to a property parameter counts against the sample
size, but will meet some conditions that assumptions would otherwise have
tested.


## `ValuesOf`

You can mark `boolean` and `enum` property parameters with `@ValuesOf` to
force the generation to run through every value in the type's domain, instead
of choosing an element from the domain at random every time.

```java
    enum Ternary { YES, NO, MAYBE }

    @RunWith(JUnitQuickcheck.class)
    public class SmallDomainsProperties {
        @Property public void hold(@ValuesOf boolean b, @ValuesOf Ternary t) {
            // Each verification will be with a different value for b and t.
        }
    }
```

## Constraint expressions

Constraint expressions allow you to filter the values that reach a property
parameter. Mark the parameter with `@When` and give that annotation's
`satisfies` attribute an [OGNL](http://commons.apache.org/ognl) expression
that will be used to decide whether a generated value will be given to that
parameter.

```java
    @RunWith(JUnitQuickcheck.class)
    public class SingleDigitProperties {
        @Property public void hold(
            @When(satisfies = "#_ >= 0 && #_ <= 9") int digit) {

            // ...
        }
    }
```

A property parameter is referred to as "_" in the constraint expression.
Constraint expressions cannot refer to other property parameters.

junit-quickcheck generates values for a property parameter with a constraint
expression until the ratio of constraint failures constraint passes is
greater than the `discardRatio` specified by `@When`, if any. Exceeding the
discard ratio raises an exception and thus fails the property.
