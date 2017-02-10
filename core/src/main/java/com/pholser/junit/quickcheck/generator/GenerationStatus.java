/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

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

import java.util.Objects;
import java.util.Optional;

/**
 * {@link Generator}s are fed instances of this interface on each generation
 * so that, if they choose, they can use these instances to influence the
 * results of a generation for a particular property parameter.
 */
public interface GenerationStatus {
    /**
     * @return an arbitrary "size" parameter; this value (probabilistically)
     * increases for every successful generation
     */
    int size();

    /**
     * @return how many attempts have been made to generate a value for a
     * property parameter
     */
    int attempts();

    /**
     * Associates the given value with the given key for the duration of the
     * generation of a value for a property parameter, or until overwritten.
     *
     * @param <T> type of the value
     * @param key a key to associate with a value
     * @param value the associated value
     * @return self, so that calls to this method can be chained
     */
    <T> GenerationStatus setValue(Key<T> key, T value);

    /**
     * Retrieves the value associated with the given key.
     *
     * @param <T> type of the value associated with the key
     * @param key key to look up
     * @return the (optional) associated value
     */
    <T> Optional<T> valueOf(Key<T> key);

    /**
     * Type-safe keys for {@link GenerationStatus#setValue(Key, Object)
     * setValue} and {@link GenerationStatus#valueOf(Key) valueOf}.
     *
     * @param <T> type of value that can be associated with the key
     */
    final class Key<T> {
        private final String name;
        private final Class<T> type;

        public Key(String name, Class<T> type) {
            if (name == null)
                throw new NullPointerException("name must not be null");
            if (type == null)
                throw new NullPointerException("type must not be null");

            this.name = name;
            this.type = type;
        }

        public T cast(Object o) {
            return type.cast(o);
        }

        @Override public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof Key<?>))
                return false;

            Key<?> other = (Key<?>) o;
            return name.equals(other.name) && type.equals(other.type);
        }

        @Override public int hashCode() {
            return Objects.hash(name, type);
        }

        @Override public String toString() {
            return name + ": " + type;
        }
    }
}
