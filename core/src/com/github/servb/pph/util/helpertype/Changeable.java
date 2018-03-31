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

import java.util.Objects;

/**
 * Changeable object to be passed to functions and to be changed in them.
 *
 * @param <T>   Object's Type.
 * @author      SerVB
 */
public final class Changeable<T> {

    /** Changeable value. */
    public T value;

    /**
     * Constructs the changeable object.
     *
     * @param value The initial value of the object.
     */
    public Changeable(final T value) {
        this.value = value;
    }

    /**
     * Sets the value to the object's value.
     *
     * @param newValue The object's value.
     */
    public void setObject(final Object newValue) {
        this.value = (T) newValue;
    }

    //<editor-fold defaultstate="collapsed" desc="hashCode & equals">
    @Override
    public int hashCode() {
        final Hash hash = Hash.std();
        hash.insert(value);
        return hash.getResult();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Changeable<?> other = (Changeable<?>) obj;
        return Objects.equals(this.value, other.value);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        if (value == null) {
            return "Changeable<N/A>{value=null}";
        }
        return String.format(
                "Changeable<%s>{value=%s}",
                value.getClass().getName(),
                value.toString()
        );
    }
    //</editor-fold>

}
