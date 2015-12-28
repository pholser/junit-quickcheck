# Generating values of other types

To generate random values for property parameters of other types, or to
override the default means of generation for a supported type, mark the
property parameter with `@From` and supply the class of the `Generator` to be
used. If you give multiple `@From` annotations, junit-quickcheck will choose
one on every generation with probability in proportion to its `frequency`
attribute (default is 1).

```java
    @RunWith(JUnitQuickcheck.class)
    public class IdentificationProperties {
        @Property public void shouldHold(@From(Version5.class) UUID u) {
            // ...
        }
    }
```

To add a generator for a type without having to use `@From`, you can package it
in a [ServiceLoader]
(http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
JAR file and place the JAR on the class path. junit-quickcheck will make
generators packaged in this way available for use. The generators in the module
`junit-quickcheck-generators` are loaded via this mechanism also; any
generators you supply and make available to the `ServiceLoader` complement
these generators rather than override them.


## Functional interfaces

Custom generators for types that are functional interfaces override the
built-in means of generation for such types. This is usually necessary for
functional interfaces that involve generics.
