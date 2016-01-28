# Supported types

"Out of the box" (core + generators), junit-quickcheck recognizes property
parameters of the following types:

* all Java primitives and primitive wrappers
* `java.math.Big(Decimal|Integer)`
* `java.util.Date`
* any `enum`
* `String`
* "functional interfaces" (interfaces with a single method that does not
  override a method from `java.lang.Object`)
* `java.util.ArrayList` and `java.util.LinkedList` of supported types
* `java.util.HashSet` and `java.util.LinkedHashSet` of supported types
* `java.util.HashMap` and `java.util.LinkedHashMap` of supported types
* arrays of supported types
* others...

When many generators can satisfy a given property parameter based on its type
(for example, `java.io.Serializable`), on a given generation junit-quickcheck
will choose one of the multiple generators at random with (roughly) equal
probability.
