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

/**
 * Point interface.
 *
 * @author SerVB
 */
public interface IPoint extends IConstPoint {

    public void setX(final int value);
    public void setY(final int value);

    public void operatorPe(final IConstPoint pos);
    public void operatorMe(final IConstPoint pos);
    public void operatorPe(final IConstSize siz);
    public void operatorMe(final IConstSize siz);
    public void operatorPe(final int offs);
    public void operatorMe(final int offs);
    public void MoveX(final int offset_x);
    public void MoveY(final int offset_y);
    public void Move(final int offset_x, final int offset_y);
}
