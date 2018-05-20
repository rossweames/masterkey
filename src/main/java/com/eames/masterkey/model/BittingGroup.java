package com.eames.masterkey.model;

/**
 * This abstract class is the base class for all bitting group classes.
 */
public abstract class BittingGroup<T>
        implements HasMaster, HasChildren<T> {

    // The group's master key.
    private int[] master;

    // The child bitting groups
    private T[] children;

    /*
        Implemented HasMaster operations
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
    public T[] getChildren() {
        return children;
    }

    @Override
    public void setChildren(T[] children) {
        this.children = children;
    }
}
