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

package com.pholser.junit.quickcheck.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.javaruntype.type.ExtendsTypeParameter;
import org.javaruntype.type.StandardTypeParameter;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.javaruntype.type.WildcardTypeParameter;

import static com.pholser.junit.quickcheck.internal.Items.*;
import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static java.lang.String.*;
import static java.util.Collections.*;
import static org.javaruntype.type.Types.*;

public class ParameterTypeContext {
    private static final String EXPLICIT_GENERATOR_TYPE_MISMATCH_MESSAGE =
        "The generator %s named in @%s on parameter %s does not produce a type-compatible object";
    private static Zilch zilch;

    private final String parameterName;
    private final AnnotatedType parameterType;
    private final String declarerName;
    private final org.javaruntype.type.Type<?> token;
    private final List<Weighted<Generator<?>>> explicits = new ArrayList<>();
    private final Map<String, org.javaruntype.type.Type<?>> typeVariables;

    private AnnotatedElement annotatedElement;
    private boolean allowMixedTypes;

    public ParameterTypeContext(
        String parameterName,
        AnnotatedType parameterType,
        String declarerName) {

        this(
            parameterName,
            parameterType,
            declarerName,
            emptyMap());
    }

    public ParameterTypeContext(
        String parameterName,
        AnnotatedType parameterType,
        String declarerName,
        Map<String, Type> typeVariables) {

        this(
            parameterName,
            parameterType,
            declarerName,
            Types.forJavaLangReflectType(parameterType.getType(), toTokens(typeVariables)),
            toTokens(typeVariables));
    }

    public ParameterTypeContext(
        String parameterName,
        AnnotatedType parameterType,
        String declarerName,
        org.javaruntype.type.Type<?> token,
        Map<String, org.javaruntype.type.Type<?>> typeVariables) {

        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.declarerName = declarerName;
        this.token = token;
        this.typeVariables = typeVariables;
    }

