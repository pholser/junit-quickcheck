package com.pholser.junit.quickcheck.examples.using;

import java.security.Key;

public class Crypto {
    public byte[] encrypt(byte[] plainBytes, Key key) {
        // fake implementation
        return plainBytes;
    }

    public byte[] decrypt(byte[] cipherBytes, Key key) {
        // fake implementation
        return cipherBytes;
    }
}
