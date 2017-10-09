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

package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.generator.Also;
import com.pholser.junit.quickcheck.generator.Only;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import static com.pholser.junit.quickcheck.Mode.*;

@Ignore("For demonstration purposes only")
@RunWith(JUnitQuickcheck.class)
public class OnlyAlsoTest {
    public enum Response { YES, NO, UNSURE }

    @Property(trials = 45) public void samplingOnly(
        @Only({"3", "4"}) int arg0,
        @Only("false") boolean arg1,
        @Only("YES") Response arg2,
        @Only({"1", "2", "0", "-1"}) int arg3) {

        System.out.printf("%d %s %s %d\n", arg0, arg1, arg2, arg3);
    }

    @Property(trials = 32) public void samplingAlso(
        @Also({"3", "4"}) int arg0,
        @Also("false") boolean arg1,
        @Also("YES") Response arg2,
        @Also({"1", "2", "0", "-1"}) int arg3) {

        System.out.printf("%d %s %s %d\n", arg0, arg1, arg2, arg3);
    }

    @Property(trials = 11, mode = EXHAUSTIVE)
    public void exhaustiveOnly(
        int arg0,
        boolean arg1,
        Response arg2,
        @Only({"1", "2", "0", "-1"}) int arg3) {

        System.out.printf("%d %s %s %d\n", arg0, arg1, arg2, arg3);
    }

    @Property(trials = 11, mode = EXHAUSTIVE)
    public void exhaustiveAlso(
        int arg0,
        boolean arg1,
        Response arg2,
        @Also({"1", "2", "0", "-1"}) int arg3) {

        System.out.printf("%d %s %s %d\n", arg0, arg1, arg2, arg3);
    }
}
