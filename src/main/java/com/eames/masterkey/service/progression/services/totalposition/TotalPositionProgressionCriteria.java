package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.service.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents the set of criteria required to generate a
 * master key bitting list using a Total Position Progression technique.
 */
public class TotalPositionProgressionCriteria {

    // Initialize the Log4j logger.
    private static final Logger logger = LogManager.getLogger(TotalPositionProgressionCriteria.class);

    // The master key cuts
    private final int[] masterCuts;

    // The progression steps
    private final int[][] progressionSteps;

    // The progression sequence
    private final int[] progressionSequence;

    /**
     * Constructor
     * This constructor has been declared private so that it can only
     * be called from within the builder.
     *
     * @param masterCuts the master cuts to set
     * @param progressionSteps the progression steps to set
     * @param progressionSequence the progression sequence to set
     */
    private TotalPositionProgressionCriteria(int[] masterCuts, int[][] progressionSteps, int[] progressionSequence) {

        this.masterCuts = masterCuts;
        this.progressionSteps = progressionSteps;
        this.progressionSequence = progressionSequence;
    }

    /**
     * Gets the master cuts.
     *
     * @return the master cuts
     */
    public int[] getMasterCuts() {
        return masterCuts;
    }

    /**
     * Gets the progression steps.
     *
     * @return the progression steps
     */
    public int[][] getProgressionSteps() {
        return progressionSteps;
    }

    /**
     * Gets the progression sequence.
     *
     * @return the progression sequence
     */
    public int[] getProgressionSequence() {
        return progressionSequence;
    }

    /**
     * This class builds {@link TotalPositionProgressionCriteria} objects.
     * All objects built by this builder have been validated.
     */
    public static class Builder {

        /**
         * The attributes to be used to build the configs
         */
        private int[] masterCuts;
        private int[][] progressionSteps;
        private int[] progressionSequence;

        /**
         * Sets the master cuts.
         * Returns the {@link Builder} so these operations can be chained.
         *
         * @param masterCuts the new master cuts
         * @return this builder
         */
        public Builder setMasterCuts(int[] masterCuts) {

            this.masterCuts = masterCuts;
            return this;
        }

        /**
         * Sets the progression steps
         * Returns the {@link Builder} so these operations can be chained.
         *
         * @param progressionSteps the new progression steps
         * @return this builder
         */
        public Builder setProgressionSteps(int[][] progressionSteps) {

            this.progressionSteps = progressionSteps;
            return this;
        }

        /**
         * Sets the progression sequence
         * Returns the {@link Builder} so these operations can be chained.
         *
         * @param progressionSequence the new progression sequence
         * @return this builder
         */
        public Builder setProgressionSequence(int[] progressionSequence) {

            this.progressionSequence = progressionSequence;
            return this;
        }

