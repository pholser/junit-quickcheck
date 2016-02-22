# Generating values of more complex types

Generators for complex types have many options for generating constituent
values.

- Use the `SourceOfRandomness` handed to the generator directly

```java
    public class PolygonGenerator extends Generator<Polygon> {
        @Override public Polygon generate(
            SourceOfRandomness r,
            GenerationStatus status) {

            int numberOfPoints = Math.abs(r.nextInt());

            int xs = new int[numberOfPoints];
            for (int i = 0; i < numberOfPoints; ++i)
                xs[i] = r.nextInt();

            int ys = new int[numberOfPoints];
            for (int i = 0; i < numberOfPoints; ++i)
                ys[i] = r.nextInt();

            return new Polygon(xs, ys, numberOfPoints);
        }
    }
```

- Your generator could have other generators as fields of your generator, and
call upon them as necessary

- Your generator can ask for a generator for values of a specific type via
gen()
-- gen().type() asks for an arbitrary generator that can produce instances of
the given type
-- gen.constructor(Class<?>, Class<?>)...
-- gen.fieldsOf(Class<?>)...
-- gen.parameter(Parameter) asks for an arbitrary generator that can produce
instances of the type of the given reflected method parameter, and that honors
all of the configuration annotations on the parameter
-- gen.field(Class, Field) asks for an arbitrary generator that can produce
instances of the type of the field of a given name on the given type, and that
honors all of the configuration annotations on the field
-- gen.field(Field) asks for an arbitrary generator that can produce instances
of the type of the given reflected field, and that honors all of the
configuration annotations on the field

- Generators for types with component types: ComponentizedGenerator
