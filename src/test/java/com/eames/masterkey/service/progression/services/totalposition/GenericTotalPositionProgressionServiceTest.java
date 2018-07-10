package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.service.ProcessingCapability;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import com.eames.masterkey.service.progression.ProgressionServiceResults;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class tests the {@link GenericTotalPositionProgressionService} class.
 */
public class GenericTotalPositionProgressionServiceTest {

    // The progression service to test
    private GenericTotalPositionProgressionService service;

    // A set of valid configs for testing
    private JSONObject configs;

    /**
     * Gets called before each test.
     */
    @BeforeEach
    public void setUp() {

        // Create the service.
        service = new GenericTotalPositionProgressionService();

        // Create the configs.
        configs = new JSONObject();
        configs.put("masterCuts", "623578");
        JSONArray steps = new JSONArray();
        steps.put("081956");
        steps.put("265790");
        steps.put("447312");
        steps.put("809134");
        configs.put("progressionSteps", steps);
        configs.put("progressionSequence", "413625");
        configs.put("startingDepth", 0);
        configs.put("macs", 7);
    }

    /**
     * Gets called after each test.
     */
    @AfterEach
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
    public void testCanProcessConfigs_MissingMasterCuts() {

        configs.remove("masterCuts");
        ProcessingCapability capability = service.canProcessConfigs(configs.toString());

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingProgressionSteps() {

        configs.remove("progressionSteps");
        ProcessingCapability capability = service.canProcessConfigs(configs.toString());

        assertEquals(ProcessingCapability.NO, capability);
    }

    @Test
    public void testCanProcessConfigs_MissingProgressionSequence() {

        configs.remove("progressionSequence");
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
            assertEquals("Generic Total Position Progression Service", results.getSource());
            assertNotNull(results.getCriteria());
            assertNotNull(results.getBittingList());

        } catch (ProgressionServiceException ex) {

            fail(ex.getMessage());
        }
    }

    @Test
    public void testGenerateBittingList_WrongTypeMasterCuts() {

        configs.put("masterCuts", 6);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_AlphaInMasterCuts() {

        configs.put("masterCuts", "1V3456");
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_WrongTypeProgressionSteps() {

        configs.put("progressionSteps", 6);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_WrongTypeProgressionSteps_2() {

        JSONArray steps = new JSONArray();
        steps.put("081956");
        steps.put("265790");
        steps.put("447312");
        steps.put(6);
        configs.put("progressionSteps", steps);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_WrongTypeProgressionSequence() {

        configs.put("progressionSequence", 6);
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_AlphaInProgressionSequence() {

        configs.put("progressionSequence", "1V3456");
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

        configs.put("startingDepth", "6");
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }

    @Test
    public void testGenerateBittingList_WrongTypeMACS() {

        configs.put("macs", "6");
        try {

            // Throws: ProgressionServiceException
            service.generateBittingList(configs.toString());

            fail();

        } catch (ProgressionServiceException ex) {

            // Expected results...
        }
    }
}
