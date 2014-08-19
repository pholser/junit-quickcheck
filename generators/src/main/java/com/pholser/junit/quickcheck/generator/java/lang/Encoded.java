/*
 The MIT License

 Copyright (c) 2010-2014 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator.java.lang;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.charset.Charset;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.GeneratorConfiguration;
import com.pholser.junit.quickcheck.generator.java.lang.strings.CodePoints;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * <p>Produces values for theory parameters of type {@link String}.</p>
 *
 * <p>This implementation produces strings whose code points correspond to code points in a given
 * {@link java.nio.charset.Charset} ({@link java.nio.charset.Charset#defaultCharset() by default}).</p>
 *
 * <p>The generated values will have {@linkplain String#length()} decided by
 * {@link com.pholser.junit.quickcheck.generator.GenerationStatus#size()}.</p>
 */
public class Encoded extends Generator<String> {
    private Charset charset = Charset.defaultCharset();

    public Encoded() {
        super(String.class);
    }

    public void configure(InCharset c) {
        charset = Charset.forName(c.value());
    }

    @Override public String generate(SourceOfRandomness random, GenerationStatus status) {
        CodePoints charsetPoints = CodePoints.forCharset(charset);

        int[] codePoints = new int[status.size()];
        for (int i = 0; i < codePoints.length; ++i)
            codePoints[i] = charsetPoints.at(random.nextInt(0, charsetPoints.size() - 1));

        return new String(codePoints, 0, codePoints.length);
    }

    /**
     * Names a {@linkplain java.nio.charset.Charset}.
     */
    @Target({ PARAMETER, FIELD })
    @Retention(RUNTIME)
    @GeneratorConfiguration
    public @interface InCharset {
        String value();
    }
}
