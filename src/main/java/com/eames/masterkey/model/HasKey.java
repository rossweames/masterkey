package com.eames.masterkey.model;

/**
 * This interface defines the operations mandatory for all classes that support a single key.
 */
public interface HasKey {

    /**
     * Gets the key cuts.
     *
     * @return the key cuts
     */
    int[] getKey();

    /**
     * Sets the key cuts.
     *
     * @param key the new key cuts
     */
    void setKey(int[] key);
}
