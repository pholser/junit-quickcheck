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

package com.pholser.junit.quickcheck.internal;

import static com.pholser.junit.quickcheck.internal.Items.choose;
import static com.pholser.junit.quickcheck.internal.Reflection.allAnnotationsByType;
import static com.pholser.junit.quickcheck.internal.Reflection.annotatedComponentTypes;
import static com.pholser.junit.quickcheck.internal.Reflection.findConstructor;
import static com.pholser.junit.quickcheck.internal.Reflection.instantiate;
import static com.pholser.junit.quickcheck.internal.Reflection.maybeWrap;
import static com.pholser.junit.quickcheck.internal.Reflection.supertypes;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static org.javaruntype.type.Types.arrayComponentOf;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.javaruntype.type.ExtendsTypeParameter;
import org.javaruntype.type.StandardTypeParameter;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.javaruntype.type.WildcardTypeParameter;
import ru.vyarus.java.generics.resolver.GenericsResolver;
import ru.vyarus.java.generics.resolver.context.ConstructorGenericsContext;
import ru.vyarus.java.generics.resolver.context.GenericsContext;
import ru.vyarus.java.generics.resolver.context.MethodGenericsContext;

public class ParameterTypeContext {
    private static final String EXPLICIT_GENERATOR_TYPE_MISMATCH_MESSAGE =
        "The generator %s named in @%s on parameter %s does not produce a type-compatible object";
    private static Zilch zilch;

    private final String parameterName;
    private final AnnotatedType parameterType;
    private final String declarerName;
    private final org.javaruntype.type.Type<?> resolved;
    private final List<Weighted<Generator<?>>> explicits = new ArrayList<>();
    private final GenericsContext generics;
    private final int parameterIndex;

    private AnnotatedElement annotatedElement;
    private boolean allowMixedTypes;

    public static ParameterTypeContext forClass(Class<?> clazz) {
        return new ParameterTypeContext(
            clazz.getTypeName(),
            FakeAnnotatedTypeFactory.makeFrom(clazz),
            clazz.getTypeName(),
            Types.forJavaLangReflectType(clazz),
            GenericsResolver.resolve(clazz));
    }

    public static ParameterTypeContext forField(Field field) {
        GenericsContext generics =
            GenericsResolver.resolve(field.getDeclaringClass());

        return new ParameterTypeContext(
            field.getName(),
            field.getAnnotatedType(),
            field.getDeclaringClass().getName(),
            Types.forJavaLangReflectType(generics.resolveFieldType(field)),
            generics);
    }

    public static ParameterTypeContext forParameter(Parameter parameter) {
        Executable exec = parameter.getDeclaringExecutable();
        Class<?> clazz = exec.getDeclaringClass();
        String declarerName = clazz.getName() + '.' + exec.getName();
        int parameterIndex = parameterIndex(exec, parameter);

        GenericsContext generics;
        org.javaruntype.type.Type<?> resolved;

        if (exec instanceof Method) {
            Method method = (Method) exec;
            MethodGenericsContext methodGenerics =
                GenericsResolver.resolve(clazz).method(method);
            resolved =
                Types.forJavaLangReflectType(
                    methodGenerics.resolveParameterType(parameterIndex));
            generics = methodGenerics;
        } else if (exec instanceof Constructor<?>) {
            Constructor<?> ctor = (Constructor<?>) exec;
            ConstructorGenericsContext constructorGenerics =
                GenericsResolver.resolve(clazz).constructor(ctor);
            resolved =
                Types.forJavaLangReflectType(
                    constructorGenerics.resolveParameterType(parameterIndex));
            generics = constructorGenerics;
        } else {
            throw new IllegalStateException("Unrecognized subtype of Executable");
        }

        return new ParameterTypeContext(
            parameter.getName(),
            parameter.getAnnotatedType(),
            declarerName,
            resolved,
            generics,
            parameterIndex);
    }

    public static ParameterTypeContext forParameter(
        Parameter parameter,
        MethodGenericsContext generics) {

        Executable exec = parameter.getDeclaringExecutable();
        Class<?> clazz = exec.getDeclaringClass();
        String declarerName = clazz.getName() + '.' + exec.getName();
        int parameterIndex = parameterIndex(exec, parameter);

        return new ParameterTypeContext(
            parameter.getName(),
            parameter.getAnnotatedType(),
            declarerName,
            Types.forJavaLangReflectType(
                generics.resolveParameterType(parameterIndex)),
            generics,
            parameterIndex);
    }

    private static int parameterIndex(Executable exec, Parameter parameter) {
        Parameter[] parameters = exec.getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            if (parameters[i].equals(parameter)) {
                return i;
            }
        }

