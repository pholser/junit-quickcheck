/*
 The MIT License

 Copyright (c) 2010-2015 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.javaruntype.type.WildcardTypeParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

/**
 * Produces values for theory parameters.
 *
 * @param <T> type of theory parameter to apply this generator's values to
 */
public abstract class Generator<T> {
    private final List<Class<T>> types = new ArrayList<>();
    private GeneratorRepository repo;

    /**
     * @param type class token for type of theory parameter this generator is applicable to
     */
    @SuppressWarnings("unchecked") protected Generator(Class<T> type) {
        this(singletonList(type));
    }

    /**
     * Used for generators of primitives/their wrappers. For example, a {@code Generator<Integer>} can be used for
     * theory parameters of type {@code Integer} or {@code int}.
     *
     * @param types class tokens for type of theory parameter this generator is applicable to
     */
    protected Generator(List<Class<T>> types) {
        this.types.addAll(types);
    }

    /**
     * @return class tokens for the types of theory parameters this generator is applicable to
     */
    public List<Class<T>> types() {
        return unmodifiableList(types);
    }

    /**
     * Tells whether this generator is allowed to be used for theory parameters of the given type.
     *
     * @param type type against which to test this generator
     * @return {@code} true if the generator is allowed to participate in generating values for theory parameters
     * of {@code type}.
     */
    public boolean canRegisterAsType(Class<?> type) {
        return true;
    }

    /**
     * <p>Produces a value for a theory parameter.</p>
     *
     * <p>A generator may raise an unchecked exception if some condition exists which would lead to a confusing
     * generation -- for example, if a generator honored a range configuration, and the endpoints were transposed.</p>
     *
     * @param random a source of randomness to be used when generating the value
     * @param status an object that the generator can use to influence the value it produces. For example, a generator
     * for lists can use the {@link GenerationStatus#size() size} method to generate lists with a given number of
     * elements.
     * @return the generated value
     */
    public abstract T generate(SourceOfRandomness random, GenerationStatus status);

    /**
     * @return whether this generator has component generators, such as for those generators that produce lists or
     * arrays.
     * @see #addComponentGenerators(java.util.List)
     */
    public boolean hasComponents() {
        return false;
    }

    /**
     * @return how many component generators this generator needs
     * @see #addComponentGenerators(java.util.List)
     */
    public int numberOfNeededComponents() {
        return 0;
    }

    /**
     * <p>Adds component generators to this generator.</p>
     *
     * <p>Some generators need component generators to create proper values. For example, lists require a single
     * component generator in order to generate elements that have the type of the list parameter's type argument.</p>
     *
     * @param newComponents component generators to add
     */
    public void addComponentGenerators(List<Generator<?>> newComponents) {
        // do nothing by default
    }

    /**
     * @param typeParameters a list of generic type parameters
     * @return whether this generator can be considered for generating values for theory parameters that have the
     * given type parameters in their signatures
     */
    public boolean canGenerateForParametersOfTypes(List<TypeParameter<?>> typeParameters) {
        return true;
    }

    /**
     * @param parameter a generic type parameter
     * @param clazz a type
     * @return whether the type is compatible with the generic type parameter
     * @see #canGenerateForParametersOfTypes(List)
     */
    public static boolean compatibleWithTypeParameter(TypeParameter<?> parameter, Class<?> clazz) {
        return parameter instanceof WildcardTypeParameter
            || parameter.getType().isAssignableFrom(Types.forJavaLangReflectType(clazz));
    }

    /**
     * <p>Configures this generator using annotations from a given annotated type.</p>
     *
     * <p>This method considers only annotations that are themselves marked with {@link GeneratorConfiguration}.</p>
     *
     * <p>By default, the generator will configure itself by:</p>
     * <ul>
     *     <li>For each of the given annotations:
     *         <ul>
     *             <li>Find a {@code public} method on the generator named {@code configure}, that accepts a single
     *             parameter of the annotation type</li>
     *             <li>Invoke the {@code configure} method reflectively, passing the annotation as the argument</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param annotatedType a type usage
     */
    public void configure(AnnotatedType annotatedType) {
        List<Annotation> configs = allAnnotations(annotatedType).stream()
                .filter(a -> a.annotationType().isAnnotationPresent(GeneratorConfiguration.class))
                .collect(toList());

        Map<Class<? extends Annotation>, Annotation> byType = new HashMap<>();
        for (Annotation each : configs)
            byType.put(each.annotationType(), each);

        configure(byType);
    }

    private void configure(Map<Class<? extends Annotation>, Annotation> byType) {
        for (Map.Entry<Class<? extends Annotation>, Annotation> each : byType.entrySet())
            configure(each.getKey(), each.getValue());
    }

    private void configure(Class<? extends Annotation> annotationType, Annotation configuration) {
        Method configurer;

        try {
            configurer = findMethod(getClass(), "configure", annotationType);
        } catch (ReflectionException ex) {
            return;
        }

        invoke(configurer, this, configuration);
    }

    public void provideRepository(GeneratorRepository provided) {
        repo = provided;
    }

    Generator<?> generatorFor(ParameterTypeContext parameter) {
        return repo.produceGenerator(parameter);
    }
}
