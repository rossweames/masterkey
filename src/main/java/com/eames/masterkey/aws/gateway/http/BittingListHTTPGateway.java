package com.eames.masterkey.aws.gateway.http;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.eames.masterkey.service.progression.ProgressionServiceException;
import com.eames.masterkey.service.progression.ProgressionServiceProviderException;
import com.eames.masterkey.service.progression.ProgressionService;
import com.eames.masterkey.service.progression.ProgressionServiceProvider;
import com.eames.masterkey.service.progression.services.RandomGenericTotalProgressionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

    // The JSON parser
    private JSONParser parser = new JSONParser();

    /**
     * The service provider is responsible for instantiating the appropriate service to handle the request based on
     * the request body (a set of configurations).
     */
    private ProgressionServiceProvider serviceProvider;

    /**
     * The default constructor
     */
    public BittingListHTTPGateway() {

        // TODO: Need to generate the list of services.

        Set<ProgressionService> services = new HashSet<>();
        services.add(new RandomGenericTotalProgressionService());

        // Instantiate the progression service provider passing it the list of services.
        serviceProvider = new ProgressionServiceProvider(services);
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

        // The response JSON object
        JSONObject responseJson = new JSONObject();
        try {

            // Construct a reader for the input stream.
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Parse the input stream into a JSON object; it contains the request body.
            // Throws: IOException, ParseException
            JSONObject requestBody = (JSONObject) parser.parse(reader);
            logger.debug("Request: {}", requestBody.toJSONString());

            // Find a service to process the body.
            // Throws: ProgressionServiceProviderException
            ProgressionService service = serviceProvider.findServiceForConfigs(requestBody);
            logger.debug("Progression Service: {}", service.getName());

            // Generate the bitting list.
            // Throws: ProgressionServiceException
            JSONObject jsonBittingList = service.generateBittingList(requestBody);
            logger.debug("Bitting List: {}", jsonBittingList.toJSONString());

            // Insert the bitting list into the response.
            responseJson.put("body", jsonBittingList);
            responseJson.put("statusCode", "200");

        } catch (ParseException ex) {

            String errorMessage = "The JSONParser failed to parse the request input stream.";
            logger.error("{} Cause: {}", errorMessage, ex.getMessage());

            responseJson.put("statusCode", "400");
            responseJson.put("errorMessage", errorMessage);
            responseJson.put("exception", ex.getMessage());

        } catch (IOException ex) {

            String errorMessage = "The JSONParser failed to read the request input stream.";
            logger.error("{} Cause: {}", errorMessage, ex.getMessage());

            responseJson.put("statusCode", "400");
            responseJson.put("errorMessage", errorMessage);
            responseJson.put("exception", ex.getMessage());

        } catch (ProgressionServiceProviderException ex) {

            String errorMessage = "The ProgressionServiceProvider failed to find a service to process the request.";
            logger.error("{} Cause: {}", errorMessage, ex.getMessage());

            responseJson.put("statusCode", "400");
            responseJson.put("errorMessage", errorMessage);
            responseJson.put("exception", ex.getMessage());

        } catch (ProgressionServiceException ex) {

            String errorMessage = "The ProgressionService failed to generate a bitting list.";
            logger.error("{} Cause: {}", errorMessage, ex.getMessage());

            responseJson.put("statusCode", "500");
            responseJson.put("errorMessage", errorMessage);
            responseJson.put("exception", ex.getMessage());
        }

        logger.debug("Response: {}", responseJson.toJSONString());

        // Write the response to rhe output stream.
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toJSONString());
        writer.close();
    }
}
