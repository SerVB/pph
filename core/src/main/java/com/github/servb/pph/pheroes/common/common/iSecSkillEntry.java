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
 * Immutable.
 *
 * @author SerVB
 */
public final class iSecSkillEntry {

    public iSecSkillEntry() {
        this(SECONDARY_SKILLS.SECSK_NONE);
    }

    public iSecSkillEntry(final SECONDARY_SKILLS skill) {
        this(skill, SECSKILL_LEVEL.SSLVL_NONE);
    }

    public iSecSkillEntry(final SECONDARY_SKILLS skill, final SECSKILL_LEVEL level) {
        m_skill = (byte) skill.getValue();
        m_level = (byte) level.getValue();
    }

    public final byte m_skill;
    public final byte m_level;
}
