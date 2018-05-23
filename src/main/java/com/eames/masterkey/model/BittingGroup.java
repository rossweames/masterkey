package com.eames.masterkey.model;

/**
 * This represents a bitting group.
 *
 * A bitting group consists of a master key and an array of {@link BittingNode}s.
 */
public class BittingGroup
        implements BittingNode, HasMaster, HasGroups {

    // The group's master key.
    private int[] master;

    // The child bitting groups
    private BittingNode[] groups;

    /*
        Implemented BittingNode operations
     */

    @Override
    public boolean hasGroups() {

        // BittingGroups always have child groups.
        return true;
    }

    /*
        Implemented HasKey operations
     */

    @Override
    public int[] getMaster() {
        return master;
    }

    @Override
    public void setMaster(int[] master) {
        this.master = master;
    }

    /*
        Implemented HasGroups operations
     */

    @Override
    public BittingNode[] getGroups() {
        return groups;
    }

    @Override
    public void setGroups(BittingNode[] groups) {
        this.groups = groups;
    }
}
