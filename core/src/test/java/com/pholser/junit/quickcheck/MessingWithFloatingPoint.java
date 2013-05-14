package com.pholser.junit.quickcheck;

public class MessingWithFloatingPoint {
    static class DecomposedFloat implements Comparable<DecomposedFloat> {
        private static final int MAX_MANTISSA = 0x7FFFFF;
        private final float f;
        final int bits;
        final int sign;
        final int exponent;
        final int gottenExponent;
        final int mantissa;

        private DecomposedFloat(float f) {
            this.f = f;
            bits = Float.floatToIntBits(f);
            sign = (bits >>> 31) & 0x1;
            exponent = ((bits >>> 23) & 0xFF) - 127;
            gottenExponent = Math.getExponent(f);
            mantissa = bits & MAX_MANTISSA;
        }

        static DecomposedFloat of(float f) {
            return new DecomposedFloat(f);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof DecomposedFloat && ((DecomposedFloat) o).f == f;
        }

        @Override
        public int hashCode() {
            return bits;
        }

        @Override
        public String toString() {
            return String.format("%.20e = %s: sign %d, exponent %d, getExponent %d, mantissa %d",
                    f, formattedBitString(), sign, exponent,
                    gottenExponent, mantissa);
        }

        private String formattedBitString() {
            String bitString = String.format("%32s", Integer.toBinaryString(bits)).replace(' ', '0');
            return bitString.substring(0, 1) + ' ' + bitString.substring(1, 9) + ' ' + bitString.substring(9);
        }

        @Override
        public int compareTo(DecomposedFloat other) {
            return Float.compare(f, other.f);
        }

        DecomposedFloat nextUp() {
            return new DecomposedFloat(Math.nextUp(f));
        }
    }

    public static void main(String[] args) {
        System.out.println(DecomposedFloat.of(Float.NEGATIVE_INFINITY));
        System.out.println(DecomposedFloat.of(-Float.MAX_VALUE));

        DecomposedFloat max = DecomposedFloat.of(Float.MAX_VALUE);
        DecomposedFloat prev = DecomposedFloat.of(-Float.MAX_VALUE);

        int count = 0;
        for (DecomposedFloat f = prev.nextUp(); f.compareTo(max) < 0; prev = f, f = f.nextUp(), ++count) {
            if (f.exponent != prev.exponent) {
                System.out.println(prev);
                System.out.println(f);
            }
        }

        System.out.println(max);
        System.out.println(DecomposedFloat.of(Float.POSITIVE_INFINITY));

        System.out.println(DecomposedFloat.of(0.0F));
        System.out.println(DecomposedFloat.of(-0.0F));
        System.out.println(DecomposedFloat.of(Float.NaN));

        System.out.println(DecomposedFloat.of(Math.nextAfter(-0.0F, -1F)));
        System.out.println(DecomposedFloat.of(Math.nextAfter(0.0F, 1F)));
    }
}
