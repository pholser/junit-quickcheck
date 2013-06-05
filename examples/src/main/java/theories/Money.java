package theories;

public class Money implements Comparable<Money> {

    static final Money NONE = new Money(0);

    int value;

    public Money(int val) {
        value = val;
    }

    public Money minus(Money amount) {
        return new Money(value - amount.value);
    }

    @Override
    public int compareTo(Money o) {
        return Integer.valueOf(value).compareTo(o.value);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Money other = (Money) obj;
        return value == other.value;
    }

}
