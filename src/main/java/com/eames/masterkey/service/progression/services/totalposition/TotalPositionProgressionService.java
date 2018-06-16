package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.model.BittingGroup;
import com.eames.masterkey.model.BittingList;
import com.eames.masterkey.model.BittingNode;
import com.eames.masterkey.model.KeyBitting;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Key;
import java.util.Arrays;

/**
 * This class is responsible for generating a bitting list using the Total Position Progression
 * technique with given progression criteria.
 */
public class TotalPositionProgressionService {

    // Initialize the Log4j logger.
    private static final Logger logger = LogManager.getLogger(TotalPositionProgressionService.class);

    // The master cuts come from the criteria passed to the constructor.
    private int[] masterCuts;

    // The progression steps come from the criteria passed to the constructor.
    private int[][] progressionSteps;

    // The progression sequence comes from the criteria passed to the constructor.
    private int[] progressionSequence;

    // The MACS comes from the criteria passed to the constructor.
    private int macs;

    // The cut count is calculated from the progression sequence.
    private int cutCount;

    // The step count is calculated from the progression sequence.
    private int stepCount;

    // The cut order is generated from the progression sequence.
    private int[] cutOrder;

    /**
     * Constructor
     *
     * @param criteria the {@link TotalPositionProgressionCriteria} to use
     */
    public TotalPositionProgressionService(TotalPositionProgressionCriteria criteria) {

        // Convert the criteria into the parts needed to generate the bitting list.
        if (criteria != null) {

            /*
             * No need to validate the criteria because they use a builder that guarantees
             * that all criteria it generates are valid.
             */

            // Get the master cuts.
            masterCuts = criteria.getMasterCuts();
            logger.debug("The master cuts: {}.", Arrays.toString(masterCuts));

            // Get the progression steps.
            progressionSteps = criteria.getProgressionSteps();
            for (int step = 0; step < progressionSteps.length; step++)
                logger.debug("The progression steps: {}.", Arrays.toString(progressionSteps[step]));

            // Get the progression sequence.
            progressionSequence = criteria.getProgressionSequence();
            logger.debug("The progression sequence: {}.", Arrays.toString(progressionSequence));

            // Get the MACS.
            macs = criteria.getMacs();
            logger.debug("The MACS: {}.", macs);

            // Calculate the step count.
            stepCount = progressionSteps.length;
            logger.debug("The step count: {}.", stepCount);

            // Calculate the cut count.
            cutCount = progressionSequence.length;
            logger.debug("The cut count: {}.", cutCount);

            /**
             * Generate the cut order array from the progression sequence.
             * The cut order array determines the order in which the progression steps are applied during the progression
             * process.
             *
             * e.g.:
             *  [1, 2, 3, 4, 5] becomes [0, 1, 2, 3, 4]
             *  [5, 4, 3, 2, 1] becomes [4, 3, 2, 1, 0]
             *  [3, 5, 2, 1, 4] becomes [3, 2, 0, 4, 1]
             */
            cutOrder = new int[cutCount];
            for (int cut = 0; cut < cutCount; cut++)
                cutOrder[progressionSequence[cut] - 1] = cut;
            logger.debug("The cut order: {}.", Arrays.toString(cutOrder));
        }

        // There are no criteria, so log an error
        else
            logger.error("No criteria were passed to the constructor.");
    }

    /**
     * Generates a bitting list using the Total Position Progression technique.
     *
     * TODO: Need .generateBittingList() tests.
     *
     * @return the newly generated {@link BittingList}
     * @throws ProgressionServiceException if any error occurs
     */
    public BittingList generateBittingList()
        throws ProgressionServiceException {

        // Validate that the criteria have been loaded.
        if (cutOrder == null) {

            final String errorMessage = "Could not generate the bitting list; no progression criteria.";
            logger.error(errorMessage);
            throw new ProgressionServiceException(errorMessage);
        }

        // Progress the system.
        BittingGroup rootBittingGroup = doProgression();

        // The bitting list to construct and return.
        BittingList bittingList = new BittingList();
        bittingList.setRootBittingGroup(rootBittingGroup);

        // Return the bitting list.
        return bittingList;
    }

