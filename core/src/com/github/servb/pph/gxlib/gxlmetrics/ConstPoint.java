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
 *
 * @author SerVB
 */
public final class ConstPoint implements IConstPoint {

    private final int x;
    private final int y;

    public ConstPoint() {
        this.x = 0;
        this.y = 0;
    }

    public ConstPoint(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public ConstPoint(final IConstPoint other) {
        this.x = other.getX();
        this.y = other.getX();
    }

    @Override
    public IPoint operatorP(final IConstPoint pos) {
        return new Point(x + pos.getX(), y + pos.getY());
    }

    @Override
    public IPoint operatorM(final IConstPoint pos) {
        return new Point(x - pos.getX(), y - pos.getY());
    }

    @Override
    public IPoint operatorP(final IConstSize siz) {
        return new Point(x + siz.getW(), y + siz.getH());
    }

    @Override
    public IPoint operatorM(final IConstSize siz) {
        return new Point(x - siz.getW(), y - siz.getH());
    }

    @Override
    public IPoint operatorP(int offs) {
        return new Point(x + offs, y + offs);
    }

    @Override
    public IPoint operatorM(int offs) {
        return new Point(x - offs, y - offs);
    }

    @Override
    public boolean operatorEe(final IConstPoint pos) {
        return x == pos.getX() && y == pos.getY();
    }

    @Override
    public @Unsigned int GetSqDelta(final IConstPoint pnt) {
        return Math.max(Math.abs(pnt.getX() - x), Math.abs(pnt.getY() - y));
    }

    @Override
    public @Unsigned int GetDelta(final IConstPoint pnt) {
        final int dx = pnt.getX() - x;
        final int dy = pnt.getY() - y;

        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.x;
        hash = 47 * hash + this.y;
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
        final ConstPoint other = (ConstPoint) obj;
        if (this.x != other.x) {
            return false;
        }
        return this.y == other.y;
    }

    @Override
    public String toString() {
        return "ConstPoint{" + x + ", " + y + '}';
    }
}
