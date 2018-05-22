package com.eames.masterkey.model;

/**
 * This class represents a master key system bitting list.
 *
 * It consists of a source service and a {@link BittingGroup}.
 */
public class BittingList
        extends BittingGroup {

    // The name of the service that generated this bitting list.
    private String source;

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
}
