import com.pholser.junit.quickcheck.internal.extractors.StringExtractor;
import com.pholser.junit.quickcheck.internal.random.JDKSourceOfRandomness;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class Extraction {
    public static void main(String[] args) {
        SourceOfRandomness random = new JDKSourceOfRandomness();
        StringExtractor extractor = new StringExtractor();

        for (int i = 0; i < 10; ++i)
            System.out.printf("%d: %s\n", i, extractor.extract(random));
    }
}
