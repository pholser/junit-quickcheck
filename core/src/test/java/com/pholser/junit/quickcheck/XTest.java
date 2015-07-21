package com.pholser.junit.quickcheck;

import com.google.common.base.Strings;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.GeneratorConfiguration;
import com.pholser.junit.quickcheck.internal.Reflection;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.javaruntype.type.Type;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.*;
import static org.javaruntype.type.Types.arrayComponentOf;

@Ignore
@RunWith(Parameterized.class)
public class XTest {
    public class X extends Generator<Object> {
        public X() {
            super(Object.class);
        }

        @Override public Object generate(SourceOfRandomness random, GenerationStatus status) {
            return null;
        }
    }

    private static @From(X.class) int basic;
    private static @From(X.class) List<@From(X.class) Integer> listOfBasic;
    private static @From(X.class) List<@From(X.class) ? extends @From(X.class) Integer> listOfExtendsBasic;
    private static @From(X.class) List<@From(X.class) ? super @From(X.class) Integer> listOfSuperBasic;
    private static @From(X.class) int @From(X.class) [] arrayOfBasic;
    private static @From(X.class) int @From(X.class) [] @From(X.class) [] twoDArrayOfBasic;
    private static @From(X.class) List<@From(X.class) Integer> @From(X.class) [] arrayOfListOfBasic;
    private static @From(X.class) List<@From(X.class) ?> @From(X.class) [] arrayOfListOfHuh;
    private static @From(X.class) List<@From(X.class) ? extends @From(X.class) Integer> @From(X.class) [] arrayOfListOfExtendsBasic;
    private static @From(X.class) List<@From(X.class) ? super @From(X.class) Integer> @From(X.class) [] arrayOfListOfSuperBasic;
    private static @From(X.class) Map<@From(X.class) Integer, @From(X.class) Integer> mapOfBasicToBasic;
    private static @From(X.class) Map<@From(X.class) ? extends @From(X.class) Integer, @From(X.class) ? super Integer> mapOfExtendsBasicToSuperBasic;

    private final String fieldName;

    public XTest(String fieldName) {
        this.fieldName = fieldName;
    }

    @Parameters
    public static Collection<?> fieldNames() {
        return asList(new String[][] {
//                { "marked" },
//                { "markedListOfMarked" },
//                { "arrayOfMarked" },
//                { "markedArray" },
//                { "markedArrayOfMarked" },
//                { "marked2DArray" },
//                { "twoDArrayOfMarked" },
//                { "arrayOfMarkedArrays" },
//                { "markedArrayOfMarkedArrayOfMarked" },
                { "markedArrayOfMarkedListOfMarkedHuh" },
        });
    }

    @Test public void x() throws Exception {
        AnnotatedType annotated = typeOf(fieldName);
        org.javaruntype.type.Type<?> token = Types.forJavaLangReflectType(annotated.getType());
        show(fieldName, annotated, token, 0);
    }

    private static void show(String name, AnnotatedType annotated, Type<?> token, int depth) {
        String indentation = Strings.repeat("\t", depth);

        System.out.println(indentation + name);
        System.out.printf("%sAnnotated type:\n%s%s\n", indentation, indentation, annotated.getType());
        System.out.printf("%sAnnotations:\n%s%s\n", indentation, indentation, asList(annotated.getAnnotations()));
        System.out.printf("%sAnnotated components:\n%s%s\n",
                indentation,
                indentation,
                Reflection.annotatedComponentTypes(annotated)
                        .stream()
                        .map(AnnotatedType::getType)
                        .collect(Collectors.toList()));
        System.out.printf("%sType token:\n%s%s\n", indentation, indentation, token);
        System.out.printf("%sType parameters:\n%s%s\n", indentation, indentation, token.getTypeParameters());

        if (token.isArray()) {
            @SuppressWarnings("unchecked")
            Type<?> component = arrayComponentOf((Type<Object[]>) token);
            AnnotatedType annotatedComponent = ((AnnotatedArrayType) annotated).getAnnotatedGenericComponentType();
            show(
                component.getName(),
                annotatedComponent,
                component,
                depth + 1);
        } else {
            List<AnnotatedType> annotatedComponents = Reflection.annotatedComponentTypes(annotated);
            List<TypeParameter<?>> typeParameters = token.getTypeParameters();

            for (int i = 0; i < annotatedComponents.size(); ++i) {
                Type<?> component = typeParameters.get(i).getType();
                AnnotatedType annotatedComponent = annotatedComponents.get(i);
                show(
                    annotatedComponent.getType().getTypeName(),
                    annotatedComponent,
                    component,
                    depth + 1);
            }
        }

        System.out.println();
    }

    private static AnnotatedType typeOf(String fieldName) throws NoSuchFieldException {
        return XTest.class.getDeclaredField(fieldName).getAnnotatedType();
    }
}
