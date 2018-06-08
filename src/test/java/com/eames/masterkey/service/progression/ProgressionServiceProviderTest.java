package com.eames.masterkey.service.progression;

import com.eames.masterkey.service.ProcessingCapability;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * This class tests the {@link ProgressionServiceProvider} class.
 */
public class ProgressionServiceProviderTest {

    private Set<ProgressionService> services;

    /**
     * This class provides default implementations for the .generateBittingList()
     * and .getName() operations so the individual tests only need to implement
     * the .canProcessConfigs() operation.
     *
     * These tests do not ever call either of these operations, so we don't care
     * how they're coded.
     */
    private abstract class TestBittingListService implements ProgressionService {

        @Override
        public String generateBittingList(String configs) {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }
    }

    /**
     * This class provides default implementations for the .describeMismatch()
     * operation so the individual tests only need to implement the .matches()
     * operation.
     *
     * These tests do not ever call any of these operations, so we don't care
     * how they're coded.
     */
    private abstract class TestMatcher extends BaseMatcher<ProgressionService> {

        @Override
        public void describeTo(Description description) {
        }
    }

    /**
     * Gets called before each test.
     */
    @Before
    public void setUp() {

        services = new HashSet<>();
    }

    /**
     * Gets called after each test.
     */
    @After
    public void tearDown() {

        services = null;
    }

    /**
     * Constructor tests
     */

    @Test
    public void testConstructNull() {

        try {

            new ProgressionServiceProvider(null);

        } catch (Exception e) {

            fail();
        }
    }

    /**
     * .findServiceForConfigs() tests
     */

    @Test
    public void testFind_Null_None() {

        ProgressionServiceProvider serviceProvider = new ProgressionServiceProvider(services);

        try {

            serviceProvider.findServiceForConfigs(null);

            fail();

        } catch (ProgressionServiceProviderException e) {

            // Expected result.
        }
    }

    @Test
    public void testFind_None() {

        ProgressionServiceProvider serviceProvider = new ProgressionServiceProvider(services);

        try {

            serviceProvider.findServiceForConfigs("");

            fail();

        } catch (ProgressionServiceProviderException e) {

            // Expected result.
        }
    }

    @Test
    public void testFind_No() {

        services.add(new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.NO;
            }
        });
        ProgressionServiceProvider serviceProvider = new ProgressionServiceProvider(services);

        try {

            serviceProvider.findServiceForConfigs("");

            fail();

        } catch (ProgressionServiceProviderException e) {

            // Expected result.
        }
    }

    @Test
    public void testFind_Maybe() {

        ProgressionService service = new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.MAYBE;
            }
        };
        services.add(service);
        ProgressionServiceProvider serviceProvider = new ProgressionServiceProvider(services);

        try {

            ProgressionService foundService = serviceProvider.findServiceForConfigs("");

            assertEquals(service, foundService);

        } catch (ProgressionServiceProviderException e) {

            fail();
        }
    }

    @Test
    public void testFind_Yes() {

        ProgressionService service = new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.YES;
            }
        };
        services.add(service);
        ProgressionServiceProvider serviceProvider = new ProgressionServiceProvider(services);

        try {

            ProgressionService foundService = serviceProvider.findServiceForConfigs("");

            assertEquals(service, foundService);

        } catch (ProgressionServiceProviderException e) {

            fail();
        }
    }

    @Test
    public void testFind_YesYes() {

        ProgressionService service = new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.YES;
            }
        };
        services.add(service);
        ProgressionService service2 = new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.YES;
            }
        };
        services.add(service2);
        ProgressionServiceProvider serviceProvider = new ProgressionServiceProvider(services);

        try {

            ProgressionService foundService = serviceProvider.findServiceForConfigs("");

            assertThat(foundService, new TestMatcher() {
                @Override
                public boolean matches(Object o) {

                    return (o.equals(service) || o.equals(service2));
                }
            });

        } catch (ProgressionServiceProviderException e) {

            fail();
        }
    }

    @Test
    public void testFind_MaybeMaybe() {

        ProgressionService service = new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.MAYBE;
            }
        };
        services.add(service);
        ProgressionService service2 = new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.MAYBE;
            }
        };
        services.add(service2);
        ProgressionServiceProvider serviceProvider = new ProgressionServiceProvider(services);

        try {

            ProgressionService foundService = serviceProvider.findServiceForConfigs("");

            assertThat(foundService, new TestMatcher() {
                @Override
                public boolean matches(Object o) {

                    return (o.equals(service) || o.equals(service2));
                }
            });

        } catch (ProgressionServiceProviderException e) {

            fail();
        }
    }

    @Test
    public void testFind_YesNoMaybe() {

        ProgressionService service = new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.YES;
            }
        };
        services.add(service);
        ProgressionService service2 = new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.NO;
            }
        };
        services.add(service2);
        ProgressionService service3 = new TestBittingListService() {
            @Override
            public ProcessingCapability canProcessConfigs(String configs) {
                return ProcessingCapability.MAYBE;
            }
        };
        services.add(service3);
        ProgressionServiceProvider serviceProvider = new ProgressionServiceProvider(services);

        try {

            ProgressionService foundService = serviceProvider.findServiceForConfigs("");

            assertEquals(service, foundService);

        } catch (ProgressionServiceProviderException e) {

            fail();
        }
    }
}
