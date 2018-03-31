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
package com.github.servb.pph.gxlib.gxlmath;

/**
 * {@code gxl.math.h} & {@code gxl.math.cpp}.
 *
 * @author SerVB
 */
public final class Static {

    /** Prevents from creating an instance of the class. */
    private Static() {}

    public static final float PI = 3.1415926535897932384626433832795f;

    public static int int_sqrt(int n) {
        int root = 0, tval;

        for (int i = 15; i >= 0; --i) {
            tval = root + (1 << i);

            if (n >= tval << i) {
                n -= tval << i;
                root |= 2 << i;
            }
        }

        return root >> 1;
    }
}