        /**
         * Validates the builder's attributes.
         *
         * @throws ValidationException if any of the attributes are
         * missing or invalid
         */
        private void validate ()
            throws ValidationException {

            /*
             * Make sure we have all the pieces.
             */
            if (masterCuts == null) {

                final String MISSING_MASTER = "The master key is missing.";
                logger.error(MISSING_MASTER);
                throw new ValidationException(MISSING_MASTER);
            }
            else if (progressionSteps == null) {

                final String MISSING_PROGRESSION_STEPS = "The progression steps are missing.";
                logger.error(MISSING_PROGRESSION_STEPS);
                throw new ValidationException(MISSING_PROGRESSION_STEPS);
            }
            else if (progressionSequence == null) {

                final String MISSING_PROGRESSION_SEQUENCE = "The progression sequence is missing.";
                logger.error(MISSING_PROGRESSION_SEQUENCE);
                throw new ValidationException(MISSING_PROGRESSION_SEQUENCE);
            }

            /*
             * The master key must have at least one column.
             */
            if (masterCuts.length < 1) {

                final String MASTER_EMPTY = "The master key must have at least one cut.";
                logger.error(MASTER_EMPTY);
                throw new ValidationException(MASTER_EMPTY);
            }

            /*
             * All the configs must have the same number of cuts.
             */
            for (int[] row : progressionSteps) {
                if (row.length != masterCuts.length) {

                    final String PROG_STEP_CUTS_DONT_MATCH_MASTER_CUTS = "The progression steps do not have the same " +
                            "number of cuts as the master key.";
                    logger.error(PROG_STEP_CUTS_DONT_MATCH_MASTER_CUTS + " (" + row.length + ", " +
                            masterCuts.length + ")");
                    throw new ValidationException(PROG_STEP_CUTS_DONT_MATCH_MASTER_CUTS);
                }
            }
            if (progressionSequence.length != masterCuts.length) {

                final String PROG_SEQ_CUTS_DONT_MATCH_MASTER_CUTS = "The progression sequence does not have the same " +
                        "number of cuts as the master key.";
                logger.error(PROG_SEQ_CUTS_DONT_MATCH_MASTER_CUTS + " (" + progressionSequence.length + ", " +
                        masterCuts.length + ")");
                throw new ValidationException(PROG_SEQ_CUTS_DONT_MATCH_MASTER_CUTS);
            }

            /*
             * The master key must not have any negative depths.
             */
            for (int col = 0; col < masterCuts.length; col++) {
                if (masterCuts[col] < 0) {

                    final String MASTER_DEPTH_NEGATIVE = "The master key contains a negative depth.";
                    logger.error(MASTER_DEPTH_NEGATIVE + " (cut=" + col + ", depth=" + masterCuts[col] + ")");
                    throw new ValidationException(MASTER_DEPTH_NEGATIVE);
                }
            }

            /*
             * Make sure that the progression steps do not contain any of the master key depths in the same column.
             */
            for (int col = 0; col < progressionSteps[0].length; col++) {

                // Count the number of times the progression step depth matches the master key depth.
                int masterMatchCount = 0;
                boolean duplicateSteps = false;
                for (int row = 0; row < progressionSteps.length; row++) {

                    // The progression step depth matches the master key depth, so bump the match count.
                    if (progressionSteps[row][col] == masterCuts[col])
                        masterMatchCount++;

                    // Compare this row's depth to the previous rows' depths.
                    for (int r2 = 0; r2 < row; r2++) {
                        if (progressionSteps[r2][col] == progressionSteps[row][col]) {
                            duplicateSteps = true;
                            break;
                        }
                    }
                }

                // The progression steps contain a master key depth in the same column
                // but not all depths match the master key depth.
                if ((masterMatchCount > 0) && (masterMatchCount != progressionSteps.length)) {

                    final String PROG_STEPS_CONTAIN_MASTER_DEPTH = "The progression steps contain a master key " +
                            "depth.";
                    logger.error(PROG_STEPS_CONTAIN_MASTER_DEPTH + " (cut=" + col + ", depth=" + masterCuts[col] +
                            ")");
                    throw new ValidationException(PROG_STEPS_CONTAIN_MASTER_DEPTH);
                }

                // The progression steps contain duplicate depths in the same column but no master key depths.
                else if (duplicateSteps && (masterMatchCount == 0)){

                    final String PROG_STEPS_CONTAIN_DUPLICATES = "The progression steps contain duplicate depths.";
                    logger.error(PROG_STEPS_CONTAIN_DUPLICATES + " (cut=" + col + ", depth=" + masterCuts[col] +
                            ")");
                    throw new ValidationException(PROG_STEPS_CONTAIN_DUPLICATES);
               }
            }

            /*
             * The progression sequence must contain exactly every position between 1 and the number of cuts.
             */
            for (int col = 0; col < progressionSequence.length; col++) {

                // The position is invalid.
                if ((progressionSequence[col] < 1) || (progressionSequence[col] > progressionSequence.length)) {

                    final String PROG_SEQ_POSITION_INVALID = "The progression sequence contains an invalid position.";
                    logger.error(PROG_SEQ_POSITION_INVALID + " (cut=" + col + ", position=" + progressionSequence[col] +
                            ")");
                    throw new ValidationException(PROG_SEQ_POSITION_INVALID);
                }

                // The position is valid.
                else {

                    // Loop through all previous positions, looking for a duplicate.
                    for (int c2 = 0; c2 < col; c2++) {
                        if (progressionSequence[col] == progressionSequence[c2]) {

                            final String PROG_SEQ_POSITION_DUPLICATED = "The progression sequence position is " +
                                    "duplicated.";
                            logger.error(PROG_SEQ_POSITION_DUPLICATED + " (cut=" + col + ", position=" +
                                    progressionSequence[col] + ")");
                            throw new ValidationException(PROG_SEQ_POSITION_DUPLICATED);
                        }
                    }
                }
            }
        }

        /**
         * Builds a {@link TotalPositionProgressionCriteria} object from the
         * attributes that have been set into it.
         *
         * @return the newly constructed {@link TotalPositionProgressionCriteria} object.
         * @throws ValidationException if any of the attributes are
         * missing or invalid
         */
        public TotalPositionProgressionCriteria build()
            throws ValidationException {

            // Validate the attributes.
            // Throws: ValidationException
            validate();

            // Construct and return the configs.
            return new TotalPositionProgressionCriteria(masterCuts, progressionSteps, progressionSequence);
        }
    }
}
