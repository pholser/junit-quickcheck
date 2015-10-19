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

import java.util.Arrays;

import com.pholser.junit.quickcheck.internal.generator.ArrayGenerator;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ZilchGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.test.generator.ABox;
import com.pholser.junit.quickcheck.test.generator.ACallable;
import com.pholser.junit.quickcheck.test.generator.Box;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.pholser.junit.quickcheck.Types.*;
import static com.pholser.junit.quickcheck.internal.generator.Generators.*;
import static org.mockito.Mockito.*;

public class SupplyingCallableGeneratorWithComponentTest {
    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    private static Box<?>[] arrayOfBox;

    private GeneratorRepository repo;
    @Mock private SourceOfRandomness random;

    @Before public void beforeEach() {
        repo = new GeneratorRepository(random)
            .register(Arrays.<Generator<?>> asList(
                new ZilchGenerator(),
                new ACallable(),
                new ABox()));
        when(random.nextInt(3)).thenReturn(1);
    }

    @Test public void coaxingGeneratorToSupplyComponentForCallable() throws Exception {
        ArrayGenerator generator =
            (ArrayGenerator) repo.generatorFor(typeOf(getClass(), "arrayOfBox"));

        Generator<?> arrayElementGenerator = generator.componentGenerator();
        assertGenerators(arrayElementGenerator, ABox.class);
        ABox boxGenerator = (ABox) componentOf(arrayElementGenerator, 0);
        Generator<?> listElementGenerator = boxGenerator.componentGenerators().get(0);
        assertGenerators(listElementGenerator, ACallable.class);
        ACallable<?> callableGenerator = (ACallable<?>) componentOf(listElementGenerator, 0);
        Generator<?> callableResultGenerator = callableGenerator.componentGenerators().get(0);
        assertGenerators(callableResultGenerator, ZilchGenerator.class);
    }
}
