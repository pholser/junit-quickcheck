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

package com.pholser.junit.quickcheck.generator.java.time;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.time.Instant;
import java.time.Year;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.pholser.junit.quickcheck.internal.Reflection.defaultValueOf;

/**
 * Produces values of type {@link ZonedDateTime}.
 */
public class ZonedDateTimeGenerator extends Generator<ZonedDateTime> {
    // Use UTC for ZonedDateTime generation.
    private static final ZoneId zoneId = ZoneId.of("UTC");
    private ZonedDateTime min = ZonedDateTime.of(Year.MIN_VALUE, 1, 1, 0, 0, 0, 0, zoneId);
    private ZonedDateTime max = ZonedDateTime.of(Year.MAX_VALUE, 12, 31, 23, 59, 59, 999_999_999, zoneId);

    public ZonedDateTimeGenerator() {
        super(ZonedDateTime.class);
    }

    /**
     * <p>Tells this generator to produce values within a specified
     * {@linkplain InRange#min() minimum} and/or {@linkplain InRange#max()
     * maximum}, inclusive, with uniform distribution, down to the nanosecond.</p>
     *
     * <p>If an endpoint of the range is not specified, the generator will use
     * dates with values of either (@link Instant#MIN) or (@link Instant#MAX) and
     * UTC zone as appropriate.</p>
     *
     * <p>{@link InRange#format()} describes
     * {@linkplain DateTimeFormatter#ofPattern(String) how the generator is to
     * interpret the range's endpoints}.</p>
     *
     * @param range annotation that gives the range's constraints
     * @throws IllegalArgumentException if the range's values cannot be
     * converted to {@code ZonedDateTime}
     */
    public void configure(InRange range) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(range.format());

        try {
            if (!defaultValueOf(InRange.class, "min").equals(range.min()))
                min = ZonedDateTime.parse(range.min(), formatter).withZoneSameInstant(zoneId);
            if (!defaultValueOf(InRange.class, "max").equals(range.max()))
                max = ZonedDateTime.parse(range.max(), formatter).withZoneSameInstant(zoneId);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(e);
        }

        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException(String.format("bad range, %s > %s", range.min(), range.max()));
    }

    @Override
    public ZonedDateTime generate(SourceOfRandomness random, GenerationStatus status) {
        // Project the ZonedDateTime to an Instant for easy long-based generation.
        return ZonedDateTime.ofInstant(
                random.nextInstant(min.toInstant(), max.toInstant()),
                zoneId);
    }
}
