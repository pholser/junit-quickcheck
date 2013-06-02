package using;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;

@RunWith(Theories.class)
public class SymmetricKeyCryptography {

    @Theory
    public void decryptReversesEncrypt(@ForAll String plaintext, @ForAll Key key) throws UnsupportedEncodingException {
        Crypto crypto = new Crypto();

        byte[] ciphertext = crypto.encrypt(plaintext.getBytes("US-ASCII"), key);

        assertEquals(plaintext, new String(crypto.decrypt(ciphertext, key)));
    }
}
