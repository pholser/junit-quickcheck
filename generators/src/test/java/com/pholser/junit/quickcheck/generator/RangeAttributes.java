package com.pholser.junit.quickcheck.generator;

import java.util.Map;

import static com.pholser.junit.quickcheck.Annotations.*;

public class RangeAttributes {
    private static final Map<String, Object> attributes;

    static {
        attributes = defaultValuesOf(InRange.class);
    }

    private RangeAttributes() {
        throw new UnsupportedOperationException();
    }

    private static <T> T rangeAttribute(String name, Class<T> type) {
        return type.cast(attributes.get(name));
    }

    public static byte minByte() {
        return rangeAttribute("minByte", Byte.class);
    }

    public static byte maxByte() {
        return rangeAttribute("maxByte", Byte.class);
    }

    public static char minChar() {
        return rangeAttribute("minChar", Character.class);
    }

    public static char maxChar() {
        return rangeAttribute("maxChar", Character.class);
    }

    public static double minDouble() {
        return rangeAttribute("minDouble", Double.class);
    }

    public static double maxDouble() {
        return rangeAttribute("maxDouble", Double.class);
    }

    public static float minFloat() {
        return rangeAttribute("minFloat", Float.class);
    }

    public static float maxFloat() {
        return rangeAttribute("maxFloat", Float.class);
    }

    public static int minInt() {
        return rangeAttribute("minInt", Integer.class);
    }

    public static int maxInt() {
        return rangeAttribute("maxInt", Integer.class);
    }

    public static long minLong() {
        return rangeAttribute("minLong", Long.class);
    }

    public static long maxLong() {
        return rangeAttribute("maxLong", Long.class);
    }

    public static short minShort() {
        return rangeAttribute("minShort", Short.class);
    }

    public static short maxShort() {
        return rangeAttribute("maxShort", Short.class);
    }
}
