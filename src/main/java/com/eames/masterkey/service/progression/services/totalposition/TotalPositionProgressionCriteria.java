package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.service.ValidationException;
import com.eames.masterkey.service.progression.ProgressionCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represents the set of criteria required to generate a
 * master key bitting list using a Total Position Progression technique.
 */
public class TotalPositionProgressionCriteria implements ProgressionCriteria {

    // Initialize the Log4j logger.
    private static final Logger logger = LogManager.getLogger(TotalPositionProgressionCriteria.class);

    /*
     * Validation constants
     */

    // The cut count validation range.
    public static final int CUT_COUNT_MIN = 3;
    public static final int CUT_COUNT_MAX = 7;

    // The starting depth validation range.
    public static final int STARTING_DEPTH_MIN = 0;
    public static final int STARTING_DEPTH_MAX = 1;

    // The MACS validation range.
    public static final int MACS_MIN = 1;
    public static final int MACS_MAX = 10;

    /*
     * Criteria attributes
     */

    // The Maximum Adjacent Cut Specification (MACS)
    private final int macs;

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
     * @param macs the MACS value to set
     * @param masterCuts the master cuts to set
     * @param progressionSteps the progression steps to set
     * @param progressionSequence the progression sequence to set
     */
    private TotalPositionProgressionCriteria(int macs, int[] masterCuts, int[][] progressionSteps, int[] progressionSequence) {

        this.macs = macs;
        this.masterCuts = masterCuts;
        this.progressionSteps = progressionSteps;
        this.progressionSequence = progressionSequence;
    }

