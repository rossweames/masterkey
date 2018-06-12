package com.eames.masterkey.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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

        keyBitting = new KeyBitting(new int[] {3, 5, 4, 1, 6});
    }

    /**
     * Gets called after each test.
     */
    @After
    public void tearDown() {

        keyBitting = null;
    }

    /*
     * Constructor tests
     */

    @Test
    public void testHasMACSViolation_Default() {

        assertNull(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testConstructor_False() {

        keyBitting = new KeyBitting(new int[] {3, 5, 4, 1, 6}, 5);
        assertFalse(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testConstructor_True() {

        keyBitting = new KeyBitting(new int[] {3, 5, 4, 1, 6}, 4);
        assertTrue(keyBitting.getHasMACSViolation());
    }

    /*
     * .hasGroups() tests
     */

    @Test
    public void testHasGroups() {

        assertFalse(keyBitting.hasGroups());
    }

    /*
     * .testForMACSViolation() tests
     */

    @Test
    public void testTestMACSViolation_NullKey() {

        keyBitting.setKey(null);
        assertNull(keyBitting.testForMACSViolation(4));
        assertNull(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testTestMACSViolation_False() {

        Boolean result = keyBitting.testForMACSViolation(5);
        assertNotNull(result);
        assertFalse(result);

        Boolean flag = keyBitting.getHasMACSViolation();
        assertNotNull(flag);
        assertFalse(flag);
    }

    @Test
    public void testTestMACSViolation_True_Beginning() {

        keyBitting = new KeyBitting(new int[] {6, 1, 4, 1, 3});
        Boolean result = keyBitting.testForMACSViolation(4);
        assertNotNull(result);
        assertTrue(result);

        Boolean flag = keyBitting.getHasMACSViolation();
        assertNotNull(flag);
        assertTrue(flag);
    }

    @Test
    public void testTestMACSViolation_True_Middle() {

        keyBitting = new KeyBitting(new int[] {2, 1, 6, 4, 6});
        Boolean result = keyBitting.testForMACSViolation(4);
        assertNotNull(result);
        assertTrue(result);

        Boolean flag = keyBitting.getHasMACSViolation();
        assertNotNull(flag);
        assertTrue(flag);
    }

    @Test
    public void testTestMACSViolation_True_End() {

        keyBitting = new KeyBitting(new int[] {3, 5, 4, 1, 6});
        Boolean result = keyBitting.testForMACSViolation(4);
        assertNotNull(result);
        assertTrue(result);

        Boolean flag = keyBitting.getHasMACSViolation();
        assertNotNull(flag);
        assertTrue(flag);
    }
}
