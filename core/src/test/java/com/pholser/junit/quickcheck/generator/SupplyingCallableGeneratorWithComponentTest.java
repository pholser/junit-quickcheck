/*
 The MIT License

 Copyright (c) 2010-2013 Paul R. Holser, Jr.

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

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.reflect.GenericArrayTypeImpl;
import com.pholser.junit.quickcheck.reflect.ParameterizedTypeImpl;
import com.pholser.junit.quickcheck.reflect.WildcardTypeImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.pholser.junit.quickcheck.internal.generator.Generators.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SupplyingCallableGeneratorWithComponentTest {
    private GeneratorRepository repo;
    @Mock private SourceOfRandomness random;

    @Before public void beforeEach() {
        repo = new GeneratorRepository(random).register(Arrays.<Generator<?>>asList(
            new IntegerGenerator(),
            new CallableGenerator(),
            new ArrayListGenerator()));
        when(random.nextInt(0, 1)).thenReturn(1);
    }

    @Test public void coaxingGeneratorToSupplyComponentForCallable() {
        ArrayGenerator generator = (ArrayGenerator) repo.generatorFor(new GenericArrayTypeImpl(
            new ParameterizedTypeImpl(List.class, new WildcardTypeImpl(new Type[] { Object.class }, new Type[0]))));

        Generator<?> arrayElementGenerator = generator.componentGenerator();
        assertGenerators(arrayElementGenerator, ArrayListGenerator.class);
        ArrayListGenerator listGenerator = (ArrayListGenerator) componentOf(arrayElementGenerator, 0);
        Generator<?> listElementGenerator = listGenerator.componentGenerators().get(0);
        assertGenerators(listElementGenerator, CallableGenerator.class);
        CallableGenerator<?> callableGenerator = (CallableGenerator<?>) componentOf(listElementGenerator, 0);
        Generator<?> callableResultGenerator = callableGenerator.componentGenerators().get(0);
        assertGenerators(callableResultGenerator, IntegerGenerator.class);
    }
}
