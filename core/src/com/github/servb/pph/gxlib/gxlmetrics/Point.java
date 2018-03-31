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
public final class Point implements IPoint {

    private int x;
    private int y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Point(final IConstPoint other) {
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
    public void setX(final int value) {
        x = value;
    }

    @Override
    public void setY(final int value) {
        y = value;
    }

    @Override
    public void operatorPe(final IConstPoint pos) {
        x += pos.getX();
        y += pos.getY();
    }

    @Override
    public void operatorMe(final IConstPoint pos) {
        x -= pos.getX();
        y -= pos.getY();
    }

    @Override
    public void operatorPe(final IConstSize siz) {
        x += siz.getW();
        y += siz.getH();
    }

    @Override
    public void operatorMe(final IConstSize siz) {
        x -= siz.getW();
        y -= siz.getH();
    }

    @Override
    public void operatorPe(final int offs) {
        x += offs;
        y += offs;
    }

    @Override
    public void operatorMe(final int offs) {
        x -= offs;
        y -= offs;
    }

    @Override
    public void MoveX(final int offset_x) {
        x += offset_x;
    }

    @Override
    public void MoveY(final int offset_y) {
        y += offset_y;
    }

    @Override
    public void Move(final int offset_x, final int offset_y) {
        x += offset_x;
        y += offset_y;
    }
}
