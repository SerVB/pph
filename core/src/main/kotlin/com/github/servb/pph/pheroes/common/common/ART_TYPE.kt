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
package com.github.servb.pph.pheroes.common.common

import com.github.servb.pph.util.helpertype.CountValueEnum
import com.github.servb.pph.util.helpertype.UniqueValueEnum

/** TODO: Provide documentation, provide tests. */
enum class ART_TYPE(override val v: Int) : UniqueValueEnum, CountValueEnum {
    ARTT_BASIC(0),
    ARTT_FURTSKILL(1),
    ARTT_NEGSPHERE(2),
    ARTT_SHOFWAR(3),
    ARTT_CURSWORD(4),
    ARTT_COUNT(5),
    ARTT_ULTIMATE(0x0F00);
}
