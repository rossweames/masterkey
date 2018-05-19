package com.eames.masterkey.service.progression;

import com.eames.masterkey.service.AbstractServiceException;

/**
 * This exception is thrown by {@link ProgressionService}.
 */
public class ProgressionServiceException
        extends AbstractServiceException {

    /**
     * Constructor
     *
     * @param message the exception message
     */
    public ProgressionServiceException(String message) {
        super(message);
    }
}
