package com.eames.masterkey.model;

import static java.lang.Math.abs;

/**
 * This class represents a key bitting.
 */
public class KeyBitting
        implements BittingNode, HasKey {

    // The 'MACS' violation status mask bit.
    public static int STATUS_MASK_MACS = 0;

    // The key.
    private int[] key;

    // The key's status mask.
    private int status = 0;

    /**
     * Constructor
     *
     * @param key the key that this object represents
     */
    public KeyBitting(int[] key) {
        this.key = key;
    }

    /*
     * Implemented BittingNode operations
     */

    /**
     * Constructor
     *
     * @param key the key that this object represents
     * @param macs the MACS to test the key against
     */
    public KeyBitting(int[] key, int macs) {

        // Set the key
        this.key = key;

        // Test for a MACS violation.
        testForMACSViolation(macs);
    }

    /*
     * Implemented BittingNode operations
     */

    @Override
    public boolean hasGroups() {

        // BittingGroups never have child groups.
        return false;
    }

    /*
     * Implemented HasKey operations
     */

    @Override
    public int[] getKey() {
        return key;
    }

    @Override
    public void setKey(int[] key) {
        this.key = key;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean testForMACSViolation(int macs) {

        // Clear the status mask.
        setHasMACSViolation(false);

        // There must be a key.
        if (key != null) {

            // Test the cuts until we find one that violates the MACS or we run out.
            Integer lastCut = null;
            for (int cut : key) {

                // The MACS has been violated.
                if ((lastCut != null) && (abs(cut - lastCut) > macs)) {
                    setHasMACSViolation(true);
                    break;
                }

                // Save the cut to compare it to the next one.
                lastCut = cut;
            }
        }

        // Return the 'MACS violation' flag.
        return getHasMACSViolation();
    }

    @Override
    public boolean getHasMACSViolation() {

        return testStatusBit(STATUS_MASK_MACS);
    }

    @Override
    public void setHasMACSViolation(boolean hasMACSViolation) {

        if (hasMACSViolation)
            setStatusBit(STATUS_MASK_MACS);
        else
            clearStatusBit(STATUS_MASK_MACS);
    }

    /**
     * Local status operations
     */

    /**
     * Tests the given status bit.
     *
     * @param statusBit the status bit to test
     * @return {@code True} if the given bit is set, {@code False} if not
     */
    private boolean testStatusBit(int statusBit) {

        return ((status & (1 << statusBit)) != 0);
    }

    /**
     * Clears the given status bit.
     *
     * @param statusBit the status bit to clear
     */
    private void clearStatusBit(int statusBit) {

        status &= ~(1 << statusBit);
    }

    /**
     * Sets the given status bit.
     *
     * @param statusBit the status bit to set
     */
    private void setStatusBit(int statusBit) {

        status |= (1 << statusBit);
    }
}
