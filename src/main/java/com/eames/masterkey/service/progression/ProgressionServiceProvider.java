package com.eames.masterkey.service.progression;

import com.eames.masterkey.service.AbstractServiceProvider;
import com.eames.masterkey.service.ProcessingCapability;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.Iterator;

/**
 * This class is a service provider for {@link ProgressionService}s.
 * It finds the appropriate service for the given configurations.
 */
public class ProgressionServiceProvider
        extends AbstractServiceProvider<ProgressionService> {

    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(ProgressionServiceProvider.class);

    /**
     * Constructor
     *
     * Registers all the {@link ProgressionService}s passed in.

     * @param services the collection of services to manage
     */
    public ProgressionServiceProvider(Collection<ProgressionService> services) {

        // Register each service.
        if (services != null)
            services.stream().forEach(s -> registerService(s));
    }

    /**
     * Attempts to find the appropriate {@link ProgressionService} to process
     * the given configurations.
     *
     * @param configs the configurations that need to be processed
     * @return the best fitting {@link ProgressionService} instance
     * @throws ProgressionServiceProviderException if an error occurred or
     * no service could be found.
     */
    public ProgressionService findServiceForConfigs(JSONObject configs)
            throws ProgressionServiceProviderException {

        // The 'maybe' service is the one we'll use if we can't find
        // a fully compatible service.
        ProgressionService maybeService = null;

        // Loop through the services and find the one that best fits the configs.
        Collection<ProgressionService> registeredServices = getRegisteredServices();
        Iterator<ProgressionService> serviceIter = registeredServices.iterator();
        while (serviceIter.hasNext()) {

            // Get the next service.
            ProgressionService service = serviceIter.next();

            // Test the service's capability to process the configs.
            ProcessingCapability capability = service.canProcessConfigs(configs);

            // The service is fully capable of processing the configs.
            if (capability == ProcessingCapability.YES)
                return service;

            // The service is marginally capable of processing the configs.
            // (We always use the first one we find.)
            else if ((maybeService == null) && (capability == ProcessingCapability.MAYBE))
                maybeService = service;
        }

        // A marginally suitable service was found.
        if (maybeService != null)
            return maybeService;

        // No suitable service was found.
        else
            throw new ProgressionServiceProviderException("No suitable progression service could be found.");
    }
}
