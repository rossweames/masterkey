package com.eames.masterkey.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the {@link BittingGroup} class.
 */
public class BittingGroupTest {

    // The bitting group to test
    private BittingGroup bittingGroup;

    /**
     * Gets called before each test.
     */
    @BeforeEach
    public void setUp() {

        bittingGroup =new BittingGroup();
    }

    /**
     * Gets called after each test.
     */
    @AfterEach
    public void tearDown() {

        bittingGroup = null;
    }

    /*
     * .hasGroups() tests
     */

    @Test
    public void testHasGroups() {

        assertTrue(bittingGroup.hasGroups());
    }
}
