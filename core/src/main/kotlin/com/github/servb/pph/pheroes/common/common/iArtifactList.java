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
public class iArtifactList extends constArtifactList {

    public final void Add(final short id, final HERO_ART_CELL assign) {
        m_Items.add(new iItem(id, assign));
    }

    public final void set(final int idx, final iItem item) {
        m_Items.set(idx, item);
    }

}
