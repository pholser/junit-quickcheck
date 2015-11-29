package com.pholser.junit.quickcheck.generator;

public class GeneratorConfigurationException extends RuntimeException {
    public GeneratorConfigurationException(String message) {
        super(message);
    }

    public GeneratorConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
