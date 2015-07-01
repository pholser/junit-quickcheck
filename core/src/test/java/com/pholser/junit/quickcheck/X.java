package com.pholser.junit.quickcheck;

import org.junit.Test;

import static java.lang.Math.*;

public class X {
    @Test public void x() {
        for (int i = 0; i < 100; ++i) {
            double v = ceil(log(random()) / log(0.5));
            System.out.printf("i = %d, v = %f --> %f\n", i, v, i * v);
        }
    }
}
