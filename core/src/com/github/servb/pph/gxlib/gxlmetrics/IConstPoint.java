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
 * Immutable Point interface. Contains only constant functions.
 *
 * @author SerVB
 */
public interface IConstPoint {

    public int getX();
    public int getY();

    public IPoint operatorP(final IConstPoint pos);
    public IPoint operatorM(final IConstPoint pos);
    public IPoint operatorP(final IConstSize siz);
    public IPoint operatorM(final IConstSize siz);
    public IPoint operatorP(final int offs);
    public IPoint operatorM(final int offs);
    public boolean operatorEe(final IConstPoint pos);
    public @Unsigned int GetSqDelta(final IConstPoint pnt);
    public @Unsigned int GetDelta(final IConstPoint pnt);
}
