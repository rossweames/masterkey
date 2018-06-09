package com.eames.masterkey.model;

/**
 * This class represents a key bitting.
 */
public class KeyBitting
        implements BittingNode, HasKey {

    // The key.
    private int[] key;

    /**
     * Constructor
     *
     * @param key the key that this object represents
     */
    public KeyBitting(int[] key) {
        this.key = key;
    }

    /*
        Implemented BittingNode operations
     */

    @Override
    public boolean hasGroups() {

        // BittingGroups never have child groups.
        return false;
    }

    /*
        Implemented HasKey operations
     */

    @Override
    public int[] getKey() {
        return key;
    }

    @Override
    public void setKey(int[] key) {
        this.key = key;
    }
}
