package com.eames.masterkey.service;

/**
 * This exception is the base class for all service provider exceptions.
 */
public abstract class AbstractServiceProviderException
        extends Exception {

    /**
     * Constructor
     *
     * @param message the exception message
     */
    public AbstractServiceProviderException(String message) {
        super(message);
    }
}
