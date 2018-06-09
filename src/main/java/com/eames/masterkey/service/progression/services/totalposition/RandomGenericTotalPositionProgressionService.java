package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.model.BittingList;
import com.eames.masterkey.model.KeyBitting;
import com.eames.masterkey.service.ProcessingCapability;
import com.eames.masterkey.service.ValidationException;
import com.eames.masterkey.service.progression.ProgressionService;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * This class is responsible for generating a bitting list using the given cut count, depth count, starting depth,
 * progression step, and MACS contained in the JSON configurations passed to it.
 *
 * The configurations are used to generate a random set of progression criteria that are, in turn, used to generate
 * the bitting list using the Total Position Progression method.
 *
 * It expects the configurations to have the following format:
 * {
 *     cutCount : [5-7] // The number of cuts in the key
 *     depthCount : [5-10] // The number of cut depths
 *     startingDepth : [0, 1] // The value of the shallowest depth
 *     progressionStep : [1, 2] // The increment between depths used during progression
 *     macs : [4-9] // The Maximum Adjacent Cut Specification
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
    private static final int CUT_COUNT_MIN = 5;
    private static final int CUT_COUNT_MAX = 7;

    // The depth count configuration
    private static final String DEPTH_COUNT_KEY = "depthCount";
    private static final int DEPTH_COUNT_MIN = 5;
    private static final int DEPTH_COUNT_MAX = 10;

    // The starting depth configuration
    private static final String STARTING_DEPTH_KEY = "startingDepth";
    private static final int STARTING_DEPTH_MIN = 0;
    private static final int STARTING_DEPTH_MAX = 1;

    // The progression increment configuration
    private static final String PROGRESSION_STEP_KEY = "progressionStep";
    private static final int PROGRESSION_STEP_MIN = 1;
    private static final int PROGRESSION_STEP_MAX = 2;

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
        TotalPositionProgressionService service = new TotalPositionProgressionService();

        // Generate the bitting list.
        // Throws: ProgressionServiceException
        BittingList bittingList = service.generateBittingList(criteria);

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

        // Serialize the bitting list to JSON.
        String jsonBittingListStr = gson.toJson(bittingList);

        // Return the JSON bitting list.
        return jsonBittingListStr;
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
             * Validate that the configurations contain the progression step attribute and that its value is valid.
             */

            Object progressionStepObj;
            try {

                // Get the progression step.
                // Throws: JSONException
                progressionStepObj = jsonConfigs.get(PROGRESSION_STEP_KEY);

            } catch (JSONException ex) {

                String errorMessage = "Missing '{}' configuration. Cause: {}";
                if (checkPhase)
                    logger.debug(errorMessage, PROGRESSION_STEP_KEY, ex.getMessage());
                else
                    logger.error(errorMessage, PROGRESSION_STEP_KEY, ex.getMessage());

                break;
            }
            if (!(progressionStepObj instanceof Integer)) {

                String errorMessage = "The '{}' configuration is not an integer.";
                if (checkPhase)
                    logger.debug(errorMessage, PROGRESSION_STEP_KEY);
                else
                    logger.error(errorMessage, PROGRESSION_STEP_KEY);

                break;
            }
            Integer progressionStep = (Integer) progressionStepObj;
            if ((progressionStep < PROGRESSION_STEP_MIN) || (progressionStep > PROGRESSION_STEP_MAX)) {

                String errorMessage = "The '{}' configuration is out of range ({}) [{}, {}].";
                if (checkPhase)
                    logger.debug(errorMessage, PROGRESSION_STEP_KEY, progressionStep, PROGRESSION_STEP_MIN,
                            PROGRESSION_STEP_MAX);
                else
                    logger.error(errorMessage, PROGRESSION_STEP_KEY, progressionStep, PROGRESSION_STEP_MIN,
                            PROGRESSION_STEP_MAX);

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
                                !k.equals(STARTING_DEPTH_KEY) && !k.equals(PROGRESSION_STEP_KEY) &&
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

        // Create a random number generator.
        // TODO: Need to seed the random number generator.
        Random random = new Random();

        /*
         * Generate the master cuts.
         */

        // TODO: Need to generate the master cuts from the configs.
        int[] masterCuts = new int[] {3, 5, 2, 1, 3};

        // Get the cut count.
        // Throws: JSONException
        int cutCount = (Integer) jsonConfigs.get(CUT_COUNT_KEY);

        // Get the depth count.
        // Throws: JSONException
        int depthCount = (Integer) jsonConfigs.get(DEPTH_COUNT_KEY);

        // Get the starting depth.
        // Throws: JSONException
        int startingDepth = (Integer) jsonConfigs.get(STARTING_DEPTH_KEY);

//        // Generate the cuts using the random integer stream.
//        int[] masterCuts = new int[cutCount];
//        random.ints(startingDepth, depthCount + startingDepth);

        /*
         * Generate the progression sequence.
         */

        // TODO: Need to generate the progression steps from the configs.
        int[][] progressionSteps = new int[][] {
                {1, 1, 1, 2, 1},
                {2, 2, 3, 3, 2},
                {4, 3, 4, 4, 4},
                {5, 4, 5, 5, 5}};

        // Get the progression step.
        // Throws: JSONException
        int progressionStep = (Integer) jsonConfigs.get(PROGRESSION_STEP_KEY);

        /*
         * Generate the progression steps.
         */

        // TODO: Need to generate the progression sequence from the configs.
        int[] progressionSequence = new int[] {1, 2, 3, 4, 5};

        try {

            // Build the progression criteria object from the randomly generated criteria.
            // Throws: ValidationException
            TotalPositionProgressionCriteria criteria = new TotalPositionProgressionCriteria.Builder()
                    .setSource(getName())
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
}
