package com.eames.masterkey.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the {@link BittingGroup} class.
 */
public class BittingGroupTest {

    // The bitting group to test
    private BittingGroup bittingGroup;

    /**
     * Gets called before each test.
     */
    @Before
    public void setUp() {

        bittingGroup =new BittingGroup();
    }

    /**
     * Gets called after each test.
     */
    @After
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
