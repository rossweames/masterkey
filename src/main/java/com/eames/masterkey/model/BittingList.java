package com.eames.masterkey.model;

/**
 * This class represents a master key system bitting list.
 *
 * It contains the root {@link BittingGroup}.
 */
public class BittingList {

    // The bitting group.
    private BittingGroup rootBittingGroup;

    /**
     * Gets the root {@link BittingGroup}.
     *
     * @return the root bitting group
     */
    public BittingGroup getRootBittingGroup() {
        return rootBittingGroup;
    }

    /**
     * Sets the root {@link BittingGroup}.
     *
     * @param rootBittingGroup the new roo bitting group
     */
    public void setRootBittingGroup(BittingGroup rootBittingGroup) {
        this.rootBittingGroup = rootBittingGroup;
    }
}
