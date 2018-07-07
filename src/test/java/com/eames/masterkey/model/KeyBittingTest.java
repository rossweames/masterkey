package com.eames.masterkey.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the {@link KeyBitting} class.
 */
public class KeyBittingTest {

    // The bitting group to test
    private KeyBitting keyBitting;

    /**
     * Gets called before each test.
     */
    @BeforeEach
    public void setUp() {

        keyBitting = new KeyBitting(new int[] {3, 5, 4, 1, 6});
    }

    /**
     * Gets called after each test.
     */
    @AfterEach
    public void tearDown() {

        keyBitting = null;
    }

    /*
     * Constructor tests
     */

    @Test
    public void testHasMACSViolation_Default() {

        assertFalse(keyBitting.getHasMACSViolation());
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
     * .setHasMACSViolation() tests
     */

    @Test
    public void testSetHasMACSViolation_0_False() {

        keyBitting.setStatus(0);
        keyBitting.setHasMACSViolation(false);
        assertFalse(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testSetHasMACSViolation_0_True() {

        keyBitting.setStatus(0);
        keyBitting.setHasMACSViolation(true);
        assertTrue(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testSetHasMACSViolation_1_False() {

        keyBitting.setStatus(1);
        keyBitting.setHasMACSViolation(false);
        assertFalse(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testSetHasMACSViolation_1_True() {

        keyBitting.setStatus(1);
        keyBitting.setHasMACSViolation(true);
        assertTrue(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testSetHasMACSViolation_2_False() {

        keyBitting.setStatus(2);
        keyBitting.setHasMACSViolation(false);
        assertFalse(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testSetHasMACSViolation_2_True() {

        keyBitting.setStatus(2);
        keyBitting.setHasMACSViolation(true);
        assertTrue(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testSetHasMACSViolation_3_False() {

        keyBitting.setStatus(3);
        keyBitting.setHasMACSViolation(false);
        assertFalse(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testSetHasMACSViolation_3_True() {

        keyBitting.setStatus(3);
        keyBitting.setHasMACSViolation(true);
        assertTrue(keyBitting.getHasMACSViolation());
    }

    /*
     * .testForMACSViolation() tests
     */

    @Test
    public void testTestMACSViolation_NullKey() {

        keyBitting.setKey(null);
        assertFalse(keyBitting.testForMACSViolation(4));
        assertFalse(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testTestMACSViolation_False() {

        assertFalse(keyBitting.testForMACSViolation(5));
        assertFalse(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testTestMACSViolation_True_Beginning() {

        keyBitting = new KeyBitting(new int[] {6, 1, 4, 1, 3});
        assertTrue(keyBitting.testForMACSViolation(4));
        assertTrue(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testTestMACSViolation_True_Middle() {

        keyBitting = new KeyBitting(new int[] {2, 1, 6, 4, 6});
        assertTrue(keyBitting.testForMACSViolation(4));
        assertTrue(keyBitting.getHasMACSViolation());
    }

    @Test
    public void testTestMACSViolation_True_End() {

        keyBitting = new KeyBitting(new int[] {3, 5, 4, 1, 6});
        assertTrue(keyBitting.testForMACSViolation(4));
        assertTrue(keyBitting.getHasMACSViolation());
    }
}
