package com.serverless.services;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.commons.CloudWatchLogsRetriever;
import com.serverless.commons.Gpt3LogClassifier;
import com.serverless.domain.Classification;
import com.serverless.http.ApiGatewayResponse;
import com.serverless.http.Response;
import org.apache.log4j.Logger;
import software.amazon.awssdk.services.cloudwatchlogs.model.FilteredLogEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a request handler for listing logs and generating API Gateway responses.
 * Implements the RequestHandler interface for handling requests.
 */
public class ListLogs implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    /** Logger instance for logging information and errors. */
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Handles the incoming request to list logs and generates an API Gateway response.
     *
     * @param input   The input data associated with the request.
     * @param context The AWS Lambda context object.
     * @return An ApiGatewayResponse with a list of classifications or an error response.
     */
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        // Prepare headers for the API Gateway response
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "AWS Lambda & Serverless");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");

        try {
            // Retrieve a list of classifications
            List<Classification> csl = new Classification().list();

            // Build a successful response with the list of classifications
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(csl)
                    .setHeaders(headers)
                    .build();
        } catch (Exception ex) {
            // If an error occurs, create an error response
            Response responseBody = new Response("Error in getting elements: " + ex, input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }
    }
}
