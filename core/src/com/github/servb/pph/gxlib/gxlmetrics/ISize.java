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
 * Size interface.
 *
 * @author SerVB
 */
public interface ISize extends IConstSize {

    /**
     * Sets the width to the new value.
     *
     * @param value The new value.
     */
    public void setW(final @Unsigned int value);

    /**
     * Sets the height to the new value.
     *
     * @param value The new value.
     */
    public void setH(final @Unsigned int value);

    /**
     * Inflates the size by the value.
     *
     * @param value The value.
     * @deprecated  Use {@link #InflateSize(int)} instead.
     */
    public void operatorPe(final @Unsigned int value);

    /**
     * Deflates the size by the value.
     *
     * @param value The value.
     * @deprecated  Use {@link #DeflateSize(int)} instead.
     */
    public void operatorMe(final @Unsigned int value);

    /**
     * Inflates the size by the value.
     *
     * @param w_offs The width offset.
     * @param h_offs The height offset.
     */
    public void InflateSize(final @Unsigned int w_offs, final @Unsigned int h_offs);

    /**
     * Inflates the size by the value.
     *
     * @param offs The value.
     */
    public void InflateSize(final @Unsigned int offs);

    /**
     * Deflates the size by the value.
     *
     * @param w_offs The width offset.
     * @param h_offs The height offset.
     */
    public void DeflateSize(final @Unsigned int w_offs, final @Unsigned int h_offs);

    /**
     * Deflates the size by the value.
     *
     * @param offs The value.
     */
    public void DeflateSize(final @Unsigned int offs);

    /** Sets metrics to zero. */
    public void Zero();

}