    /**
     * Gets the MACS.
     *
     * @return the MACS
     */
    public int getMacs() {
        return macs;
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
        private int macs;
        private int[] masterCuts;
        private int[][] progressionSteps;
        private int[] progressionSequence;

        /**
         * Sets the MACS.
         * Returns the {@link Builder} so these operations can be chained.
         *
         * @param macs the new MACS
         * @return this builder
         */
        public Builder setMACS(int macs) {

            this.macs = macs;
            return this;
        }

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
             * The MACS must be within range.
             */
            if (!validateMACS(macs)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The MACS is out of range (");
                sb.append(macs);
                sb.append(") [");
                sb.append(TotalPositionProgressionCriteria.MACS_MIN);
                sb.append(", ");
                sb.append(TotalPositionProgressionCriteria.MACS_MAX);
                sb.append("].");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }

            /*
             * The master key cuts must be within range.
             */
            if (!validateCutCount(masterCuts.length)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The master key has an invalid number of cuts (");
                sb.append(masterCuts.length);
                sb.append(") [");
                sb.append(TotalPositionProgressionCriteria.CUT_COUNT_MIN);
                sb.append(", ");
                sb.append(TotalPositionProgressionCriteria.CUT_COUNT_MAX);
                sb.append("].");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }

            /*
             * All the configs must have the same number of cuts.
             */
            for (int[] row : progressionSteps) {
                if (row.length != masterCuts.length) {

                    StringBuilder sb = new StringBuilder();
                    sb.append("The progression steps do not have the same number of cuts as the master key (");
                    sb.append(row.length);
                    sb.append(", ");
                    sb.append(masterCuts.length);
                    sb.append(").");
                    String errorMessage = sb.toString();
                    logger.error(errorMessage);
                    throw new ValidationException(errorMessage);
                }
            }
            if (progressionSequence.length != masterCuts.length) {

                StringBuilder sb = new StringBuilder();
                sb.append("The progression sequence does not have the same number of cuts as the master key (");
                sb.append(progressionSequence.length);
                sb.append(", ");
                sb.append(masterCuts.length);
                sb.append(").");
                String errorMessage = sb.toString();
                logger.error(errorMessage);
                throw new ValidationException(errorMessage);
            }

            /*
             * The master key must not have any negative depths.
             */
            for (int col = 0; col < masterCuts.length; col++) {
                if (masterCuts[col] < 0) {

                    StringBuilder sb = new StringBuilder();
                    sb.append("The master key contains a negative depth (cut=");
                    sb.append(col);
                    sb.append(", depth=");
                    sb.append(masterCuts[col]);
                    sb.append(").");
                    String errorMessage = sb.toString();
                    logger.error(errorMessage);
                    throw new ValidationException(errorMessage);
                }
            }

            /*
             * Make sure that the progression steps do not contain any of the master key depths in the same column.
             */
            for (int col = 0; col < progressionSteps[0].length; col++) {

                // Count the number of times the progression step depth matches the master key depth.
                for (int row = 0; row < progressionSteps.length; row++) {

                    // The progression step depth matches the master key depth, so bump the match count.
                    if (progressionSteps[row][col] == masterCuts[col]) {

                        StringBuilder sb = new StringBuilder();
                        sb.append("The progression steps contain a master key depth (cut=");
                        sb.append(col);
                        sb.append(", step=");
                        sb.append(row);
                        sb.append(", depth=");
                        sb.append(progressionSteps[row][col]);
                        sb.append(").");
                        String errorMessage = sb.toString();
                        logger.error(errorMessage);
                        throw new ValidationException(errorMessage);
                    }

                    // Compare this row's depth to the previous rows' depths.
                    for (int r2 = 0; r2 < row; r2++) {
                        if (progressionSteps[r2][col] == progressionSteps[row][col]) {

                            StringBuilder sb = new StringBuilder();
                            sb.append("The progression steps contain duplicate depths (cut=");
                            sb.append(col);
                            sb.append(", step=");
                            sb.append(row);
                            sb.append(", depth=");
                            sb.append(progressionSteps[row][col]);
                            sb.append(").");
                            String errorMessage = sb.toString();
                            logger.error(errorMessage);
                            throw new ValidationException(errorMessage);
                        }
                    }
                }
            }

            /*
             * The progression sequence must contain exactly every position between 1 and the number of cuts.
             */
            for (int col = 0; col < progressionSequence.length; col++) {

                // The position is invalid.
                if ((progressionSequence[col] < 1) || (progressionSequence[col] > progressionSequence.length)) {

                    StringBuilder sb = new StringBuilder();
                    sb.append("The progression sequence contains an invalid position (cut=");
                    sb.append(col);
                    sb.append(", position=");
                    sb.append(progressionSequence[col]);
                    sb.append(").");
                    String errorMessage = sb.toString();
                    logger.error(errorMessage);
                    throw new ValidationException(errorMessage);
                }

                // The position is valid.
                else {

                    // Loop through all previous positions, looking for a duplicate.
                    for (int c2 = 0; c2 < col; c2++) {
                        if (progressionSequence[col] == progressionSequence[c2]) {

                            StringBuilder sb = new StringBuilder();
                            sb.append("The progression sequence position is duplicated (cut=");
                            sb.append(col);
                            sb.append(", position=");
                            sb.append(progressionSequence[col]);
                            sb.append(").");
                            String errorMessage = sb.toString();
                            logger.error(errorMessage);
                            throw new ValidationException(errorMessage);
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
            return new TotalPositionProgressionCriteria(macs, masterCuts, progressionSteps, progressionSequence);
        }
    }

    /*
     * Class operations
     */

    /**
     * Validates the given cut count.
     *
     * @param cutCount the cut count to validate
     * @return {@code True} if the cut count is valid, {@code false} if not.
     */
    public static boolean validateCutCount(int cutCount) {

       return ((cutCount >= CUT_COUNT_MIN) && (cutCount <= CUT_COUNT_MAX));
    }

    /**
     * Validates the given starting depth.
     *
     * @param startingDepth the starting depth to validate
     * @return {@code True} if the starting depth is valid, {@code false} if not.
     */
    public static boolean validateStartingDepth(int startingDepth) {

        return ((startingDepth >= STARTING_DEPTH_MIN) && (startingDepth <= STARTING_DEPTH_MAX));
    }

    /**
     * Validates the given MACS.
     *
     * @param macs the MACS to validate
     * @return {@code True} if the MACS is valid, {@code false} if not.
     */
    public static boolean validateMACS(int macs) {

        return ((macs >= MACS_MIN) && (macs <= MACS_MAX));
    }
}
