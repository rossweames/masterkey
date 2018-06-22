package com.eames.masterkey.model;

/**
 * This interface defines the operations mandatory for all classes that support a single master key.
 */
public interface HasMaster {

    /**
     * Gets the master {@link KeyBitting}.
     *
     * @return the master key bitting
     */
    KeyBitting getMaster();

    /**
     * Sets the master {@link KeyBitting}.
     *
     * @param master the new master key cuts
     */
    void setMaster(KeyBitting master);
}
