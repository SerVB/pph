package com.github.servb.pph.pheroes.common.common;

/**
 *
 * @author SerVB
 */
public class iArtifactList extends constArtifactList {

    public final void Add(final short id, final HERO_ART_CELL assign) {
        getM_Items().add(new iItem(id, assign));
    }

    public final void set(final int idx, final iItem item) {
        getM_Items().set(idx, item);
    }

}
