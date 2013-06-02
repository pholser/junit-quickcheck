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

package com.pholser.junit.quickcheck;

import org.junit.Ignore;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * Swiped from <a href="http://stackoverflow.com/questions/837484/junit-theory-for-hashcode-equals-contract">here</a>.
 */
@Ignore("turn on when testing lots of permutations")
@RunWith(Theories.class)
public class EqualsHashCodeTest {
    @Theory public void equalsIsReflexive(@ForAll Object o) {
        assumeThat(o, is(not(equalTo(null))));

        assertTrue(o.equals(o));
    }

    @Theory public void equalsIsSymmetric(@ForAll Object x, @ForAll Object y) {
        assumeThat(x, is(not(equalTo(null))));
        assumeThat(y, is(not(equalTo(null))));
        assumeTrue(y.equals(x));

        assertTrue(x.equals(y));
    }

    @Theory public void equalsIsTransitive(@ForAll(sampleSize = 20) Object x,
        @ForAll(sampleSize = 20) Object y,
        @ForAll(sampleSize = 20) Object z) {

        assumeThat(x, is(not(equalTo(null))));
        assumeThat(y, is(not(equalTo(null))));
        assumeThat(z, is(not(equalTo(null))));
        assumeTrue(x.equals(y) && y.equals(z));

        assertTrue(z.equals(x));
    }

    @Theory public void equalsIsConsistent(@ForAll Object x, @ForAll Object y) {
        assumeThat(x, is(not(equalTo(null))));
        boolean alwaysTheSame = x.equals(y);

        for (int i = 0; i < 30; i++)
            assertThat(x.equals(y), is(alwaysTheSame));
    }

    @Theory public void equalsReturnFalseOnNull(@ForAll Object x) {
        assumeThat(x, is(not(equalTo(null))));

        assertFalse(x.equals(null));
    }

    @Theory public void hashCodeIsSelfConsistent(@ForAll Object x) {
        assumeThat(x, is(not(equalTo(null))));
        int alwaysTheSame = x.hashCode();

        for (int i = 0; i < 30; i++)
            assertThat(x.hashCode(), is(alwaysTheSame));
    }

    @Theory public void hashCodeIsConsistentWithEquals(@ForAll Object x, @ForAll Object y) {
        assumeThat(x, is(not(equalTo(null))));
        assumeTrue(x.equals(y));

        assertEquals(x.hashCode(), y.hashCode());
    }

    @Theory public void equalsWorks(@ForAll Object x, @ForAll Object y) {
        assumeThat(x, is(not(equalTo(null))));
        assumeTrue(x == y);

        assertTrue(x.equals(y));
    }

    @Theory public void notEqualsWorks(@ForAll Object x, @ForAll Object y) {
        assumeThat(x, is(not(equalTo(null))));
        assumeTrue(x != y);

        assertFalse(x.equals(y));
    }
}
