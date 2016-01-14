/*
 The MIT License

 Copyright (c) 2010-2016 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator.java.util;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.GeneratorConfiguration;
import com.pholser.junit.quickcheck.generator.java.lang.StringGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.generator.java.util.RFC4122.Namespaces.*;

/**
 * Home for machinery to produce {@link UUID}s according to
 * <a href="http://www.ietf.org/rfc/rfc4122.txt">RFC 4122</a>.
 */
public final class RFC4122 {
    private RFC4122() {
        throw new UnsupportedOperationException();
    }

    private abstract static class AbstractUUIDGenerator extends Generator<UUID> {
        protected AbstractUUIDGenerator() {
            super(UUID.class);
        }

        protected final void setVersion(byte[] bytes, byte mask) {
            bytes[6] &= 0x0F;
            bytes[6] |= mask;
        }

        protected final void setVariant(byte[] bytes) {
            bytes[8] &= 0x3F;
            bytes[8] |= 0x80;
        }

        protected final UUID newUUID(byte[] bytes) {
            ByteBuffer bytesIn = ByteBuffer.wrap(bytes);
            return new UUID(bytesIn.getLong(), bytesIn.getLong());
        }
    }

    private abstract static class NameBasedUUIDGenerator extends AbstractUUIDGenerator {
        private final StringGenerator stringGenerator = new StringGenerator();
        private final int versionMask;
        private final MessageDigest digest;
        private Namespace namespace;

        protected NameBasedUUIDGenerator(String hashAlgorithmName, int versionMask) {
            this.versionMask = versionMask;
            digest = MessageDigests.get(hashAlgorithmName);
        }

        @Override public UUID generate(SourceOfRandomness random, GenerationStatus status) {
            digest.reset();
            digest.update((namespace == null ? Namespaces.URL : namespace.value()).bytes);
            digest.update(stringGenerator.generate(random, status).getBytes(Charset.forName("UTF-8")));

            byte[] hash = digest.digest();
            setVersion(hash, (byte) versionMask);
            setVariant(hash);
            return newUUID(hash);
        }

        protected void setNamespace(Namespace namespace) {
            this.namespace = namespace;
        }
    }

    static final class MessageDigests {
        private MessageDigests() {
            throw new UnsupportedOperationException();
        }

        static MessageDigest get(String algorithmName) {
            try {
                return MessageDigest.getInstance(algorithmName);
            } catch (NoSuchAlgorithmException shouldNeverHappen) {
                throw new IllegalStateException(shouldNeverHappen);
            }
        }
    }

    /**
     * Produces values of type {@link UUID} that are RFC 4122 Version 3
     * identifiers.
     */
    public static class Version3 extends NameBasedUUIDGenerator {
        public Version3() {
            super("MD5", 0x30);
        }

        /**
         * Tells this generator to prepend the given "namespace" UUID to the
         * names it generates for UUID production.
         *
         * @param namespace a handle for a "namespace" UUID
         */
        public void configure(Namespace namespace) {
            setNamespace(namespace);
        }
    }

    /**
     * Produces values of type {@link UUID} that are RFC 4122 Version 4
     * identifiers.
     */
    public static class Version4 extends AbstractUUIDGenerator {
        @Override public UUID generate(SourceOfRandomness random, GenerationStatus status) {
            byte[] bytes = random.nextBytes(16);
            setVersion(bytes, (byte) 0x40);
            setVariant(bytes);
            return newUUID(bytes);
        }
    }

    /**
     * Produces values of type {@link UUID} that are RFC 4122 Version 5
     * identifiers.
     */
    public static class Version5 extends NameBasedUUIDGenerator {
        public Version5() {
            super("SHA-1", 0x50);
        }

        /**
         * Tells this generator to prepend the given "namespace" UUID to the
         * names it generates for UUID production.
         *
         * @param namespace a handle for a "namespace" UUID
         */
        public void configure(Namespace namespace) {
            setNamespace(namespace);
        }
    }

    /**
     * Used in version 3 and version 5 UUID generation to specify a
     * "namespace" UUID for use in generation.
     */
    @Target({ PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE })
    @Retention(RUNTIME)
    @GeneratorConfiguration
    public @interface Namespace {
        /**
         * @return a handle on a "namespace" UUID to use in generation
         */
        Namespaces value() default URL;
    }

    /**
     * Well-known "namespace" UUIDs.
     */
    public enum Namespaces {
        /** Fully-qualified DNS name. */
        DNS(0x10),

        /** URL. */
        URL(0x11),

        /** ISO object identifier. */
        ISO_OID(0x12),

        /** X.500 distinguished name. */
        X500_DN(0x14);

        final byte[] bytes;

        Namespaces(int difference) {
            this.bytes = new byte[] { 0x6B, (byte) 0xA7, (byte) 0xB8, (byte) difference,
                (byte) 0x9D, (byte) 0xAD,
                0x11, (byte) 0xD1,
                (byte) 0x80, (byte) 0xB4,
                0x00, (byte) 0xC0, 0x4F, (byte) 0xD4, 0x30, (byte) 0xC8 };
        }
    }
}
