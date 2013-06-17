package com.pholser.junit.quickcheck.examples.using;

import com.pholser.junit.quickcheck.ForAll;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.security.Key;

import static org.junit.Assert.*;

@RunWith(Theories.class)
public class SymmetricKeyCryptography {
    @Theory public void decryptReversesEncrypt(@ForAll String plaintext, @ForAll Key key) throws Exception {
        Crypto crypto = new Crypto();

        byte[] ciphertext = crypto.encrypt(plaintext.getBytes("US-ASCII"), key);

        assertEquals(plaintext, new String(crypto.decrypt(ciphertext, key)));
    }
}
