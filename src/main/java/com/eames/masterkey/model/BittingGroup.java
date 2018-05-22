package com.eames.masterkey.model;

/**
 * This represents a bitting group.
 *
 * A bitting group consists of a master key and an array of {@link BittingNode}s.
 */
public class BittingGroup
        implements BittingNode, HasMaster, HasChildren<BittingNode> {

    // The group's master key.
    private int[] master;

    // The child bitting groups
    private BittingNode[] children;

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
        Implemented HasChildren operations
     */

    @Override
    public BittingNode[] getChildren() {
        return children;
    }

    @Override
    public void setChildren(BittingNode[] children) {
        this.children = children;
    }
}
