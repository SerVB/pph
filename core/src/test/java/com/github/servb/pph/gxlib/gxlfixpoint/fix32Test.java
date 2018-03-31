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
package test.java.com.github.servb.pph.gxlib.gxlfixpoint;

import com.github.servb.pph.gxlib.gxlfixpoint.fix32;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * {@code com.github.servb.gxlib.gxlfixpoint.fix32} test class.
 *
 * @author SerVB
 */
public class fix32Test {

    /**
     * Test of closeValues method, of class fix32.
     */
    @Test
    public void testCloseValues() {
        System.out.println("closeValues");

        {
            final fix32 a = new fix32();
            final fix32 b = new fix32();
            assertTrue(fix32.closeValues(a, b));
        }

        {
            final fix32 a = new fix32(0.0f);
            final fix32 b = new fix32(-0.0f);
            assertTrue(fix32.closeValues(a, b));
        }

        {
            final fix32 a = new fix32(0.0f);
            final fix32 b = new fix32(0.0f + fix32.PRECISION / 10);
            assertTrue(fix32.closeValues(a, b));
        }

        {
            final fix32 a = new fix32(1.2f);
            final fix32 b = new fix32(1.2f + fix32.PRECISION / 10);
            assertTrue(fix32.closeValues(a, b));
        }

        {
            final fix32 a = new fix32(1.2f);
            final fix32 b = new fix32(1.2f + 2 * fix32.PRECISION);
            assertFalse(fix32.closeValues(a, b));
        }

        {
            final fix32 a = new fix32(0.0f);
            final fix32 b = new fix32(0.4f);
            assertFalse(fix32.closeValues(a, b));
        }

        {
            final fix32 a = new fix32(0.0f);
            final fix32 b = new fix32(-10.0f);
            assertFalse(fix32.closeValues(a, b));
        }

        {
            final fix32 a = new fix32(1);
            final fix32 b = new fix32(2);
            assertFalse(fix32.closeValues(a, b));
        }
    }

