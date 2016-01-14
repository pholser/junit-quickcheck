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

package com.pholser.junit.quickcheck.generator.java.lang;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.charset.Charset;

import com.pholser.junit.quickcheck.generator.GeneratorConfiguration;
import com.pholser.junit.quickcheck.generator.java.lang.strings.CodePoints;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * <p>Produces {@link String}s whose code points correspond to code points in
 * a given {@link java.nio.charset.Charset}
 * ({@link java.nio.charset.Charset#defaultCharset() by default}).</p>
 */
public class Encoded extends AbstractStringGenerator {
    private CodePoints charsetPoints;

    public Encoded() {
        initialize(Charset.defaultCharset());
    }

    /**
     * Tells this generator to emit strings in the given charset.
     *
     * @param charset a charset to use as the source for characters of
     * generated strings
     */
    public void configure(InCharset charset) {
        initialize(Charset.forName(charset.value()));
    }

    private void initialize(Charset charset) {
        charsetPoints = CodePoints.forCharset(charset);
    }

    @Override protected int nextCodePoint(SourceOfRandomness random) {
        return charsetPoints.at(random.nextInt(0, charsetPoints.size() - 1));
    }

    @Override protected boolean codePointInRange(int codePoint) {
        return charsetPoints.contains(codePoint);
    }

    /**
     * Names a {@link java.nio.charset.Charset}.
     */
    @Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
    @Retention(RUNTIME)
    @GeneratorConfiguration
    public @interface InCharset {
        String value();
    }
}
