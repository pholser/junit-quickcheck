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

import java.util.List;

import com.pholser.junit.quickcheck.internal.Reflection;
import com.pholser.junit.quickcheck.internal.generator.CorePropertyParameterTest;
import com.pholser.junit.quickcheck.test.generator.Box;
import org.javaruntype.type.Types;

import static com.google.common.collect.Lists.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class BoxOfSuperLongPropertyParameterTest extends CorePropertyParameterTest {
    public static final Box<? super Long> TYPE_BEARER = null;

    @Override protected void primeSourceOfRandomness() {
        when(randomForParameterGenerator.nextLong()).thenReturn(3L).thenReturn(4L).thenReturn(5L);
        org.javaruntype.type.Type<?> longType = Types.forJavaLangReflectType(Long.class);
        List<org.javaruntype.type.Type<?>> supertypes = newArrayList(Reflection.supertypes(longType));
        when(randomForGeneratorRepo.nextInt(eq(0), anyInt()))
            .thenReturn(supertypes.indexOf(longType))
            .thenReturn(0)
            .thenReturn(supertypes.indexOf(longType))
            .thenReturn(0)
            .thenReturn(supertypes.indexOf(longType))
            .thenReturn(0);
    }

    @Override protected int trials() {
        return 3;
    }

    @SuppressWarnings("unchecked")
    @Override protected List<?> randomValues() {
        return asList(new Box<>(3L), new Box<>(4L), new Box<>(5L));
    }

    @Override public void verifyInteractionWithRandomness() {
        verify(randomForParameterGenerator, times(3)).nextLong();
    }
}
