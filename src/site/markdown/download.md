# Download

junit-quickcheck's machinery is contained in the JAR file for the module
`junit-quickcheck-core`. You will want to start out also with the JAR file for
the module `junit-quickcheck-generators`, which consists of generators for
basic Java types, such as primitives, arrays, and collections.

There is also a module `junit-quickcheck-guava`, containing generators for
[Guava](https://github.com/google/guava) types.

Releases are synced to the central Maven repository. Declare `<dependency>`
elements in your POM like so:

```xml
    ...
    <dependencies>
      ...
      <dependency>
        <groupId>com.pholser</groupId>
        <artifactId>junit-quickcheck-core</artifactId>
        <version>0.6-alpha-2</version>
      </dependency>
      <dependency>
        <groupId>com.pholser</groupId>
        <artifactId>junit-quickcheck-generators</artifactId>
        <version>0.6-alpha-2</version>
      </dependency>
      ...
    </dependencies>
    ...
```
