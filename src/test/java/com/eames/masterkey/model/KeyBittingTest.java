package com.eames.masterkey.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * This class tests the {@link KeyBitting} class.
 */
public class KeyBittingTest {

    // The bitting group to test
    private KeyBitting keyBitting;

    /**
     * Gets called before each test.
     */
    @Before
    public void setUp() {

        keyBitting = new KeyBitting(new int[] {3, 5, 4, 1, 2});
    }

    /**
     * Gets called after each test.
     */
    @After
    public void tearDown() {

        keyBitting = null;
    }

    /*
     * .hasGroups() tests
     */

    @Test
    public void testHasGroups() {

        assertFalse(keyBitting.hasGroups());
    }
}
