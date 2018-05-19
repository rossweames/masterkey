package com.eames.masterkey.service.progression.services;

import com.eames.masterkey.service.ProcessingCapability;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests the {@link RandomGenericTotalProgressionService} class.
 */
public class RandomGenericTotalProgressionServiceTest {

    // The progression service to test
    private RandomGenericTotalProgressionService service;

    // A set of valid configs for testing
    private JSONObject configs;

    /**
     * Gets called before each test.
     */
    @Before
    public void setUp() {

        // Create the service.
        service = new RandomGenericTotalProgressionService();

        // Create the configs.
        configs = new JSONObject();
        configs.put("cutCount", 6);
        configs.put("depthCount", 10);
        configs.put("startingDepth", 0);
        configs.put("progressionStep", 2);
        configs.put("macs", 4);
   }

    /**
     * Gets called after each test.
     */
    @After
    public void tearDown() {

        service = null;
        configs = null;
    }

    /*
     * .canProcessConfigs() tests
     */

    @Test
    public void testCanProcessConfigs_Null() {

        ProcessingCapability capability = service.canProcessConfigs(null);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_Empty() {

        ProcessingCapability capability = service.canProcessConfigs(new JSONObject());

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_Valid() {

        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingCutCount() {

        configs.remove("cutCount");
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_WrongTypeCutCount() {

        configs.put("cutCount", "6");

        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_TooSmallCutCount() {

        configs.put("cutCount", 4);

        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MinCutCount() {

        configs.put("cutCount", 5);

        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_MaxCutCount() {

        configs.put("cutCount", 7);

        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_TooLargeCutCount() {

        configs.put("cutCount", 8);

        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingDepthCount() {

        configs.remove("depthCount");
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_WrongTypeDepthCount() {

        configs.put("depthCount", "10");
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_TooSmallDepthCount() {

        configs.put("depthCount", 4);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MinDepthCount() {

        configs.put("depthCount", 5);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_MaxDepthCount() {

        configs.put("depthCount", 10);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_TooLargeDepthCount() {

        configs.put("depthCount", 11);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingStartingDepth() {

        configs.remove("startingDepth");
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_WrongTypeStartingDepth() {

        configs.put("startingDepth", "0");
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_TooSmallStartingDepth() {

        configs.put("startingDepth", -1);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MinStartingDepth() {

        configs.put("startingDepth", 0);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_MaxStartingDepth() {

        configs.put("startingDepth", 1);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_TooLargeStartingDepth() {

        configs.put("startingDepth", 2);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingProgressionStep() {

        configs.remove("progressionStep");
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_WrongTypeProgressionStep() {

        configs.put("progressionStep", "2");
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_TooSmallProgressionStep() {

        configs.put("progressionStep", 0);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MinProgressionStep() {

        configs.put("progressionStep", 1);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_MaxProgressionStep() {

        configs.put("progressionStep", 2);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_TooLargeProgressionStep() {

        configs.put("progressionStep", 3);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingMACS() {

        configs.remove("macs");
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_WrongTypeMACS() {

        configs.put("macs", "2");
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_TooSmallMACS() {

        configs.put("macs", 0);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MinMACS() {

        configs.put("macs", 1);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_MaxMACS() {

        configs.put("macs", 10);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_TooLargeMACS() {

        configs.put("macs", 11);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_ExtraConfig() {

        configs.put("master", 53247);
        ProcessingCapability capability = service.canProcessConfigs(configs);

        assertEquals(ProcessingCapability.MAYBE, capability);
    }

    /*
     * .generateBittingList() tests
     */

}