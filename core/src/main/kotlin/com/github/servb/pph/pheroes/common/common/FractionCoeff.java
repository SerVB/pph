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
package com.github.servb.pph.pheroes.common.common;

/**
 *
 * @author SerVB
 */
public final class FractionCoeff {

    public int num, denum;

    public FractionCoeff() {
        num = -1;
        denum = -1;
    }

    public FractionCoeff(final int num, final int denum) {
        this.num = num;
        this.denum = denum;
    }

    public final boolean IsValid() {
        return num != -1 && denum != -1;
    }

    public final FractionCoeff GetNormalized() {
        if (num > denum) {
            return new FractionCoeff(num / denum, 1);
        }
        if (denum > num) {
            return new FractionCoeff(1, (denum + num - 1) / num);
        }
        return new FractionCoeff(1, 1);
    }

}
