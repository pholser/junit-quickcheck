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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.*;

public class CartesianIterator<T> implements Iterator<List<T>> {
    private final List<Buffer<T>> buffers;
    private final boolean allStartedAvailable;
    private int count = 0;

    public CartesianIterator(List<Iterator<T>> sources) {
        this.buffers = sources.stream()
            .map(s -> new Buffer<>(s, new ArrayList<>()))
            .collect(toList());
        allStartedAvailable = sources.stream().allMatch(Iterator::hasNext);
    }

    @Override public boolean hasNext() {
        return allStartedAvailable
            && buffers.stream().anyMatch(Buffer::available);
    }

    @Override public List<T> next() {
        List<T> result = new ArrayList<>();
        int n = count;

        for (Buffer<T> each : buffers) {
            int divisor = each.divisor();

            result.add(each.get(n));

            n /= divisor;
        }

        ++count;
        return result;
    }

    private static class Buffer<T> {
        private final Iterator<T> source;
        private final List<T> consumed;
        private int index;

        Buffer(Iterator<T> source, List<T> consumed) {
            this.source = source;
            this.consumed = consumed;
        }

        boolean available() {
            return source.hasNext() || index < consumed.size() - 1;
        }

        int divisor() {
            return source.hasNext() ? consumed.size() + 1 : consumed.size();
        }

        T get(int n) {
            index = n % divisor();
            if (index == consumed.size())
                consumed.add(source.next());

            return consumed.get(index);
        }
    }
}
