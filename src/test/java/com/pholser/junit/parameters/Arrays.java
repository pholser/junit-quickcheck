package com.pholser.junit.parameters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Arrays {
    static {
        new Arrays();
    }

    private Arrays() {
        // empty on purpose
    }

    public static List<?> toList(Object array) {
        int length = Array.getLength(array);
        List<Object> items = new ArrayList<Object>();
        for (int i = 0; i < length; ++i)
            items.add(Array.get(array, i));
        return items;
    }
}
