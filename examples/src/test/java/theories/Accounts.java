package theories;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class Accounts {

    @DataPoints
    public static Money[] BALANCE = { Money.NONE, new Money(10), new Money(100), new Money(1000) };

    @Theory
    public void withdrawingReducesBalance(Money originalBalance, Money withdrawalAmount) {
        assumeThat(originalBalance, greaterThan(Money.NONE));
        assumeThat(withdrawalAmount, allOf(greaterThan(Money.NONE), lessThan(originalBalance)));

        Account account = new Account(originalBalance);

        account.withdraw(withdrawalAmount);

        assertEquals(originalBalance.minus(withdrawalAmount), account.balance());
    }
}
