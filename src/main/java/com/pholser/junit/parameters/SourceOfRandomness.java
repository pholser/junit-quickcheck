package com.pholser.junit.parameters;

public interface SourceOfRandomness {
    boolean nextBoolean();

    void nextBytes(byte[] bytes);

    byte[] nextBytes(int length);

    double nextDouble();

    float nextFloat();

    double nextGaussian();

    int nextInt();

    int nextInt(int n);

    long nextLong();

    void setSeed(long seed);
}
