package com.pholser.junit.quickcheck.internal;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

import com.pholser.junit.quickcheck.reflect.GenericArrayTypeImpl;
import com.pholser.junit.quickcheck.reflect.WildcardTypeImpl;

import com.pholser.junit.quickcheck.reflect.ParameterizedTypeImpl;

import org.javaruntype.exceptions.TypeValidationException;

import com.pholser.junit.quickcheck.internal.extractors.BasicExtractorSource;
import com.pholser.junit.quickcheck.internal.extractors.ExtractorRepository;
import com.pholser.junit.quickcheck.reflect.TypeVariableImpl;
import org.junit.Before;
import org.junit.Test;

public class FindingExtractorInvolvingTypeVariableTest {
    private ExtractorRepository repo;
    private TypeVariable<Class<?>> typeVariable;

    @Before
    public void setUp() {
        repo = new ExtractorRepository().add(new BasicExtractorSource());
        typeVariable = new TypeVariableImpl<Class<?>>("E", List.class, new Type[] { Object.class });
    }

    @Test(expected = TypeValidationException.class)
    public void withBareTypeVariable() {
        repo.extractorFor(typeVariable);
    }

    @Test(expected = TypeValidationException.class)
    public void withTypeVariableInParameterizedType() {
        repo.extractorFor(new ParameterizedTypeImpl(List.class, typeVariable));
    }

    @Test(expected = TypeValidationException.class)
    public void withTypeVariableInBoundsOfParameterizedType() {
        repo.extractorFor(new ParameterizedTypeImpl(List.class,
            new WildcardTypeImpl(new Type[] { typeVariable }, new Type[0])));
    }

    @Test(expected = TypeValidationException.class)
    public void withTypeVariableInGenericArrayType() {
        repo.extractorFor(new GenericArrayTypeImpl(typeVariable));
    }
}
