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

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * <p>Produces instances of a class by reflecting the class's fields and
 * generating random values for them.</p>
 *
 * <p>All fields of the class and its class hierarchy are auto-generated.</p>
 *
 * <p>In order for this generator to work, the type it is given must have an
 * accessible zero-arg constructor.</p>
 *
 * <p>If a field is marked with an annotation that influences the generation of
 * a given kind of value, that annotation will be applied to the generation of
 * values for that field.</p>
 *
 * <p>This generator is intended to be used with
 * {@link com.pholser.junit.quickcheck.From}, and not to be available via the
 * {@link java.util.ServiceLoader} mechanism.</p>
 *
 * @param <T> the type of objects generated
 */
public class Fields<T> extends Generator<T> {
    private final List<Field> fields;
    private final List<Generator<?>> fieldGenerators = new ArrayList<>();

    /**
     * @param type the type of objects to be generated
     */
    public Fields(Class<T> type) {
        super(type);

        this.fields = allDeclaredFieldsOf(type);

        instantiate(type);
    }

    @Override public T generate(SourceOfRandomness random, GenerationStatus status) {
        Class<T> type = types().get(0);
        Object generated = instantiate(type);

        for (Field each : fields)
            setField(each, generated, gen().field(each).generate(random, status), true);

        return type.cast(generated);
    }

    @Override public boolean canRegisterAsType(Class<?> type) {
        return false;
    }

    @Override public void provide(Generators provided) {
        super.provide(provided);

        fieldGenerators.clear();
        for (Field each : fields)
            fieldGenerators.add(gen().field(each));
    }

    @Override public void configure(AnnotatedType annotatedType) {
        super.configure(annotatedType);

        for (int i = 0; i < fields.size(); ++i)
            fieldGenerators.get(i).configure(fields.get(i).getAnnotatedType());
    }
}
