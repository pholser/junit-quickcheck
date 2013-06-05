package assumptions;

import java.util.ArrayList;
import java.util.List;

class PrimeFactors {

    public static List<Integer> of(int n) {
        List<Integer> primes = new ArrayList<Integer>();

        for (int canditate = 2; canditate <= n; canditate++) {
            for (; n % canditate == 0; n /= canditate) {
                primes.add(canditate);
            }
        }

        return primes;
    }

}
