package com.eames.masterkey.model;

/**
 * This interface defines the operations mandatory for all classes that support a single master key.
 */
public interface HasMaster {

    /**
     * Gets the master key cuts.
     *
     * @return the master key cuts
     */
    int[] getMaster();

    /**
     * Sets the masterkey cuts.
     *
     * @param master the new master key cuts
     */
    void setMaster(int[] master);
}
