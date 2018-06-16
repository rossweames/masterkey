package com.eames.masterkey.model;

/**
 * This class represents a master key system bitting list.
 *
 * It consists of a source service and a {@link BittingGroup}.
 */
public class BittingList {

    // The name of the service that generated this bitting list.
    private String source;

    // The bitting group.
    private BittingGroup rootBittingGroup;

    /**
     * Gets the name of the service that generated this bitting list.
     *
     * @return the name of the service
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the name of the service that generated this bitting list.
     *
     * @param source the new name of the service
     */
    public void setSource(String source) {
        this.source = source;
    }

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
