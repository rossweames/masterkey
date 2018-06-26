package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.service.ProcessingCapability;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import com.eames.masterkey.service.progression.ProgressionServiceResults;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests the {@link RandomGenericTotalPositionProgressionService} class.
 */
public class RandomGenericTotalPositionProgressionServiceTest {

    // The progression service to test
    private RandomGenericTotalPositionProgressionService service;

    // A set of valid configs for testing
    private JSONObject configs;

    /**
     * Gets called before each test.
     */
    @Before
    public void setUp() {

        // Create the service.
        service = new RandomGenericTotalPositionProgressionService();

        // Create the configs.
        configs = new JSONObject();
        configs.put("cutCount", 6);
        configs.put("depthCount", 10);
        configs.put("startingDepth", 0);
        configs.put("doubleStepProgression", true);
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

        ProcessingCapability capability = service.canProcessConfigs("");

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_Valid() {

        ProcessingCapability capability = service.canProcessConfigs(configs.toString());

        assertEquals(ProcessingCapability.YES, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingCutCount() {

        configs.remove("cutCount");
        ProcessingCapability capability = service.canProcessConfigs(configs.toString());

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingDepthCount() {

        configs.remove("depthCount");
        ProcessingCapability capability = service.canProcessConfigs(configs.toString());

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingStartingDepth() {

        configs.remove("startingDepth");
        ProcessingCapability capability = service.canProcessConfigs(configs.toString());

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingProgressionStep() {

        configs.remove("doubleStepProgression");
        ProcessingCapability capability = service.canProcessConfigs(configs.toString());

        assertEquals(ProcessingCapability.NO, capability);
    }
    @Test
    public void testCanProcessConfigs_MissingMACS() {

        configs.remove("macs");
        ProcessingCapability capability = service.canProcessConfigs(configs.toString());

        assertEquals(ProcessingCapability.NO, capability);
    }
    @Test
    public void testCanProcessConfigs_ExtraConfig() {

        configs.put("master", 53247);
        ProcessingCapability capability = service.canProcessConfigs(configs.toString());

        assertEquals(ProcessingCapability.MAYBE, capability);
    }

    /*
     * .generateBittingList() tests
     */

    @Test
    public void testGenerateBittingList_Null() {

        try {

            service.generateBittingList(null);

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results
        }
    }

    @Test
    public void testGenerateBittingList_Valid() {

        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);
            assertNotNull(results.getSource());
            assertEquals("Random Generic Total Position Progression Service", results.getSource());
            assertNotNull(results.getCriteria());
            assertNotNull(results.getBittingList());

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_WrongTypeCutCount() {

        configs.put("cutCount", "6");
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_TooSmallCutCount() {

        configs.put("cutCount", 2);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_MinCutCount() {

        configs.put("cutCount", 3);
        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_MaxCutCount() {

        configs.put("cutCount", 7);
        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_TooLargeCutCount() {

        configs.put("cutCount", 8);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_WrongTypeDepthCount() {

        configs.put("depthCount", "10");
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_TooSmallDepthCount() {

        configs.put("depthCount", 2);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_MinDepthCount() {

        configs.put("doubleStepProgression", false);
        configs.put("depthCount", 3);
        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_MaxDepthCount() {

        configs.put("depthCount", 10);
        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_TooLargeDepthCount() {

        configs.put("depthCount", 11);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }


    @Test
    public void testGenerateBittingList_WrongTypeStartingDepth() {

        configs.put("startingDepth", "0");
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_TooSmallStartingDepth() {

        configs.put("startingDepth", -1);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_MinStartingDepth() {

        configs.put("startingDepth", 0);
        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_MaxStartingDepth() {

        configs.put("startingDepth", 1);
        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_TooLargeStartingDepth() {

        configs.put("startingDepth", 2);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }


    @Test
    public void testGenerateBittingList_WrongTypeProgressionStep() {

        configs.put("doubleStepProgression", "2");
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_DoubleProgressionStep_OddDepthCount() {

        configs.put("depthCount", 5);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_DoubleProgressionStep_TooSmallDepthCount() {

        configs.put("depthCount", 2);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_DoubleProgressionStep_MinDepthCount() {

        configs.put("depthCount", 4);
        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_WrongTypeMACS() {

        configs.put("macs", "2");
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_TooSmallMACS() {

        configs.put("macs", 0);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_MinMACS() {

        configs.put("macs", 1);
        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_MaxMACS() {

        configs.put("macs", 10);
        try {

            ProgressionServiceResults results = service.generateBittingList(configs.toString());
            assertNotNull(results);

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_TooLargeMACS() {

        configs.put("macs", 11);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }
}