    private static Map<String, org.javaruntype.type.Type<?>> toTokens(
        Map<String, Type> typeVariables) {

        return typeVariables.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> Types.forJavaLangReflectType(e.getValue())));
    }

    public ParameterTypeContext annotate(AnnotatedElement element) {
        this.annotatedElement = element;

        List<From> generators = allAnnotationsByType(element, From.class);
        if (!generators.isEmpty() && element instanceof AnnotatedWildcardType)
            throw new IllegalArgumentException("Wildcards cannot be marked with @From");

        addGenerators(generators);
        return this;
    }

    public ParameterTypeContext allowMixedTypes(boolean value) {
        this.allowMixedTypes = value;
        return this;
    }

    public boolean allowMixedTypes() {
        return allowMixedTypes;
    }

    private void addGenerators(List<From> generators) {
        for (From each : generators) {
            Generator<?> generator = makeGenerator(each.value());
            ensureCorrectType(generator);
            explicits.add(new Weighted<>(generator, each.frequency()));
        }
    }

    @SuppressWarnings("unchecked")
    private Generator<?> makeGenerator(Class<? extends Generator> generatorType) {
        // for Ctor/Fields
        Constructor<? extends Generator> ctor = findConstructor(generatorType, Class.class);
        if (ctor != null)
            return instantiate(ctor, rawParameterType());

        return instantiate(generatorType);
    }

    private Class<?> rawParameterType() {
        if (type() instanceof ParameterizedType)
            return (Class<?>) ((ParameterizedType) type()).getRawType();
        return (Class<?>) type();
    }

    private void ensureCorrectType(Generator<?> generator) {
        org.javaruntype.type.Type<?> parameterTypeToken =
            Types.forJavaLangReflectType(type(), typeVariables);

        for (Class<?> each : generator.types()) {
            if (!maybeWrap(parameterTypeToken.getRawClass()).isAssignableFrom(maybeWrap(each))) {
                throw new IllegalArgumentException(
                    format(
                        EXPLICIT_GENERATOR_TYPE_MISMATCH_MESSAGE,
                        each,
                        From.class.getName(),
                        parameterName));
            }
        }
    }

    public String name() {
        return declarerName + ':' + parameterName;
    }

    public AnnotatedType annotatedType() {
        return parameterType;
    }

    public Type type() {
        return parameterType.getType();
    }

    /**
     * @deprecated This will likely go away when languages whose compilers
     * and interpreters produce class files that support annotations on type
     * uses.
     * @see <a href="https://github.com/pholser/junit-quickcheck/issues/77">
     * this issue</a>
     * @return the annotated program element this context represents
     */
    @Deprecated
    public AnnotatedElement annotatedElement() {
        return annotatedElement;
    }

    /**
     * @deprecated This will likely go away when languages whose compilers
     * and interpreters produce class files that support annotations on type
     * uses.
     * @see <a href="https://github.com/pholser/junit-quickcheck/issues/77">
     * this issue</a>
     * @return the annotated program element this context represents
     */
    @Deprecated
    public boolean topLevel() {
        return annotatedElement instanceof Parameter || annotatedElement instanceof Field;
    }

    public List<Weighted<Generator<?>>> explicitGenerators() {
        return unmodifiableList(explicits);
    }

    public boolean annotatedWith(Class<? extends Annotation> annotationType) {
        return parameterType.getAnnotation(annotationType) != null;
    }

    public boolean isArray() {
        return token.isArray();
    }

    public ParameterTypeContext arrayComponentContext() {
        @SuppressWarnings("unchecked")
        org.javaruntype.type.Type<?> component = arrayComponentOf((org.javaruntype.type.Type<Object[]>) token);
        AnnotatedType annotatedComponent = ((AnnotatedArrayType) parameterType).getAnnotatedGenericComponentType();
        return new ParameterTypeContext(
            annotatedComponent.getType().getTypeName(),
            annotatedComponent,
            parameterType.getType().getTypeName(),
            component,
            typeVariables)
            .annotate(annotatedComponent)
            .allowMixedTypes(true);
    }

    public Class<?> getRawClass() {
        return token.getRawClass();
    }

    public boolean isEnum() {
        return getRawClass().isEnum();
    }

    public List<TypeParameter<?>> getTypeParameters() {
        return token.getTypeParameters();
    }

    public List<ParameterTypeContext> typeParameterContexts(SourceOfRandomness random) {
        List<ParameterTypeContext> typeParameterContexts = new ArrayList<>();
        List<TypeParameter<?>> typeParameters = getTypeParameters();
        List<AnnotatedType> annotatedTypeParameters = annotatedComponentTypes(annotatedType());

        for (int i = 0; i < typeParameters.size(); ++i) {
            TypeParameter<?> p = typeParameters.get(i);
            AnnotatedType a =
                annotatedTypeParameters.size() > i
                    ? annotatedTypeParameters.get(i)
                    : zilch();

            if (p instanceof StandardTypeParameter<?>) {
                typeParameterContexts.add(
                    new ParameterTypeContext(
                        p.getType().getName(),
                        a,
                        annotatedType().getType().getTypeName(),
                        p.getType(),
                        typeVariables)
                    .allowMixedTypes(!(a instanceof TypeVariable))
                    .annotate(a));
            } else if (p instanceof WildcardTypeParameter) {
                typeParameterContexts.add(
                    new ParameterTypeContext(
                        "Zilch",
                        a,
                        annotatedType().getType().getTypeName(),
                        Types.forJavaLangReflectType(Zilch.class),
                        typeVariables)
                    .allowMixedTypes(true)
                    .annotate(a));
            } else if (p instanceof ExtendsTypeParameter<?>) {
                typeParameterContexts.add(
                    new ParameterTypeContext(
                        p.getType().getName(),
                        annotatedComponentTypes(a).get(0),
                        annotatedType().getType().getTypeName(),
                        p.getType(),
                        typeVariables)
                    .allowMixedTypes(false)
                    .annotate(a));
            } else {
                // must be "? super X"
                Set<org.javaruntype.type.Type<?>> supertypes = supertypes(p.getType());
                org.javaruntype.type.Type<?> choice = choose(supertypes, random);
                typeParameterContexts.add(
                    new ParameterTypeContext(
                        p.getType().getName(),
                        annotatedComponentTypes(a).get(0),
                        annotatedType().getType().getTypeName(),
                        choice,
                        typeVariables)
                    .allowMixedTypes(false)
                    .annotate(a));
            }
        }

        return typeParameterContexts;
    }

    private static AnnotatedType zilch() {
        try {
            return ParameterTypeContext.class.getDeclaredField("zilch").getAnnotatedType();
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }
}
