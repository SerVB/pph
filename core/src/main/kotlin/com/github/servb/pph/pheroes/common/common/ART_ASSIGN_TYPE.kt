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
enum class ART_ASSIGN_TYPE(override val v: Int) : UniqueValueEnum, CountValueEnum {
    ART_ASSIGN_HEAD(0),
    ART_ASSIGN_NECK(1),
    ART_ASSIGN_TORSO(2),
    ART_ASSIGN_SHOULDERS(3),
    ART_ASSIGN_HANDS(4),
    ART_ASSIGN_FINGERS(5),
    ART_ASSIGN_LEGS(6),
    ART_ASSIGN_FEET(7),
    ART_ASSIGN_MISC(8),
    ART_ASSIGN_COUNT(9);
}
