package com.pholser.junit.quickcheck.examples.theories;

public class Account {
    private Money balance;

    public Account(Money balance) {
        this.balance = balance;
    }

    public void withdraw(Money amount) {
        balance = balance.minus(amount);
    }

    public Money balance() {
        return balance;
    }
}
