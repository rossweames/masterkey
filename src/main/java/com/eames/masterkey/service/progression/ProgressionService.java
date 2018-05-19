package com.eames.masterkey.service.progression;

import com.eames.masterkey.service.Service;
import com.eames.masterkey.service.ProcessingCapability;
import org.json.simple.JSONObject;

/**
 * This interface defines the operations that must be implemented by all progression services.
 */
public interface ProgressionService
        extends Service {

    /**
     * Checks whether this service can process the given configurations.
     *
     * @param configs the configurations to test
     * @return a {@link ProcessingCapability} object that contains the
     * result of the test
     */
    ProcessingCapability canProcessConfigs(JSONObject configs);

    /**
     * Generates a JSON bitting list using the given configurations.
     *
     * @param configs the configurations to use
     * @return a JSON object representing the generated bitting list
     * @throws ProgressionServiceException if any error occurs
     */
    JSONObject generateBittingList(JSONObject configs)
            throws ProgressionServiceException;
}
