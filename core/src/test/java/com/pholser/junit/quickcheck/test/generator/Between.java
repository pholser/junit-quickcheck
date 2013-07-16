package com.pholser.junit.quickcheck.test.generator;

import com.pholser.junit.quickcheck.generator.GeneratorConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target(PARAMETER)
@Retention(RUNTIME)
@GeneratorConfiguration
public @interface Between {
    int min();

    int max();
}
