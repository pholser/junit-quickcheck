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

package com.pholser.junit.quickcheck.internal;

import java.util.Collection;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public final class Items {
    private Items() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <T> T choose(Collection<T> items, SourceOfRandomness random) {
        Object[] asArray = items.toArray(new Object[items.size()]);
        return (T) asArray[random.nextInt(items.size())];
    }

    public static <T> T chooseWeighted(Collection<Weighted<T>> items, SourceOfRandomness random) {
        if (items.size() == 1)
            return items.iterator().next().item;

        int range = items.stream().mapToInt(i -> i.weight).sum();
        int sample = random.nextInt(range);

        int threshold = 0;
        for (Weighted<T> each : items) {
            threshold += each.weight;
            if (sample < threshold)
                return each.item;
        }

        throw new AssertionError(String.format("sample = %d, range = %d", sample, range));
    }
}
