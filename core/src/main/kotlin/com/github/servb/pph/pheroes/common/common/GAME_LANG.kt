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

/** Game languages.  TODO: provide tests, add all langs. */
enum class GAME_LANG(override val v: Int) : UniqueValueEnum, CountValueEnum {
    GLNG_ENGLISH(0),
    GLNG_RUSSIAN(1),
    /*GLNG_POLISH,
    GLNG_SLOVAK,
    GLNG_GERMAN,
    GLNG_CZECH,
    GLNG_ITALIAN,
    GLNG_FRENCH,
    GLNG_SPANISH,*/
    GLNG_COUNT(2);
}
