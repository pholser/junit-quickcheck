package com.pholser.junit.quickcheck;

public class MessingWithDoublePrecision {
    static class DecomposedDouble implements Comparable<DecomposedDouble> {
        private static final long MAX_MANTISSA = 0x000FFFFFFFFFFFFFL;
        private final double d;
        final long bits;
        final long sign;
        final long exponent;
        final int gottenExponent;
        final long mantissa;

        private DecomposedDouble(double d) {
            this(d, Double.doubleToLongBits(d));
        }

        private DecomposedDouble(double d, long bits) {
            this(d, bits, (bits >>> 63) & 0x1, ((bits >>> 52) & 0x7FF) - 1023, Math.getExponent(d),
                    bits & MAX_MANTISSA);
        }

        public DecomposedDouble(double d, long bits, long sign, long exponent, int gottenExponent, long mantissa) {
            this.d = d;
            this.bits = bits;
            this.sign = sign;
            this.exponent = exponent;
            this.gottenExponent = gottenExponent;
            this.mantissa = mantissa;
        }

        public DecomposedDouble(long sign, long exponent, long mantissa) {
            this.sign = sign;
            this.exponent = exponent;
            this.gottenExponent = (int) exponent;
            this.mantissa = mantissa;
            this.bits = (sign << 63) | ((exponent + 1023) << 52) | mantissa;
            this.d = Double.longBitsToDouble(bits);
        }

        static DecomposedDouble of(double d) {
            return new DecomposedDouble(d);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof DecomposedDouble && ((DecomposedDouble) o).d == d;
        }

        @Override
        public int hashCode() {
            return (int) bits;
        }

        @Override
        public String toString() {
            return String.format("%.20e = %s: sign %d, exponent %d, getExponent %d, mantissa %d",
                    d, formattedBitString(), sign, exponent,
                    gottenExponent, mantissa);
        }

        private String formattedBitString() {
            String bitString = String.format("%32s", Long.toBinaryString(bits)).replace(' ', '0');
            return bitString.substring(0, 1) + ' ' + bitString.substring(1, 12) + ' ' + bitString.substring(12);
        }

        @Override
        public int compareTo(DecomposedDouble other) {
            return Double.compare(d, other.d);
        }

        DecomposedDouble nextUp() {
            return new DecomposedDouble(Math.nextUp(d));
        }

        DecomposedDouble next() {
            if (mantissa == MAX_MANTISSA) {
                return new DecomposedDouble(sign, exponent, 0);
            }
            return new DecomposedDouble(sign, exponent + 1, MAX_MANTISSA);
        }
    }

    public static void main(String[] args) {
        System.out.println(DecomposedDouble.of(Double.NEGATIVE_INFINITY));
        System.out.println(DecomposedDouble.of(-Double.MAX_VALUE));

        DecomposedDouble max = DecomposedDouble.of(Double.MAX_VALUE);
        DecomposedDouble prev = DecomposedDouble.of(0.0);

        for (DecomposedDouble d = new DecomposedDouble(Math.nextUp(0.0));
             d.compareTo(max) < 0;
             prev = d, d = d.next()) {
            if (d.exponent != prev.exponent) {
                System.out.println(prev);
                System.out.println(d);
            }
        }

        System.out.println(max);
        System.out.println(DecomposedDouble.of(Double.POSITIVE_INFINITY));

        System.out.println(DecomposedDouble.of(0.0));
        System.out.println(DecomposedDouble.of(-0.0));
        System.out.println(DecomposedDouble.of(Double.NaN));

        System.out.println(DecomposedDouble.of(Math.nextAfter(-0.0, -1.0)));
        System.out.println(DecomposedDouble.of(Math.nextAfter(0.0, 1.0)));
    }
}
