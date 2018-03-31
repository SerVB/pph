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
package com.github.servb.gxlib.gxlmath;

import com.github.servb.util.helpertype.ConstArray;

/**
 * /gxlib/gxl.math.h and /gxlib/gxl.math.cpp
 *
 * @author SerVB
 */
public final class Static {

    /**
     * @deprecated Not used in source project... TODO: Delete.
     */
    public static final ConstArray<Float> SIN_TABLE_FLT;

    /**
     * @deprecated Not used in source project... TODO: Delete.
     */
    public static final ConstArray<Float> COS_TABLE_FLT;

    /**
     * @deprecated Not used in source project... TODO: Delete.
     */
    public static final ConstArray<fix32> SIN_TABLE_FIX;

    /**
     * @deprecated Not used in source project... TODO: Delete.
     */
    public static final ConstArray<fix32> COS_TABLE_FIX;

    static {
        final Float[] sinTableFlt = new Float[256];
        final Float[] cosTableFlt = new Float[256];
        final fix32[] sinTableFix = new fix32[256];
        final fix32[] cosTableFix = new fix32[256];

        final float pi2 = PI * 2.0f;
        final float pi05 = PI / 2.0f;
        for (int xx = 0; xx < 256; ++xx) {
            final float fxx = (float) xx;
            final float sinValue = (float) Math.sin((fxx / 256.0f * pi2) - pi05);
            final float cosValue = (float) Math.cos((fxx / 256.0f * pi2) - pi05);

            sinTableFlt[xx] = sinValue;
            cosTableFlt[xx] = cosValue;
            sinTableFix[xx] = sinValue;
            cosTableFix[xx] = cosValue;
        }

        SIN_TABLE_FLT = new ConstArray<Float>(Float.class, sinTableFlt);
        COS_TABLE_FLT = new ConstArray<Float>(Float.class, cosTableFlt);
        SIN_TABLE_FIX = new ConstArray<fix32>(fix32.class, sinTableFix);
        COS_TABLE_FIX = new ConstArray<fix32>(fix32.class, cosTableFix);
    }
}
