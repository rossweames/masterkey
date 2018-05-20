package com.eames.masterkey.service;

import java.util.Collection;

public interface ServiceProvider<T> {

    void registerService(T service)
            throws AbstractServiceProviderException;

    void unregisterService(T service)
            throws AbstractServiceProviderException;

    Collection<T> getRegisteredServices();
}
