package com.eames.masterkey.aws.gateway.http;

import com.eames.masterkey.service.progression.ProgressionService;
import com.eames.masterkey.service.progression.ProgressionServiceProvider;
import com.eames.masterkey.service.progression.services.totalposition.RandomGenericTotalPositionProgressionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * This class tests the {@link BittingListHTTPGateway} class.
 */
public class BittingListHTTPGatewayTest {

    // The gateway
    private BittingListHTTPGateway gateway;

    /**
     * Gets called before each test.
     */
    @Before
    public void setUp() {

        // Instantiate the gateway.
        gateway = new BittingListHTTPGateway();
    }

    /**
     * Gets called after each test.
     */
    @After
    public void tearDown() {

        // Clear the gateway.
        gateway = null;
    }

    /*
     * Constructor tests
     */

    @Test
    public void testConstructor() {

        ProgressionServiceProvider serviceProvider = gateway.getServiceProvider();
        assertNotNull(serviceProvider);

        Collection<ProgressionService> services = serviceProvider.getRegisteredServices();
        assertNotNull(services);
        assertEquals(1, services.size());
        assertEquals(RandomGenericTotalPositionProgressionService.class, services.iterator().next().getClass());
    }
}
