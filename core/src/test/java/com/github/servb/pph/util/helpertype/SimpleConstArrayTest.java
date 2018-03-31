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
package test.java.com.github.servb.pph.util.helpertype;

import com.github.servb.pph.util.helpertype.SimpleConstArray;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author SerVB
 */
public class SimpleConstArrayTest {

    /**
     * Test of get method, of class SimpleConstArray.
     */
    @Test
    public void testGet() {
        System.out.println("get");

        final Float[] cosTableFlt = new Float[256];

        final float pi2 = (float) Math.PI * 2.0f;
        final float pi05 = (float) Math.PI / 2.0f;
        for (int xx = 0; xx < 256; ++xx) {
            final float fxx = (float) xx;
            final float cosValue = (float) Math.cos((fxx / 256.0f * pi2) - pi05);

            cosTableFlt[xx] = cosValue;
        }

        final SimpleConstArray<Float> COS_TABLE_FLT = new SimpleConstArray<Float>(cosTableFlt);

        for (int xx = 0; xx < 256; ++xx) {
            final Object expResult = cosTableFlt[xx];
            final Object result = COS_TABLE_FLT.get(xx);
            assertEquals(expResult, result);
        }
    }
}
