package com.pholser.junit.quickcheck.examples.using;

import java.security.Key;

class Crypto {
    byte[] encrypt(byte[] plainBytes, Key key) {
        // fake implementation
        return plainBytes;
    }

    byte[] decrypt(byte[] cipherBytes, Key key) {
        // fake implementation
        return cipherBytes;
    }
}