    /**
     * Test of zero method, of class fix32.
     */
    @Test
    public void testZero() {
        System.out.println("zero");

        {
            final fix32 expResult = new fix32(0.0f);
            final fix32 result = fix32.zero();
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 expResult = new fix32(0);
            final fix32 result = fix32.zero();
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 expResult = new fix32();
            final fix32 result = fix32.zero();
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 expResult = new fix32(1.0f);
            final fix32 result = fix32.zero();
            assertFalse(fix32.closeValues(expResult, result));
        }

        {
            final fix32 expResult = new fix32(0.1f);
            final fix32 result = fix32.zero();
            assertFalse(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of operatorE method, of class fix32.
     */
    @Test
    public void testOperatorE_fix32() {
        System.out.println("operatorE");

        {
            final fix32 rhs = new fix32();
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorE(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(5);
            final fix32 instance = new fix32(2);
            final fix32 expResult = new fix32(5);
            final fix32 result = instance.operatorE(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorE method, of class fix32.
     */
    @Test
    public void testOperatorE_int() {
        System.out.println("operatorE");

        {
            final int rhs = 0;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorE(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = 6;
            final fix32 instance = new fix32(3);
            final fix32 expResult = new fix32(6);
            final fix32 result = instance.operatorE(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorE method, of class fix32.
     */
    @Test
    public void testOperatorE_float() {
        System.out.println("operatorE");

        {
            final float f = 0.0f;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorE(f);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final float f = -0.1f;
            final fix32 instance = new fix32(42);
            final fix32 expResult = new fix32(-0.1f);
            final fix32 result = instance.operatorE(f);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorE method, of class fix32.
     */
    @Test
    public void testOperatorE_fix32fp() {
        System.out.println("operatorE");

        {
            final fix32.fp v = new fix32.fp(0);
            final fix32 instance = new fix32(56);
            final fix32 expResult = new fix32(0);
            final fix32 result = instance.operatorE(v);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32.fp v = new fix32.fp(0x38000);
            final fix32 instance = new fix32(1);
            final fix32 expResult = new fix32(3.5f);
            final fix32 result = instance.operatorE(v);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of set method, of class fix32.
     */
    @Test
    public void testSet_int() {
        System.out.println("set");

        {
            final int i = 0;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32(0);
            instance.set(i);
            assertTrue(fix32.closeValues(expResult, instance));
        }

        {
            final int i = -7;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32(-7);
            instance.set(i);
            assertTrue(fix32.closeValues(expResult, instance));
        }

        {
            final int i = 1;
            final fix32 instance = new fix32(42);
            final fix32 expResult = new fix32(1);
            instance.set(i);
            assertTrue(fix32.closeValues(expResult, instance));
        }
    }

    /**
     * Test of set method, of class fix32.
     */
    @Test
    public void testSet_float() {
        System.out.println("set");

        {
            final float f = 0.0f;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32(0);
            instance.set(f);
            assertTrue(fix32.closeValues(expResult, instance));
        }

        {
            final float f = -1.0f;
            final fix32 instance = new fix32(5);
            final fix32 expResult = new fix32(-1);
            instance.set(f);
            assertTrue(fix32.closeValues(expResult, instance));
        }

        {
            final float f = -1.66f;
            final fix32 instance = new fix32(5.2f);
            final fix32 expResult = new fix32(-1.66f);
            instance.set(f);
            assertTrue(fix32.closeValues(expResult, instance));
        }
    }

    /**
     * Test of to_float method, of class fix32.
     */
    @Test
    public void testTo_float() {
        System.out.println("to_float");

        {
            final fix32 instance = new fix32();
            final float expResult = 0.0f;
            final float result = instance.to_float();
            assertEquals(expResult, result, fix32.PRECISION);
        }

        {
            final fix32 instance = new fix32(5.4f);
            final float expResult = 5.4f;
            final float result = instance.to_float();
            assertEquals(expResult, result, fix32.PRECISION);
        }

        {
            final fix32 instance = new fix32(-5.45f);
            final float expResult = -5.45f;
            final float result = instance.to_float();
            assertEquals(expResult, result, fix32.PRECISION);
        }
    }

    /**
     * Test of operatorM method, of class fix32.
     */
    @Test
    public void testOperatorM_0args() {
        System.out.println("operatorM");

        {
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorM();
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 instance = new fix32(1);
            final fix32 expResult = new fix32(-1);
            final fix32 result = instance.operatorM();
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 instance = new fix32(42.42f);
            final fix32 expResult = new fix32(-42.42f);
            final fix32 result = instance.operatorM();
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 instance = new fix32(-42.f);
            final fix32 expResult = new fix32(42.f);
            final fix32 result = instance.operatorM();
            assertTrue(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of operatorPe method, of class fix32.
     */
    @Test
    public void testOperatorPe_fix32() {
        System.out.println("operatorPe");

        {
            final fix32 rhs = new fix32();
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorPe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(4);
            final fix32 instance = new fix32(3);
            final fix32 expResult = new fix32(7);
            final fix32 result = instance.operatorPe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(4);
            final fix32 instance = new fix32(-3);
            final fix32 expResult = new fix32(1);
            final fix32 result = instance.operatorPe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorMe method, of class fix32.
     */
    @Test
    public void testOperatorMe_fix32() {
        System.out.println("operatorMe");

        {
            final fix32 rhs = new fix32();
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorMe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(5);
            final fix32 instance = new fix32(6);
            final fix32 expResult = new fix32(1);
            final fix32 result = instance.operatorMe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(5);
            final fix32 instance = new fix32(0.1f);
            final fix32 expResult = new fix32(-4.9f);
            final fix32 result = instance.operatorMe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorAe method, of class fix32.
     */
    @Test
    public void testOperatorAe_fix32() {
        System.out.println("operatorAe");

        {
            final fix32 rhs = new fix32();
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorAe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(6);
            final fix32 instance = new fix32(1.1f);
            final fix32 expResult = new fix32(6.6f);
            final fix32 result = instance.operatorAe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(6);
            final fix32 instance = new fix32(-1.1f);
            final fix32 expResult = new fix32(-6.6f);
            final fix32 result = instance.operatorAe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(-6);
            final fix32 instance = new fix32(-1.1f);
            final fix32 expResult = new fix32(6.6f);
            final fix32 result = instance.operatorAe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorSe method, of class fix32.
     */
    @Test
    public void testOperatorSe_fix32() {
        System.out.println("operatorSe");

        {
            final fix32 rhs = new fix32(1);
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorSe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(1);
            final fix32 instance = new fix32(2);
            final fix32 expResult = new fix32(2);
            final fix32 result = instance.operatorSe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(10);
            final fix32 instance = new fix32(2);
            final fix32 expResult = new fix32(0.2f);
            final fix32 result = instance.operatorSe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final fix32 rhs = new fix32(-10);
            final fix32 instance = new fix32(2);
            final fix32 expResult = new fix32(-0.2f);
            final fix32 result = instance.operatorSe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorPe method, of class fix32.
     */
    @Test
    public void testOperatorPe_int() {
        System.out.println("operatorPe");

        {
            final int rhs = 0;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorPe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = 10;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32(10);
            final fix32 result = instance.operatorPe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = -10;
            final fix32 instance = new fix32(6.6f);
            final fix32 expResult = new fix32(-3.4f);
            final fix32 result = instance.operatorPe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorMe method, of class fix32.
     */
    @Test
    public void testOperatorMe_int() {
        System.out.println("operatorMe");

        {
            final int rhs = 0;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorMe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = 5;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32(-5);
            final fix32 result = instance.operatorMe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = 42;
            final fix32 instance = new fix32(42.1f);
            final fix32 expResult = new fix32(0.1f);
            final fix32 result = instance.operatorMe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorAe method, of class fix32.
     */
    @Test
    public void testOperatorAe_int() {
        System.out.println("operatorAe");

        {
            final int rhs = 0;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorAe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = 0;
            final fix32 instance = new fix32(6);
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorAe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = -1;
            final fix32 instance = new fix32(6);
            final fix32 expResult = new fix32(-6);
            final fix32 result = instance.operatorAe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = 5;
            final fix32 instance = new fix32(2);
            final fix32 expResult = new fix32(10);
            final fix32 result = instance.operatorAe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorSe method, of class fix32.
     */
    @Test
    public void testOperatorSe_int() {
        System.out.println("operatorSe");

        {
            final int rhs = 1;
            final fix32 instance = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = instance.operatorSe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = 10;
            final fix32 instance = new fix32(1);
            final fix32 expResult = new fix32(0.1f);
            final fix32 result = instance.operatorSe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }

        {
            final int rhs = -5;
            final fix32 instance = new fix32(10);
            final fix32 expResult = new fix32(-2);
            final fix32 result = instance.operatorSe(rhs);
            assertTrue(fix32.closeValues(expResult, result));
            assertTrue(result == instance);
        }
    }

    /**
     * Test of operatorP method, of class fix32.
     */
    @Test
    public void testOperatorP() {
        System.out.println("operatorP");

        {
            final fix32 lhs = new fix32();
            final fix32 rhs = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorP(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(5);
            final fix32 rhs = new fix32(6);
            final fix32 expResult = new fix32(11);
            final fix32 result = fix32.operatorP(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(-5.5f);
            final fix32 rhs = new fix32(6);
            final fix32 expResult = new fix32(0.5f);
            final fix32 result = fix32.operatorP(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of operatorM method, of class fix32.
     */
    @Test
    public void testOperatorM_fix32_fix32() {
        System.out.println("operatorM");

        {
            final fix32 lhs = new fix32();
            final fix32 rhs = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorM(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(5);
            final fix32 rhs = new fix32(4);
            final fix32 expResult = new fix32(1);
            final fix32 result = fix32.operatorM(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(-5);
            final fix32 rhs = new fix32(4);
            final fix32 expResult = new fix32(-9);
            final fix32 result = fix32.operatorM(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of operatorA method, of class fix32.
     */
    @Test
    public void testOperatorA_fix32_fix32() {
        System.out.println("operatorA");

        {
            final fix32 lhs = new fix32();
            final fix32 rhs = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(5);
            final fix32 rhs = new fix32(4);
            final fix32 expResult = new fix32(20);
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(-5.5f);
            final fix32 rhs = new fix32(4);
            final fix32 expResult = new fix32(-22);
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of operatorS method, of class fix32.
     */
    @Test
    public void testOperatorS_fix32_fix32() {
        System.out.println("operatorS");

        {
            final fix32 lhs = new fix32();
            final fix32 rhs = new fix32(2);
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(5);
            final fix32 rhs = new fix32(2);
            final fix32 expResult = new fix32(2.5f);
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(5);
            final fix32 rhs = new fix32(1);
            final fix32 expResult = new fix32(5);
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(5);
            final fix32 rhs = new fix32(-1);
            final fix32 expResult = new fix32(-5);
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of operatorA method, of class fix32.
     */
    @Test
    public void testOperatorA_fix32_int() {
        System.out.println("operatorA");

        {
            final fix32 lhs = new fix32();
            final int rhs = 0;
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(6);
            final int rhs = 0;
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(6);
            final int rhs = 8;
            final fix32 expResult = new fix32(48);
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(6);
            final int rhs = -10;
            final fix32 expResult = new fix32(-60);
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of operatorA method, of class fix32.
     */
    @Test
    public void testOperatorA_int_fix32() {
        System.out.println("operatorA");

        {
            final int lhs = 0;
            final fix32 rhs = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final int lhs = 3;
            final fix32 rhs = new fix32();
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final int lhs = 0;
            final fix32 rhs = new fix32(7);
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final int lhs = 4;
            final fix32 rhs = new fix32(1);
            final fix32 expResult = new fix32(4);
            final fix32 result = fix32.operatorA(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of operatorS method, of class fix32.
     */
    @Test
    public void testOperatorS_fix32_int() {
        System.out.println("operatorS");

        {
            final fix32 lhs = new fix32();
            final int rhs = 1;
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(6);
            final int rhs = 2;
            final fix32 expResult = new fix32(3);
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final fix32 lhs = new fix32(12);
            final int rhs = -3;
            final fix32 expResult = new fix32(-4);
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of operatorS method, of class fix32.
     */
    @Test
    public void testOperatorS_int_fix32() {
        System.out.println("operatorS");

        {
            final int lhs = 0;
            final fix32 rhs = new fix32(5);
            final fix32 expResult = new fix32();
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final int lhs = 1;
            final fix32 rhs = new fix32(5);
            final fix32 expResult = new fix32(0.2f);
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }

        {
            final int lhs = 4;
            final fix32 rhs = new fix32(-5);
            final fix32 expResult = new fix32(-0.8f);
            final fix32 result = fix32.operatorS(lhs, rhs);
            assertTrue(fix32.closeValues(expResult, result));
        }
    }

    /**
     * Test of floor method, of class fix32.
     */
    @Test
    public void testFloor() {
        System.out.println("floor");

        {
            final fix32 instance = new fix32();
            final int expResult = 0;
            final int result = instance.floor();
            assertEquals(expResult, result);
        }

        {
            final fix32 instance = new fix32(1.1f);
            final int expResult = 1;
            final int result = instance.floor();
            assertEquals(expResult, result);
        }

        {
            final fix32 instance = new fix32(1.97f);
            final int expResult = 1;
            final int result = instance.floor();
            assertEquals(expResult, result);
        }

        {
            final fix32 instance = new fix32(-0.2f);
            final int expResult = -1;
            final int result = instance.floor();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of ceil method, of class fix32.
     */
    @Test
    public void testCeil() {
        System.out.println("ceil");

        {
            final fix32 instance = new fix32();
            final int expResult = 0;
            final int result = instance.ceil();
            assertEquals(expResult, result);
        }

        {
            final fix32 instance = new fix32(-0.8f);
            final int expResult = 0;
            final int result = instance.ceil();
            assertEquals(expResult, result);
        }

        {
            final fix32 instance = new fix32(2.8f);
            final int expResult = 3;
            final int result = instance.ceil();
            assertEquals(expResult, result);
        }

        {
            final fix32 instance = new fix32(2.4f);
            final int expResult = 3;
            final int result = instance.ceil();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorEe method, of class fix32.
     */
    @Test
    public void testOperatorEe_fix32_fix32() {
        System.out.println("operatorEe");

        {
            final fix32 a = new fix32();
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorEe(a, b);
            final boolean result2 = fix32.operatorEe(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult, result2);
        }

        {
            final fix32 a = new fix32(5.2f);
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorEe(a, b);
            final boolean result2 = fix32.operatorEe(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult, result2);
        }

        {
            final fix32 a = new fix32(0);
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorEe(a, b);
            final boolean result2 = fix32.operatorEe(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult, result2);
        }

        {
            final fix32 a = new fix32(0);
            final fix32 b = new fix32(0.0f);
            final boolean expResult = true;
            final boolean result = fix32.operatorEe(a, b);
            final boolean result2 = fix32.operatorEe(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult, result2);
        }
    }

    /**
     * Test of operatorNee method, of class fix32.
     */
    @Test
    public void testOperatorNee_fix32_fix32() {
        System.out.println("operatorNee");

        {
            final fix32 a = new fix32();
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorNee(a, b);
            final boolean result2 = fix32.operatorNee(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult, result2);
        }

        {
            final fix32 a = new fix32(9);
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorNee(a, b);
            final boolean result2 = fix32.operatorNee(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult, result2);
        }

        {
            final fix32 a = new fix32(9);
            final fix32 b = new fix32(9.2f);
            final boolean expResult = true;
            final boolean result = fix32.operatorNee(a, b);
            final boolean result2 = fix32.operatorNee(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult, result2);
        }

        {
            final fix32 a = new fix32(9);
            final fix32 b = new fix32(9.0f);
            final boolean expResult = false;
            final boolean result = fix32.operatorNee(a, b);
            final boolean result2 = fix32.operatorNee(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult, result2);
        }
    }

    /**
     * Test of operatorG method, of class fix32.
     */
    @Test
    public void testOperatorG_fix32_fix32() {
        System.out.println("operatorG");

        {
            final fix32 a = new fix32();
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorG(a, b);
            final boolean expResult2 = false;
            final boolean result2 = fix32.operatorG(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }

        {
            final fix32 a = new fix32(-1);
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorG(a, b);
            final boolean expResult2 = true;
            final boolean result2 = fix32.operatorG(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }
    }

    /**
     * Test of operatorL method, of class fix32.
     */
    @Test
    public void testOperatorL_fix32_fix32() {
        System.out.println("operatorL");

        {
            final fix32 a = new fix32();
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorL(a, b);
            final boolean expResult2 = false;
            final boolean result2 = fix32.operatorL(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }

        {
            final fix32 a = new fix32(6);
            final fix32 b = new fix32(4);
            final boolean expResult = false;
            final boolean result = fix32.operatorL(a, b);
            final boolean expResult2 = true;
            final boolean result2 = fix32.operatorL(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }
    }

    /**
     * Test of operatorGe method, of class fix32.
     */
    @Test
    public void testOperatorGe_fix32_fix32() {
        System.out.println("operatorGe");

        {
            final fix32 a = new fix32();
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorGe(a, b);
            final boolean expResult2 = true;
            final boolean result2 = fix32.operatorGe(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }

        {
            final fix32 a = new fix32(5);
            final fix32 b = new fix32(6);
            final boolean expResult = false;
            final boolean result = fix32.operatorGe(a, b);
            final boolean expResult2 = true;
            final boolean result2 = fix32.operatorGe(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }
    }

    /**
     * Test of operatorLe method, of class fix32.
     */
    @Test
    public void testOperatorLe_fix32_fix32() {
        System.out.println("operatorLe");

        {
            final fix32 a = new fix32();
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorLe(a, b);
            final boolean expResult2 = true;
            final boolean result2 = fix32.operatorLe(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }

        {
            final fix32 a = new fix32(4);
            final fix32 b = new fix32(8);
            final boolean expResult = true;
            final boolean result = fix32.operatorLe(a, b);
            final boolean expResult2 = false;
            final boolean result2 = fix32.operatorLe(b, a);
            assertEquals(expResult, result);
            assertEquals(expResult2, result2);
        }
    }

    /**
     * Test of operatorEe method, of class fix32.
     */
    @Test
    public void testOperatorEe_fix32_int() {
        System.out.println("operatorEe");

        {
            final fix32 a = new fix32();
            final int b = 0;
            final boolean expResult = true;
            final boolean result = fix32.operatorEe(a, b);
            assertEquals(expResult, result);
        }

        {
            final fix32 a = new fix32(5);
            final int b = 4;
            final boolean expResult = false;
            final boolean result = fix32.operatorEe(a, b);
            assertEquals(expResult, result);
        }

        {
            final fix32 a = new fix32(5.0f);
            final int b = 5;
            final boolean expResult = true;
            final boolean result = fix32.operatorEe(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorNee method, of class fix32.
     */
    @Test
    public void testOperatorNee_fix32_int() {
        System.out.println("operatorNee");

        {
            final fix32 a = new fix32();
            final int b = 0;
            final boolean expResult = false;
            final boolean result = fix32.operatorNee(a, b);
            assertEquals(expResult, result);
        }

        {
            final fix32 a = new fix32();
            final int b = 10;
            final boolean expResult = true;
            final boolean result = fix32.operatorNee(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorG method, of class fix32.
     */
    @Test
    public void testOperatorG_fix32_int() {
        System.out.println("operatorG");

        {
            final fix32 a = new fix32();
            final int b = 0;
            final boolean expResult = false;
            final boolean result = fix32.operatorG(a, b);
            assertEquals(expResult, result);
        }

        {
            final fix32 a = new fix32();
            final int b = 5;
            final boolean expResult = false;
            final boolean result = fix32.operatorG(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorL method, of class fix32.
     */
    @Test
    public void testOperatorL_fix32_int() {
        System.out.println("operatorL");

        {
            final fix32 a = new fix32();
            final int b = 0;
            final boolean expResult = false;
            final boolean result = fix32.operatorL(a, b);
            assertEquals(expResult, result);
        }

        {
            final fix32 a = new fix32();
            final int b = -60;
            final boolean expResult = false;
            final boolean result = fix32.operatorL(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorGe method, of class fix32.
     */
    @Test
    public void testOperatorGe_fix32_int() {
        System.out.println("operatorGe");

        {
            final fix32 a = new fix32();
            final int b = 0;
            final boolean expResult = true;
            final boolean result = fix32.operatorGe(a, b);
            assertEquals(expResult, result);
        }

        {
            final fix32 a = new fix32();
            final int b = 11;
            final boolean expResult = false;
            final boolean result = fix32.operatorGe(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorLe method, of class fix32.
     */
    @Test
    public void testOperatorLe_fix32_int() {
        System.out.println("operatorLe");

        {
            final fix32 a = new fix32();
            final int b = 0;
            final boolean expResult = true;
            final boolean result = fix32.operatorLe(a, b);
            assertEquals(expResult, result);
        }

        {
            final fix32 a = new fix32();
            final int b = 8;
            final boolean expResult = true;
            final boolean result = fix32.operatorLe(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorEe method, of class fix32.
     */
    @Test
    public void testOperatorEe_int_fix32() {
        System.out.println("operatorEe");

        {
            final int a = 0;
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorEe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 7;
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorEe(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorNee method, of class fix32.
     */
    @Test
    public void testOperatorNee_int_fix32() {
        System.out.println("operatorNee");

        {
            final int a = 0;
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorNee(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 4;
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorNee(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorG method, of class fix32.
     */
    @Test
    public void testOperatorG_int_fix32() {
        System.out.println("operatorG");

        {
            final int a = 0;
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorG(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 6;
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorG(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorL method, of class fix32.
     */
    @Test
    public void testOperatorL_int_fix32() {
        System.out.println("operatorL");

        {
            final int a = 0;
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorL(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 15;
            final fix32 b = new fix32();
            final boolean expResult = false;
            final boolean result = fix32.operatorL(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorGe method, of class fix32.
     */
    @Test
    public void testOperatorGe_int_fix32() {
        System.out.println("operatorGe");

        {
            final int a = 0;
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorGe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 10;
            final fix32 b = new fix32(10);
            final boolean expResult = true;
            final boolean result = fix32.operatorGe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 0;
            final fix32 b = new fix32(6);
            final boolean expResult = false;
            final boolean result = fix32.operatorGe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = -7;
            final fix32 b = new fix32(4);
            final boolean expResult = false;
            final boolean result = fix32.operatorGe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 6;
            final fix32 b = new fix32(4);
            final boolean expResult = true;
            final boolean result = fix32.operatorGe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 16;
            final fix32 b = new fix32(-4);
            final boolean expResult = true;
            final boolean result = fix32.operatorGe(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of operatorLe method, of class fix32.
     */
    @Test
    public void testOperatorLe_int_fix32() {
        System.out.println("operatorLe");

        {
            final int a = 0;
            final fix32 b = new fix32();
            final boolean expResult = true;
            final boolean result = fix32.operatorLe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = -5;
            final fix32 b = new fix32(-5);
            final boolean expResult = true;
            final boolean result = fix32.operatorLe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = -8;
            final fix32 b = new fix32(-5);
            final boolean expResult = true;
            final boolean result = fix32.operatorLe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = -18;
            final fix32 b = new fix32(7);
            final boolean expResult = true;
            final boolean result = fix32.operatorLe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 8;
            final fix32 b = new fix32(7);
            final boolean expResult = false;
            final boolean result = fix32.operatorLe(a, b);
            assertEquals(expResult, result);
        }

        {
            final int a = 38;
            final fix32 b = new fix32(-5);
            final boolean expResult = false;
            final boolean result = fix32.operatorLe(a, b);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of hashCode method, of class fix32.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");

        {
            final fix32 instance1 = new fix32();
            final fix32 instance2 = new fix32();
            final int result1 = instance1.hashCode();
            final int result2 = instance2.hashCode();
            assertEquals(result1, result2);
        }

        {
            final fix32 instance1 = new fix32(5.5f);
            final fix32 instance2 = new fix32(5.5f);
            final int result1 = instance1.hashCode();
            final int result2 = instance2.hashCode();
            assertEquals(result1, result2);
        }
    }

    /**
     * Test of equals method, of class fix32.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");

        {
            final Object obj = new fix32();
            final fix32 instance = new fix32();
            final boolean expResult = true;
            final boolean result = instance.equals(obj);
            assertEquals(expResult, result);
        }

        {
            final Object obj = new fix32(6.4f);
            final fix32 instance = new fix32(6.4f);
            final boolean expResult = true;
            final boolean result = instance.equals(obj);
            assertEquals(expResult, result);
        }

        {
            final Object obj = new fix32(6);
            final fix32 instance = new fix32(4);
            final boolean expResult = false;
            final boolean result = instance.equals(obj);
            assertEquals(expResult, result);
        }

        {
            final Object obj = new fix32(-4);
            final fix32 instance = new fix32(4);
            final boolean expResult = false;
            final boolean result = instance.equals(obj);
            assertEquals(expResult, result);
        }
    }

}