        throw new IllegalStateException(
            "Cannot find parameter " + parameter + " on " + exec);
    }

    private ParameterTypeContext(
        String parameterName,
        AnnotatedType parameterType,
        String declarerName,
        org.javaruntype.type.Type<?> resolvedType,
        GenericsContext generics) {

        this(
            parameterName,
            parameterType,
            declarerName,
            resolvedType,
            generics,
            -1);
    }

    private ParameterTypeContext(
        String parameterName,
        AnnotatedType parameterType,
        String declarerName,
        org.javaruntype.type.Type<?> resolvedType,
        GenericsContext generics,
        int parameterIndex) {

        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.declarerName = declarerName;
        this.resolved = resolvedType;
        this.generics = generics;
        this.parameterIndex = parameterIndex;
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

    /**
     * Gives a context for generation of the return type of a lambda method.
     *
     * @param method method whose return type we want to resolve
     * @return an associated parameter context
     */
    public ParameterTypeContext methodReturnTypeContext(Method method) {
        if (!(generics instanceof MethodGenericsContext)) {
            throw new IllegalStateException(
                "invoking methodReturnTypeContext in present of " + generics);
        }

        MethodGenericsContext testMethodGenerics =
            (MethodGenericsContext) generics;
        MethodGenericsContext argMethodGenerics =
            testMethodGenerics.parameterType(parameterIndex).method(method);

        return new ParameterTypeContext(
            "return value",
            method.getAnnotatedReturnType(),
            method.getName(),
            Types.forJavaLangReflectType(argMethodGenerics.resolveReturnType()),
            argMethodGenerics);
    }

    private void addGenerators(List<From> generators) {
        for (From each : generators) {
            Generator<?> generator = makeGenerator(each.value());
            ensureCorrectType(generator);
            explicits.add(new Weighted<>(generator, each.frequency()));
        }
    }

    private Generator<?> makeGenerator(Class<? extends Generator> generatorType) {
        Constructor<? extends Generator> ctor;

        try {
            // for Ctor/Fields
            ctor = findConstructor(generatorType, Class.class);
        } catch (ReflectionException ex) {
            return instantiate(generatorType);
        }

        return instantiate(ctor, rawParameterType());
    }

    private Class<?> rawParameterType() {
        if (type() instanceof ParameterizedType)
            return resolved.getRawClass();
        if (type() instanceof TypeVariable<?>)
            return resolved.getRawClass();

        return (Class<?>) type();
    }

    private void ensureCorrectType(Generator<?> generator) {
        for (Class<?> each : generator.types()) {
            if (!maybeWrap(resolved.getRawClass()).isAssignableFrom(maybeWrap(each))) {
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
        return annotatedElement instanceof Parameter
            || annotatedElement instanceof Field;
    }

    public List<Weighted<Generator<?>>> explicitGenerators() {
        return unmodifiableList(explicits);
    }

    public ParameterTypeContext arrayComponentContext() {
        @SuppressWarnings("unchecked")
        org.javaruntype.type.Type<?> component =
            arrayComponentOf((org.javaruntype.type.Type<Object[]>) resolved);
        AnnotatedType annotatedComponent = annotatedArrayComponent(component);
        return new ParameterTypeContext(
            annotatedComponent.getType().getTypeName(),
            annotatedComponent,
            parameterType.getType().getTypeName(),
            component,
            generics)
            .annotate(annotatedComponent)
            .allowMixedTypes(true);
    }

    private AnnotatedType annotatedArrayComponent(org.javaruntype.type.Type<?> component) {
        return parameterType instanceof AnnotatedArrayType
            ? ((AnnotatedArrayType) parameterType).getAnnotatedGenericComponentType()
            : FakeAnnotatedTypeFactory.makeFrom(component.getComponentClass());
    }

    public boolean isArray() {
        return resolved.isArray();
    }

    public Class<?> getRawClass() {
        return resolved.getRawClass();
    }

    public boolean isEnum() {
        return getRawClass().isEnum();
    }

    public List<TypeParameter<?>> getTypeParameters() {
        return resolved.getTypeParameters();
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

            if (p instanceof StandardTypeParameter<?>)
                addStandardTypeParameterContext(typeParameterContexts, p, a);
            else if (p instanceof WildcardTypeParameter)
                addWildcardTypeParameterContext(typeParameterContexts, a);
            else if (p instanceof ExtendsTypeParameter<?>)
                addExtendsTypeParameterContext(typeParameterContexts, p, a);
            else {
                // must be "? super X"
                addSuperTypeParameterContext(random, typeParameterContexts, p, a);
            }
        }

        return typeParameterContexts;
    }

    private void addStandardTypeParameterContext(
        List<ParameterTypeContext> typeParameterContexts,
        TypeParameter<?> p,
        AnnotatedType a) {

        typeParameterContexts.add(
            new ParameterTypeContext(
                p.getType().getName(),
                a,
                annotatedType().getType().getTypeName(),
                p.getType(),
                generics)
            .allowMixedTypes(!(a instanceof TypeVariable))
            .annotate(a));
    }

    private void addWildcardTypeParameterContext(
        List<ParameterTypeContext> typeParameterContexts,
        AnnotatedType a) {

        typeParameterContexts.add(
            new ParameterTypeContext(
                "Zilch",
                a,
                annotatedType().getType().getTypeName(),
                Types.forJavaLangReflectType(Zilch.class),
                GenericsResolver.resolve(Zilch.class))
                .allowMixedTypes(true)
                .annotate(a));
    }

    private void addExtendsTypeParameterContext(
        List<ParameterTypeContext> typeParameterContexts,
        TypeParameter<?> p,
        AnnotatedType a) {

        typeParameterContexts.add(
            new ParameterTypeContext(
                p.getType().getName(),
                annotatedComponentTypes(a).get(0),
                annotatedType().getType().getTypeName(),
                p.getType(),
                generics)
                .allowMixedTypes(false)
                .annotate(a));
    }

    private void addSuperTypeParameterContext(
        SourceOfRandomness random,
        List<ParameterTypeContext> typeParameterContexts,
        TypeParameter<?> p,
        AnnotatedType a) {

        Set<org.javaruntype.type.Type<?>> supertypes = supertypes(p.getType());
        org.javaruntype.type.Type<?> choice = choose(supertypes, random);

        typeParameterContexts.add(
            new ParameterTypeContext(
                p.getType().getName(),
                annotatedComponentTypes(a).get(0),
                annotatedType().getType().getTypeName(),
                choice,
                generics)
                .allowMixedTypes(false)
                .annotate(a));
    }

    private static AnnotatedType zilch() {
        try {
            return ParameterTypeContext.class.getDeclaredField("zilch")
                .getAnnotatedType();
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }
}
