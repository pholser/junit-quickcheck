/*
 The MIT License

 Copyright (c) 2010-2011 Paul R. Holser, Jr.

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

import java.lang.reflect.Type;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import org.javaruntype.type.Types;

public class ParameterContext {
    private static final String EXPLICIT_GENERATOR_TYPE_MISMATCH_MESSAGE =
        "The generator %s named in @%s on parameter of type %s does not produce a type-compatible object";

    private final Type parameterType;
    private final GeneratorRepository repo = new GeneratorRepository();
    private int sampleSize;

    public ParameterContext(Type parameterType) {
        this.parameterType = parameterType;
    }

    public ParameterContext addQuantifier(ForAll quantifier) {
    	org.javaruntype.type.Type<?> parmType = Types.forJavaLangReflectType(parameterType);
    	if (parmType.getRawClass().isEnum())
    		// No point in generating duplicates
    		this.sampleSize = parmType.getRawClass().getEnumConstants().length;
    	else
    		this.sampleSize = quantifier.sampleSize();
        return this;
    }

    public ParameterContext addGenerators(From generators) {
        for (Class<? extends Generator> each : generators.value()) {
            Generator<?> generator = Reflection.instantiate(each);
            ensureCorrectType(generator);
            repo.add(generator);
        }

        return this;
    }

    private void ensureCorrectType(Generator<?> generator) {
        org.javaruntype.type.Type<?> parmType = Types.forJavaLangReflectType(parameterType);
        for (Class<?> each : generator.types()) {
            org.javaruntype.type.Type<?> generatorType = Types.forJavaLangReflectType(each);
            if (!parmType.isAssignableFrom(generatorType)) {
                throw new IllegalArgumentException(String.format(EXPLICIT_GENERATOR_TYPE_MISMATCH_MESSAGE, each,
                    From.class.getName(), parameterType));
            }
        }
    }

    public Type parameterType() {
        return parameterType;
    }

    public int sampleSize() {
        return sampleSize;
    }

    public Generator<?> explicitGenerator() {
        return repo.isEmpty() ? null : repo.generatorFor(parameterType);
    }
}
