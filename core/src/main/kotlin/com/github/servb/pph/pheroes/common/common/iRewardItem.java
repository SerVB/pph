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
 * Reward item. Immutable.
 *
 * @author SerVB
 */
public final class iRewardItem {

    public iRewardItem(final REWARD_ITEM_TYPE type, final int fparam, final int sparam) {
        m_type = type;
        m_fParam = fparam;
        m_sParam = sparam;
    }

    public final REWARD_ITEM_TYPE m_type;
    public final int m_fParam;
    public final int m_sParam;
}
