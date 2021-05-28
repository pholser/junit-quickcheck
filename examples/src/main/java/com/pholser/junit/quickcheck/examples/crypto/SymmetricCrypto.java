/*
 The MIT License

 Copyright (c) 2010-2021 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.examples.crypto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

final class SymmetricCrypto {
    private static final int KEY_LENGTH = 128;

    private final Cipher aes;
    private final Random random = new SecureRandom();

    SymmetricCrypto() {
        try {
            aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new AssertionError(e);
        }
    }

    EncryptionResult encrypt(byte[] plaintext, Key key)
        throws GeneralSecurityException {

        byte[] iv = initializationVector();
        aes.init(ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] ciphertext = aes.doFinal(plaintext);

        return new EncryptionResult(ciphertext, iv);
    }

    byte[] decrypt(EncryptionResult enciphered, Key key)
        throws GeneralSecurityException {

        aes.init(DECRYPT_MODE, key, new IvParameterSpec(enciphered.iv));
        return aes.doFinal(enciphered.ciphertext);
    }

    @SuppressFBWarnings("DMI_RANDOM_USED_ONLY_ONCE")
    private byte[] initializationVector() {
        byte[] iv = new byte[KEY_LENGTH / 8];
        random.nextBytes(iv);
        return iv;
    }

    static class EncryptionResult {
        final byte[] ciphertext;
        final byte[] iv;

        EncryptionResult(byte[] ciphertext, byte[] iv) {
            this.ciphertext = ciphertext;
            this.iv = iv;
        }
    }
}
