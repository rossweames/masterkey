package com.eames.masterkey.service;

/**
 * This exception is the base class for all service exceptions.
 */
public abstract class AbstractServiceException
        extends Exception {

    /**
     * Constructor
     *
     * @param message the exception message
     */
    protected AbstractServiceException(String message) {
        super(message);
    }
}
