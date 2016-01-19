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

package com.pholser.junit.quickcheck.generator.java.time;

import java.time.ZoneOffset;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Reflection.*;

/**
 * Produces values of type {@link ZoneOffset}.
 */
public class ZoneOffsetGenerator extends Generator<ZoneOffset> {
    /* The way ZoneOffsets work, ZoneOffset.MAX (-18:00) is actually
       the lower end of the seconds range, whereas ZoneOffset.MIN (+18:00)
       is the upper end. */
    private ZoneOffset min = ZoneOffset.MAX;
    private ZoneOffset max = ZoneOffset.MIN;

    public ZoneOffsetGenerator() {
        super(ZoneOffset.class);
    }

    /**
     * <p>Tells this generator to produce values within a specified
     * {@linkplain InRange#min() minimum} and/or {@linkplain InRange#max()
     * maximum}, inclusive, with uniform distribution.</p>
     *
     * <p>If an endpoint of the range is not specified, the generator will use
     * ZoneOffsets with values of either {@code ZoneOffset#MIN} or
     * {@code ZoneOffset#MAX} as appropriate.</p>
     *
     * <p>{@linkplain InRange#format()} is ignored. ZoneOffsets are always
     * parsed using their zone id.</p>
     *
     * @see ZoneOffset#of(String)
     * @param range annotation that gives the range's constraints
     */
    public void configure(InRange range) {
        if (!defaultValueOf(InRange.class, "min").equals(range.min()))
            min = ZoneOffset.of(range.min());
        if (!defaultValueOf(InRange.class, "max").equals(range.max()))
            max = ZoneOffset.of(range.max());

        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException(String.format("bad range, %s > %s", min, max));
    }

    @Override public ZoneOffset generate(SourceOfRandomness random, GenerationStatus status) {
        int minSeconds = min.getTotalSeconds();
        int maxSeconds = max.getTotalSeconds();

        return ZoneOffset.ofTotalSeconds(
            (minSeconds <= maxSeconds)
                ? random.nextInt(minSeconds, maxSeconds)
                : random.nextInt(maxSeconds, minSeconds));
    }
}
