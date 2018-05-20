package com.eames.masterkey.service;

import java.util.Collection;

/**
 * This interface defines all operations mandatory for all service providers.
 *
 * @param <T> the type of service provider
 */
public interface ServiceProvider<T> {

    /**
     * Registers the given service with this provider.
     *
     * @param service the service to register
     * @throws AbstractServiceProviderException if an error occurs
     */
    void registerService(T service)
            throws AbstractServiceProviderException;

    /**
     * Unregisters the given service from this service provider.
     *
     * @param service the service to unregister
     * @throws AbstractServiceProviderException if an error occurs
     */
    void unregisterService(T service)
            throws AbstractServiceProviderException;

    /**
     * Gets the collection of registered services.
     *
     * @return the collection of registered services
     */
    Collection<T> getRegisteredServices();
}
