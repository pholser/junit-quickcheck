/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * An access point for available generators.
 */
public interface Generators {
    /**
     * <p>Gives a generator that can produce an instance of the type described
     * by the field with the given name and containing type.</p>
     *
     * <p>If the field is marked with an annotation that influences the
     * generation of a given kind of value, that annotation will be applied to
     * the resulting generator's values.</p>
     *
     * @param type containing type for a field
     * @param fieldName name of a field
     * @return generator that can produce values of the field's type
     */
    Generator<?> field(Class<?> type, String fieldName);

    /**
     * <p>Gives a generator that can produce an instance of the type described
     * by a constructor of a given type that accepts the given types of
     * arguments. It will generate values for the parameters of the given
     * constructor, and then invoke the constructor.</p>
     *
     * <p>If the constructor's parameters are marked with annotations that
     * influence the generation of a given kind of value, those annotations
     * will be applied to the generators that will produce values to be used
     * as arguments to the constructor.</p>
     *
     * @param <T> type of objects produced by the resulting generator
     * @param type containing type for a constructor
     * @param argumentTypes types of arguments to the constructor
     * @return generator that can produce values using the constructor
     */
    <T> Generator<T> constructor(Class<T> type, Class<?>... argumentTypes);

    /**
     * <p>Gives a generator that can produce an instance of the given type
     * by reflecting the class's fields on up its class hierarchy and
     * generating random values for them.</p>
     *
     * <p>The given type must have an accessible zero-arg constructor.</p>
     *
     * <p>If a field of the given type is marked with an annotation that
     * influences the generation of a given kind of value, that annotation
     * will be applied to the generation of values for that field.</p>
     *
     * @param <T> type of objects produced by the resulting generator
     * @param type a type
     * @return generator that can produce values of that type
     */
    <T> Generator<T> fieldsOf(Class<T> type);

    /**
     * <p>Gives a generator that can produce values of the given type.</p>
     *
     * @param <T> type of objects produced by the resulting generator
     * @param type a type
     * @return generator that can produce values of that type
     */
    <T> Generator<T> type(Class<T> type);

    /**
     * <p>Gives a generator that can produce instances of the type of the
     * given reflected method parameter.</p>
     *
     * <p>If the parameter is marked with an annotation that influences the
     * generation of its value, that annotation will be applied to the
     * generation of values for that parameter's type.</p>
     *
     * @param parameter a reflected method parameter
     * @return generator that can produce values of the parameter's type
     */
    Generator<?> parameter(Parameter parameter);

    /**
     * <p>Gives a generator that can produce instances of the type of the
     * given reflected field.</p>
     *
     * <p>If the field is marked with an annotation that influences the
     * generation of its value, that annotation will be applied to the
     * generation of values for that field's type.</p>
     *
     * @param field a reflected field
     * @return generator that can produce values of the field's type
     */
    Generator<?> field(Field field);
}
