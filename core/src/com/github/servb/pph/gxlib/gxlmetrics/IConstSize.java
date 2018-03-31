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
 * Immutable Size interface. Contains only constant functions.
 *
 * @author SerVB
 */
public interface IConstSize {

    /**
     * Returns the width.
     *
     * @return The width.
     */
    public @Unsigned int getW();

    /**
     * Returns the height.
     *
     * @return The height.
     */
    public @Unsigned int getH();

    /**
     * Returns the sum of this and other sizes.
     *
     * @param other The other size.
     * @return The sum.
     */
    public ISize operatorP(final IConstSize other);

    /**
     * Returns the sum of this size and {other, other} size.
     *
     * @param other The other metric.
     * @return The sum.
     */
    public ISize operatorP(final @Unsigned int other);

    /**
     * Returns the distraction of this size and {other, other} size.
     *
     * @param other The other metric.
     * @return The distraction.
     */
    public ISize operatorM(final @Unsigned int other);

    /**
     * Checks if this size is equal to the other.
     *
     * @param other The other size.
     * @return The result of the check.
     */
    public boolean operatorEe(final IConstSize other);

    /**
     * Returns the aspect ratio width/height.
     *
     * @return The aspect ratio.
     */
    public float GetAspectRatio();

    /**
     * Checks if the size is equal to zero.
     *
     * @return The result of the check.
     */
    public boolean IsZero();

}
