package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.model.BittingGroup;
import com.eames.masterkey.model.BittingList;
import com.eames.masterkey.model.BittingNode;
import com.eames.masterkey.model.KeyBitting;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            for (int[] progressionStepRow : progressionSteps)
                logger.debug("The progression steps: {}.", Arrays.toString(progressionStepRow));

            // Get the progression sequence.
            int[] progressionSequence = criteria.getProgressionSequence();
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

            /*
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

        // Instantiate the root bitting group
        BittingGroup rootBittingGroup = new BittingGroup();

        // Holds the current step level for each cut (initializes each to 0).
        int[] levels = new int[cutCount];

        // Start progressing at the top-level block.
        progressBlock(cutCount, levels, rootBittingGroup);

        // Return the filled-out root bitting group.
        return rootBittingGroup;
    }

    /**
     * Progresses the block with the given block level.
     *
     * @param blockLevel the block level to progress [1, cutCount])
     * @param levels the set of block current levels
     * @param parentBittingGroup the parent {@link BittingGroup} to fill out
     */
    private void progressBlock(int blockLevel, int[] levels, BittingGroup parentBittingGroup) {

        // Decrement the block level.
        blockLevel--;

        // Set the parent bitting group's master.
        parentBittingGroup.setMaster(generateMasterKey(blockLevel, levels));

        // We're still processing groups.
        if (blockLevel > 0) {

            // Create the next level bitting group's nodes.
            BittingNode[] bittingNodes = new BittingGroup[stepCount];
            parentBittingGroup.setGroups(bittingNodes);

            // Loop through the steps.
            for (int step = 0; step < stepCount; step++) {

                // Set the level.
                levels[cutOrder[blockLevel]] = step;

                // Create the next level bitting group and set it into the bitting node array.
                BittingGroup bittingGroup = new BittingGroup();
                bittingNodes[step] = bittingGroup;

                // Process the next level down.
                progressBlock(blockLevel, levels, bittingGroup);
            }
        }

        // We're processing the key group.
        else {

            // Create the key group's nodes.
            BittingNode[] bittingNodes = new KeyBitting[stepCount];
            parentBittingGroup.setGroups(bittingNodes);

            // Loop through the steps.
            for (int step = 0; step < stepCount; step++) {

                // Set the level.
                levels[cutOrder[blockLevel]] = step;

                KeyBitting keyBitting = generateChangeKey(levels);
                bittingNodes[step] = keyBitting;
            }
        }
    }

    /**
     * Generates a master key {@link KeyBitting} for the given level.
     *
     * @param blockLevel the level for which to generate the master key
     * @param levels the levels to use
     * @return the newly generated key
     */
    private KeyBitting generateMasterKey(int blockLevel, int[] levels) {

        // Generate the key depths for those cuts that come from the progression steps.
        int[] depths = new int[cutCount];
        for (int seq = 0; seq <= blockLevel; seq++)
            depths[cutOrder[seq]] = masterCuts[cutOrder[seq]];

        // Generate the key depths for those cuts that come from the master.
        for (int seq = blockLevel + 1; seq < cutCount; seq++)
            depths[cutOrder[seq]] = progressionSteps[levels[cutOrder[seq]]][cutOrder[seq]];

        // Instantiate the key, test it for a MACS violation, and return it.
        return new KeyBitting(depths, macs);
    }

    /**
     * Generates a change key {@link KeyBitting} for the given set of levels.
     *
     * @param levels the levels to use
     * @return the newly generated change key
     */
    private KeyBitting generateChangeKey(int[] levels) {

        // Generate the key depths.
        int[] depths = new int[cutCount];
        for (int cut = 0; cut < cutCount; cut++)
            depths[cut] = progressionSteps[levels[cut]][cut];

        // Instantiate the key, test it for a MACS violation, and return it.
        return new KeyBitting(depths, macs);
    }
}
