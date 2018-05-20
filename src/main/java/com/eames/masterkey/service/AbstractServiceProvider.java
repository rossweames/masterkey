package com.eames.masterkey.service;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

public abstract class AbstractServiceProvider<T>
        implements ServiceProvider<T> {

    // The collection of registered services
    private final Set<T> registeredServices = new HashSet<>();

    @Override
    public void registerService(T service) {

        // Add the service to the set.
        registeredServices.add(service);
    }

    @Override
    public void unregisterService(T service) {

        // Remove the service from the set.
        registeredServices.remove(service);
    }

    @Override
    public Collection<T> getRegisteredServices() {
        return registeredServices;
    }
}
