package com.eames.masterkey.service.progression.services.totalposition;

import com.eames.masterkey.model.BittingList;
import com.eames.masterkey.service.ProcessingCapability;
import com.eames.masterkey.service.ValidationException;
import com.eames.masterkey.service.progression.ProgressionService;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import com.eames.masterkey.service.progression.ProgressionServiceResults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * This abstract class is a partial implementation of the {@link ProgressionService} interface.
 *
 * It implements the .canProcessConfigs() and .generateBittingList() operations and leaves implementation of the
 * .getName() operation to the concrete child classes.
 *
 * In addition, it defines two abstract operations that must be implemented by the concrete child classes.
 */
public abstract class AbstractTotalPositionProgressionService
        implements ProgressionService {

    // Initialize the Log4j logger.
    private static final Logger logger = LogManager.getLogger(AbstractTotalPositionProgressionService.class);

    // The service name
    private String name;

    // The expected configs attribute keys
    private String[] attributeKeys;

    /**
     * Constructor
     *
     * @param name the service name
     * @param attributeKeys the attribute keys expected in the configs
     */
    public AbstractTotalPositionProgressionService(String name, String[] attributeKeys) {

        this.name = name;
        this.attributeKeys = attributeKeys;
    }

    /**
     * Implemented {@link ProgressionService} operations
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
    public ProgressionServiceResults generateBittingList(String configs)
            throws ProgressionServiceException {

        logger.info("Generating a bitting list using the {} service.", getName());

        try {

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
            // Throws: ValidationException
            TotalPositionProgressionCriteria criteria = generateProgressionCriteria(jsonConfigs);

            // Construct a progression service.
            TotalPositionProgressionService service = new TotalPositionProgressionService(criteria);

            // Generate the bitting list.
            // Throws: ProgressionServiceException
            BittingList bittingList = service.generateBittingList();

            // Construct and return the results.
            return new ProgressionServiceResults(getName(), criteria, bittingList);

        } catch (ValidationException ex) {

            StringBuilder sb = new StringBuilder();
            sb.append("A validation error occurred. Cause: ");
            sb.append(ex.getMessage());
            String errorMessage = sb.toString();
            logger.error(errorMessage);

            throw new ProgressionServiceException(errorMessage);

        } catch (JSONException ex) {

            StringBuilder sb = new StringBuilder();
            sb.append("An unexpected error occurred. Cause: ");
            sb.append(ex.getMessage());
            String errorMessage = sb.toString();
            logger.error(errorMessage);

            throw new ProgressionServiceException(errorMessage);
        }
    }

    @Override
    public String getName() {

        return name;
    }

    /*
     * Abstract operations that must be implemented by child classes
     */

    /**
     * Validates the given configs then generates a set of Total Position Progression criteria from them.
     * Assume the configs have been validated.
     *
     * @param jsonConfigs the configs to use
     * @return the newly generated progression criteria
     * @throws ValidationException if any validation error occurs
     */
    protected abstract TotalPositionProgressionCriteria generateProgressionCriteria(JSONObject jsonConfigs)
            throws ValidationException;

    /*
     * Local operations
     */

    /**
     * Validates the structure of the given JSON string and converts it into a JSONObject.
     * The JSONObject returned will never be {@code null} and will always contain a capability. If the capability is
     * {@code ProcessingCapability.YES} or {@code ProcessingCapability.MAYBE}, then the JSONObject contains the
     * appropriate attributes.
     *
     * This operation logs debug messages when in the 'check' phase and error messages when in the 'generate' phase.
     *
     * @param configs the JSON configs string to validate and convert
     * @param checkPhase {@code True} if this operation is being called during the 'check' phase,
     *        {@code false} if it is being called during the 'generate' phase.
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

            // Verify that the configuration contains all the expected attributes.
            boolean missingAttribute = false;
            for (String attrKey : attributeKeys) {
                // Verify that the configurations contain the master cuts attribute.
                if (!containsAttribute(jsonConfigs, attrKey, checkPhase)) {
                    missingAttribute = true;
                    break;
                }
            }
            if (missingAttribute)
                break;

            // Check whether the configuration contains unrecognized attributes and inject the appropriate capability.
            if (containsUnrecognizedAttributes(jsonConfigs, checkPhase))
                jsonConfigs.put(CAPABILITY_KEY, ProcessingCapability.MAYBE);
            else
                jsonConfigs.put(CAPABILITY_KEY, ProcessingCapability.YES);

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
     * Tests whether the given configs contain any unrecognized attributes.
     *
     * @param jsonConfigs the configs to test
     * @param checkPhase {@code True} if this operation is being called during the 'check' phase, {@code false} if it
     *        is being called during the 'generate' phase.
     * @return {@code True} if the configs contain unrecognizes attributes, {@code false} if not
     */
    private boolean containsUnrecognizedAttributes(JSONObject jsonConfigs, boolean checkPhase) {

        // The configurations contain extra, unrecognized attributes.
        if (jsonConfigs.keySet().size() > attributeKeys.length) {

            if (checkPhase)
                logger.info("This service can process the configurations if necessary.");

            String errorMessage = "The configurations contain the following attributes that will be ignored:";
            if (checkPhase)
                logger.debug(errorMessage);
            else
                logger.error(errorMessage);

            List attrKetList = Arrays.asList(attributeKeys);
            jsonConfigs.keySet().stream()
                    .filter(k -> !attrKetList.contains(k))
                    .forEach(k -> {

                        String errorMessage2 = "Key: {}";
                        if (checkPhase)
                            logger.debug(errorMessage2, k);
                        else
                            logger.error(errorMessage2, k);
                    });

            return true;
        }

        // The configuration is a match.
        else {

            if (checkPhase)
                logger.info("This service prefers to process the configurations.");

            return false;
        }
    }

    /*
     * Class operations
     */

    /**
     * Tests the given JSON configs for the given attribute.
     *
     * @param jsonConfigs the JSON configs to test
     * @param attributeKey the attribute to test for
     * @param checkPhase {@code True} if this operation is being called during the 'check' phase, {@code false} if it
     *        is being called during the 'generate' phase.
     * @return {@code True} if the configs contain the given attribute, {@cide false} if not
     */
    static private boolean containsAttribute(JSONObject jsonConfigs, String attributeKey, boolean checkPhase) {

        try {

            // Attempt to get the given attribute.
            // Throws: JSONException
            jsonConfigs.get(attributeKey);

            return true;

        } catch (JSONException ex) {

            String errorMessage = "Missing '{}' configuration. Cause: {}";
            if (checkPhase)
                logger.debug(errorMessage, attributeKey, ex.getMessage());
            else
                logger.error(errorMessage, attributeKey, ex.getMessage());

            return false;
        }
    }
}
