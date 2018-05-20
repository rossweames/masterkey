package com.eames.masterkey.service;

/**
 * This exception is the base class for all service exceptions.
 */
public class ValidationException
    extends Exception {

    /**
     * Constructor
     *
     * @param message the exception message
     */
    public ValidationException(String message) {
        super(message);
    }
}
