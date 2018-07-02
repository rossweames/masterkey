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

    /**
     * Gets the key status.
     *
     * @return the status
     */
    int getStatus();

    /**
     * Sets the key status.
     *
     * @param status the new key status
     */
    void setStatus(int status);

    /**
     * Tests for a MACS violation.
     * Sets the hasMACSViolation state appropriately.
     *
     * @param macs the MACS to test against
     * @return {@code True} if the key violates the given MACS, {@code false} if not.
     */
    boolean testForMACSViolation(int macs);

    /**
     * Gets the MACS violation flag.
     *
     * @return {@code True} if the key has a MACS violation, {@code false} if not.
     */
    boolean getHasMACSViolation();

    /**
     * Sets the MACS violation flag.
     *
     * @param hasMACSViolation the new MACS violation flag
     */
    void setHasMACSViolation(boolean hasMACSViolation);
}
