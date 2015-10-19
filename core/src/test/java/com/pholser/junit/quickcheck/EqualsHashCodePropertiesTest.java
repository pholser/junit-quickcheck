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

package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * Swiped from <a href="http://stackoverflow.com/questions/837484/junit-theory-for-hashcode-equals-contract">here</a>.
 */
@Category(LongRunning.class)
@RunWith(JUnitQuickcheck.class)
public class EqualsHashCodePropertiesTest {
    @Property public void equalsIsReflexive(Object o) {
        assumeThat(o, not(equalTo(null)));

        assertTrue(o.equals(o));
    }

    @Property public void equalsIsSymmetric(Object x, Object y) {
        assumeThat(x, not(equalTo(null)));
        assumeThat(y, not(equalTo(null)));
        assumeTrue(y.equals(x));

        assertTrue(x.equals(y));
    }

    @Property public void equalsIsTransitive(Object x, Object y, Object z) {
        assumeThat(x, not(equalTo(null)));
        assumeThat(y, not(equalTo(null)));
        assumeThat(z, not(equalTo(null)));
        assumeTrue(x.equals(y) && y.equals(z));

        assertTrue(z.equals(x));
    }

    @Property public void equalsIsConsistent(Object x, Object y) {
        assumeThat(x, is(not(equalTo(null))));
        boolean alwaysTheSame = x.equals(y);

        for (int i = 0; i < 30; i++)
            assertThat(x.equals(y), is(alwaysTheSame));
    }

    @Property public void equalsReturnsFalseOnNull(Object x) {
        assumeThat(x, not(equalTo(null)));

        assertFalse(x.equals(null));
    }

    @Property public void hashCodeIsSelfConsistent(Object x) {
        assumeThat(x, not(equalTo(null)));
        int alwaysTheSame = x.hashCode();

        for (int i = 0; i < 30; i++)
            assertThat(x.hashCode(), is(alwaysTheSame));
    }

    @Property public void hashCodeIsConsistentWithEquals(Object x, Object y) {
        assumeThat(x, not(equalTo(null)));
        assumeTrue(x.equals(y));

        assertEquals(x.hashCode(), y.hashCode());
    }

    @Property public void equalsWorks(Object x, Object y) {
        assumeThat(x, not(equalTo(null)));
        assumeTrue(x == y);

        assertTrue(x.equals(y));
    }

    @Property public void notEqualsWorks(Object x, Object y) {
        assumeThat(x, not(equalTo(null)));
        assumeTrue(x != y);

        assertFalse(x.equals(y));
    }
}
