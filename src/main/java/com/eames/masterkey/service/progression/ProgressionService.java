package com.eames.masterkey.service.progression;

import com.eames.masterkey.service.Service;
import com.eames.masterkey.service.ProcessingCapability;

/**
 * This interface defines the operations that must be implemented by all progression services.
 */
public interface ProgressionService
        extends Service {

    /**
     * Checks whether this service can process the given configurations.
     *
     * @param configs the JSON configurations to test
     * @return a {@link ProcessingCapability} object that contains the
     * result of the test
     */
    ProcessingCapability canProcessConfigs(String configs);

    /**
     * Generates a JSON bitting list using the given configurations.
     *
     * @param configs the JSON configurations to use
     * @return the generated JSON bitting list
     * @throws ProgressionServiceException if any error occurs
     */
    String generateBittingList(String configs)
            throws ProgressionServiceException;
}
