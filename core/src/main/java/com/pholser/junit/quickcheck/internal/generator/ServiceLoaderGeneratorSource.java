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

package com.pholser.junit.quickcheck.internal.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.pholser.junit.quickcheck.generator.Generator;

import static java.util.Collections.*;
import static java.util.Comparator.*;

public class ServiceLoaderGeneratorSource implements Iterable<Generator<?>> {
    @SuppressWarnings("rawtypes") private final ServiceLoader<Generator> loader;

    public ServiceLoaderGeneratorSource() {
        loader = ServiceLoader.load(Generator.class);
    }

    @Override public Iterator<Generator<?>> iterator() {
        List<Generator<?>> generators = new ArrayList<>();

        for (Generator<?> each : loader)
            generators.add(each);

        Collections.sort(
            generators,
            comparing(generator -> generator.getClass().getName()));

        return unmodifiableList(generators).iterator();
    }
}
