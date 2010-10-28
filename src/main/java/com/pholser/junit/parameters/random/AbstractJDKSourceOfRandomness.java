package com.pholser.junit.parameters.random;

import java.util.Random;

abstract class AbstractJDKSourceOfRandomness implements SourceOfRandomness {
    private final Random random;

    protected AbstractJDKSourceOfRandomness(Random random) {
        this.random = random;
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public void nextBytes(byte[] bytes) {
        random.nextBytes(bytes);
    }

    @Override
    public byte[] nextBytes(int length) {
        byte[] bytes = new byte[length];
        nextBytes(bytes);
        return bytes;
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public float nextFloat() {
        return random.nextFloat();
    }

    @Override
    public double nextGaussian() {
        return random.nextGaussian();
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int n) {
        return random.nextInt(n);
    }

    @Override
    public long nextLong() {
        return random.nextLong();
    }

    @Override
    public void setSeed(long seed) {
        random.setSeed(seed);
    }
}
