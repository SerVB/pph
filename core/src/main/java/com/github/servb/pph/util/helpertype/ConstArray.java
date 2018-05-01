/*
 * Copyright 2018 SerVB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.servb.pph.util.helpertype;

/**
 * Emulates constant arrays from C++.
 *
 * @author SerVB
 * @param <K> Type of enumeration. Default is EnumC.
 * @param <E> Type of elements. Has to be immutable type.
 */
public class ConstArray<K extends EnumC, E> {

    /** Stores elements. */
    private final E[] storage;

    /**
     * Constructs the object.
     *
     * @param storage Elements.
     */
    public ConstArray(final E[] storage) {
        this.storage = storage.clone();
    }

    /**
     * Returns element at {@code idx}.
     *
     * @param idx   Index of returning element.
     * @return      Element at {@code idx}.
     */
    public final E get(final int idx) {
        return storage[idx];
    }

    /**
     * Returns element by value.
     *
     * @param value Contains index of returning element.
     * @return      Element at {@code idx}.
     */
    public final E get(final K value) {
        return storage[value.getValue()];
    }

    /**
     * Returns the length of the array.
     *
     * @return The length of the array.
     */
    public final int size() {
        return storage.length;
    }
}
