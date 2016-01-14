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

package com.pholser.junit.quickcheck.test.generator;

public class Foo {
    private final int i;
    private final boolean marked;

    public Foo(int i) {
        this(i, false);
    }

    public Foo(int i, boolean marked) {
        this.i = i;
        this.marked = marked;
    }

    public int i() {
        return i;
    }

    public int slow() throws InterruptedException {
        Thread.sleep(500);
        return i;
    }

    public boolean marked() {
        return marked;
    }

    @Override public int hashCode() {
        return i;
    }

    @Override public boolean equals(Object o) {
        return o instanceof Foo && ((Foo) o).i() == i;
    }

    @Override public String toString() {
        return "Foo[" + i + ']';
    }
}
