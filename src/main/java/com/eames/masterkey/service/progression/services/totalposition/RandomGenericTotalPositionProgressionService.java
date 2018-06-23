package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.model.BittingList;
import com.eames.masterkey.service.ProcessingCapability;
import com.eames.masterkey.service.ValidationException;
import com.eames.masterkey.service.progression.ProgressionService;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import com.google.gson.*;
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
 */
public class RandomGenericTotalPositionProgressionService
        implements ProgressionService {

    /*
     * The configuration constants
     */

    // The capability that gets injected during the validation operation
    private static final String CAPABILITY_KEY = "capability";

    // The cut count configuration
    private static final String CUT_COUNT_KEY = "cutCount";
    private static final int CUT_COUNT_MIN = 3;
    private static final int CUT_COUNT_MAX = 7;

    // The depth count configuration
    private static final String DEPTH_COUNT_KEY = "depthCount";
    private static final int DEPTH_COUNT_MIN = 3;
    private static final int DEPTH_COUNT_MAX = 10;

    // The starting depth configuration
    private static final String STARTING_DEPTH_KEY = "startingDepth";
    private static final int STARTING_DEPTH_MIN = 0;
    private static final int STARTING_DEPTH_MAX = 1;

    // The double step progression configuration
    private static final String DOUBLE_STEP_PROGRESSION_KEY = "doubleStepProgression";

    // The Maximum Adjacent Cut Specification
    private static final String MACS_KEY = "macs";
    private static final int MACS_MIN = 1;
    private static final int MACS_MAX = 10;

    // Initialize the Log4j logger.
    private static final Logger logger = LogManager.getLogger(RandomGenericTotalPositionProgressionService.class);

    /*
     * Overridden {@link ProgressionService} operations
     */

    @Override
    public ProcessingCapability canProcessConfigs(String configs) {

        logger.info("Verifying that this service can process the configurations.");

        // Convert the JSON string in to a JSONObject and validate.
        // Pass a 'true' to indicate that we're in the 'check' phase.
        JSONObject jsonConfigs = getJSONConfigs(configs, true);

        // Return the capability.
        // Throws: JSONException
        return (ProcessingCapability) jsonConfigs.get(CAPABILITY_KEY);
    }

    @Override
    public String generateBittingList(String configs)
            throws ProgressionServiceException {

        logger.info("Generating a bitting list using the {} service.", getName());

        // Convert the JSON string in to a JSONObject and validate.
        // Pass a 'false' to indicate that we're in the 'generate' phase.
        JSONObject jsonConfigs = getJSONConfigs(configs, false);

        // This service cannot process the given configs.
        // Throws: JSONException
        Object capabilityObj = jsonConfigs.get(CAPABILITY_KEY);
        if (ProcessingCapability.NO.equals(capabilityObj)) {

            final String errorMessage = "Configurations not valid for this service.";
            logger.error(errorMessage);
            throw new ProgressionServiceException(errorMessage);
        }

        // Generate the progression criteria from the configs.
        // Throws: ProgressionServiceException
        TotalPositionProgressionCriteria criteria = generateProgressionCriteria(jsonConfigs);

        // Construct a progression service.
        TotalPositionProgressionService service = new TotalPositionProgressionService(criteria);

        // Generate the bitting list.
        // Throws: ProgressionServiceException
        BittingList bittingList = service.generateBittingList();

        // Construct the results to be returned.
        TotalPositionServiceResults results = new TotalPositionServiceResults(getName(), criteria, bittingList);

        // TODO: Should this translation to JSON be moved to the HttpGateway class?
        // TODO: Create a base class for the return results.

        /*
         * Construct a gson instance using the gson builder.
         * Specify that int arrays should be serialized as strings.
         */
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(int[].class, (JsonSerializer<int[]>) (src, type, jsonSerializationContext) -> {
                    StringBuilder sb = new StringBuilder();
                    for (int v : src)
                        sb.append(v);
                    return new JsonPrimitive(sb.toString());
                })
                .create();

        // Serialize the results to JSON and return them.
        return gson.toJson(results);
    }

    @Override
    public String getName() {

        return "Random Generic Total Position Progression Service";
    }

    /*
     * Local operations
     */

    /**
     * Validates the given JSON string and converts it into a JSONObject.
     * The JSONObject returned will never be {@code null} and will always contain a capability. If the capability is
     * {@code ProcessingCapability.YES} or {@code ProcessingCapability.MAYBE}, then the JSONObject contains valid
     * configs.
     *
     * This operation logs debug messages when in the 'check' phase and error messages when in the 'generate' phase.
     *
     * @param configs the JSON configs string to validate and convert
     * @param checkPhase {@code True} if this operation is being called during the 'check' phase,
     * {@code false} if it is being called during the 'generate' phase.
     * @return the JSONObject constructed from the JSON configs string, including the
     * {@link ProcessingCapability}
     */
    private JSONObject getJSONConfigs(String configs, boolean checkPhase) {

        if (checkPhase)
            logger.info("Verifying that this service can process the configurations.");

        // The JSON object to return.
        // This JSON object will have the capability injected into it before being returned.
        JSONObject jsonConfigs = null;

        do {

            if (configs == null) {

                String errorMessage = "No configurations provided.";
                if (checkPhase)
                    logger.debug(errorMessage);
                else
                    logger.error(errorMessage);

                break;
            }

            try {

                // Instantiate a JSON object from the string.
                // Throws: JSONException
                jsonConfigs = new JSONObject(configs);

            } catch (JSONException ex) {

                String errorMessage = "Could not parse the configuration string into JSON. Cause: {}";
                if (checkPhase)
                    logger.debug(errorMessage, ex.getMessage());
                else
                    logger.error(errorMessage, ex.getMessage());

                break;
            }

            /*
             * Validate that the configurations contain the cut count attribute and that its value is valid.
             */

            Object cutCountObj;
            try {

                // Get the cut count.
                // Throws: JSONException
                cutCountObj = jsonConfigs.get(CUT_COUNT_KEY);

            } catch (JSONException ex) {

                String errorMessage = "Missing '{}' configuration. Cause: {}";
                if (checkPhase)
                    logger.debug(errorMessage, CUT_COUNT_KEY, ex.getMessage());
                else
                    logger.error(errorMessage, CUT_COUNT_KEY, ex.getMessage());

                break;
            }
            if (!(cutCountObj instanceof Integer)) {

                String errorMessage = "The '{}' configuration is not an integer.";
                if (checkPhase)
                    logger.debug(errorMessage, CUT_COUNT_KEY);
                else
                    logger.error(errorMessage, CUT_COUNT_KEY);

                break;
            }
            Integer cutCount = (Integer) cutCountObj;
            if ((cutCount < CUT_COUNT_MIN) || (cutCount > CUT_COUNT_MAX)) {

                String errorMessage = "The '{}' configuration is out of range ({}) [{}, {}].";
                if (checkPhase)
                    logger.debug(errorMessage, CUT_COUNT_KEY, cutCount, CUT_COUNT_MIN, CUT_COUNT_MAX);
                else
                    logger.error(errorMessage, CUT_COUNT_KEY, cutCount, CUT_COUNT_MIN, CUT_COUNT_MAX);

                break;
            }

            /*
             * Validate that the configurations contain the depth count attribute and that its value is valid.
             */

            Object depthCountObj;
            try {

                // Get the depth count.
                // Throws: JSONException
                depthCountObj = jsonConfigs.get(DEPTH_COUNT_KEY);

            } catch (JSONException ex) {

                String errorMessage = "Missing '{}' configuration. Cause: {}";
                if (checkPhase)
                    logger.debug(errorMessage, DEPTH_COUNT_KEY, ex.getMessage());
                else
                    logger.error(errorMessage, DEPTH_COUNT_KEY, ex.getMessage());

                break;
            }
            if (!(depthCountObj instanceof Integer)) {

                String errorMessage = "The '{}' configuration is not an integer.";
                if (checkPhase)
                    logger.debug(errorMessage, DEPTH_COUNT_KEY);
                else
                    logger.error(errorMessage, DEPTH_COUNT_KEY);

                break;
            }
            Integer depthCount = (Integer) depthCountObj;
            if ((depthCount < DEPTH_COUNT_MIN) || (depthCount > DEPTH_COUNT_MAX)) {

                String errorMessage = "The '{} configuration is out of range ({}) [{}, {}].";
                if (checkPhase)
                    logger.debug(errorMessage, DEPTH_COUNT_KEY, depthCount, DEPTH_COUNT_MIN, DEPTH_COUNT_MAX);
                else
                    logger.error(errorMessage, DEPTH_COUNT_KEY, depthCount, DEPTH_COUNT_MIN, DEPTH_COUNT_MAX);

                break;
            }

            /*
             * Validate that the configurations contain the starting depth attribute and that its value is valid.
             */

            Object startingDepthObj;
            try {

                // Get the starting depth.
                // Throws: JSONException
                startingDepthObj = jsonConfigs.get(STARTING_DEPTH_KEY);

            } catch (JSONException ex) {

                String errorMessage = "Missing '{}' configuration. Cause: {}";
                if (checkPhase)
                    logger.debug(errorMessage, STARTING_DEPTH_KEY, ex.getMessage());
                else
                    logger.error(errorMessage, STARTING_DEPTH_KEY, ex.getMessage());

                break;
            }
            if (!(startingDepthObj instanceof Integer)) {

                String errorMessage = "The '{}' configuration is not an integer.";
                if (checkPhase)
                    logger.debug(errorMessage, STARTING_DEPTH_KEY);
                else
                    logger.error(errorMessage, STARTING_DEPTH_KEY);

                break;
            }
            Integer startingDepth = (Integer) startingDepthObj;
            if ((startingDepth < STARTING_DEPTH_MIN) || (startingDepth > STARTING_DEPTH_MAX)) {

                String errorMessage = "The '{}' configuration is out of range ({}) [{}, {}].";
                if (checkPhase)
                    logger.debug(errorMessage, STARTING_DEPTH_KEY, startingDepth, STARTING_DEPTH_MIN,
                            STARTING_DEPTH_MAX);
                else
                    logger.error(errorMessage, STARTING_DEPTH_KEY, startingDepth, STARTING_DEPTH_MIN,
                            STARTING_DEPTH_MAX);

                break;
            }

            /*
             * Validate that the configurations contain the double step progression attribute and that its value is valid.
             */

            Object doubleStepProgressionObj;
            try {

                // Get the double step progression flag.
                // Throws: JSONException
                doubleStepProgressionObj = jsonConfigs.get(DOUBLE_STEP_PROGRESSION_KEY);

            } catch (JSONException ex) {

                String errorMessage = "Missing '{}' configuration. Cause: {}";
                if (checkPhase)
                    logger.debug(errorMessage, DOUBLE_STEP_PROGRESSION_KEY, ex.getMessage());
                else
                    logger.error(errorMessage, DOUBLE_STEP_PROGRESSION_KEY, ex.getMessage());

                break;
            }
            if (!(doubleStepProgressionObj instanceof Boolean)) {

                String errorMessage = "The '{}' configuration is not a boolean.";
                if (checkPhase)
                    logger.debug(errorMessage, DOUBLE_STEP_PROGRESSION_KEY);
                else
                    logger.error(errorMessage, DOUBLE_STEP_PROGRESSION_KEY);

                break;
            }

            /*
             * Validate that the configurations contain the MACS attribute and that its value is valid.
             */

            Object macsObj;
            try {

                // Get the MACS.
                // Throws: JSONException
                macsObj = jsonConfigs.get(MACS_KEY);

            } catch (JSONException ex) {

                String errorMessage = "Missing '{}' configuration. Cause: {}";
                if (checkPhase)
                    logger.debug(errorMessage, MACS_KEY, ex.getMessage());
                else
                    logger.error(errorMessage, MACS_KEY, ex.getMessage());

                break;
            }
            if (!(macsObj instanceof Integer)) {

                String errorMessage = "The '{}' configuration is not an integer.";
                if (checkPhase)
                    logger.debug(errorMessage, MACS_KEY);
                else
                    logger.error(errorMessage, MACS_KEY);

                break;
            }
            Integer macs = (Integer) macsObj;
            if ((macs < MACS_MIN) || (macs > MACS_MAX)) {

                String errorMessage = "The '{}' configuration is out of range ({}) [{}, {}].";
                if (checkPhase)
                    logger.debug(errorMessage, MACS_KEY, macs, MACS_MIN, MACS_MAX);
                else
                    logger.error(errorMessage, MACS_KEY, macs, MACS_MIN, MACS_MAX);

                break;
            }

            // The configurations contain extra, unrecognized attributes.
            if (jsonConfigs.keySet().size() > 5) {

                if (checkPhase)
                    logger.info("This service can process the configurations if necessary.");

                String errorMessage = "The configurations contain the following attributes that will be ignored:";
                if (checkPhase)
                    logger.debug(errorMessage);
                else
                    logger.error(errorMessage);

                jsonConfigs.keySet().stream()
                        .filter(k -> (!k.equals(CUT_COUNT_KEY) && !k.equals(DEPTH_COUNT_KEY) &&
                                !k.equals(STARTING_DEPTH_KEY) && !k.equals(DOUBLE_STEP_PROGRESSION_KEY) &&
                                !k.equals(MACS_KEY)))
                        .forEach(k -> {

                            String errorMessage2 = "Key: {}";
                            if (checkPhase)
                                logger.debug(errorMessage2, k);
                            else
                                logger.error(errorMessage2, k);
                        });

                // Inject the 'maybe' capability into the JSON object.
                jsonConfigs.put(CAPABILITY_KEY, ProcessingCapability.MAYBE);
            }

            // The configuration is a match.
            else {

                if (checkPhase)
                    logger.info("This service prefers to process the configurations.");

                // Inject the 'yes' capability into the JSON object.
                jsonConfigs.put(CAPABILITY_KEY, ProcessingCapability.YES);
            }

            // Return the JSON object.
            return jsonConfigs;
        }
        while (false);

        if (checkPhase)
            logger.info("This service CANNOT process the configurations.");

        // Construct an empty JSON object if necessary.
        if (jsonConfigs == null)
            jsonConfigs = new JSONObject();

        // Inject the 'no' capability into the JSON object.
        jsonConfigs.put(CAPABILITY_KEY, ProcessingCapability.NO);

        // Return the JSON object.
        return jsonConfigs;
    }

    /**
     * Generates a random set of Total Position Progression criteria from the given configs.
     *
     * @param jsonConfigs the configs to use
     * @return the newly generated progression criteria
     * @throws ProgressionServiceException if any error occurs
     */
    private TotalPositionProgressionCriteria generateProgressionCriteria(JSONObject jsonConfigs)
        throws ProgressionServiceException {

        // Get the MACS.
        // Throws: JSONException
        int macs = (Integer) jsonConfigs.get(MACS_KEY);
        logger.debug("Generating progression criteria with a MACS of '{}'.", macs);

        // TODO: Need to seed the random number generator.
        // Create a random number generator.
        Random random = new Random();

        /*
         * Get the configs.
         */

        // Get the cut count.
        // Throws: JSONException
        int cutCount = (Integer) jsonConfigs.get(CUT_COUNT_KEY);
        logger.debug("Generating progression criteria with {} cuts.", cutCount);

        // Get the depth count.
        // Throws: JSONException
        int depthCount = (Integer) jsonConfigs.get(DEPTH_COUNT_KEY);
        logger.debug("Generating progression criteria with {} depths.", depthCount);

        // Get the starting depth.
        // Throws: JSONException
        int startingDepth = (Integer) jsonConfigs.get(STARTING_DEPTH_KEY);
        logger.debug("Generating progression criteria with a starting depth of '{}'.", startingDepth);

        // Get the double step progression flag.
        // Throws: JSONException
        boolean doubleStepProgression = (boolean) jsonConfigs.get(DOUBLE_STEP_PROGRESSION_KEY);
        logger.debug("Generating progression criteria with a {} step progression.",
                doubleStepProgression ? "double" : "single");

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

        try {

            // Build the progression criteria object from the randomly generated criteria.
            // Throws: ValidationException
            TotalPositionProgressionCriteria criteria = new TotalPositionProgressionCriteria.Builder()
                    .setMACS(macs)
                    .setMasterCuts(masterCuts)
                    .setProgressionSteps(progressionSteps)
                    .setProgressionSequence(progressionSequence)
                    .build();

            // Return the criteria.
            return criteria;

        } catch (ValidationException ex) {

            logger.error("Failed to build the progression criteria. Cause: {}", ex.getMessage());
            throw new ProgressionServiceException(ex.getMessage());
        }
    }

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
