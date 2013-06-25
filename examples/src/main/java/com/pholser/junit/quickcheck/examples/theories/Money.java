package com.pholser.junit.quickcheck.examples.theories;

class Money implements Comparable<Money> {
    static final Money NONE = new Money(0);

    private final int value;

    Money(int val) {
        value = val;
    }

    Money minus(Money amount) {
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
