package com.pholser.junit.quickcheck;

import com.pholser.junit.quickcheck.test.generator.AString;
import java.util.HashMap;

public class GeneratorDescriptorsIssue235 {
    public static final HashMap<
        @Produced({
            @From(value = AString.class, frequency = 50),
            @From(value = AString.class, frequency = 50)
        })
        String,
        String> manifestAttributes = null;
}
