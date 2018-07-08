package com.github.servb.pph.gxlib.gxlcommontpl;

import com.github.servb.pph.util.helpertype.Changeable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 *
 * @author SerVB
 */
public final class StaticTest {

    /**
     * Test of iSwap method, of class Static.
     */
    @Test
    public void testISwap() {
        System.out.println("iSwap");

        {
            final Changeable<Integer> a = new Changeable<Integer>(1);
            final Changeable<Integer> b = new Changeable<Integer>(2);
            Static.iSwap(a, b);
            assertEquals(2, (int) a.getValue());
            assertEquals(1, (int) b.getValue());
        }

        {
            final Changeable<Integer> a = new Changeable<Integer>(20);
            final Changeable<Integer> b = new Changeable<Integer>(20);
            Static.iSwap(a, b);
            assertEquals(20, (int) a.getValue());
            assertEquals(20, (int) b.getValue());
        }

        {
            final Changeable<Integer> a = new Changeable<Integer>(20);
            final Changeable<Integer> b = new Changeable<Integer>(null);
            Static.iSwap(a, b);
            assertEquals(null, a.getValue());
            assertEquals(20, (int) b.getValue());
        }
    }

    /**
     * Test of iCLAMP method, of class Static.
     */
    @Test
    public void testICLAMP_3args_1() {
        System.out.println("iCLAMP byte");

        final byte[][] generated = {
            {1, 2, 3, 2},
            {1, 1, 1, 1},
            {1, 1, 0, 1},
            {10, 30, 12, 12},
            {10, 30, 16, 16},
            {10, 15, 10, 10},
            {10, 15, 15, 15}
        };

        for (final byte[] test : generated) {
            final byte min = test[0];
            final byte max = test[1];
            final byte value = test[2];
            final byte expResult = test[3];
            final byte result = Static.iCLAMP(min, max, value);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of iCLAMP method, of class Static.
     */
    @Test
    public void testICLAMP_3args_2() {
        System.out.println("iCLAMP short");

        final short[][] generated = {
            {1, 2, 3, 2},
            {1, 1, 1, 1},
            {1, 1, 0, 1},
            {10, 30, 12, 12},
            {10, 30, 16, 16},
            {10, 15, 10, 10},
            {10, 15, 15, 15}
        };

        for (final short[] test : generated) {
            final short min = test[0];
            final short max = test[1];
            final short value = test[2];
            final short expResult = test[3];
            final short result = Static.iCLAMP(min, max, value);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of iCLAMP method, of class Static.
     */
    @Test
    public void testICLAMP_3args_3() {
        System.out.println("iCLAMP int");

        final int[][] generated = {
            {1, 2, 3, 2},
            {1, 1, 1, 1},
            {1, 1, 0, 1},
            {10, 30, 12, 12},
            {10, 30, 16, 16},
            {10, 15, 10, 10},
            {10, 15, 15, 15}
        };

        for (final int[] test : generated) {
            final int min = test[0];
            final int max = test[1];
            final int value = test[2];
            final int expResult = test[3];
            final int result = Static.iCLAMP(min, max, value);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of iCLAMP method, of class Static.
     */
    @Test
    public void testICLAMP_3args_4() {
        System.out.println("iCLAMP");

        final long[][] generated = {
            {1, 2, 3, 2},
            {1, 1, 1, 1},
            {1, 1, 0, 1},
            {10, 30, 12, 12},
            {10, 30, 16, 16},
            {10, 15, 10, 10},
            {10, 15, 15, 15}
        };

        for (final long[] test : generated) {
            final long min = test[0];
            final long max = test[1];
            final long value = test[2];
            final long expResult = test[3];
            final long result = Static.iCLAMP(min, max, value);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of iCLAMP method, of class Static.
     */
    @Test
    public void testICLAMP_3args_5() {
        System.out.println("iCLAMP float");

        final float[][] generated = {
            {1, 2, 3, 2},
            {1, 1, 1, 1},
            {1, 1, 0, 1},
            {10, 30, 12, 12},
            {10, 30, 16, 16},
            {10, 15, 10, 10},
            {10, 15, 15, 15}
        };

        for (final float[] test : generated) {
            final float min = test[0];
            final float max = test[1];
            final float value = test[2];
            final float expResult = test[3];
            final float result = Static.iCLAMP(min, max, value);
            assertEquals(expResult, result, 1e-6);
        }
    }

    /**
     * Test of iCLAMP method, of class Static.
     */
    @Test
    public void testICLAMP_3args_6() {
        System.out.println("iCLAMP double");

        final double[][] generated = {
            {1, 2, 3, 2},
            {1, 1, 1, 1},
            {1, 1, 0, 1},
            {10, 30, 12, 12},
            {10, 30, 16, 16},
            {10, 15, 10, 10},
            {10, 15, 15, 15}
        };

        for (final double[] test : generated) {
            final double min = test[0];
            final double max = test[1];
            final double value = test[2];
            final double expResult = test[3];
            final double result = Static.iCLAMP(min, max, value);
            assertEquals(expResult, result, 1e-6);
        }
    }

    /**
     * Test of iDIF method, of class Static.
     */
    @Test
    public void testIDIF_byte_byte() {
        System.out.println("iDIF byte");

        final byte[][] generated = {
            {2, 3, 1},
            {1, 1, 0},
            {1, 0, 1},
            {30, 12, 18},
            {30, 16, 14},
            {15, 10, 5},
            {15, 15, 0}
        };

        for (final byte[] test : generated) {
            final byte x = test[0];
            final byte y = test[1];
            final byte expResult = test[2];
            final byte result = Static.iDIF(x, y);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of iDIF method, of class Static.
     */
    @Test
    public void testIDIF_short_short() {
        System.out.println("iDIF short");

        final short[][] generated = {
            {2, 3, 1},
            {1, 1, 0},
            {1, 0, 1},
            {30, 12, 18},
            {30, 16, 14},
            {15, 10, 5},
            {15, 15, 0}
        };

        for (final short[] test : generated) {
            final short x = test[0];
            final short y = test[1];
            final short expResult = test[2];
            final short result = Static.iDIF(x, y);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of iDIF method, of class Static.
     */
    @Test
    public void testIDIF_int_int() {
        System.out.println("iDIF int");

        final int[][] generated = {
            {2, 3, 1},
            {1, 1, 0},
            {1, 0, 1},
            {30, 12, 18},
            {30, 16, 14},
            {15, 10, 5},
            {15, 15, 0}
        };

        for (final int[] test : generated) {
            final int x = test[0];
            final int y = test[1];
            final int expResult = test[2];
            final int result = Static.iDIF(x, y);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of iDIF method, of class Static.
     */
    @Test
    public void testIDIF_long_long() {
        System.out.println("iDIF long");

        final long[][] generated = {
            {2, 3, 1},
            {1, 1, 0},
            {1, 0, 1},
            {30, 12, 18},
            {30, 16, 14},
            {15, 10, 5},
            {15, 15, 0}
        };

        for (final long[] test : generated) {
            final long x = test[0];
            final long y = test[1];
            final long expResult = test[2];
            final long result = Static.iDIF(x, y);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of iDIF method, of class Static.
     */
    @Test
    public void testIDIF_float_float() {
        System.out.println("iDIF float");

        final float[][] generated = {
            {2, 3, 1},
            {1, 1, 0},
            {1, 0, 1},
            {30, 12, 18},
            {30, 16, 14},
            {15, 10, 5},
            {15, 15, 0}
        };

        for (final float[] test : generated) {
            final float x = test[0];
            final float y = test[1];
            final float expResult = test[2];
            final float result = Static.iDIF(x, y);
            assertEquals(expResult, result, 1e-6);
        }
    }

    /**
     * Test of iDIF method, of class Static.
     */
    @Test
    public void testIDIF_double_double() {
        System.out.println("iDIF double");

        final double[][] generated = {
            {2, 3, 1},
            {1, 1, 0},
            {1, 0, 1},
            {30, 12, 18},
            {30, 16, 14},
            {15, 10, 5},
            {15, 15, 0}
        };

        for (final double[] test : generated) {
            final double x = test[0];
            final double y = test[1];
            final double expResult = test[2];
            final double result = Static.iDIF(x, y);
            assertEquals(expResult, result, 1e-6);
        }
    }

    /**
     * Test of iALIGN method, of class Static.
     */
    @Test
    public void testIALIGN() {
        System.out.println("iALIGN");

        final int[][] generated = {
            {2, 3, 3},
            {1, 1, 1},
            {1, 10, 10},
            {30, 12, 36},
            {30, 16, 32},
            {15, 10, 20},
            {15, 15, 15}
        };

        for (final int[] test : generated) {
            final int val = test[0];
            final int al = test[1];
            final int expResult = test[2];
            final int result = Static.iALIGN(val, al);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of iWRAP method, of class Static.
     */
    @Test
    public void testIWRAP() {
        System.out.println("iWRAP");

        final short[][] generated = {
            {2, 3, 5, 4},
            {1, 1, 2, 1},
            {1, 10, 34, 25},
            {30, 12, 16, 26},
            {30, 16, 32, 30},
            {15, 10, 12, 13},
            {15, 15, 16, 15}
        };

        for (final short[] test : generated) {
            final short val = test[0];
            final short minv = test[1];
            final short wrap = test[2];
            final short expResult = test[3];
            final short result = Static.iWRAP(val, minv, wrap);
            assertEquals(expResult, result);
        }
    }

}
