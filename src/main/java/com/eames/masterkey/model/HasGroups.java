package com.eames.masterkey.model;

/**
 * This interface defines the operations mandatory for all classes that support groups.
 */
public interface HasGroups {

    /**
     * Gets the groups.
     *
     * @return the groups
     */
    BittingNode[] getGroups();

    /**
     * Sets the groups.
     *
     * @param groups the new groups
     */
    void setGroups(BittingNode[] groups);
}
