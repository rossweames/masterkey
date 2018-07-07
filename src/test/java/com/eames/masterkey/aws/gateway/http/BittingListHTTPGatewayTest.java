package com.eames.masterkey.aws.gateway.http;

import com.eames.masterkey.service.Service;
import com.eames.masterkey.service.progression.ProgressionService;
import com.eames.masterkey.service.progression.ProgressionServiceProvider;
import com.eames.masterkey.service.progression.services.totalposition.GenericTotalPositionProgressionService;
import com.eames.masterkey.service.progression.services.totalposition.RandomGenericTotalPositionProgressionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the {@link BittingListHTTPGateway} class.
 */
public class BittingListHTTPGatewayTest {

    // The gateway
    private BittingListHTTPGateway gateway;

    /**
     * Gets called before each test.
     */
    @BeforeEach
    public void setUp() {

        // Instantiate the gateway.
        gateway = new BittingListHTTPGateway();
    }

    /**
     * Gets called after each test.
     */
    @AfterEach
    public void tearDown() {

        // Clear the gateway.
        gateway = null;
    }

    /*
     * Constructor tests
     */

    @Test
    public void testConstructor() {

        // Expect the following classes.
        Class[] expectedServiceClasses = new Class[] {
                RandomGenericTotalPositionProgressionService.class,
                GenericTotalPositionProgressionService.class};

        ProgressionServiceProvider serviceProvider = gateway.getServiceProvider();
        assertNotNull(serviceProvider);

        Collection<ProgressionService> services = serviceProvider.getRegisteredServices();
        assertNotNull(services);

        // Test the found service's classes against the expected service classes.
        Class[] foundServiceClasses = new Class[services.size()];
        int i = 0;
        for (Service service : services)
            foundServiceClasses[i++] = service.getClass();
        assertArrayEquals(expectedServiceClasses, foundServiceClasses);
    }
}
