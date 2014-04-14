package com.pholser.junit.quickcheck;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Set;

public class FunWithEncodings {
    public static void main(String[] args) {
        for (Charset each : Charset.availableCharsets().values())
            showRangesFor(each);
    }

    private static void showRangesFor(Charset c) {
        Set<Range<Integer>> ranges = encodableCodePoints(c).asRanges();

        System.out.print("# encodable code points for charset " + c.name() + ": ");
        System.out.println(size(ranges));

        for (Range<Integer> each : ranges)
            System.out.printf("\t%06x ... %06x\n", each.lowerEndpoint(), each.upperEndpoint());

        System.out.println();
    }

    private static int size(Set<Range<Integer>> ranges) {
        int size = 0;
        for (Range<Integer> each : ranges)
            size += (each.upperEndpoint() - each.lowerEndpoint() + 1);
        return size;
    }

    private static RangeSet<Integer> encodableCodePoints(Charset c) {
        CharsetEncoder encoder;
        try {
            encoder = c.newEncoder();
        } catch (UnsupportedOperationException e) {
            System.out.println("Charset " + c.name() + " does not support encoding");
            return ImmutableRangeSet.of();
        }

        return encodableCodePoints(encoder);
    }

    private static RangeSet<Integer> encodableCodePoints(CharsetEncoder encoder) {
        RangeSet<Integer> ranges = TreeRangeSet.create();

        int start = 0;
        boolean inRange = false;
        int current = 0;

        for (; current <= 0x10FFFF; ++current) {
            encoder.reset();

            String s = new String(new int[] { current }, 0, 1);
            if (encoder.canEncode(s)) {
                if (!inRange) {
                    inRange = true;
                    start = current;
                }
            } else if (inRange) {
                inRange = false;
                ranges.add(Range.closed(start, current - 1));
            }
        }

        if (inRange) {
            ranges.add(Range.closed(start, current - 1));
        }

        return ranges;
    }
}
