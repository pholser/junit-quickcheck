/*
 The MIT License

 Copyright (c) 2010-2014 Paul R. Holser, Jr.

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

import com.pholser.junit.quickcheck.internal.ParameterContext;
import com.pholser.junit.quickcheck.internal.Reflection;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.reflect.Field;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * <p>A generator that produces instances of a class by introspecting the class's fields and generating random
 * values for them.</p>
 *
 * <p>All fields of the class and its class hierarchy are auto-generated.</p>
 *
 * <p>In order for this generator to work, the type it is given must have an accessible zero-arg constructor.</p>
 *
 * <p>If a field is marked with an annotation that influences the generation of a given kind of value, it will
 * be applied to the generation of values for that field.</p>
 *
 * @param <T> the type of objects generated
 */
public class Fields<T> extends Generator<T> {
    public Fields(Class<T> type) {
        super(type);
    }

    @Override public T generate(SourceOfRandomness random, GenerationStatus status) {
        Class<T> type = types().get(0);
        Object generated = Reflection.instantiateQuietly(type);

        for (Field each : allDeclaredFieldsOf(type)) {
            ParameterContext parameter = new ParameterContext(each.getGenericType()).annotate(each);
            setField(each, generated, generatorFor(parameter).generate(random, status), true);
        }

        return type.cast(generated);
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return false;
    }
}
