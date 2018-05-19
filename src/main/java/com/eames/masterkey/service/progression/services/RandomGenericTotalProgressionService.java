package com.eames.masterkey.service.progression.services;

import com.eames.masterkey.service.ProcessingCapability;
import com.eames.masterkey.service.progression.ProgressionService;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * This class is responsible for generating a bitting list using the given cut count, depth count, starting depth,
 * progression step, and MACS contained in the JSON configurations passed to it.
 *
 * The configurations are used to generate a random set of progression criteria that are, in turn, used to generate
 * the bitting list using the Total Progression method.
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
public class RandomGenericTotalProgressionService
        implements ProgressionService {

    /**
     * The configuration constants
     */

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
    private static final Logger logger = LogManager.getLogger(RandomGenericTotalProgressionService.class);

    /**
     * Overridden {@link ProgressionService} operations
     */

    @Override
    public ProcessingCapability canProcessConfigs(JSONObject configs) {

        logger.info("Verifying that this service can process the configurations.");

        // Default the capability to 'no'.
        ProcessingCapability capability = ProcessingCapability.NO;

        do {

            if (configs == null) {
                logger.debug("No configurations provided.");
                break;
            }

            /**
             * Validate that the configurations contain the cut count attribute and that its value is valid.
             */

            Object cutCountObj = configs.get(CUT_COUNT_KEY);
            if (cutCountObj == null) {
                logger.debug("Missing '" + CUT_COUNT_KEY + "' configuration.");
                break;
            }
            if (!(cutCountObj instanceof Integer)) {
                logger.debug("The '" + CUT_COUNT_KEY + "' configuration is not an integer.");
                break;
            }
            Integer cutCount = (Integer) cutCountObj;
            if ((cutCount < CUT_COUNT_MIN) || (cutCount > CUT_COUNT_MAX)) {
                logger.debug("The '" + CUT_COUNT_KEY + "' configuration is out of range (" + cutCount + ") [" +
                        CUT_COUNT_MIN + ", " + CUT_COUNT_MAX + "].");
                break;
            }

            /**
             * Validate that the configurations contain the depth count attribute and that its value is valid.
             */

            Object depthCountObj = configs.get(DEPTH_COUNT_KEY);
            if (depthCountObj == null) {
                logger.debug("Missing '" + DEPTH_COUNT_KEY + "' configuration.");
                break;
            }
            if (!(depthCountObj instanceof Integer)) {
                logger.debug("The '" + DEPTH_COUNT_KEY + "' configuration is not an integer.");
                break;
            }
            Integer depthCount = (Integer) depthCountObj;
            if ((depthCount < DEPTH_COUNT_MIN) || (depthCount > DEPTH_COUNT_MAX)) {
                logger.debug("The '" + DEPTH_COUNT_KEY + "' configuration is out of range (" + depthCount + ") [" +
                        DEPTH_COUNT_MIN + ", " + DEPTH_COUNT_MAX + "].");
                break;
            }

            /**
             * Validate that the configurations contain the starting depth attribute and that its value is valid.
             */

            Object startingDepthObj = configs.get(STARTING_DEPTH_KEY);
            if (startingDepthObj == null) {
                logger.debug("Missing '" + STARTING_DEPTH_KEY + "' configuration.");
                break;
            }
            if (!(startingDepthObj instanceof Integer)) {
                logger.debug("The '" + STARTING_DEPTH_KEY + "' configuration is not an integer.");
                break;
            }
            Integer startingDepth = (Integer) startingDepthObj;
            if ((startingDepth < STARTING_DEPTH_MIN) || (startingDepth > STARTING_DEPTH_MAX)) {
                logger.debug("The '" + STARTING_DEPTH_KEY + "' configuration is out of range (" + startingDepth +
                        ") [" + STARTING_DEPTH_MIN + ", " + STARTING_DEPTH_MAX + "].");
                break;
            }

            /**
             * Validate that the configurations contain the progression step attribute and that its value is valid.
             */

            Object progressionStepObj = configs.get(PROGRESSION_STEP_KEY);
            if (progressionStepObj == null) {
                logger.debug("Missing '" + PROGRESSION_STEP_KEY + "' configuration.");
                break;
            }
            if (!(progressionStepObj instanceof Integer)) {
                logger.debug("The '" + PROGRESSION_STEP_KEY + "' configuration is not an integer.");
                break;
            }
            Integer progressionStep = (Integer) progressionStepObj;
            if ((progressionStep < PROGRESSION_STEP_MIN) || (progressionStep > PROGRESSION_STEP_MAX)) {
                logger.debug("The '" + PROGRESSION_STEP_KEY + "' configuration is out of range (" + progressionStep +
                        ") [" + PROGRESSION_STEP_MIN + ", " + PROGRESSION_STEP_MAX + "].");
                break;
            }

            /**
             * Validate that the configurations contain the MACS attribute and that its value is valid.
             */

            Object macsObj = configs.get(MACS_KEY);
            if (macsObj == null) {
                logger.debug("Missing '" + MACS_KEY + "' configuration.");
                break;
            }
            if (!(macsObj instanceof Integer)) {
                logger.debug("The '" + MACS_KEY + "' configuration is not an integer.");
                break;
            }
            Integer macs = (Integer) macsObj;
            if ((macs < MACS_MIN) || (macs > MACS_MAX)) {
                logger.debug("The '" + MACS_KEY + "' configuration is out of range (" + macs + ") [" + MACS_MIN +
                        ", " + MACS_MAX + "].");
                break;
            }

            // The configurations contain extra, unrecognized attributes.
            if (configs.keySet().size() > 5) {

                logger.info("This service can process the configurations if necessary.");
                logger.debug("The configurations contain the following attributes that will be ignored:");

                configs.keySet().stream()
                        .filter(k -> (!k.equals(CUT_COUNT_KEY) && !k.equals(DEPTH_COUNT_KEY) &&
                                !k.equals(STARTING_DEPTH_KEY) && !k.equals(PROGRESSION_STEP_KEY) &&
                                !k.equals(MACS_KEY)))
                        .forEach(k -> logger.debug("Key: {}", k));

                // Return a 'maybe' capability.
                return ProcessingCapability.MAYBE;
            }

            /**
             * If we get here, everything looks good.
             */

            logger.info("This service prefers to process the configurations.");

            // Return a 'yes' capability.
            return ProcessingCapability.YES;
        }
        while (false);

        logger.info("This service CANNOT process the configurations.");

        // Return a 'no' capability.
        return ProcessingCapability.NO;
    }

    @Override
    public JSONObject generateBittingList(JSONObject configs)
            throws ProgressionServiceException {

        // First, validate the configs to be sure.
        if (canProcessConfigs(configs) == ProcessingCapability.NO)
            throw new ProgressionServiceException("Configurations not valid for this service.");

        // TODO: Need to generate the real bitting list.

        JSONObject jsonBittingList = new JSONObject();
        jsonBittingList.put("source", getName());
        jsonBittingList.put("master", "354215");

        // Return the JSON bitting list.
        return jsonBittingList;
    }

    @Override
    public String getName() {

        return "Random Generic Total Position Progression Service";
    }
}
