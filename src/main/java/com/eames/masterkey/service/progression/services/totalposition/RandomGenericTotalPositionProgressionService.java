package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.service.AutoRegister;
import com.eames.masterkey.service.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

/**
 * This class is responsible for generating a bitting list using the given cut count, depth count, starting depth,
 * progression step, and MACS contained in the JSON configurations passed to it.
 *
 * The configurations are used to generate a random set of progression criteria that are, in turn, used to generate
 * the bitting list using the Total Position Progression method.
 *
 * It expects the configurations to have the following format:
 * {
 *     cutCount : [3-7] // The number of cuts in the key
 *     depthCount : [3-10] // The number of cut depths
 *     startingDepth : [0, 1] // The value of the shallowest depth
 *     doubleStepProgression : [false, true] // Single or double step depth progression
 *     macs : [1-10] // The Maximum Adjacent Cut Specification
 * }
 *
 * This Progression service is automatically registered by the
 * {@link com.eames.masterkey.aws.gateway.http.BittingListHTTPGateway} at startup.
 */
@AutoRegister
public class RandomGenericTotalPositionProgressionService
        extends AbstractTotalPositionProgressionService {

    // The cut count configuration
    private static final String CUT_COUNT_KEY = "cutCount";

    // The depth count configuration
    private static final String DEPTH_COUNT_KEY = "depthCount";
    private static final int DEPTH_COUNT_MIN = 3;
    private static final int DEPTH_COUNT_MAX = 10;

    // The starting depth configuration
    private static final String STARTING_DEPTH_KEY = "startingDepth";

    // The double step progression configuration
    private static final String DOUBLE_STEP_PROGRESSION_KEY = "doubleStepProgression";
    private static final int DOUBLE_STEP_DEPTH_MIN = 4;

    // The Maximum Adjacent Cut Specification
    private static final String MACS_KEY = "macs";

    // Initialize the Log4j logger.
    private static final Logger logger = LogManager.getLogger(RandomGenericTotalPositionProgressionService.class);

    /**
     * Constructor
     */
    public RandomGenericTotalPositionProgressionService() {

        super("Random Generic Total Position Progression Service",
                new String[] { CUT_COUNT_KEY, DEPTH_COUNT_KEY, STARTING_DEPTH_KEY, DOUBLE_STEP_PROGRESSION_KEY,
                        MACS_KEY });
    }

    /*
     * Implemented {@link AbstractTotalPositionProgressionService} operations
     */

    @Override
    protected TotalPositionProgressionCriteria generateProgressionCriteria(JSONObject jsonConfigs)
        throws ValidationException {

        // The 'configs' will never be null.

        /*
         * Validate the configs.
         */

        try {

            /*
             * Validate that the 'cut count' attribute is valid.
             */

            // Get and validate the 'cut count'.
            // Throws: JSONException
            Object cutCountObj = jsonConfigs.get(CUT_COUNT_KEY);
            if (!(cutCountObj instanceof Integer)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(CUT_COUNT_KEY);
                sb.append("' configuration is not an integer.");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            Integer cutCount = (Integer) cutCountObj;
            if (!TotalPositionProgressionCriteria.validateCutCount(cutCount)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(CUT_COUNT_KEY);
                sb.append("' configuration is out of range (");
                sb.append(cutCount);
                sb.append(") [");
                sb.append(TotalPositionProgressionCriteria.CUT_COUNT_MIN);
                sb.append(", ");
                sb.append(TotalPositionProgressionCriteria.CUT_COUNT_MAX);
                sb.append("].");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            logger.debug("Generating progression criteria with {} cuts.", cutCount);

            /*
             * Validate that the 'depth count' attribute is valid.
             */

            // Get and validate the 'depth count'.
            // Throws: JSONException
            Object depthCountObj = jsonConfigs.get(DEPTH_COUNT_KEY);
            if (!(depthCountObj instanceof Integer)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(DEPTH_COUNT_KEY);
                sb.append("' configuration is not an integer.");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            Integer depthCount = (Integer) depthCountObj;
            if ((depthCount < DEPTH_COUNT_MIN) || (depthCount > DEPTH_COUNT_MAX)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(DEPTH_COUNT_KEY);
                sb.append("' configuration is out of range (");
                sb.append(depthCount);
                sb.append(") [");
                sb.append(DEPTH_COUNT_MIN);
                sb.append(", ");
                sb.append(DEPTH_COUNT_MAX);
                sb.append("].");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            logger.debug("Generating progression criteria with {} depths.", depthCount);

            /*
             * Validate that the 'starting depth' attribute is valid.
             */

            // Get and validate the 'starting depth'.
            // Throws: JSONException
            Object startingDepthObj = jsonConfigs.get(STARTING_DEPTH_KEY);
            if (!(startingDepthObj instanceof Integer)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(STARTING_DEPTH_KEY);
                sb.append("' configuration is not an integer.");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            Integer startingDepth = (Integer) startingDepthObj;
            if (!TotalPositionProgressionCriteria.validateStartingDepth(startingDepth)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(STARTING_DEPTH_KEY);
                sb.append("' configuration is out of range (");
                sb.append(startingDepth);
                sb.append(") [");
                sb.append(TotalPositionProgressionCriteria.STARTING_DEPTH_MIN);
                sb.append(", ");
                sb.append(TotalPositionProgressionCriteria.STARTING_DEPTH_MAX);
                sb.append("].");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            logger.debug("Generating progression criteria with a starting depth of '{}'.", startingDepth);

            /*
             * Validate that the 'double step progression' attribute is valid.
             */

            // Get and validate the 'double step progression' flag.
            // Throws: JSONException
            Object doubleStepProgressionObj = jsonConfigs.get(DOUBLE_STEP_PROGRESSION_KEY);
            if (!(doubleStepProgressionObj instanceof Boolean)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(DOUBLE_STEP_PROGRESSION_KEY);
                sb.append("' configuration is not a boolean.");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            Boolean doubleStepProgression = (Boolean) doubleStepProgressionObj;
            if (doubleStepProgression && ((depthCount % 2 != 0) || (depthCount < DOUBLE_STEP_DEPTH_MIN))) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(DOUBLE_STEP_PROGRESSION_KEY);
                sb.append("' configuration is not valid for an odd depth count or a depth count less than ");
                sb.append(DOUBLE_STEP_DEPTH_MIN);
                sb.append(" (");
                sb.append(depthCount);
                sb.append(").");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            logger.debug("Generating progression criteria with a {} step progression.",
                    doubleStepProgression ? "double" : "single");

            /*
             * Validate that the 'MACS' attribute is valid.
             */

            // Get and validate the 'MACS'.
            // Throws: JSONException
            Object macsObj = jsonConfigs.get(MACS_KEY);
            if (!(macsObj instanceof Integer)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(MACS_KEY);
                sb.append("' configuration is not an integer.");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            Integer macs = (Integer) macsObj;
            if (!TotalPositionProgressionCriteria.validateMACS(macs)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(MACS_KEY);
                sb.append("' configuration is out of range (");
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
            logger.debug("Generating progression criteria with a MACS of '{}'.", macs);

            /*
             * Generate the criteria.
             */

            // Create a random number generator.
            Random random = new Random();

            /*
             * Generate the master cuts.
             */

            /*
             * Generate the master cuts using random integers.
             * All cuts must be in the range: [startingDepth, depthCount + startingDepth - 1]
             * Adjacent cuts must honor the MACS.
             */
            int maxDepth = depthCount + startingDepth - 1;
            int[] masterCuts = new int[cutCount];
            masterCuts[0] = random.nextInt(depthCount) + startingDepth;
            for (int cut = 1; cut < cutCount; cut++) {

                int lastVal = masterCuts[cut - 1];
                int minVal = Integer.max(startingDepth, lastVal - macs);
                int maxVal = Integer.min(lastVal + macs, maxDepth);
                masterCuts[cut] = random.nextInt(maxVal - minVal + 1) + minVal;
            }
            logger.debug("Generated the master key: {}.", Arrays.toString(masterCuts));

            /*
             * Generate the progression steps.
             */

            /*
             * Generate the progression steps.
             * Progression steps never include the master cut.
             * Single step progressions use every depth.
             * Double step progressions use all odd or all even depths, matching the oddness/evenness of the master.
             */
            int stepCount = (doubleStepProgression ? (depthCount / 2) : depthCount) - 1;
            int[][] progressionSteps = new int[stepCount][cutCount];
            for (int cut = 0; cut < cutCount; cut++) {

                // Get the master cut value and its oddness/evenness.
                int masterCutValue = masterCuts[cut];
                boolean masterIsEven = (masterCutValue % 2) == 0;

                // Generate and shuffle the column's progression steps.
                int[] colSteps = new int[stepCount];
                int step = 0;
                for (int depth = 0; depth < depthCount; depth++) {

                    // The depth cannot match the master cut and double step progression
                    // depths must match the master's oddness/evenness.
                    int depthVal = depth + startingDepth;
                    if (depthVal != masterCutValue) {
                        if (!doubleStepProgression ||
                            (doubleStepProgression && (((depthVal % 2) == 0) == masterIsEven)))
                        colSteps[step++] = depthVal;
                    }
                }
                shuffle(colSteps, random);

                // Move the column's progression steps to the progression steps.
                for (step = 0; step < stepCount; step++)
                    progressionSteps[step][cut] = colSteps[step];
            }
            for (int row = 0; row < stepCount; row++)
                logger.debug("Generated the progression steps: {}.", Arrays.toString(progressionSteps[row]));

            /*
             * Generate the progression sequence.
             */

            // Generate and shuffle the progression sequence.
            int[] progressionSequence = new int[cutCount];
            for (int cut = 0; cut < cutCount; cut++)
                progressionSequence[cut] = cut + 1;
            shuffle(progressionSequence, random);
            logger.debug("Generated the progression sequence: {}.", Arrays.toString(progressionSequence));

            /*
             * Generate the progression criteria.
             */

            // Build the progression criteria object from the randomly generated criteria.
            // Throws: ValidationException
            TotalPositionProgressionCriteria criteria = new TotalPositionProgressionCriteria.Builder()
                    .setMACS(macs)
                    .setMasterCuts(masterCuts)
                    .setProgressionSteps(progressionSteps)
                    .setProgressionSequence(progressionSequence)
                    .setStartingDepth(startingDepth)
                    .build();

            // Return the criteria.
            return criteria;

        } catch (JSONException ex) {

            StringBuilder sb = new StringBuilder();
            sb.append("An unexpected error occurred. Cause: ");
            sb.append(ex.getMessage());
            String errorMessage = sb.toString();
            logger.error(errorMessage);

            throw new ValidationException(errorMessage);
        }
    }

    /*
     * Local operations
     */

    /**
     * Shuffles the given int array.
     *
     * @param array the array to shuffle
     * @param random the random number generator to use
     */
    private static void shuffle(int[] array, Random random) {

        for (int i = array.length - 1; i > 0; --i) {

            int j = random.nextInt(i + 1);
            int temp =  array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
