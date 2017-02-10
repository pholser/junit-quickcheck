/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator.java.util;

import java.util.List;
import java.util.UUID;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Generating;
import com.pholser.junit.quickcheck.generator.BasicGeneratorPropertyParameterTest;

import static com.pholser.junit.quickcheck.Generating.*;
import static com.pholser.junit.quickcheck.generator.java.util.RFC4122.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;

public class RFC4122Version5PropertyParameterTest
    extends BasicGeneratorPropertyParameterTest {

    @From(Version5.class)
    @Namespace(Namespaces.DNS)
    public static final UUID TYPE_BEARER = null;

    @Override protected void primeSourceOfRandomness() throws Exception {
        when(Generating.charsForString(randomForParameterGenerator))
            .thenReturn(0x61).thenReturn(0x62).thenReturn(0x63)
            .thenReturn(0x64).thenReturn(0x65).thenReturn(0x66);
        when(distro.sampleWithMean(1, randomForParameterGenerator)).thenReturn(0);
        when(distro.sampleWithMean(2, randomForParameterGenerator)).thenReturn(1);
        when(distro.sampleWithMean(3, randomForParameterGenerator)).thenReturn(2);
        when(distro.sampleWithMean(4, randomForParameterGenerator)).thenReturn(3);
    }

    @Override protected int trials() {
        return 4;
    }

    @Override protected List<?> randomValues() {
        return asList(
            UUID.fromString("4ebd0208-8328-5d69-8c44-ec50939c0967"),
            UUID.fromString("4f3f2898-69e3-5a0d-820a-c4e87987dbce"),
            UUID.fromString("656c78e8-4631-5bef-9c7f-e2795ecee6d6"),
            UUID.fromString("96aa5957-8466-5a38-9bbb-adf7eab336c9"));
    }

    @Override public void verifyInteractionWithRandomness() {
        verifyCharsForString(randomForParameterGenerator, times(6));
    }
}
