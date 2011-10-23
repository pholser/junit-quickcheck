package com.pholser.junit.quickcheck.internal.extractors;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

import com.pholser.junit.quickcheck.RegisterableRandomValueExtractor;
import com.pholser.junit.quickcheck.internal.random.SourceOfRandomness;

public class StringExtractor extends RegisterableRandomValueExtractor<String> {
    private final CharsetDecoder decoder;

    public StringExtractor() {
        super(String.class);

        decoder = Charset.forName("UTF-8").newDecoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE)
            .replaceWith(" ");
    }

    @Override
    public String extract(SourceOfRandomness random) {
        int size = random.nextInt(0, 100);
        byte[] raw = random.nextBytes(size);

        try {
            return decoder.decode(ByteBuffer.wrap(raw)).toString();
        } catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
