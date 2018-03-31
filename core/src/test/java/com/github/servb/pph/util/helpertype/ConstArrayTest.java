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

import com.github.servb.pph.pheroes.common.common.NATION_TYPE;
import com.github.servb.pph.util.helpertype.ConstArray;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author SerVB
 */
public class ConstArrayTest {

    /**
     * Test of get method, of class ConstArray.
     */
    @Test
    public void testGet() {
        System.out.println("get");

        final Integer[] nationArgs = new Integer[NATION_TYPE.NATION_COUNT.getValue()];

        for (int xx = 0; xx < NATION_TYPE.NATION_COUNT.getValue(); ++xx) {
            nationArgs[xx] = xx;
        }

        final ConstArray<NATION_TYPE, Integer> NATION_ARGS = new ConstArray<NATION_TYPE, Integer>(nationArgs);

        for (final NATION_TYPE nt : NATION_TYPE.values()) {
            if (nt.equals(NATION_TYPE.NATION_COUNT)) {
                continue;
            }
            final Object expResult = nationArgs[nt.getValue()];
            final Object result = NATION_ARGS.get(nt);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of size method, of class ConstArray.
     */
    @Test
    public void testSize() {
        System.out.println("size");

        final Integer[] nationArgs = new Integer[NATION_TYPE.NATION_COUNT.getValue()];

        for (int xx = 0; xx < NATION_TYPE.NATION_COUNT.getValue(); ++xx) {
            nationArgs[xx] = xx;
        }

        final ConstArray<NATION_TYPE, Integer> NATION_ARGS = new ConstArray<NATION_TYPE, Integer>(nationArgs);

        final int expResult = NATION_TYPE.NATION_COUNT.getValue();
        final int result = NATION_ARGS.size();
        assertEquals(expResult, result);
    }

}
