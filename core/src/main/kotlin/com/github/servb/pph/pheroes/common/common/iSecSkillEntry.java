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
