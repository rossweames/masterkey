package com.eames.masterkey.aws.gateway.http;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.eames.masterkey.service.AutoRegister;
import com.eames.masterkey.service.progression.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is the gateway that receives a set of JSON configurations through an HTTP request. It then finds a
 * {@link ProgressionService} that can interpret those configurations and generate a JSON bitting list, which this
 * gateway then passes back to the caller within an HTTP response.
 */
public class BittingListHTTPGateway
        implements RequestStreamHandler {

    // Initialize the Log4j logger.
    private static final Logger logger = LogManager.getLogger(BittingListHTTPGateway.class);

    /**
     * The service provider is responsible for instantiating the appropriate service to handle the request based on
     * the request body (a set of configurations).
     */
    private final ProgressionServiceProvider serviceProvider;

    /**
     * The default constructor
     */
    public BittingListHTTPGateway() {

        // Discover the progression services.
        Set<ProgressionService> services = discoverProgressionServices();

        // Instantiate the progression service provider passing it the list of services.
        serviceProvider = new ProgressionServiceProvider(services);
    }

    /**
     * Gets the {@link ProgressionServiceProvider}.
     *
     * @return the service provider
     */
    public ProgressionServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    /**
     * Gets called by the AWS Gateway API whenever an HTTP request is made for the Bitting List resource.
     *
     * @param inputStream the request input stream
     * @param outputStream the response output stream
     * @param context the HTTP request context
     * @throws IOException if any error occurs
     */
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {

        logger.info("BittingListHTTPGateway got a request.");

        try {

            // Construct a reader for the input stream.
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the input stream (request) into a string.
            String jsonRequestStr = IOUtils.toString(reader);
            logger.debug("Request: {}", jsonRequestStr);

            // Find a service to process the body.
            // Throws: ProgressionServiceProviderException
            ProgressionService service = serviceProvider.findServiceForConfigs(jsonRequestStr);
            logger.debug("Progression Service: {}", service.getName());

            // Generate the bitting list.
            // Throws: ProgressionServiceException
            ProgressionServiceResults results = service.generateBittingList(jsonRequestStr);

            /*
             * Construct a gson instance using the gson builder.
             * Specify that int arrays should be serialized as strings.
             */
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(int[].class, (JsonSerializer<int[]>) (src, type, jsonSerializationContext) -> {
                        StringBuilder sb = new StringBuilder();
                        for (int v : src) {

                            // Depths of 10 are represented by 0s.
                            if (v == 10)
                                v = 0;

                            // Append the depth.
                            sb.append(v);
                        }
                        return new JsonPrimitive(sb.toString());
                    })
                    .create();

            // Serialize the results to JSON.
            String resultsStr = gson.toJson(results);
            logger.debug("Results: {}", resultsStr);

            // Write the response to the output stream.
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
            writer.write(resultsStr);
            writer.close();

        } catch (ProgressionServiceProviderException ex) {

            String errorMessage = "The ProgressionServiceProvider failed to find a service to process the request.";
            logger.error("{} Cause: {}", errorMessage, ex.getMessage());

            StringBuilder sb = new StringBuilder();
            sb.append(errorMessage);
            sb.append(" ");
            sb.append(ex.getMessage());
            throw new IOException(sb.toString());

        } catch (ProgressionServiceException ex) {

            String errorMessage = "The ProgressionService failed to generate a bitting list.";
            logger.error("{} Cause: {}", errorMessage, ex.getMessage());

            StringBuilder sb = new StringBuilder();
            sb.append(errorMessage);
            sb.append(" ");
            sb.append(ex.getMessage());
            throw new IOException(sb.toString());

        } catch (Exception ex) {

            String errorMessage = "An unexpected exception occurred.";
            logger.error("{} Cause: {}", errorMessage, ex.getMessage());

            StringBuilder sb = new StringBuilder();
            sb.append(errorMessage);
            sb.append(" ");
            sb.append(ex.getMessage());
            throw new IOException(sb.toString());
        }
    }

    /**
     * Discovers and returns all auto-registering {@link ProgressionService}s defined in the system.
     * Finds all services that have an {@link @AutoRegister} annotation.
     *
     * @return the {@link Set} of {@link ProgressionService}s
     */
    private Set<ProgressionService> discoverProgressionServices() {

        logger.info("Discovering progression services.");

        // Instantiate a Reflections object for scanning the services package.
        Reflections reflections = new Reflections("com.eames.masterkey.service.progression.services");

        // Add all auto-registered progression services to the service set.
        Set<ProgressionService> services = new HashSet<>();
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(AutoRegister.class)) {

            logger.debug("Discovered an auto-register class ({}).", clazz.getName());
            
            // Keep only the ProgressionService classes.
            if (ProgressionService.class.isAssignableFrom(clazz)) {

                logger.debug("Discovered an auto-register progression service class ({}).", clazz.getName());

                try {

                    // Instantiate the class and add it to the service set.
                    // Throws: InstantiationException, InstantiationException
                    services.add((ProgressionService) clazz.newInstance());

                    logger.debug("Added '{}' to the progression service provider.", clazz.getName());

                } catch (InstantiationException | IllegalAccessException e) {

                    logger.error("Unable to instantiate a progression service {}. Cause: {}", clazz.getName(),
                            e.getMessage());
                }
            }
        }

        // Return the set of services discovered.
        return services;
    }
}
