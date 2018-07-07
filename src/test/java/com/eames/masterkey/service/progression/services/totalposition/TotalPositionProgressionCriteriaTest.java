package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.service.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

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
    @BeforeEach
    private void setUp() {

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
    @AfterEach
    private void tearDown() {

        // Clear the builder.
        configBuilder = null;
    }

    /**
     * General tests
     */

    @Test
    private void testBuild_Valid() {

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
    private void testBuild_NegativeMACS() {

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
    private void testBuild_ZeroMACS() {

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
    public void testBuild_2MasterCut() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {1, 2})
                    .setProgressionSteps(new int[][] {{2, 3}})
                    .setProgressionSequence(new int[] {1, 2})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
        }
    }

    @Test
    public void testBuild_3MasterCut() {

        try {

            TotalPositionProgressionCriteria criteria = configBuilder
                    .setMasterCuts(new int[] {1, 2, 3})
                    .setProgressionSteps(new int[][] {{2, 3, 4}})
                    .setProgressionSequence(new int[] {1, 2, 3})
                    .build();

            assertNotNull(criteria);

        } catch (ValidationException e) {

            fail(e.getMessage());
        }
    }

    @Test
    public void testBuild_7MasterCut() {

        try {

            TotalPositionProgressionCriteria criteria = configBuilder
                    .setMasterCuts(new int[] {1, 2, 3, 4, 5, 6, 7})
                    .setProgressionSteps(new int[][] {{2, 3, 4, 5, 6, 7, 8}})
                    .setProgressionSequence(new int[] {1, 2, 3, 4, 5, 6, 7})
                    .build();

            assertNotNull(criteria);

        } catch (ValidationException e) {

            fail(e.getMessage());
        }
    }

    @Test
    public void testBuild_8MasterCut() {

        try {

            configBuilder
                    .setMasterCuts(new int[] {1, 2, 3, 4, 5, 6, 7, 8})
                    .setProgressionSteps(new int[][] {{2, 3, 4, 5, 6, 7, 8, 9}})
                    .setProgressionSequence(new int[] {1, 2, 3, 4, 5, 6, 7, 8})
                    .build();

            fail();

        } catch (ValidationException e) {

            // Expected results
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

            fail();

        } catch (ValidationException e) {

            // Expected results
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

            fail();

        } catch (ValidationException e) {

            // Expected results
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

    // TODO: Need tests for .validateCutCount().

    // TODO: Need tests for .validateStartingDepth().

    // TODO: Need tests for .validateMACS().

}
