package assumptions;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

import java.math.BigInteger;

import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;

@RunWith(Theories.class)
public class PrimeFactorsTheories {
    @Theory
    public void factorsPassPrimalityTest(@ForAll int n) {
        assumeThat(n, greaterThan(0));

        for (int each : PrimeFactors.of(n)) {
            assertTrue(BigInteger.valueOf(each).isProbablePrime(1000));
        }
    }

    @Theory
    public void factorsMultiplyToOriginal(@ForAll int n) {
        assumeThat(n, greaterThan(0));

        int product = 1;
        for (int each : PrimeFactors.of(n)) {
            product *= each;
        }

        assertEquals(n, product);
    }

    @Theory
    public void factorizationsAreUnique(@ForAll int m, @ForAll int n) {
        assumeThat(m, greaterThan(0));
        assumeThat(n, greaterThan(0));
        assumeThat(m, not(equalTo(n)));

        assertThat(PrimeFactors.of(m), not(equalTo(PrimeFactors.of(n))));
    }
}
