package com.eames.masterkey.service.progression;

import com.eames.masterkey.service.AbstractServiceProviderException;

/**
 * This exception is thrown by {@link ProgressionServiceProvider}.
 */
public class ProgressionServiceProviderException
        extends AbstractServiceProviderException {

    /**
     * Constructor
     *
     * @param message the exception message
     */
    public ProgressionServiceProviderException(String message) {
        super(message);
    }
}
