# Generating null values

By default, generated values are never `null`. One possible way to generate an optional
value of type `T` in Java is to declare a parameter of type `Optional<T>`.

However, in some cases it preferable to generate values that can include
`null`. For example, Kotlin's type system explicitly manages nullable types and
therefore Kotlin programs use nullable types like `T?` instead of `Optional<T>` to
indicate that a value may be absent. 

In order to generate `null` values, attach the JSR 303
`javax.annotation.Nullable` annotation to a parameter of any supported type:

```java
    @RunWith(JUnitQuickcheck.class)
    public class NullableParameterProperties {
        @Property public void validPhoneNumber(@Nullable String areaCode, String lineNumber) {
            // ...
        }
    }
```

Alternatively, use the annotation `@NullAllowed` introduced by junit-quickcheck,
which makes it possible to indicate the probability of generating a `null` value:

```java
    @RunWith(JUnitQuickcheck.class)
    public class NullableParameterProperties {
        @Property public void validPhoneNumber(@NullAllowed(probability = 0.8f) String areaCode, String lineNumber) {
            // ...
        }
    }
```
