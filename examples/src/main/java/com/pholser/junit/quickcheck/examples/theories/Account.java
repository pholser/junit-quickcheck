package com.pholser.junit.quickcheck.examples.theories;

import com.pholser.junit.quickcheck.examples.theories.Money;

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
