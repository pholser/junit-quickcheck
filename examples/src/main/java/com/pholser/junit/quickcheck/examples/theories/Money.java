package com.pholser.junit.quickcheck.examples.theories;

public class Money implements Comparable<Money> {
    static final Money NONE = new Money(0);

    private final int value;

    public Money(int val) {
        value = val;
    }

    public Money minus(Money amount) {
        return new Money(value - amount.value);
    }

    @Override
    public int compareTo(Money other) {
        return Integer.valueOf(value).compareTo(other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Money other = (Money) o;
        return value == other.value;
    }
}
