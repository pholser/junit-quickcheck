# Migrating from theories to properties

- Instead of the `Theories` runner, with `@Theory` methods with parameters
marked `@ForAll`, we use the `JUnitQuickcheck` runner, with `@Property`
methods. `@ForAll` is marked as deprecated; support for the
`Theories`-based junit-quickcheck may be removed soon.
- There are `Property.trials()` executions of the property, instead of a
combinatorial number of executions of a `Theory`
- Use the `@When` annotation to control seed and parameter value generation
if desired
- You can still mark property parameters with configuration annotations,
just like you did with `@ForAll` theory parameters
- Your generators should work the same as before
