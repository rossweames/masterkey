package com.eames.masterkey.aws.gateway.http;

import com.eames.masterkey.service.Service;
import com.eames.masterkey.service.progression.ProgressionService;
import com.eames.masterkey.service.progression.ProgressionServiceProvider;
import com.eames.masterkey.service.progression.services.totalposition.GenericTotalPositionProgressionService;
import com.eames.masterkey.service.progression.services.totalposition.RandomGenericTotalPositionProgressionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

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
        ArrayList<Class> expectedServiceList = new ArrayList<>();
        expectedServiceList.add(RandomGenericTotalPositionProgressionService.class);
        expectedServiceList.add(GenericTotalPositionProgressionService.class);
        Object[] expectedServices = expectedServiceList.stream().map(c -> c.getName()).sorted().toArray();

        ProgressionServiceProvider serviceProvider = gateway.getServiceProvider();
        assertNotNull(serviceProvider);

        Collection<ProgressionService> services = serviceProvider.getRegisteredServices();
        assertNotNull(services);

        // Test the found service's classes against the expected service classes.
        Object[] foundServices = services.stream().map(s -> s.getClass().getName()).sorted().toArray();
        assertArrayEquals(expectedServices, foundServices);
    }
}
