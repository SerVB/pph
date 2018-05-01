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

import java.util.ArrayList;

/**
 *
 * @author SerVB
 */
public class constArtifactList {

    public constArtifactList() {
        m_Items = new ArrayList<iItem>();
    }

    public static final class iItem {

        public iItem(final short id, final HERO_ART_CELL assign) {
            this.id = id;
            this.assign = assign;
        }

        public final short id;
        public final HERO_ART_CELL assign;
    };

    public final int Count() {
        return m_Items.size();
    }

    public final iItem At(final int idx) {
        return m_Items.get(idx);
    }

    protected final ArrayList<iItem> m_Items;

}
