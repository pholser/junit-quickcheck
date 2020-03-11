package com.pholser.junit.quickcheck;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.GeneratorConfiguration;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RunWith(JUnitQuickcheck.class)
public class FieldMetaAnnotationTest {
    @From(StringGen.class) // <-- if you remove this the test passes
    @From(StringGen.class) @Regex("hello") String stringGen;

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @GeneratorConfiguration
    @interface Regex {
        String value();
    }

    public static class StringGen extends Generator<String> {
        private Regex regex;

        public StringGen() {
            super(String.class);
        }

        public void configure(Regex regex) {
            this.regex = regex;
        }

        @Override
        public String generate(SourceOfRandomness random, GenerationStatus status) {
            assert regex != null: "regex was not set";
            return regex.value();
        }
    }

    public static class ExtraGen extends Generator<String> {
        public ExtraGen() {
            super(String.class);
        }

        @Override
        public String generate(SourceOfRandomness random, GenerationStatus status) {
            Generator<String> stringGen = (Generator<String>) gen().field(FieldMetaAnnotationTest.class, "stringGen");
            return stringGen.generate(random, status);
        }
    }


    @Property public void helloworld(@From(ExtraGen.class) String value) {
        assertThat(value, is("hello"));
    }
}
