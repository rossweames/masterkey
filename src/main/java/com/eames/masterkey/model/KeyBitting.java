package com.eames.masterkey.model;

import static java.lang.Math.abs;

/**
 * This class represents a key bitting.
 */
public class KeyBitting
        implements BittingNode, HasKey {

    // The key.
    private int[] key;

    // Indicates that this key bitting has a MACS violation.
    private Boolean hasMACSViolation;


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

    @Override
    public Boolean testForMACSViolation(int macs) {

        // Can't do the test without a key.
        if (key == null)
            return null;

        // Initialize the 'hasMACSViolation' to false.
        hasMACSViolation = false;

        // Test the cuts until we find one that violates the MACS or we run out.
        Integer lastCut = null;
        for (int cut : key) {

            // The MACS has been violated.
            if ((lastCut != null) && (abs(cut - lastCut) > macs)) {
                hasMACSViolation = true;
                break;
            }

            // Save the cut to compare it to the next one.
            lastCut = cut;
        }

        // Return the 'hasMACSViolation' flag.
        return hasMACSViolation;
    }

    @Override
    public Boolean getHasMACSViolation() {
        return hasMACSViolation;
    }

    @Override
    public void setHasMACSViolation(boolean hasMACSViolation) {
        this.hasMACSViolation = hasMACSViolation;
    }
}