    /**
     * Starts the progression to generate the bitting list.
     * Creates the bitting list then recursively progresses all levels.
     *
     * @return the bitting list's root {@link BittingGroup}
     */
    private BittingGroup doProgression() {

//        // Instantiate the root bitting group
//        BittingGroup bittingGroup = new BittingGroup();
//
//        // Progress the system recursively, filling out the root bitting group.
//        progressBlock(0, bittingGroup);

        // TODO: Need to make progression recursive.

        // TODO: Need to set the master keys into the groups.

        int[] levels = new int[cutCount];
        int[] cuts = new int[cutCount];

        BittingGroup bittingGroup5 = new BittingGroup();
        bittingGroup5.setMaster(masterCuts);
        BittingNode[] bittingNodes5 = new BittingGroup[stepCount];
        bittingGroup5.setGroups(bittingNodes5);

        for (cuts[5] = 0; cuts[5] < stepCount; cuts[5]++) {

            levels[cutOrder[5]] = cuts[5];

            BittingGroup bittingGroup4 = new BittingGroup();
            bittingNodes5[cuts[5]] = bittingGroup4;
//            bittingGroup4.setMaster();
            BittingNode[] bittingNodes4 = new BittingGroup[stepCount];
            bittingGroup4.setGroups(bittingNodes4);

            for (cuts[4] = 0; cuts[4] < stepCount; cuts[4]++) {

                levels[cutOrder[4]] = cuts[4];

                BittingGroup bittingGroup3 = new BittingGroup();
                bittingNodes4[cuts[4]] = bittingGroup3;
//                bittingGroup3.setMaster();
                BittingNode[] bittingNodes3 = new BittingGroup[stepCount];
                bittingGroup3.setGroups(bittingNodes3);

                for (cuts[3] = 0; cuts[3] < stepCount; cuts[3]++) {

                    levels[cutOrder[3]] = cuts[3];

                    BittingGroup bittingGroup2 = new BittingGroup();
                    bittingNodes3[cuts[3]] = bittingGroup2;
//                    bittingGroup2.setMaster();
                    BittingNode[] bittingNodes2 = new BittingGroup[stepCount];
                    bittingGroup2.setGroups(bittingNodes2);

                    for (cuts[2] = 0; cuts[2] < stepCount; cuts[2]++) {

                        levels[cutOrder[2]] = cuts[2];

                        BittingGroup bittingGroup1 = new BittingGroup();
                       bittingNodes2[cuts[2]] = bittingGroup1;
//                        bittingGroup1.setMaster();
                        BittingNode[] bittingNodes1 = new BittingGroup[stepCount];
                        bittingGroup1.setGroups(bittingNodes1);

                        for (cuts[1] = 0; cuts[1] < stepCount; cuts[1]++) {

                            levels[cutOrder[1]] = cuts[1];

                            BittingGroup bittingGroup0 = new BittingGroup();
                            bittingNodes1[cuts[1]] = bittingGroup0;
//                            bittingGroup0.setMaster();
                            BittingNode[] bittingNodes0 = new KeyBitting[stepCount];
                            bittingGroup0.setGroups(bittingNodes0);

                            for (cuts[0] = 0; cuts[0] < stepCount; cuts[0]++) {

                                levels[cutOrder[0]] = cuts[0];

                                KeyBitting keyBitting = generateKey(levels);
                                bittingNodes0[cuts[0]] = keyBitting;
                            }
                        }
                    }
                }
            }
        }

        // Return the filled-out root bitting group.
//        return bittingGroup;
        return bittingGroup5;
    }

    /**
     * Progresses the block with the given level.
     *
     * @param blockLevel the block level to progress
     * @param bittingGroup the {@link BittingGroup} to fill out
     */
    private void progressBlock(int blockLevel, BittingGroup bittingGroup) {

        // TODO: Need to progress the block.

        if (blockLevel < cutCount)
            progressBlock(blockLevel + 1, bittingGroup);
        else
            return;
    }

    /**
     * Generates a {@link KeyBitting} for the given set of levels.
     *
     * @param levels the levels to use
     * @return the newly generated key \
     */
    private KeyBitting generateKey(int[] levels) {

        // Generate the key depths.
        int[] depths = new int[cutCount];
        for (int cut = 0; cut < cutCount; cut++)
            depths[cut] = progressionSteps[levels[cut]][cut];

        // Instantiate the key and test for a MACS violation.
        KeyBitting keyBitting = new KeyBitting(depths);
        keyBitting.testForMACSViolation(macs);

        // Return the key.
        return keyBitting;
    }
}
