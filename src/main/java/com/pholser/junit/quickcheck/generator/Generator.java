/*
 The MIT License

 Copyright (c) 2010-2012 Paul R. Holser, Jr.

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

package com.pholser.junit.quickcheck.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pholser.junit.quickcheck.internal.ReflectionException;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import static com.pholser.junit.quickcheck.internal.Reflection.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

/**
 * Produces values for theory parameters.
 *
 * @param <T> type of theory parameter to apply this generator's values to
 */
public abstract class Generator<T> {
    private final List<Class<T>> types = new ArrayList<Class<T>>();

    /**
     * @param type class token for type of theory parameter this generator is applicable to
     */
    @SuppressWarnings("unchecked")
    protected Generator(Class<T> type) {
        this(asList(type));
    }

    /**
     * Used for generators of primitives/their wrappers. For example, a {@code Generator<Integer>} can be used for
     * theory parameters of type {@code Integer} or {@code int}.
     *
     * @param types class tokens for type of theory parameter this generator is applicable to
     */
    protected Generator(List<Class<T>> types) {
        this.types.addAll(types);
    }

    /**
     * @return class tokens for the types of theory parameters this generator is applicable to
     */
    public List<Class<T>> types() {
        return unmodifiableList(types);
    }

    /**
     * <p>Produces a value for a theory parameter.</p>
     *
     * <p>A generator may raise an unchecked exception if some condition exists which would lead to a confusing
     * generation -- for example, if a generator honored {@link InRange} and its {@link InRange#minInt()} were
     * greater than {@link InRange#maxInt()}.</p>
     *
     * @param random a source of randomness to be used when generating the value.
     * @param status an object that the generator can use to influence the value it produces. For example, a generator
     * for lists can use the {@link GenerationStatus#size() size} method to generate lists with a given number of
     * elements.
     * @return the generated value
     */
    public abstract T generate(SourceOfRandomness random, GenerationStatus status);

    /**
     * @return whether this generator has component generators, such as for those generators that produce lists or
     * arrays.
     * @see #addComponentGenerators(java.util.List)
     */
    public boolean hasComponents() {
        return false;
    }

    /**
     * @return how many component generators this generator needs
     * @see #addComponentGenerators(java.util.List)
     */
    public int numberOfNeededComponents() {
        return 0;
    }

    /**
     * <p>Adds component generators to this generator.</p>
     *
     * <p>Some generators need component generators to create proper values. For example, lists require a single
     * component generator in order to generate elements that have the type of the list parameter's type argument.</p>
     *
     * @param componentGenerators component generators to add
     */
    public void addComponentGenerators(List<Generator<?>> componentGenerators) {
        // do nothing by default
    }

    /**
     * <p>Tells this generator to configure itself using annotations from a theory parameter.</p>
     *
     * <p>The annotations fed to this method will be those annotations on the theory parameter that are themselves
     * marked with {@link GeneratorConfiguration}.</p>
     *
     * <p>By default, the generator will configure itself by:</p>
     * <ul>
     *     <li>For each of the given annotations:</li>
     *     <ul>
     *         <li>Find a {@code public} method on the generator named {@code configure}, that accepts a single
     *         parameter of the annotation type</li>
     *         <li>Invoke the {@code configure} method reflectively, passing the annotation as the argument</li>
     *     </ul>
     * </ul>
     *
     * @param configurationsByType a map of configuration annotations, keyed by annotation type
     */
    public void configure(Map<Class<? extends Annotation>, Annotation> configurationsByType) {
        for (Map.Entry<Class<? extends Annotation>, Annotation> eachEntry : configurationsByType.entrySet())
            configure(eachEntry.getKey(), eachEntry.getValue());
    }

    private void configure(Class<? extends Annotation> annotationType, Annotation configuration) {
        Method configurer;

        try {
            configurer = findMethodQuietly(getClass(), "configure", annotationType);
        } catch (ReflectionException ex) {
            return;
        }

        invokeQuietly(configurer, this, configuration);
    }
}
