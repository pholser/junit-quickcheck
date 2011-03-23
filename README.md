# junit-quickcheck: QuickCheck-style parameter suppliers for JUnit theories

junit-quickcheck is a library that supplies [JUnit](http://junit.org) [theories](http://groups.csail.mit.edu/pag/pubs/test-theory-demo-oopsla2007.pdf) with random values with which to test the validity of the theories.

### Background

The [Haskell](http://haskell.org) library [QuickCheck](http://www.cse.chalmers.se/~rjmh/QuickCheck/manual.html) allows programmers to specify properties of a function that should hold true for some large set of possible arguments to the function, then runs the function using lots of random arguments to see whether the property holds up.

JUnit's answer to function properties is the notion of theories. Programmers write parameterized tests marked as theories, run using a special test runner:

    @RunWith(Theories.class)
    public class Accounts {
        @Theory
        public void withdrawingReducesBalance(Money originalBalance, Money withdrawalAmount) {
            Account account = new Account(originalBalance);

            account.withdraw(withdrawalAmount);

            assertEquals(originalBalance.minus(withdrawalAmount), account.balance());
        }
    }

TDD/BDD builds up designs example by example. The resulting test suites give developers
confidence that their code works for the examples they thought of. Theories...

### About junit-quickcheck

junit-quickcheck was written by Paul Holser, and is distributed under the MIT License.

    The MIT License

    Copyright (c) 2010-2011 Paul R. Holser, Jr.

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
