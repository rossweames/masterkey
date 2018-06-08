package com.eames.masterkey.service.progression;

import com.eames.masterkey.service.ValidationException;
import com.eames.masterkey.service.progression.services.totalposition.TotalPositionProgressionCriteria;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * This class tests the {@link TotalPositionProgressionCriteria} class.
 */
public class TotalPositionProgressionCriteriaTest {

    /**
     * The test constants
     */
    private static final int TEST_MACS = 4;
    private static final int[] TEST_MASTER_CUTS = {1, 3, 5, 6, 4, 2};
    private static final int[][] TEST_PROGRESSION_STEPS = {
            {2, 1, 1, 1, 1, 1},
            {3, 2, 2, 2, 2, 3},
            {4, 4, 3, 3, 3, 4},
            {5, 5, 4, 4, 5, 5},
            {6, 6, 6, 5, 6, 6}
    };
    private static final int[] TEST_PROGRESSION_SEQUENCE = {1, 2, 3, 4, 5, 6};

    /**
     * The config builder.
     */
    private TotalPositionProgressionCriteria.Builder configBuilder;

    /**
     * Gets called before each test.
     */
    @Before
    public void setUp() {

        // Construct the builder and fill it with valid values.
        configBuilder = new TotalPositionProgressionCriteria.Builder()
                .setMACS(TEST_MACS)
                .setMasterCuts(TEST_MASTER_CUTS)
                .setProgressionSteps(TEST_PROGRESSION_STEPS)
                .setProgressionSequence(TEST_PROGRESSION_SEQUENCE);
    }

    /**
     * Gets called after each test.
     */
    @After
    public void tearDown() {

        // Clear the builder.
        configBuilder = null;
    }

    /**
     * General tests
     */

    @Test
    public void testBuild_Valid() {

        try {

            TotalPositionProgressionCriteria criteria = configBuilder.build();

            assertNotNull(criteria);

        } catch (ValidationException e) {

            fail(e.getMessage());
        }
    }

    /**
     * MACS tests
     */

    @Test
    public void testBuild_NegativeMACS() {

        try {

            int macs =  -1;

            configBuilder
                    .setMACS(macs)
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ZeroMACS() {

        try {

            int macs =  0;

            configBuilder
                    .setMACS(macs)
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    /**
     * Master key tests
     */

    @Test
    public void testBuild_MissingMasterCuts() {

        try {

            configBuilder.setMasterCuts(null).build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_0MasterCut() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {})
                    .setProgressionSteps(new int[][] {{}})
                    .setProgressionSequence(new int[] {})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_1MasterCut() {

        try {

            TotalPositionProgressionCriteria criteria = configBuilder
                    .setMasterCuts(new int[] {4})
                    .setProgressionSteps(new int[][] {{6}})
                    .setProgressionSequence(new int[] {1})
                    .build();

            assertNotNull(criteria);

        } catch (ValidationException e) {

            fail(e.getMessage());
        }
    }

    @Test
    public void testBuild_NegativeMasterCutDepth() {

        try {

            int[] masterCuts = Arrays.copyOf(TEST_MASTER_CUTS, TEST_MASTER_CUTS.length);
            masterCuts[0] = -1;

            configBuilder
                    .setMasterCuts(masterCuts)
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    /**
     * Progression steps tests
     */

    @Test
    public void testBuild_MissingProgressionSteps() {

        try {

            configBuilder.setProgressionSteps(null).build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ProgressionStepsLessThanMasterCut() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {4, 2})
                    .setProgressionSteps(new int[][] {{6}})
                    .setProgressionSequence(new int[] {1, 2})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ProgressionStepsGreaterThanMasterCut() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {4})
                    .setProgressionSteps(new int[][] {{6, 5}})
                    .setProgressionSequence(new int[] {1})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ProgressionStepAllSameValueAsMasterCut1() {

        try {

            TotalPositionProgressionCriteria criteria = configBuilder
                    .setMasterCuts(new int[] {4})
                    .setProgressionSteps(new int[][] {{4}})
                    .setProgressionSequence(new int[] {1})
                    .build();

            assertNotNull(criteria);

        } catch (ValidationException e) {

            fail(e.getMessage());
        }
    }

    @Test
    public void testBuild_ProgressionStepAllSameValueAsMasterCut2() {

        try {

            TotalPositionProgressionCriteria criteria = configBuilder
                    .setMasterCuts(new int[] {2, 1})
                    .setProgressionSteps(new int[][] {
                            {1, 1},
                            {3, 1}})
                    .setProgressionSequence(new int[] {1, 2})
                    .build();

            assertNotNull(criteria);

        } catch (ValidationException e) {

            fail(e.getMessage());
        }
    }

    @Test
    public void testBuild_ProgressionStepSameValueAsMasterCut() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {2, 1})
                    .setProgressionSteps(new int[][] {
                            {1, 1},
                            {3, 2}})
                    .setProgressionSequence(new int[] {1, 2})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ProgressionStepDuplicateValue() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {2, 1})
                    .setProgressionSteps(new int[][] {
                            {1, 2},
                            {3, 2}})
                    .setProgressionSequence(new int[] {1, 2})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    /**
     * Progression sequence tests
     */

    @Test
    public void testBuild_MissingProgressionSequence() {

        try {

            configBuilder.setProgressionSequence(null).build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ProgressionSequenceLessThanMasterCut() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {4, 2})
                    .setProgressionSteps(new int[][] {{6, 4}, {2, 6}})
                    .setProgressionSequence(new int[] {1})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ProgressionSequenceGreaterThanMasterCut() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {4})
                    .setProgressionSteps(new int[][] {{6}})
                    .setProgressionSequence(new int[] {1, 2})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ProgressionSequenceTooSmallPosition() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {4})
                    .setProgressionSteps(new int[][] {{6}})
                    .setProgressionSequence(new int[] {0})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ProgressionSequenceTooLargePosition() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {4})
                    .setProgressionSteps(new int[][] {{6}})
                    .setProgressionSequence(new int[] {2})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_ProgressionSequenceDuplicatePosition() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {4, 5})
                    .setProgressionSteps(new int[][] {{6, 7}, {2, 3}})
                    .setProgressionSequence(new int[] {1, 1})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }
}
