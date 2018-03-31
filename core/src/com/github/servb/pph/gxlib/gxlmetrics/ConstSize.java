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
package com.github.servb.pph.gxlib.gxlmetrics;

import org.checkerframework.checker.signedness.qual.Unsigned;

/**
 * Immutable Size class.
 *
 * @author SerVB
 */
public final class ConstSize implements IConstSize {

    /** Width. */
    private final @Unsigned int w;
    /** Height. */
    private final @Unsigned int h;

    /**
     * Constructs the object by metrics.
     *
     * @param w The width.
     * @param h The height.
     */
    public ConstSize(final @Unsigned int w, final @Unsigned int h) {
        this.w = w;
        this.h = h;
    }

    /**
     * Constructs the object by the other object.
     *
     * @param other The other size.
     */
    public ConstSize(final IConstSize other) {
        this.w = other.getW();
        this.h = other.getH();
    }

    @Override
    public @Unsigned int getW() {
        return w;
    }

    @Override
    public @Unsigned int getH() {
        return h;
    }

    @Override
    public ISize operatorP(final IConstSize other) {
        return new Size(w + other.getW(), h + other.getH());
    }

    @Override
    public ISize operatorP(final @Unsigned int other) {
        return new Size(w + other, h + other);
    }

    @Override
    public ISize operatorM(final @Unsigned int other) {
        return new Size(w - other, h - other);
    }

    @Override
    public boolean operatorEe(final IConstSize other) {
        return w == other.getW() && h == other.getH();
    }

    @Override
    public float GetAspectRatio() {
        return ((float) w) / ((float) h);
    }

    @Override
    public boolean IsZero() {
        return w == 0 && h == 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.w;
        hash = 53 * hash + this.h;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConstSize other = (ConstSize) obj;
        if (this.w != other.w) {
            return false;
        }
        return this.h == other.h;
    }

    @Override
    public String toString() {
        return "ConstSize{" + w + ", " + h + '}';
    }
}
