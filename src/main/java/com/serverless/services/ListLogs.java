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

public class ListLogs implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "AWS Lambda & Serverless");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");

        try {
            List<Classification> csl = new Classification().list();

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(csl)
                    .setHeaders(headers)
                    .build();
        } catch (Exception ex) {
            Response responseBody = new Response("Error in getting elements ......" + ex, input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }
    }
}