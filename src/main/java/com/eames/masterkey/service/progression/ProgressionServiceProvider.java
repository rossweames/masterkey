package com.eames.masterkey.service.progression;

import com.eames.masterkey.service.AbstractServiceProvider;
import com.eames.masterkey.service.ProcessingCapability;

import java.util.Collection;

/**
 * This class is a service provider for {@link ProgressionService}s.
 * It finds the appropriate service for the given configurations.
 */
public class ProgressionServiceProvider
        extends AbstractServiceProvider<ProgressionService> {

    /**
     * Constructor
     *
     * Registers all the {@link ProgressionService}s passed in.

     * @param services the collection of services to manage
     */
    public ProgressionServiceProvider(Collection<ProgressionService> services) {

        // Register each service.
        if (services != null)
            services.stream().forEach(this::registerService);
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
    public ProgressionService findServiceForConfigs(String configs)
            throws ProgressionServiceProviderException {

        // The 'maybe' service is the one we'll use if we can't find
        // a fully compatible service.
        ProgressionService maybeService = null;

        // Loop through the services and find the one that best fits the configs.
        Collection<ProgressionService> registeredServices = getRegisteredServices();
        for (ProgressionService service : registeredServices) {

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
