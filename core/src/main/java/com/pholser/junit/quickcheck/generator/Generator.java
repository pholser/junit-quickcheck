/*
 The MIT License

 Copyright (c) 2010-2020 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.javaruntype.type.WildcardTypeParameter;

import javax.annotation.Nonnull;

import static java.math.BigDecimal.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * Produces values for property parameters.
 *
 * @param <T> type of property parameter to apply this generator's values to
 */
public abstract class Generator<T> implements Gen<T>, Shrink<T> {
    private final List<Class<T>> types = new ArrayList<>();

    private Generators repo;

    /**
     * @param type class token for type of property parameter this generator is
     * applicable to
     */
    protected Generator(Class<T> type) {
        this(singletonList(type));
    }

    /**
     * Used for generators of primitives and their wrappers. For example, a
     * {@code Generator<Integer>} can be used for property parameters of type
     * {@code Integer} or {@code int}.
     *
     * @param types class tokens for type of property parameter this generator
     * is applicable to
     */
    protected Generator(List<Class<T>> types) {
        this.types.addAll(types);
    }

    /**
     * @return class tokens for the types of property parameters this generator
     * is applicable to
     */
    @Nonnull
    public List<Class<T>> types() {
        return unmodifiableList(types);
    }

    /**
     * Tells whether this generator is allowed to be used for property
     * parameters of the given type.
     *
     * @param type type against which to test this generator
     * @return {@code true} if the generator is allowed to participate in
     * generating values for property parameters of {@code type}
     */
    public boolean canRegisterAsType(Class<?> type) {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Generators first ensure that they {@linkplain #canShrink(Object) can
     * participate} in shrinking the given value, and if so, they
     * {@linkplain #doShrink(SourceOfRandomness, Object) produce shrinks}.</p>
     */
    @Nonnull
    @Override public final List<T> shrink(@Nonnull SourceOfRandomness random, Object larger) {
        if (!canShrink(larger)) {
            throw new IllegalStateException(
                getClass() + " not capable of shrinking " + larger);
        }

        return doShrink(random, narrow(larger));
    }

    /**
     * <p>Tells whether this generator is allowed to participate in the
     * {@link Shrink} process for the given "larger" value.</p>
     *
     * <p>Unless overridden, the only criterion for whether a generator is
     * allowed to participate in shrinking a value is if the value can be
     * safely cast to the type of values the generator produces.</p>
     *
     * @param larger the "larger" value
     * @return whether this generator can participate in "shrinking" the larger
     * value
     */
    public boolean canShrink(Object larger) {
        return types().get(0).isInstance(larger);
    }

    /**
     * <p>Gives some objects that are "smaller" than a given "larger"
     * object.</p>
     *
     * <p>Unless overridden, a generator will produce an empty list of
     * "smaller" values.</p>
     *
     * @param random source of randomness to use in shrinking, if desired
     * @param larger the larger object
     * @return objects that are "smaller" than the larger object. Smaller objects should be provided before bigger objects to make shrinking process faster.
     */
    @Nonnull
    public List<T> doShrink(@Nonnull SourceOfRandomness random, T larger) {
        return emptyList();
    }

    /**
     * <p>Gives a hint to the shrinking process as to the magnitude of the given
     * value. The shrinking process will prefer trying values of greater
     * magnitude before values of lesser magnitude. If not overridden, this
     * implementation returns "one".</p>
     *
     * <p><em>Note to generator writers:</em> Do not worry about normalizing
     * a magnitude to a positive value; the shrinking mechanism will take care
     * of it.</p>
     *
     * @see #narrow(Object)
     * @param value the value to assess
     * @return a measure of the given value's magnitude
     */
    @Nonnull
    public BigDecimal magnitude(Object value) {
        return ONE;
    }

    /**
     * <p>Attempts to "narrow" the given object to the type this generator
     * produces.</p>
     *
     * <p>This is intended for use only by junit-quickcheck itself, and not by
     * creators of custom generators.</p>
     *
     * @param wider target of the narrowing
     * @return narrowed the result of the narrowing
     * @throws ClassCastException if the narrowing cannot be performed
     */
    protected final T narrow(Object wider) {
        return types().get(0).cast(wider);
    }

    /**
     * <p>This is intended for use only by junit-quickcheck itself, and not by
     * creators of custom generators.</p>
     *
     * @return whether this generator has component generators, such as for
     * those generators that produce lists or maps
     * @see #addComponentGenerators(java.util.List)
     */
    public boolean hasComponents() {
        return false;
    }

    /**
     * <p>This is intended for use only by junit-quickcheck itself, and not by
     * creators of custom generators.</p>
     *
     * @return how many component generators this generator needs
     * @see #addComponentGenerators(java.util.List)
     */
    public int numberOfNeededComponents() {
        return 0;
    }

    /**
     * <p>Adds component generators to this generator.</p>
     *
     * <p>Some generators need component generators to create proper values.
     * For example, list generators require a single component generator in
     * order to generate elements that have the type of the list parameter's
     * type argument.</p>
     *
     * <p>This is intended for use only by junit-quickcheck itself, and not by
     * creators of custom generators.</p>
     *
     * @param newComponents component generators to add
     */
    public void addComponentGenerators(@Nonnull List<Generator<?>> newComponents) {
        // do nothing by default
    }

    /**
     * @param typeParameters a list of generic type parameters
     * @return whether this generator can be considered for generating values
     * for property parameters that have the given type parameters in their
     * signatures
     */
    public boolean canGenerateForParametersOfTypes(@Nonnull List<TypeParameter<?>> typeParameters) {
        return true;
    }

    /**
     * <p>Configures this generator using annotations from a given annotated
     * type.</p>
     *
     * <p>This method considers only annotations that are themselves marked
     * with {@link GeneratorConfiguration}.</p>
     *
     * <p>By default, the generator will configure itself using this
     * procedure:</p>
     * <ul>
     *   <li>For each of the given annotations:
     *     <ul>
     *       <li>Find a {@code public} method on the generator named
     *       {@code configure}, that accepts a single parameter of the
     *       annotation type</li>
     *       <li>Invoke the {@code configure} method reflectively, passing the
     *       annotation as the argument</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * @param annotatedType a type usage
     * @throws GeneratorConfigurationException if the generator does not
     * "understand" one of the generation configuration annotations on
     * the annotated type
     */
    public void configure(@Nonnull AnnotatedType annotatedType) {
        configureStrict(collectConfigurationAnnotations(annotatedType));
    }

    /**
     * @param element an annotated program element
     */
    public void configure(@Nonnull AnnotatedElement element) {
        configureLenient(collectConfigurationAnnotations(element));
    }

    /**
     * <p>Supplies the available generators to this one.</p>
     *
     * <p>This is intended for use only by junit-quickcheck itself, and not by
     * creators of custom generators.</p>

     * @param provided repository of available generators
     */
    public void provide(Generators provided) {
        repo = provided;
    }

    /**
     * Used by the framework to make a copy of the receiver.
     *
     * @return a copy of the receiver
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public Generator<T> copy() {
        return (Generator<T>) instantiate(getClass());
    }

    /**
     * @param parameter a generic type parameter
     * @param clazz a type
     * @return whether the type is compatible with the generic type parameter
     * @see #canGenerateForParametersOfTypes(List)
     */
    protected static boolean compatibleWithTypeParameter(
        TypeParameter<?> parameter,
        Class<?> clazz) {

        return parameter instanceof WildcardTypeParameter
            || parameter.getType().isAssignableFrom(Types.forJavaLangReflectType(clazz));
    }

    /**
     * @return an access point for the available generators
     */
    protected Generators gen() {
        return repo;
    }

    /**
     * @param random a source of randomness used when locating other
     * generators
     * @return an access point for the available generators
     */
    protected Generators gen(SourceOfRandomness random) {
        return repo.withRandom(random);
    }

    /**
     * Gives a list of the {@link GeneratorConfiguration} annotations present
     * on the given program element.
     *
     * @param element an annotated program element
     * @return what configuration annotations are present on that element
     */
    protected static List<Annotation> configurationAnnotationsOn(AnnotatedElement element) {
        return allAnnotations(element).stream()
            .filter(a -> a.annotationType().isAnnotationPresent(GeneratorConfiguration.class))
            .collect(toList());
    }

    private Map<Class<? extends Annotation>, Annotation> collectConfigurationAnnotations(
        AnnotatedElement element) {

        if (element == null)
            return emptyMap();

        List<Annotation> configs = configurationAnnotationsOn(element);

        Map<Class<? extends Annotation>, Annotation> byType = new HashMap<>();
        for (Annotation each : configs)
            byType.put(each.annotationType(), each);
        return byType;
    }

    private void configureStrict(Map<Class<? extends Annotation>, Annotation> byType) {
        for (Map.Entry<Class<? extends Annotation>, Annotation> each : byType.entrySet())
            configureStrict(each.getKey(), each.getValue());
    }

    private void configureStrict(
        Class<? extends Annotation> annotationType,
        Annotation configuration) {

        configure(
            annotationType,
            configuration,
            ex -> {
                throw new GeneratorConfigurationException(
                    String.format(
                        "Generator %s does not understand configuration annotation %s",
                        getClass().getName(),
                        annotationType.getName()),
                    ex);
            });
    }

    private void configureLenient(Map<Class<? extends Annotation>, Annotation> byType) {
        for (Map.Entry<Class<? extends Annotation>, Annotation> each : byType.entrySet())
            configureLenient(each.getKey(), each.getValue());
    }

    private void configureLenient(
        Class<? extends Annotation> annotationType,
        Annotation configuration) {

        configure(annotationType, configuration, ex -> {});
    }

    private void configure(
        Class<? extends Annotation> annotationType,
        Annotation configuration,
        Consumer<ReflectionException> exceptionHandler) {

        Method configurer = null;

        try {
            configurer = findMethod(getClass(), "configure", annotationType);
        } catch (ReflectionException ex) {
            exceptionHandler.accept(ex);
        }

        if (configurer != null)
            invoke(configurer, this, configuration);
    }
}
