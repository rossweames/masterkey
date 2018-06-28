package com.eames.masterkey.service.progression;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This {@link java.lang.annotation.Annotation} is used by the
 * {@link com.eames.masterkey.aws.gateway.http.BittingListHTTPGateway} to automatically register
 * {@link ProgressionService}s at startup.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRegister {
}
