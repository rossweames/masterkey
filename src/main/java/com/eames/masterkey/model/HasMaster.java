package com.eames.masterkey.model;

/**
 * This interface defines the operations mandatory for all classes that support master keys.
 */
public interface HasMaster {

    /**
     * Gets the master key cuts.
     *
     * @return the master key cuts
     */
    int[] getMaster();

    /**
     * Sets the master key cuts.
     *
     * @param master the new master key cuts
     */
    void setMaster(int[] master);
}
