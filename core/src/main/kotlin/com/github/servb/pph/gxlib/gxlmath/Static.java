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
