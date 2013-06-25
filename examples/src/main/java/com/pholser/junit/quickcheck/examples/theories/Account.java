package com.pholser.junit.quickcheck.examples.theories;

class Account {
    private Money balance;

    Account(Money balance) {
        this.balance = balance;
    }

    void withdraw(Money amount) {
        balance = balance.minus(amount);
    }

    Money balance() {
        return balance;
    }
}
