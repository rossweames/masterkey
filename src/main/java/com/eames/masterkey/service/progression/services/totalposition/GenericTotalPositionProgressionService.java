package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.service.AutoRegister;
import com.eames.masterkey.service.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is responsible for generating a bitting list using the given master cuts, progression steps, progression
 * sequence, and MACS contained in the JSON configurations passed to it.
 *
 * The configurations are used to generate the bitting list using the Total Position Progression method.
 *
 * It expects the configurations to have the following format:
 * {
 *     masterCuts : // An array of depths representing the top-level master key for the system
 *     progressionSteps : // A matrix of depths used to generate all the change keys for the system
 *     progressionSequence : // An array specifying the order in which the chambers are to be progressed
 *     startingDepth : [0, 1] // The value of the shallowest depth
 *     macs : [1-10] // The Maximum Adjacent Cut Specification
 * }
 *
 * TODO: Need tests for this class.
 *
 * This Progression service is automatically registered by the
 * {@link com.eames.masterkey.aws.gateway.http.BittingListHTTPGateway} at startup.
 */
@AutoRegister
public class GenericTotalPositionProgressionService
        extends AbstractTotalPositionProgressionService {

    /*
     * The configuration constants
     */

    // The master key configuration
    private static final String MASTER_CUTS_KEY = "masterCuts";

    // The progression steps configuration
    private static final String PROGRESSION_STEPS_KEY = "progressionSteps";

    // The progression sequence configuration
    private static final String PROGRESSION_SEQUENCE_KEY = "progressionSequence";

    // The starting depth configuration
    private static final String STARTING_DEPTH_KEY = "startingDepth";

    // The Maximum Adjacent Cut Specification
    private static final String MACS_KEY = "macs";

    // Initialize the Log4j logger.
    private static final Logger logger = LogManager.getLogger(GenericTotalPositionProgressionService.class);

    /**
     * Constructor
     */
    public GenericTotalPositionProgressionService() {

        super("Generic Total Position Progression Service",
                new String[] { MASTER_CUTS_KEY, PROGRESSION_STEPS_KEY, PROGRESSION_SEQUENCE_KEY, STARTING_DEPTH_KEY,
                        MACS_KEY });
    }

    /*
     * Implemented {@link AbstractTotalPositionProgressionService} operations
     */

    @Override
    protected TotalPositionProgressionCriteria generateProgressionCriteria(JSONObject jsonConfigs)
        throws ValidationException {

        // The 'configs' will never be null.

        try {

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

            /*
             * Validate that the 'master cuts' attribute is valid.
             */

            // Get and validate the 'master cuts'.
            // Throws: JSONException
            Object masterCutsObj = jsonConfigs.get(MASTER_CUTS_KEY);
            if (!(masterCutsObj instanceof String)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(MASTER_CUTS_KEY);
                sb.append("' configuration is not a string.");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }

            // Convert the 'master cuts' string into an int array.
            // Throws: ValidationException
            int[] masterCuts = convertKeyStringToKeyCuts((String) masterCutsObj, startingDepth, MASTER_CUTS_KEY);

            /*
             * Validate that the 'progression steps' attribute is valid.
             */

            // Get and validate the 'progression steps'.
            // Throws: JSONException
            Object progressionStepsObj = jsonConfigs.get(PROGRESSION_STEPS_KEY);
            if (!(progressionStepsObj instanceof JSONArray)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(PROGRESSION_STEPS_KEY);
                sb.append("' configuration is not an array.");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }
            JSONArray progressionStepsArray = (JSONArray) progressionStepsObj;
            int rowCount = progressionStepsArray.length();
            int[][] progressionSteps = new int[rowCount][];
            for (int row = 0; row < rowCount; row++) {

                // Get the steps as a string.
                // Throws: JSONException
                Object stepsObj = progressionStepsArray.get(row);
                if (!(stepsObj instanceof String)) {

                    StringBuilder sb = new StringBuilder();
                    sb.append("The '");
                    sb.append(PROGRESSION_STEPS_KEY);
                    sb.append("' configuration contains an element that is not a string (");
                    sb.append(row);
                    sb.append(").");
                    String errorMessage = sb.toString();
                    logger.error(errorMessage);

                    throw new ValidationException(errorMessage);
                }

                // Convert the 'steps' string into an int array.
                // Throws: ValidationException
                int[] steps = convertKeyStringToKeyCuts((String) stepsObj, startingDepth, PROGRESSION_STEPS_KEY);

                // Set the steps into the matrix.
                progressionSteps[row] = steps;
            }

            /*
             * Validate that the 'progression sequence' attribute is valid.
             */

            // Get and validate the 'progression sequence'.
            // Throws: JSONException
            Object progressionSequenceObj = jsonConfigs.get(PROGRESSION_SEQUENCE_KEY);
            if (!(progressionSequenceObj instanceof String)) {

                StringBuilder sb = new StringBuilder();
                sb.append("The '");
                sb.append(PROGRESSION_SEQUENCE_KEY);
                sb.append("' configuration is not an string.");
                String errorMessage = sb.toString();
                logger.error(errorMessage);

                throw new ValidationException(errorMessage);
            }

            // Convert the 'progression sequence' string into an int array.
            // Throws: ValidationException
            int[] progressionSequence = convertKeyStringToKeyCuts((String) progressionSequenceObj, startingDepth, PROGRESSION_SEQUENCE_KEY);

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
            int macs = (Integer) macsObj;

            /*
             * Attempt to construct a TotalPositionProgressionCriteria from the configs.
             */

            // Build a progression criteria object from the criteria.
            // Throws: ValidationException
            TotalPositionProgressionCriteria criteria = new TotalPositionProgressionCriteria.Builder()
                    .setMACS(macs)
                    .setMasterCuts(masterCuts)
                    .setProgressionSteps(progressionSteps)
                    .setProgressionSequence(progressionSequence)
                    .build();

            // Return the newly created criteria.
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
     * Converts the given numeric key string into a set of key cuts.
     *
     * @param keyString the numeric key string to convert
     * @param startingDepth the starting depth (0 or 1)
     * @param attributeKey the attribute key (for the exception)
     * @return the converted set of key cuts
     * @throws ValidationException if the key string cannot be converted into a set of key cuts.
     */
    private int[] convertKeyStringToKeyCuts(String keyString, int startingDepth, String attributeKey)
        throws ValidationException {

        try {

            // Parse the numeric string into a Long.
            // Throws: NumberFormatException
            Long keyStringLong = Long.parseLong(keyString);

            // Extract the cut depths and populate the key cuts with them.
            int cutCount = keyString.length();
            int[] keyCuts = new int[cutCount];
            int keyStringVal = keyStringLong.intValue();
            for (int cut = 0; cut < cutCount; cut++) {

                // Calculate the depth.
                // (If the depth is '0' and the starting depth is '1', then this depth is actually a '10'.)
                int depth = keyStringVal % 10;
                if ((depth == 0) && (startingDepth == 1))
                    depth = 10;

                // Set the depth into the key cuts.
                keyCuts[cutCount - cut - 1] = depth;
                keyStringVal /= 10;
            }

            // Return the int array.
            return keyCuts;

        } catch (NumberFormatException ex) {

            StringBuilder sb = new StringBuilder();
            sb.append("The '");
            sb.append(attributeKey);
            sb.append("' configuration contains non-numeric values (");
            sb.append(keyString);
            sb.append(").");
            String errorMessage = sb.toString();
            logger.error(errorMessage);

            throw new ValidationException(errorMessage);
        }
    }
}
