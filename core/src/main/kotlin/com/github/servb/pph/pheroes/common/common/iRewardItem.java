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
